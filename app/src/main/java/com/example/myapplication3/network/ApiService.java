package com.example.myapplication3.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApiService {
    private final RequestQueue requestQueue;
    private final Context context;

    public ApiService(Context context) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.context = context;
    }

    public void createMovie(String title, String genre, String director, float rating, final ApiCallback callback) {
        String url = Constants.BASE_URL + "/MoviesApi";
        JSONObject movieData = new JSONObject();

        try {
            movieData.put("title", title);
            movieData.put("genre", genre);
            movieData.put("director", director);
            movieData.put("rating", rating);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError("Error creating the film JSON: " + e.getMessage());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                movieData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error creating the film: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    public void getMovies(final ApiCallback callback) {
        String url = Constants.BASE_URL + "/MoviesApi";

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        callback.onSuccess(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("Error fetching films: " + error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }
}