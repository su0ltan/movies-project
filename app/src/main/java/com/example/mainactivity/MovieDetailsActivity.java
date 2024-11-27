package com.example.mainactivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;


import com.bumptech.glide.Glide;
import com.example.mainactivity.Favorite.MovieDatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView imageViewPoster;
    private TextView textViewTitle, textViewReleaseDate, textViewOverview, textViewRateCount, textViewRateAvg, textlanguage;
    private Button  buttonRateMovie;
    private ImageButton watchIcon, favoriteIcon;
    Movie movie;
    private String guestSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_details);
        watchIcon = findViewById(R.id.watchIcon);
        favoriteIcon = findViewById(R.id.favoriteIcon);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Movie Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        createGuestSession();
        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        textViewRateCount= findViewById(R.id.textViewRateCount);
        textViewRateAvg = findViewById(R.id.textViewRateAvg);
        textlanguage = findViewById(R.id.textViewlanguage);
        watchIcon = findViewById(R.id.watchIcon);
        favoriteIcon = findViewById(R.id.favoriteIcon);
        buttonRateMovie = findViewById(R.id.buttonRateMovie);
        movie = (Movie) getIntent().getSerializableExtra("movie");
        MovieDatabaseHelper dbHelper = new MovieDatabaseHelper(this);
        if (movie != null) {
            // Set movie details
            textViewTitle.setText(movie.getOriginalTitle());
            textViewReleaseDate.setText("Release Date: " + movie.getReleaseDate());
            textViewOverview.setText(movie.getOverview());
            textlanguage.setText("language: "+movie.getOriginalLanguage());
            textViewRateCount.setText("Rating Count: "+movie.getVoteCount());
            textViewRateAvg.setText("Rating Avrage: "+movie.getVoteAverage());

            String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getBackdropPath();
            Glide.with(this)
                    .load(imageUrl)
                    .into(imageViewPoster);
            boolean isInFavorites = dbHelper.isExist(movie.getId());
            boolean isInWatchlist = dbHelper.isMovieInWatchlist(movie.getId());
            updateIconStates();
            favoriteIcon.setOnClickListener(v -> {
                if (dbHelper.isExist(movie.getId())) {
                    dbHelper.removeMovie(movie.getId());
                    favoriteIcon.setImageResource(R.drawable.favorate_moviedetails);
                } else {
                    dbHelper.insertMovie(movie.getId());
                    favoriteIcon.setImageResource(R.drawable.favorate_moviedetails_clicked);
                }
            });

            watchIcon.setOnClickListener(v -> {
                if (dbHelper.isMovieInWatchlist(movie.getId())) {
                    dbHelper.removeMovieFromWatchlist(movie.getId());
                    watchIcon.setImageResource(R.drawable.watchlist_moviedetail);
                } else {
                    dbHelper.insertMovieToWatchlist(movie.getId());
                    watchIcon.setImageResource(R.drawable.watchlist_moviedetaail_clicked);
                }
            });
            buttonRateMovie.setOnClickListener(v -> {
                if (guestSessionId == null) {
                    Toast.makeText(MovieDetailsActivity.this, "Guest session not available", Toast.LENGTH_SHORT).show();
                    return;
                }
                showRatingDialog();
            });

        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        updateIconStates();
    }


    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Rate Movie");

        // Set up the input (Number Picker)
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(20);
        numberPicker.setDisplayedValues(new String[]{
                "0.5", "1.0", "1.5", "2.0", "2.5", "3.0", "3.5", "4.0", "4.5",
                "5.0", "5.5", "6.0", "6.5", "7.0", "7.5", "8.0", "8.5", "9.0",
                "9.5", "10.0"
        });
        numberPicker.setWrapSelectorWheel(false);

        builder.setView(numberPicker);

        // Set up the buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            int valueIndex = numberPicker.getValue() - 1;
            double rating = 0.5 + (0.5 * valueIndex);
            submitRating(rating);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }



    private void submitRating(double rating) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            String key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiNTdiZGRmMjNmYWE3ZGU3YWIzOGI0OWMyOTZkZjVkNCIsIm5iZiI6MTczMTM0MTIzNy44Nzc5NzI0LCJzdWIiOiI2NzMyMmEzODBkNzU4MDQwZWI0YjFjMzYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tAcIRUOQtdPSkaEo_7c9ah6CPJAeYgwVYD11ntBdp4Q";

            String url = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/rating?guest_session_id=" + guestSessionId;

            MediaType mediaType = MediaType.parse("application/json");
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("value", rating);
            } catch (JSONException e) {
                e.printStackTrace();
                handler.post(() -> Toast.makeText(MovieDetailsActivity.this, "Failed to create JSON body", Toast.LENGTH_SHORT).show());
                return;
            }

            RequestBody body = RequestBody.create(mediaType, jsonBody.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + key)
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.body() != null) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    int statusCode = jsonResponse.getInt("status_code");
                    String statusMessage = jsonResponse.getString("status_message");

                    handler.post(() -> {
                        // Display the status message to the user
                        Toast.makeText(MovieDetailsActivity.this, statusMessage, Toast.LENGTH_LONG).show();
                    });
                } else {
                    handler.post(() -> Toast.makeText(MovieDetailsActivity.this, "Failed to submit rating", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                handler.post(() -> Toast.makeText(MovieDetailsActivity.this, "An error occurred", Toast.LENGTH_SHORT).show());
            }
        });
    }





    private void createGuestSession() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            OkHttpClient client = new OkHttpClient();
            String key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiNTdiZGRmMjNmYWE3ZGU3YWIzOGI0OWMyOTZkZjVkNCIsIm5iZiI6MTczMTM0MTIzNy44Nzc5NzI0LCJzdWIiOiI2NzMyMmEzODBkNzU4MDQwZWI0YjFjMzYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tAcIRUOQtdPSkaEo_7c9ah6CPJAeYgwVYD11ntBdp4Q";

            String url = "https://api.themoviedb.org/3/authentication/guest_session/new";

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + key)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonResponse);
                    guestSessionId = jsonObject.getString("guest_session_id");
                    // You might want to save this ID for future use
                } else {
                    handler.post(() -> Toast.makeText(MovieDetailsActivity.this, "Failed to create guest session", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                handler.post(() -> Toast.makeText(MovieDetailsActivity.this, "An error occurred", Toast.LENGTH_SHORT).show());
            }
        });
    }

    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }



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
    private void updateIconStates() {
        MovieDatabaseHelper dbHelper = new MovieDatabaseHelper(this);

        boolean isInFavorites = dbHelper.isExist(movie.getId());
        boolean isInWatchlist = dbHelper.isMovieInWatchlist(movie.getId());

        if (isInFavorites) {
            favoriteIcon.setImageResource(R.drawable.favorate_moviedetails_clicked);
        } else {
            favoriteIcon.setImageResource(R.drawable.favorate_moviedetails);
        }

        if (isInWatchlist) {
            watchIcon.setImageResource(R.drawable.watchlist_moviedetaail_clicked);
        } else {
            watchIcon.setImageResource(R.drawable.watchlist_moviedetail);
        }
    }

}