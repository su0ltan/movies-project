package com.example.mainactivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {



    private RecyclerView recyclerViewMovies;
    private MovieAdapter movieAdapter;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private EditText editTextSearch;


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Recent Movies");
        }
        editTextSearch = findViewById(R.id.editTextSearch);
        // Initialize with an empty list and update later
        movieAdapter = new MovieAdapter(new ArrayList<>());
        recyclerViewMovies.setAdapter(movieAdapter);


        // Fetch movies

        fetchMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_watchlist) {
            // Navigate to WatchListActivity
            Intent intent = new Intent(this, WatchListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_favorites) {
            // Navigate to FavoritesActivity
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void fetchMovies() {

        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();

                String key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiNTdiZGRmMjNmYWE3ZGU3YWIzOGI0OWMyOTZkZjVkNCIsIm5iZiI6MTczMTM0MTIzNy44Nzc5NzI0LCJzdWIiOiI2NzMyMmEzODBkNzU4MDQwZWI0YjFjMzYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tAcIRUOQtdPSkaEo_7c9ah6CPJAeYgwVYD11ntBdp4Q";


            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&year=2024")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer "+ key)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    // Parse response to list of movies
                    List<Movie> movies = Movie.parseMovies(response.body().string());

                    // Update UI on the main thread
                    mainThreadHandler.post(() -> updateRecyclerView(movies));
                } else {
                    mainThreadHandler.post(() -> Toast.makeText(MainActivity.this, "Failed to fetch movies", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                mainThreadHandler.post(() -> Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show());
            }
        });



    }

    private void updateRecyclerView(List<Movie> movies) {
        movieAdapter = new MovieAdapter(movies);
            recyclerViewMovies.setAdapter(movieAdapter);
    }




}