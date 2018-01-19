package empolyesecurity.greendao;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.List;

import empolyesecurity.greendao.modelpojo.Result;


/**
 * Created by vivid on 1/11/17.
 */

public class RetrofitAdapter extends RecyclerView.Adapter<RetrofitAdapter.MovieViewHolder> {

    private List<Result> movies;
    private int rowLayout;
     Context context;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w150";

    public static class MovieViewHolder extends RecyclerView.ViewHolder {


        TextView data;
        TextView movieDescription;
        ImageView thumbnail;



        public MovieViewHolder(View v) {
            super(v);

            data = (TextView) v.findViewById(R.id.textViewName);
            movieDescription = (TextView) v.findViewById(R.id.textViewVersion);
            thumbnail = (ImageView) v.findViewById(R.id.imageView);
        }
    }

    public RetrofitAdapter(List<Result> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public RetrofitAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        holder.data.setText(movies.get(position).getTitle());
        holder.movieDescription.setText(movies.get(position).getReleaseDate());

        System.out.println("posterpathhhhhhhhhhhhhhh"+movies.get(position).getPosterPath());
     /*   Picasso.with(context)
                .load(BASE_URL_IMG+movies.get(position).getPosterPath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.thumbnail);*/
       /* Glide.with(context)
                .load(BASE_URL_IMG+movies.get(position).getPosterPath())
                .into(holder.thumbnail);*/


       /* Glide
                .with(context)
                .load(BASE_URL_IMG+movies.get(position).getPosterPath())
                .into(holder.thumbnail);*/
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}