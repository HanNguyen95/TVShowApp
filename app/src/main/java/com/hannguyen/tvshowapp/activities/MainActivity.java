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

import com.hannguyen.tvshowapp.R;
import com.hannguyen.tvshowapp.adapters.TVShowsAdapter;
import com.hannguyen.tvshowapp.broadcast.NetworkBroadCast;
import com.hannguyen.tvshowapp.databinding.ActivityMainBinding;
import com.hannguyen.tvshowapp.listeners.TVShowsListener;
import com.hannguyen.tvshowapp.models.TVShow;
import com.hannguyen.tvshowapp.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TVShowsListener {

    private ActivityMainBinding activityMainBinding;
    private MostPopularTVShowsViewModel viewModel;
    private List<TVShow> tvShows = new ArrayList<>();
    private TVShowsAdapter tvShowsAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private NetworkBroadCast networkBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkBroadCast = new NetworkBroadCast();
        activityMainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        doInitialization();
    }

    private void doInitialization(){
        activityMainBinding.tvShowRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(MainActivity.this).get(MostPopularTVShowsViewModel.class);
        tvShowsAdapter = new TVShowsAdapter(tvShows,this::onTVShowClicked);
        activityMainBinding.tvShowRecyclerView.setAdapter(tvShowsAdapter);
        activityMainBinding.tvShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!activityMainBinding.tvShowRecyclerView.canScrollVertically(1)){
                    if(currentPage <= totalAvailablePages){
                        currentPage +=1;
                        getMostPopularTVShows();
                    }
                }
            }
        });
        activityMainBinding.imageWatchList.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),WatchlistActivity.class)));
        activityMainBinding.imageSearch.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),SearchActivity.class)));
        getMostPopularTVShows();
    }

    private void getMostPopularTVShows(){
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, mostPopularTVShowsResponse->{
            toggleLoading();
            if(mostPopularTVShowsResponse != null){
                totalAvailablePages = mostPopularTVShowsResponse.getPages();
                if(mostPopularTVShowsResponse.getTvShows() != null){
                    int oldCount = tvShows.size();
                    tvShows.addAll(mostPopularTVShowsResponse.getTvShows());
                    tvShowsAdapter.notifyItemRangeInserted(oldCount,tvShows.size());
                }
            }
        });
    }

    private void toggleLoading(){
        if(currentPage == 1){
            if(activityMainBinding.getIsLoading() !=null && activityMainBinding.getIsLoading()){
                activityMainBinding.setIsLoading(false);
            }else{
                activityMainBinding.setIsLoading(true);
            }
        }else{
            if(activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()){
                activityMainBinding.setIsLoadingMore(false);
            }else{
                activityMainBinding.setIsLoadingMore(true);
            }
        }
    }

    @Override
    public void onTVShowClicked(TVShow tvShow) {
        Intent intent = new Intent(getApplicationContext(),TVShowDetailsActivity.class);
//        intent.putExtra("id",tvShow.getId());
//        intent.putExtra("name",tvShow.getName());
//        intent.putExtra("startDate",tvShow.getStartDate());
//        intent.putExtra("country",tvShow.getCountry());
//        intent.putExtra("network",tvShow.getNetwork());
//        intent.putExtra("status",tvShow.getStatus());
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