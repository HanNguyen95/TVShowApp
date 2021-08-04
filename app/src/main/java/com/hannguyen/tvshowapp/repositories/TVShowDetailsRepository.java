package com.hannguyen.tvshowapp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.hannguyen.tvshowapp.network.ApiClient;
import com.hannguyen.tvshowapp.network.ApiService;
import com.hannguyen.tvshowapp.responses.TVShowDetailsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailsRepository {
    private ApiService apiService;

    public TVShowDetailsRepository(){
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public MutableLiveData<TVShowDetailsResponse> getTVShowDetails(String tvShowId){
        MutableLiveData<TVShowDetailsResponse> data = new MutableLiveData<>();
        apiService.getTVShowDetails(tvShowId).enqueue(new Callback<TVShowDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TVShowDetailsResponse> call,@NonNull Response<TVShowDetailsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull  Call<TVShowDetailsResponse> call,@NonNull Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }

}
