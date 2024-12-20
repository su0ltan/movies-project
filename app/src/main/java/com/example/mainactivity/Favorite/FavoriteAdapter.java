package com.example.mainactivity.Favorite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.mainactivity.Movie;
import com.example.mainactivity.MovieDetailsActivity;
import com.example.mainactivity.R;

import java.util.List;

import android.content.Context;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private List<Movie> favoriteList; // Replace String with your actual data type
    private final Context context;

    // Constructor
        public FavoriteAdapter(Context context, List<Movie> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }
    public void updateData(Movie newMovie) {
        this.favoriteList.add(newMovie);
        String x = String.valueOf(favoriteList.size());
        Log.d("ccc", favoriteList.toString());

    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(com.example.mainactivity.R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        // Bind data to each item in the list


        Movie movie = favoriteList.get(position);

        if (movie == null || position >= favoriteList.size()) {
            return; // Avoid binding if data is inconsistent
        }
        holder.textTitle.setText(movie.getTitle());
        holder.textOverview.setText(movie.getOverview());
        holder.favDeleteIcon.setOnClickListener(v -> {
            MovieDatabaseHelper dbHelper = new MovieDatabaseHelper(context);
            dbHelper.removeMovie(movie.getId());
            favoriteList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, favoriteList.size());
        });
        String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(holder.posterImage)
                .load(imageUrl)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(holder.posterImage);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MovieDetailsActivity.class);
            intent.putExtra("movie", movie);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return favoriteList.size(); // Return the size of the dataset
    }

    // ViewHolder class to represent each item
    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        // Define item views, for example:
        // TextView titleText;
        ImageView posterImage;
        TextView textTitle;
        TextView textOverview;
        ImageButton favDeleteIcon;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialize item views, for example:
            // titleText = itemView.findViewById(R.id.titleText);

            posterImage = itemView.findViewById(R.id.posterImage1);
            textTitle = itemView.findViewById(R.id.textTitle2);
            textOverview = itemView.findViewById(R.id.textOverview2);
            favDeleteIcon = itemView.findViewById(R.id.favDeleteIcon);
        }


    }
}
