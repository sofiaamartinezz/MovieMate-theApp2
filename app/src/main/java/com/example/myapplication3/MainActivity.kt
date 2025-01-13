package com.example.myapplication3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import com.example.myapplication3.network.ApiService
import com.example.myapplication3.network.ApiCallback
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = ApiService(this)

        setContent {
            MoviesScreen(apiService)
        }
    }
}

@Composable
fun MoviesScreen(apiService: ApiService) {
    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var moviesList by remember { mutableStateOf("No movies available") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Create and Get Movies",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = genre,
            onValueChange = { genre = it },
            label = { Text("Genre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = director,
            onValueChange = { director = it },
            label = { Text("Director") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = rating,
            onValueChange = {
                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                    rating = it
                }
            },
            label = { Text("Rating (0-5)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (title.isNotEmpty() && genre.isNotEmpty() && director.isNotEmpty() && rating.isNotEmpty()) {
                    val ratingValue = rating.toFloatOrNull() ?: 0f
                    if (ratingValue in 0f..5f) {
                        apiService.createMovie(title, genre, director, ratingValue, object : ApiCallback {
                            override fun onSuccess(response: String) {
                                title = ""
                                genre = ""
                                director = ""
                                rating = ""
                            }

                            override fun onError(error: String) {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5998))
        ) {
            Text("Create Movie")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                apiService.getMovies(object : ApiCallback {
                    override fun onSuccess(response: String) {
                        try {
                            val jsonArray = JSONArray(response)
                            val moviesText = StringBuilder()

                            for (i in 0 until jsonArray.length()) {
                                val movie = jsonArray.getJSONObject(i)
                                moviesText.append("Title: ${movie.optString("title")}\n")
                                moviesText.append("Genre: ${movie.optString("genre")}\n")
                                moviesText.append("Director: ${movie.optString("director")}\n")
                                moviesText.append("Rating: ${movie.optDouble("rating")}/5\n\n")
                            }

                            moviesList = if (moviesText.isEmpty()) "No movies available"
                            else moviesText.toString()

                        } catch (e: Exception) {
                            moviesList = "No movies available"
                        }
                    }

                    override fun onError(error: String) {
                        moviesList = "No movies available"
                    }
                })
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B5998))
        ) {
            Text("Show Movies")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = moviesList)
    }
}