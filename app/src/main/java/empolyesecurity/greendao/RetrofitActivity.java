package empolyesecurity.greendao;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.Query;

import java.util.List;

import empolyesecurity.greendao.DBbeanclass.User;
import empolyesecurity.greendao.DBbeanclass.UserDao;
import empolyesecurity.greendao.api.MovieApi;
import empolyesecurity.greendao.api.MovieService;
import empolyesecurity.greendao.modelpojo.DaoSession;
import empolyesecurity.greendao.modelpojo.Movies;
import empolyesecurity.greendao.modelpojo.Result;
import empolyesecurity.greendao.modelpojo.ResultDao;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    MovieService movieService;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private Query<Result> notesQuery;


    private ResultDao resultDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        recyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        movieService = MovieApi.getClient().create(MovieService.class);
        DaoSession daoSession = ((AppController) getApplication()).getDaoSession();
        resultDao = daoSession.getResultDao();
        notesQuery = resultDao.queryBuilder().orderAsc(ResultDao.Properties.Title).build();

        if (AppController.checkConnection(RetrofitActivity.this)) {
            // Its Available...
            retrofitJsonParse();
            Toast.makeText(RetrofitActivity.this, "Available", Toast.LENGTH_SHORT).show();


        } else {
            // Not Available...
            databaseValues();


            Toast.makeText(RetrofitActivity.this, "Not Available", Toast.LENGTH_SHORT).show();
        }


       // retrofitJsonParse();
    }
    public void retrofitJsonParse(){
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        moviesCall().enqueue(new Callback<Movies>() {
            @Override
            public void onResponse(Call<Movies> call, Response<Movies> response) {
                Log.e("Success", new Gson().toJson(response.body()));

                List<Result> movies = response.body().getResults();
                // Movies resource = response.body();
                //  int statusCode = response.code();

                resultDao.deleteAll();
                for(int i=0;i<movies.size();i++){

                    String title = movies.get(i).getTitle();
                    String relesedate = movies.get(i).getReleaseDate();
                    String posterpath = movies.get(i).getPosterPath();
                    System.out.println("vvvvvvvvvvvvv"+title);

                        Result result = new Result();
                result.setTitle(title);
                result.setReleaseDate(relesedate);
                result.setPosterPath(posterpath);
                resultDao.insert(result);
                    Log.d("DaoExample", "Inserted new result, ID: " + result.getIds());
                }

                recyclerView.setAdapter(new RetrofitAdapter(movies, R.layout.card_view, getApplicationContext()));



                pDialog.cancel();

            }

            @Override
            public void onFailure(Call<Movies> call, Throwable t) {

                pDialog.dismiss();
            }
        });
    }
    private Call<Movies> moviesCall(){

        return movieService.getTopRatedMovies("ec01f8c2eb6ac402f2ca026dc2d9b8fd","en_US",1);
    }


    public void databaseValues(){

        List<Result> contacts = notesQuery.list();
        System.out.println("valuessssssssssss+"+contacts);
        if (contacts.size() > 0) {



            recyclerView.setAdapter(new RetrofitAdapter(contacts, R.layout.card_view, getApplicationContext()));
        }
    }
}
