package com.example.mainactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainactivity.Favorite.MovieDatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WatchListActivity extends AppCompatActivity {

    private RecyclerView watchListRecyclerView;
    private WatchListAdapter watchListAdapter;
    private List<Movie> movieList = new ArrayList<>();

    private ExecutorService executorService;
    private Handler mainHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_watch_list);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Watch List");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        watchListRecyclerView = findViewById(R.id.WatchList_recyclerViewMovies);
        watchListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        watchListAdapter = new WatchListAdapter(this, movieList);
        watchListRecyclerView.setAdapter(watchListAdapter);

        executorService = Executors.newFixedThreadPool(3);
        mainHandler = new Handler(Looper.getMainLooper());
        fetchMovies();
    }




    private void fetchMovies() {
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            MovieDatabaseHelper dbHelper = new MovieDatabaseHelper(getApplicationContext());
            List<Integer> ids = dbHelper.readWatchlistMovies();

            for (Integer id : ids) {
                String key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiNTdiZGRmMjNmYWE3ZGU3YWIzOGI0OWMyOTZkZjVkNCIsIm5iZiI6MTczMTM0MTIzNy44Nzc5NzI0LCJzdWIiOiI2NzMyMmEzODBkNzU4MDQwZWI0YjFjMzYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tAcIRUOQtdPSkaEo_7c9ah6CPJAeYgwVYD11ntBdp4Q";

                Request request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/" + id + "?language=en-US")
                        .get()
                        .addHeader("accept", "application/json")
                        .addHeader("Authorization", "Bearer " + key)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.isSuccessful() && response.body() != null) {
                        Movie movie = Movie.parseMovie(response.body().string());
                        mainHandler.post(() -> {
                            movieList.add(movie);
                            watchListAdapter.notifyDataSetChanged();
                        });
                    } else {
                        Log.e("WatchList", "Failed to fetch movie with ID " + id);
                    }
                } catch (IOException e) {
                    Log.e("WatchList", "Exception during API call", e);
                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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

        if (id == android.R.id.home) {
            // Handle the Up button
            finish();
            return true;
        } else if (id == R.id.action_watchlist) {
            // We're already in WatchListActivity
            return true;
        } else if (id == R.id.action_favorites) {
            // Navigate to FavoritesActivity
            Intent intent = new Intent(this, FavoritesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}