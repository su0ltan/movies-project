package com.example.mainactivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainactivity.Favorite.FavoriteAdapter;
import com.example.mainactivity.Favorite.MovieDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FavoritesActivity extends AppCompatActivity {


    FavoriteAdapter favoriteAdapter ;
    RecyclerView favoriteRecylcle;

    private List<Movie> movieList = new ArrayList<>();

    private ExecutorService executorService;
    private Handler mainHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        favoriteRecylcle = findViewById(R.id.Favorites_recyclerViewMovies);
        favoriteRecylcle.setLayoutManager(new LinearLayoutManager(this));
        favoriteAdapter = new FavoriteAdapter(getApplicationContext(),movieList);
        favoriteRecylcle.setAdapter(favoriteAdapter);



        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Favorites");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        executorService = Executors.newFixedThreadPool(3); // Create a thread pool of 3 threads
        mainHandler = new Handler(Looper.getMainLooper());
        fetchMovies();
    }

    private void fetchMovies() {
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();

            MovieDatabaseHelper movieDatabaseHelper = new MovieDatabaseHelper(getApplicationContext());
            List<Integer> ids = movieDatabaseHelper.readMovies();

            List<Movie> movies = new ArrayList<>();
            ids.forEach(id -> {
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
                        movies.add(movie);
                        mainHandler.post(() -> {
                            movieList.add(movie);
                            favoriteAdapter = new FavoriteAdapter(this, movieList);
                            favoriteRecylcle.setAdapter(favoriteAdapter);
                        });
                        Log.d("xxx11", movie.getTitle());
                    } else {
                        Log.e("fetchMovies", "Failed to fetch movie with ID " + id);
                    }
                } catch (IOException e) {
                    Log.e("fetchMovies", "Exception during API call", e);
                }
            });

            // Update RecyclerView on the main thread
//            runOnUiThread(() -> updateRecyclerView(movies));
        });
    }


    private void updateRecyclerView(Movie movie) {
        favoriteAdapter.updateData(movie);
        favoriteAdapter.notifyDataSetChanged();
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
            // Navigate to WatchListActivity
            Intent intent = new Intent(this, WatchListActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_favorites) {
            // We're already in FavoritesActivity
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
