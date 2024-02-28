package com.srs.movieproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

// MainActivity.java
public class MainActivity extends AppCompatActivity {
    private static final String API_URL = "https://www.omdbapi.com/?s=all&apikey=1e5920fc";
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        movies = new ArrayList<>();
        adapter = new MovieAdapter(movies);
        recyclerView.setAdapter(adapter);

        fetchDataFromApi();
    }

    private void fetchDataFromApi() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, API_URL,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("Search");
                        Log.d(TAG, "fetchDataFromApi: "+jsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject movieObject = jsonArray.getJSONObject(i);
                            Movie movie = new Movie();
                            movie.setTitle(movieObject.getString("Title"));
                            movie.setYear(movieObject.getString("Year"));
                            movie.setImdbID(movieObject.getString("imdbID"));
                            movie.setType(movieObject.getString("Type"));
                            movie.setPoster(movieObject.getString("Poster"));
                            movies.add(movie);
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show());

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
