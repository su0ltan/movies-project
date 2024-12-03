package com.example.mainactivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;


import androidx.appcompat.app.AppCompatDelegate;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
    private ToggleButton toggleButton;
    private TextView tvAnimation, tvAll,tvAction, tvDrama, tvCrime;
    private boolean isShowingRecentMovies = true; // Default to showing recent movies


    private List<Movie> movies = new ArrayList<>(); // Avoid null issues

    private List<Movie> filteredMovies; // Filtered results


    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_main);

        recyclerViewMovies = findViewById(R.id.recyclerViewMovies);
        recyclerViewMovies.setLayoutManager(new LinearLayoutManager(this));

        filteredMovies = new ArrayList<>();


        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Recent Movies");
        }
        toggleButton = findViewById(R.id.toggleButton);


        // Initialize TextViews
        tvAnimation = findViewById(R.id.tvAnimation);
        tvAction = findViewById(R.id.tvAction);
        tvDrama = findViewById(R.id.tvDrama);
        tvCrime = findViewById(R.id.tvCrime);
        tvAll = findViewById(R.id.tvAll);

        // Set click listeners
        setGenreClickListener(tvAnimation);
        setGenreClickListener(tvAction);
        setGenreClickListener(tvDrama);
        setGenreClickListener(tvCrime);

        setGenreClickListener(tvAll);


        // Set up the ToggleButton listener
        toggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isShowingRecentMovies = !isChecked; // Toggle between recent and top rated
            updateMovieList();
            if (isChecked) {
                // State: Show Recent
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_watch_later_yellow, 0, 0, 0);

            } else {
                // State: Show Top Rated
                toggleButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_star_rate_24, 0, 0, 0);

            }
        });

        updateMovieList();
        editTextSearch = findViewById(R.id.editTextSearch);
        // Initialize with an empty list and update later
        movieAdapter = new MovieAdapter(getApplicationContext(),new ArrayList<>());
        recyclerViewMovies.setAdapter(movieAdapter);


        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // Fetch movies


    }

    private void setGenreClickListener(TextView textView) {
        textView.setOnClickListener(view -> {
            // Reset all TextView styles
            resetGenreStyles();

            // Apply shadow effect to the selected TextView
         textView.setBackground(getDrawable(R.drawable.cloud_text_selected_bg));

            if (textView.getId() == R.id.tvAll) {
                filterMovies(""); // Clear filter
            } else  if (textView.getId() == R.id.tvAction) {
                filterMovies("action"); // Clear filter
            }  if (textView.getId() == R.id.tvAnimation) {
                filterMovies("animation"); // Clear filter
            } else if (textView.getId() == R.id.tvCrime) {
                filterMovies("crime"); // Clear filter
            } else if (textView.getId() == R.id.tvDrama) {
                filterMovies("drama"); // Clear filter
            }
        });
    }


    private void resetGenreStyles() {
        resetTextViewStyle(tvAnimation);
        resetTextViewStyle(tvAction);
        resetTextViewStyle(tvDrama);
        resetTextViewStyle(tvCrime);
        resetTextViewStyle(tvAll);
    }


    private void resetTextViewStyle(TextView textView) {
        textView.setShadowLayer(0, 0, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.black));
        textView.setBackground(getDrawable(R.drawable.cloud_text_bg));
    }

// In MainActivity.java

    private void filterMovies(String query) {
        filteredMovies.clear();

        if (query.isEmpty() || query.equals("")) {
            filteredMovies.addAll(movies); // Show all movies if query is empty
        } else {
            String lowerCaseQuery = query.toLowerCase();

            for (Movie movie : movies) {
                // Check title or overview
                if (movie.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        movie.getOverview().toLowerCase().contains(lowerCaseQuery)) {
                    filteredMovies.add(movie);
                    continue; // Skip to the next movie
                }

                // Check genres
                for (String genre : movie.getGenres()) {
                    if (genre.toLowerCase().contains(lowerCaseQuery)) {
                        filteredMovies.add(movie);
                        break; // Match found, no need to check other genres
                    }
                }

                // Check actors
                for (String actor : movie.getActors()) {
                    if (actor.toLowerCase().contains(lowerCaseQuery)) {
                        filteredMovies.add(movie);
                        break; // Match found, no need to check other actors
                    }
                }
            }
        }

        // Update UI with the filtered list
        updateRecyclerView(filteredMovies);
    }





    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "resume", Toast.LENGTH_SHORT).show();

        movieAdapter.notifyDataSetChanged();

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

        if (id == R.id.action_change_language) {
            AppUtils.toggleLanguage(this, MainActivity.class);
            return true;
        } else if (id == R.id.action_watchlist) {
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





    @Override
    protected void attachBaseContext(Context newBase) {
        String language = newBase.getSharedPreferences("AppPrefs", MODE_PRIVATE)
                .getString("Language", Locale.getDefault().getLanguage());
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    private void fetchMovies(boolean fetchRecentMovies) {

        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();

                String key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiNTdiZGRmMjNmYWE3ZGU3YWIzOGI0OWMyOTZkZjVkNCIsIm5iZiI6MTczMTM0MTIzNy44Nzc5NzI0LCJzdWIiOiI2NzMyMmEzODBkNzU4MDQwZWI0YjFjMzYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tAcIRUOQtdPSkaEo_7c9ah6CPJAeYgwVYD11ntBdp4Q";
            String url;
            if (fetchRecentMovies) {
                // Fetch recent movies
                url = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&year=2024";
            } else {
                // Fetch top-rated movies (30 movies across two pages)
                url = "https://api.themoviedb.org/3/movie/top_rated?language=en-US&page=1";
            }

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer "+ key)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    // Parse response to list of movies

                    movies = Movie.parseMovies(response.body().string());






                    // Update UI on the main thread
                    mainThreadHandler.post(() -> {updateRecyclerView(movies);
                        movieAdapter.notifyDataSetChanged();
                        if (fetchRecentMovies) {
                            if (getSupportActionBar() != null) {
//                                getSupportActionBar().setTitle(getString(R.string.recentMovies));
                            }
                        } else {
                            if (getSupportActionBar() != null) {
//                                getSupportActionBar().setTitle(getString(R.string.topRatedMovies));
                            }
                        }});
                } else {
                    mainThreadHandler.post(() -> Toast.makeText(MainActivity.this, getString(R.string.failedToFetchMovies), Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                e.printStackTrace();
                mainThreadHandler.post(() -> Toast.makeText(MainActivity.this, getString(R.string.errorOccurred), Toast.LENGTH_SHORT).show());
            }
        });



    }

    private void updateRecyclerView(List<Movie> movies) {
        movieAdapter = new MovieAdapter(getApplicationContext(), movies);
            recyclerViewMovies.setAdapter(movieAdapter);
        TextView txtRecentMovies = findViewById(R.id.txtRecentMovies);
        if (isShowingRecentMovies) {
            txtRecentMovies.setText(getString(R.string.recentMovies));
        } else {
            txtRecentMovies.setText(getString(R.string.topRatedMovies));
        }
    }
    private void updateMovieList() {
        if (isShowingRecentMovies) {
            fetchMovies(true); // Fetch recent movies
        } else {
            fetchMovies(false); // Fetch top-rated movies
        }
    }






}