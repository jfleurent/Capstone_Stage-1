package com.example.jeffr.capstone_stage2;

import com.example.jeffr.capstone_stage2.models.RestaurantId;
import com.example.jeffr.capstone_stage2.models.RestaurantInfo;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RetrofitService {

    @GET("/maps/api/place/nearbysearch/json")
    Call<RestaurantId> getRestaurantId(@QueryMap Map<String, String> param);

    @GET("/maps/api/place/details/json")
    Call<RestaurantInfo> getRestaurantInfo(@QueryMap Map<String, String> param);
}
