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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.mainactivity.Favorite.MovieDatabaseHelper;

public class MovieDetailsActivity extends AppCompatActivity {
    private ImageView imageViewPoster;
    private TextView textViewTitle, textViewReleaseDate, textViewOverview, textViewRateCount, textViewRateAvg, textlanguage;
    private Button  buttonRateMovie;
    private ImageButton watchIcon, favoriteIcon;
    Movie movie;

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
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        updateIconStates();
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