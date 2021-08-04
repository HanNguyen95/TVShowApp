package com.hannguyen.tvshowapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hannguyen.tvshowapp.models.TVShow;
import com.hannguyen.tvshowapp.network.ApiClient;
import com.hannguyen.tvshowapp.network.ApiService;
import com.hannguyen.tvshowapp.responses.TVShowResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowsRepository {
    private ApiService apiService;

    public MostPopularTVShowsRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TVShowResponse> getMostPopularTVShows(int page){
        MutableLiveData<TVShowResponse> data = new MutableLiveData<>();
        apiService.getMostPopularTVShows(page).enqueue(new Callback<TVShowResponse>() {
            @Override
            public void onResponse(Call<TVShowResponse> call, Response<TVShowResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TVShowResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }
}
