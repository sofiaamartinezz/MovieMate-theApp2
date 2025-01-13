package com.example.myapplication3

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapplication3.network.ApiService
import com.example.myapplication3.network.ApiCallback
import com.example.myapplication3.ui.theme.MyApplication3Theme
import org.json.JSONArray

class MainActivity : ComponentActivity() {
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = ApiService(this)

        setContent {
            MyApplication3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieApp(apiService)
                }
            }
        }
    }
}

@Composable
fun MovieApp(apiService: ApiService) {
    var title by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var director by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf("") }
    var moviesList by remember { mutableStateOf("No movies available") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Movies App",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
                if (title.isBlank() || genre.isBlank() || director.isBlank() || rating.isBlank()) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val ratingValue = rating.toFloatOrNull()
                if (ratingValue == null || ratingValue !in 0f..5f) {
                    Toast.makeText(context, "Rating must be between 0 and 5", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                apiService.createMovie(title, genre, director, ratingValue, object : ApiCallback {
                    override fun onSuccess(response: String) {
                        title = ""
                        genre = ""
                        director = ""
                        rating = ""
                        Toast.makeText(context, "Movie created successfully", Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(error: String) {
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
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
                                moviesText.append("Rating: ${movie.optDouble("rating")}/5\n")
                                moviesText.append("------------------------\n")
                            }

                            moviesList = moviesText.toString().ifEmpty { "No movies available" }
                        } catch (e: Exception) {
                            moviesList = "Error loading movies"
                        }
                    }

                    override fun onError(error: String) {
                        moviesList = "Error: $error"
                    }
                })
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Show Movies")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = moviesList,
            modifier = Modifier.fillMaxWidth()
        )
    }
}