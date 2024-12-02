package com.example.mainactivity.Favorite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MovieDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Movie.db";
    private static final int DATABASE_VERSION = 4;
    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_ID = "id";
    public static final String TABLE_WATCHLIST = "watchlist";
    public static final String COLUMN_WATCHLIST_ID = "id";

    public  static  final  String TABLE_RATE = "movieRate";
    public static final String COLUMN_RATE = "rate";
    public static final String COLUMN_RATE_ID = "movieId";




    public MovieDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_MOVIES_TABLE = "CREATE TABLE movies (" +
                "id INTEGER PRIMARY KEY" + // Store only the id
                ")";
        db.execSQL(CREATE_MOVIES_TABLE);

        String CREATE_WATCHLIST_TABLE = "CREATE TABLE " + TABLE_WATCHLIST + " (" +
                COLUMN_WATCHLIST_ID + " INTEGER PRIMARY KEY" +
                ")";
        db.execSQL(CREATE_WATCHLIST_TABLE);

        String CREATE_RATE_TABLE = "CREATE TABLE " + TABLE_RATE + " (" +
                COLUMN_RATE_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_RATE + " REAL" +
                ")";
        db.execSQL(CREATE_RATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrades if the version changes
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WATCHLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATE);
        onCreate(db);
    }

    public void insertRate(int movieId, double rate) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get a writable database instance
        ContentValues values = new ContentValues();

        values.put(COLUMN_RATE_ID, movieId); // Add the movieId
        values.put(COLUMN_RATE, rate);      // Add the rate

        // Insert the values into the database
        long result = db.insert(TABLE_RATE, null, values);

        if (result == -1) {
            Log.e("Database", "Failed to insert rate for movieId: " + movieId);
        } else {
            Log.d("Database", "Successfully inserted rate for movieId: " + movieId);
        }

        db.close(); // Close the database connection
    }
    public boolean isRated(int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM " + TABLE_RATE + " WHERE " + COLUMN_RATE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(movieId)});
        boolean exists = cursor.moveToFirst(); // Returns true if the cursor is not empty
        cursor.close();
        db.close();
        return exists;
    }
    public Double getRate(int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_RATE + " FROM " + TABLE_RATE + " WHERE " + COLUMN_RATE_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(movieId)});

        Double rate = null;
        if (cursor.moveToFirst()) {
            rate = cursor.getDouble(0); // Get the first column (rate) value
        }

        cursor.close();
        db.close();
        return rate; // Returns null if the movieId does not exist
    }




    public long insertMovie(int id) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);


        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(TABLE_MOVIES, null, values);

        // Close the database
        db.close();

        return newRowId;
    }



    public List<Integer> readMovies() {
        List<Integer> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to select all rows from the movies table
        Cursor cursor = db.query(
                TABLE_MOVIES,   // Table name
                null,           // Columns (null to select all)
                null,           // Selection (null to select all rows)
                null,           // Selection arguments
                null,           // Group by
                null,           // Having
                null            // Order by
        );

        // Iterate through the cursor and add each movie to the list
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));


                // Create a new Movie object and add it to the list

                movieList.add(id);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database
        cursor.close();
        db.close();

        return movieList;
    }

    public boolean isExist(int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM movies WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(movieId)});

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }


    public void removeMovie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("movies", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public long insertMovieToWatchlist(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WATCHLIST_ID, id);
        long newRowId = db.insert(TABLE_WATCHLIST, null, values);
        db.close();
        return newRowId;
    }

    public List<Integer> readWatchlistMovies() {
        List<Integer> movieList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_WATCHLIST,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_WATCHLIST_ID));
                movieList.add(id);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return movieList;
    }

    public boolean isMovieInWatchlist(int movieId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_WATCHLIST_ID + " FROM " + TABLE_WATCHLIST + " WHERE " + COLUMN_WATCHLIST_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(movieId)});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public void removeMovieFromWatchlist(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WATCHLIST, COLUMN_WATCHLIST_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }


}
