package com.example.mainactivity;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.example.mainactivity.models.Person;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Movie implements Serializable{

    public boolean isItInFavorite = false;
    public boolean isItInWatch = false;
    public List<Person>actors;
    private boolean adult;
    private String backdropPath;
    private List<Integer> genreIds;

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> genres;
    public static final List<Map<String, Object>> GENRES;

    static {
        List<Map<String, Object>> genres = new ArrayList<>();
        genres.add(createGenre(28, "Action"));
        genres.add(createGenre(12, "Adventure"));
        genres.add(createGenre(16, "Animation"));
        genres.add(createGenre(35, "Comedy"));
        genres.add(createGenre(80, "Crime"));
        genres.add(createGenre(99, "Documentary"));
        genres.add(createGenre(18, "Drama"));
        genres.add(createGenre(10751, "Family"));
        genres.add(createGenre(14, "Fantasy"));
        genres.add(createGenre(36, "History"));
        genres.add(createGenre(27, "Horror"));
        genres.add(createGenre(10402, "Music"));
        genres.add(createGenre(9648, "Mystery"));
        genres.add(createGenre(10749, "Romance"));
        genres.add(createGenre(878, "Science Fiction"));
        genres.add(createGenre(10770, "TV Movie"));
        genres.add(createGenre(53, "Thriller"));
        genres.add(createGenre(10752, "War"));
        genres.add(createGenre(37, "Western"));
        GENRES = Collections.unmodifiableList(genres); // Make the list unmodifiable
    }
    private static Map<String, Object> createGenre(int id, String name) {
        Map<String, Object> genre = new HashMap<>();
        genre.put("id", id);
        genre.put("name", name);
        return Collections.unmodifiableMap(genre); // Make the map unmodifiable
    }

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
                 double voteAverage, int voteCount, int id, boolean adult, List<Integer> genreIds, String originalTitle , String originalLanguage, List<String> genre, List<Person> p) {
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
        this.genres = genre;
        this.actors = p;
    }
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

        List<Person> p;

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

                p = getActors(id);

                // Parse genre IDs
                List<Integer> genreIds = new ArrayList<>();
                List<String> genres = new ArrayList<>();

                JSONArray genreIdsArray = movieObject.getJSONArray("genre_ids");
                for (int j = 0; j < genreIdsArray.length(); j++) {
                   genreIds.add(genreIdsArray.getInt(j));
                    int ida= genreIdsArray.getInt(j);

                    GENRES.forEach(stringObjectMap -> {
                        if ((int)stringObjectMap.get("id") == (ida)){
//                            System.out.println(title);
//
//                            System.out.println("Text changed to: " + stringObjectMap.get("name"));
                           genres.add(stringObjectMap.get("name").toString());
                        }

                    });



                }

                // Create Movie object and add to list
                Movie movie = new Movie(title, overview, posterPath, backdropPath, releaseDate,
                        voteAverage, voteCount, id, adult, genreIds, originalTitle, originalLanguage, genres, p);
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    private static List<Person> getActors(int id) {

        final List<Person>[] persons = new List[]{new ArrayList<>()};


         final ExecutorService executorService = Executors.newSingleThreadExecutor();
         final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
        executorService.execute(() -> {
            OkHttpClient client = new OkHttpClient();

            String key = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiNTdiZGRmMjNmYWE3ZGU3YWIzOGI0OWMyOTZkZjVkNCIsIm5iZiI6MTczMTM0MTIzNy44Nzc5NzI0LCJzdWIiOiI2NzMyMmEzODBkNzU4MDQwZWI0YjFjMzYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.tAcIRUOQtdPSkaEo_7c9ah6CPJAeYgwVYD11ntBdp4Q";


            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/"+id+"/credits?language=en-US")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer "+ key)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    // Parse response to list of movies


                    persons[0] = Person.parsePerson(response.body().string());




                    // Update UI on the main thread
                } else {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return persons[0];

    }

    public static Movie parseMovie(String jsonResponse) {

        JSONObject jsonObject = null;
        Movie movie = null;
        try {
            jsonObject = new JSONObject(jsonResponse);
            List <Integer> genreIds = new ArrayList<>();
         JSONArray genreIdsArray = jsonObject.getJSONArray("genres");



         List<Person> p = new ArrayList<>();

         List<String> genre = new ArrayList<>();
            for (int j = 0; j < genreIdsArray.length(); j++) {
                genre.add(genreIdsArray.getString(j));
            }
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
                    jsonObject.getString("original_title"),
                    genre,
                    p




            );
        } catch (JSONException e) {
            throw new RuntimeException(e);


        }



        return movie;

    }


}
