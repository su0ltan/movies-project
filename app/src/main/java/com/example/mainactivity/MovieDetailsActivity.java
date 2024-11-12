package com.example.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView imageViewPoster;
    private TextView textViewTitle, textViewReleaseDate, textViewOverview, textViewRateCount, textViewRateAvg, textlanguage;
    private Button buttonAddWatchlist, buttonAddFavorites, buttonRateMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Movie Details");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageViewPoster = findViewById(R.id.imageViewPoster);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        textViewRateCount= findViewById(R.id.textViewRateCount);
        textViewRateAvg = findViewById(R.id.textViewRateAvg);
        textlanguage = findViewById(R.id.textViewlanguage);
        buttonAddWatchlist = findViewById(R.id.buttonAddWatchlist);
        buttonAddFavorites = findViewById(R.id.buttonAddFavorites);
        buttonRateMovie = findViewById(R.id.buttonRateMovie);
        Movie movie = (Movie) getIntent().getSerializableExtra("movie");

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
        }
        // TODO: Implement button functionalities (Add to Watchlist, Favorites, Rate Movie)
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
}