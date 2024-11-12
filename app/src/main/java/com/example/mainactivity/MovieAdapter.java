package com.example.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.mainactivity.Favorite.MovieDatabaseHelper;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static List<Movie> movies;
    Context context;



    public MovieAdapter(Context context, List<Movie> movies) {
        this.movies = movies; this.context=context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        String x = String.valueOf(movie.getId());
        holder.textTitle.setText(x);
        holder.textOverview.setText(movie.getOverview());
        MovieDatabaseHelper dbHelper = new MovieDatabaseHelper(context);

        boolean isExist = dbHelper.isExist(movie.getId());

        // Set initial favorite icon state
        if (isExist) {
            holder.btnFavorite.setImageResource(R.drawable.baseline_favorite_24_cllicked);
        } else {
            holder.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
        }

        // Load poster image with Glide
        String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(holder.posterView)
                .load(imageUrl)
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(holder.posterView);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), MovieDetailsActivity.class);
            intent.putExtra("movie", movie);
            v.getContext().startActivity(intent);
        });

        // Set up favorite button toggle functionality
        holder.btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.isExist(movie.getId())) {
                    // Remove from favorites if it exists
                  dbHelper.removeMovie(movie.getId());
                    holder.btnFavorite.setImageResource(R.drawable.baseline_favorite_border_24);
                    Toast.makeText(v.getContext(), "Removed from Favorites", Toast.LENGTH_SHORT).show();
                } else {
                    // Add to favorites if it doesn't exist
                    dbHelper.insertMovie(movie.getId());
                    holder.btnFavorite.setImageResource(R.drawable.baseline_favorite_24_cllicked);
                    Toast.makeText(v.getContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView textTitle;
        TextView textOverview;
        TextView textRateCount;
        TextView textRateAvg;
        TextView textlanguage;
        ImageView posterView;

        ImageButton btnWatchLater, btnFavorite, btnViewDetails;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textOverview = itemView.findViewById(R.id.textOverview);
            textRateCount = itemView.findViewById(R.id.textViewRateCount);
            textRateAvg = itemView.findViewById(R.id.textViewRateAvg);
            textlanguage= itemView.findViewById(R.id.textViewlanguage);
            posterView = itemView.findViewById(R.id.posterImage);
            btnFavorite = itemView.findViewById(R.id.favoriteIcon);
            btnWatchLater = itemView.findViewById(R.id.watchIcon);
            btnViewDetails = itemView.findViewById(R.id.detailsIcon);




        }
    }
}
