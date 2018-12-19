package com.example.jeffr.capstone_stage2;

import com.example.jeffr.capstone_stage2.data.Restaurant;
import retrofit2.http.GET;

public interface RestaurantRetrofitService {

  @GET
  Call<Restaurant> getRestaurant()
}
