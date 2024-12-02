package com.example.mainactivity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.example.mainactivity.Movie;
import com.example.mainactivity.MovieDetailsActivity;
import com.example.mainactivity.R;
import com.example.mainactivity.Favorite.MovieDatabaseHelper;

import java.util.List;
public class WatchListAdapter extends RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder> {


    private List<Movie> watchList;
    private Context context;

    public WatchListAdapter(Context context, List<Movie> watchList) {
        this.context = context;
        this.watchList = watchList;
    }

    @NonNull
    @Override
    public WatchListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_watchlist, parent, false);
        return new WatchListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchListViewHolder holder, int position) {
        Movie movie = watchList.get(position);

        holder.textTitle.setText(movie.getTitle());
        holder.textOverview.setText(movie.getOverview());

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

        holder.watchDeleteIcon.setOnClickListener(v -> {
            MovieDatabaseHelper dbHelper = new MovieDatabaseHelper(context);
            dbHelper.removeMovieFromWatchlist(movie.getId());
            watchList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, watchList.size());
            Toast.makeText(context, context.getString(R.string.removedFromWatchList), Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return watchList.size();
    }

    public static class WatchListViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;
        TextView textTitle;
        TextView textOverview;
        ImageButton watchDeleteIcon;

        public WatchListViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.posterImage1);
            textTitle = itemView.findViewById(R.id.textTitle2);
            textOverview = itemView.findViewById(R.id.textOverview2);
            watchDeleteIcon = itemView.findViewById(R.id.watchDeleteIcon);
        }
    }




}
