package com.example.mainactivity;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {


    @Override
    protected List<Movie> doInBackground(Void... voids) {
        List<Movie> movieList = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=1&sort_by=popularity.desc&year=2024")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer YOUR_ACCESS_TOKEN")
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();

                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray results = jsonObject.getJSONArray("results");

                for (int i = 0; i < results.length(); i++) {
                    JSONObject movieObject = results.getJSONObject(i);

                    boolean adult = movieObject.getBoolean("adult");
                    String backdropPath = movieObject.getString("backdrop_path");

                    // Convert genre_ids JSONArray to List<Integer>
                    JSONArray genreArray = movieObject.getJSONArray("genre_ids");
                    List<Integer> genreIds = new ArrayList<>();
                    for (int j = 0; j < genreArray.length(); j++) {
                        genreIds.add(genreArray.getInt(j));
                    }

                    int id = movieObject.getInt("id");
                    String originalLanguage = movieObject.getString("original_language");
                    String originalTitle = movieObject.getString("original_title");
                    String overview = movieObject.getString("overview");
                    double popularity = movieObject.getDouble("popularity");
                    String posterPath = movieObject.getString("poster_path");
                    String releaseDate = movieObject.getString("release_date");
                    String title = movieObject.getString("title");
                    boolean video = movieObject.getBoolean("video");
                    double voteAverage = movieObject.getDouble("vote_average");
                    int voteCount = movieObject.getInt("vote_count");

                    Movie movie = new Movie(adult, backdropPath, genreIds, id, originalLanguage, originalTitle,
                            overview, popularity, posterPath, releaseDate, title, video,
                            voteAverage, voteCount);
                    movieList.add(movie);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
