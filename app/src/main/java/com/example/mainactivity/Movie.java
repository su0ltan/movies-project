package com.example.mainactivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
public class Movie implements Serializable{

    public boolean isItInFavorite = false;
    public boolean isItInWatch = false;
    private boolean adult;
    private String backdropPath;
    private List<Integer> genreIds;
    private int id;
    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private String releaseDate;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    // Constructor
    public Movie(String title, String overview, String posterPath, String backdropPath, String releaseDate,
                 double voteAverage, int voteCount, int id, boolean adult, List<Integer> genreIds, String originalTitle , String originalLanguage) {
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.id = id;
        this.adult = adult;
        this.genreIds = genreIds;
        this.originalLanguage=originalLanguage;
        this.originalTitle=originalTitle;
    }

    // Getters and Setters
    public boolean isAdult() { return adult; }
    public void setAdult(boolean adult) { this.adult = adult; }

    public String getBackdropPath() { return backdropPath; }
    public void setBackdropPath(String backdropPath) { this.backdropPath = backdropPath; }

    public List<Integer> getGenreIds() { return genreIds; }
    public void setGenreIds(List<Integer> genreIds) { this.genreIds = genreIds; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getOriginalLanguage() { return originalLanguage; }
    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }

    public String getOriginalTitle() { return originalTitle; }
    public void setOriginalTitle(String originalTitle) { this.originalTitle = originalTitle; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public double getPopularity() { return popularity; }
    public void setPopularity(double popularity) { this.popularity = popularity; }

    public String getPosterPath() { return posterPath; }
    public void setPosterPath(String posterPath) { this.posterPath = posterPath; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isVideo() { return video; }
    public void setVideo(boolean video) { this.video = video; }

    public double getVoteAverage() { return voteAverage; }
    public void setVoteAverage(double voteAverage) { this.voteAverage = voteAverage; }

    public int getVoteCount() { return voteCount; }
    public void setVoteCount(int voteCount) { this.voteCount = voteCount; }


    public static List<Movie> parseMovies(String jsonResponse) {
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject movieObject = resultsArray.getJSONObject(i);

                String title = movieObject.getString("title");
                String overview = movieObject.getString("overview");
                String originalTitle = movieObject.optString("original_title","");
                String posterPath = movieObject.optString("poster_path", "");
                String backdropPath = movieObject.optString("backdrop_path", "");
                String releaseDate = movieObject.optString("release_date", "");
                String originalLanguage = movieObject.optString("original_language","");
                double voteAverage = movieObject.optDouble("vote_average", 0.0);
                int voteCount = movieObject.optInt("vote_count", 0);
                int id = movieObject.getInt("id");
                boolean adult = movieObject.getBoolean("adult");

                // Parse genre IDs
                List<Integer> genreIds = new ArrayList<>();
                JSONArray genreIdsArray = movieObject.getJSONArray("genre_ids");
                for (int j = 0; j < genreIdsArray.length(); j++) {
                    genreIds.add(genreIdsArray.getInt(j));
                }

                // Create Movie object and add to list
                Movie movie = new Movie(title, overview, posterPath, backdropPath, releaseDate,
                        voteAverage, voteCount, id, adult, genreIds, originalTitle, originalLanguage);
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }
    public static Movie parseMovie(String jsonResponse) {

        JSONObject jsonObject = null;
        Movie movie = null;
        try {
            jsonObject = new JSONObject(jsonResponse);
            List <Integer> genreIds = new ArrayList<>();
            movie  = new Movie(
                    jsonObject.getString("title"),
                    jsonObject.getString("overview"),
                    jsonObject.getString("poster_path"),
                    jsonObject.getString("backdrop_path"),
                    jsonObject.getString("release_date"),
                    jsonObject.getDouble("vote_average"),
                    jsonObject.getInt("vote_count"),
                    jsonObject.getInt("id"),
                    jsonObject.getBoolean("adult"),
                    genreIds,
                    jsonObject.getString("original_language"),
                    jsonObject.getString("original_title")
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);


        }



        return movie;

    }


}
