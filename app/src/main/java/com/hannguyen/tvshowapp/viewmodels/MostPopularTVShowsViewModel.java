package com.hannguyen.tvshowapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hannguyen.tvshowapp.repositories.MostPopularTVShowsRepository;
import com.hannguyen.tvshowapp.responses.TVShowResponse;

public class MostPopularTVShowsViewModel extends ViewModel {
    private MostPopularTVShowsRepository mostPopularTVShowsRepository;

    public MostPopularTVShowsViewModel(){
        mostPopularTVShowsRepository = new MostPopularTVShowsRepository();
    }

    public LiveData<TVShowResponse> getMostPopularTVShows(int page){
        return mostPopularTVShowsRepository.getMostPopularTVShows(page);
    }
}
