package com.hannguyen.tvshowapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.hannguyen.tvshowapp.repositories.SearchTVShowRepository;
import com.hannguyen.tvshowapp.responses.TVShowResponse;

public class SearchViewModel extends ViewModel {
    private SearchTVShowRepository searchTVShowRepository;
    public SearchViewModel(){
        searchTVShowRepository = new SearchTVShowRepository();
    }
    public LiveData<TVShowResponse> searchTVShow(String query,int page){
        return searchTVShowRepository.searchTVShow(query,page);
    }
}
