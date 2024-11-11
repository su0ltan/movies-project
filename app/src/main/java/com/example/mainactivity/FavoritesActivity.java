package com.example.mainactivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mainactivity.Favorite.FavoriteAdapter;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FavoritesActivity extends AppCompatActivity {


    FavoriteAdapter favoriteAdapter ;
    RecyclerView favoriteRecylcle;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorites);

        favoriteRecylcle = findViewById(R.id.Favorites_recyclerViewMovies);
        favoriteRecylcle.setLayoutManager(new LinearLayoutManager(this));
        favoriteAdapter = new FavoriteAdapter(getApplicationContext(),new ArrayList<>());
        favoriteRecylcle.setAdapter(favoriteAdapter);



    }
}