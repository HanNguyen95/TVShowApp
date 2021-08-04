package com.hannguyen.tvshowapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

import com.hannguyen.tvshowapp.R;
import com.hannguyen.tvshowapp.adapters.TVShowsAdapter;
import com.hannguyen.tvshowapp.broadcast.NetworkBroadCast;
import com.hannguyen.tvshowapp.databinding.ActivitySearchBinding;
import com.hannguyen.tvshowapp.listeners.TVShowsListener;
import com.hannguyen.tvshowapp.models.TVShow;
import com.hannguyen.tvshowapp.viewmodels.SearchViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchActivity extends AppCompatActivity implements TVShowsListener {

    private ActivitySearchBinding activitySearchBinding;
    private SearchViewModel searchViewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;
    private NetworkBroadCast networkBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        networkBroadCast = new NetworkBroadCast();
        activitySearchBinding = DataBindingUtil.setContentView(this,R.layout.activity_search);
        doInitialization();
    }

    private void doInitialization(){
     activitySearchBinding.imageBack.setOnClickListener(view->onBackPressed());
     activitySearchBinding.tvShowRecyclerView.setHasFixedSize(true);
     searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
     tvShowsAdapter = new TVShowsAdapter(tvShows,this);
     activitySearchBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);
     activitySearchBinding.inputSearch.addTextChangedListener(new TextWatcher() {
         @Override
         public void beforeTextChanged(CharSequence s, int start, int count, int after) {
         }

         @Override
         public void onTextChanged(CharSequence s, int start, int before, int count) {
             if(timer!=null){
                 timer.cancel();
             }
         }

         @Override
         public void afterTextChanged(Editable editable) {
             if(!editable.toString().trim().isEmpty()){
                 timer = new Timer();
                 timer.schedule(new TimerTask() {
                     @Override
                     public void run() {
                         new Handler(Looper.getMainLooper()).post(new Runnable() {
                             @Override
                             public void run() {
                                 currentPage = 1;
                                 totalAvailablePages = 1;
                                 searchTVShow(editable.toString());
                             }
                         });
                     }
                 },800);
             } else{
                 tvShows.clear();
                 tvShowsAdapter.notifyDataSetChanged();
             }
         }
     });
     activitySearchBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
         @Override
         public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
             super.onScrolled(recyclerView, dx, dy);
             if(!activitySearchBinding.tvShowRecyclerView.canScrollVertically(1)){
                 if(!activitySearchBinding.inputSearch.getText().toString().isEmpty()){
                     if(currentPage < totalAvailablePages){
                         currentPage +=1;
                         searchTVShow(activitySearchBinding.inputSearch.getText().toString());
                     }
                 }
             }
         }
     });
     activitySearchBinding.inputSearch.requestFocus();
    }

    private void searchTVShow(String query){
        toggleLoading();
        searchViewModel.searchTVShow(query,currentPage).observe(this,tvShowResponse -> {
            toggleLoading();
            if(tvShowResponse!= null){
                totalAvailablePages = tvShowResponse.getPages();
                if(tvShowResponse.getTvShows()!=null){
                    int oldCount = tvShows.size();
                    tvShows.addAll(tvShowResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount,tvShows.size());
                }
            }
        });
    }

    private void toggleLoading(){
        if(currentPage == 1){
            if(activitySearchBinding.getIsLoading() !=null && activitySearchBinding.getIsLoading()){
                activitySearchBinding.setIsLoading(false);
            }else{
                activitySearchBinding.setIsLoading(true);
            }
        }else{
            if(activitySearchBinding.getIsLoadingMore() != null && activitySearchBinding.getIsLoadingMore()){
                activitySearchBinding.setIsLoadingMore(false);
            }else{
                activitySearchBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(),TVShowDetailsActivity.class);
        intent.putExtra("tvShow",tvShow);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadCast,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(networkBroadCast);
    }
}