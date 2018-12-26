package com.example.jeffr.capstone_stage2;

import android.provider.ContactsContract;

import com.example.jeffr.capstone_stage2.data.RetrofitParsePhotos;
import com.example.jeffr.capstone_stage2.data.Review;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface RetrofitService {

    @GET("/maps/api/place/nearbysearch/json")
    Call<RetrofitParsePhotos> getRestaurantPhotosReferences(@QueryMap Map<String, String> param);

    @GET("/maps/api/place/details/json")
    Call<List<Review>> getRestaurantReviews(@QueryMap Map<String, String> param);
}
