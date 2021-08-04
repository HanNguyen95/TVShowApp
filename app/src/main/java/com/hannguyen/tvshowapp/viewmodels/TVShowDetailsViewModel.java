package com.hannguyen.tvshowapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hannguyen.tvshowapp.database.TVShowsDatabase;
import com.hannguyen.tvshowapp.models.TVShow;
import com.hannguyen.tvshowapp.repositories.TVShowDetailsRepository;
import com.hannguyen.tvshowapp.responses.TVShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsViewModel extends AndroidViewModel {

    private TVShowDetailsRepository tvShowDetailsRepository;
    private TVShowsDatabase tvShowsDatabase;

    public TVShowDetailsViewModel(@NonNull Application application){
        super(application);
        tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowsDatabase = TVShowsDatabase.getTvShowsDatabase(application);
    }

    public MutableLiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        return tvShowDetailsRepository.getTVShowDetails(tvShowId);
    }

    public Completable addToWatchlist(TVShow tvShow){
        return tvShowsDatabase.tvShowDao().addToWatchList(tvShow);
    }

    public Flowable<TVShow> getTVShowFromWatchlist(String tvShowId){
        return tvShowsDatabase.tvShowDao().getTVShowFromWatchlist(tvShowId);
    }

    public Completable removeTVShowFromWatchlist(TVShow tvShow){
        return tvShowsDatabase.tvShowDao().removeFromWatchList(tvShow);
    }

}
