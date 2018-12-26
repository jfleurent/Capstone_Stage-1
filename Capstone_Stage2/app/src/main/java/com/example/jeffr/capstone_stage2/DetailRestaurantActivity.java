package com.example.jeffr.capstone_stage2;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.jeffr.capstone_stage2.adapters.SectionsPagerAdapter;
import com.example.jeffr.capstone_stage2.adapters.TabAdapter;
import com.example.jeffr.capstone_stage2.data.Photo;
import com.example.jeffr.capstone_stage2.data.Restaurant;
import com.example.jeffr.capstone_stage2.data.RestaurantId;
import com.example.jeffr.capstone_stage2.data.RestaurantInfo;
import com.example.jeffr.capstone_stage2.data.Review;
import com.example.jeffr.capstone_stage2.databinding
        .ActivityDetailRestaurantBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

//TODO remove toolbar and add the one in layout to make it move
public class DetailRestaurantActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://maps.googleapis.com";
    private static final String PHOTO_QUERY = "/maps/api/place/photo?maxwidth=400&photoreference=";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Restaurant restaurant;
    private List<Review> reviews;
    private List<Photo> photos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        getSupportActionBar().hide();
        restaurant = (Restaurant) getIntent().getExtras().getSerializable("Restaurant");
        ActivityDetailRestaurantBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_detail_restaurant);
        binding.setRestaurant(restaurant);
        viewPager = findViewById(R.id.detail_restaurant_viewpager);
        tabLayout = findViewById(R.id.tabs);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitService retrofitService = retrofit
                .create(RetrofitService.class);

        Map<String, String> params = new HashMap<>();
        params.put("key", getResources().getString(R.string.google_maps_api));
        String coordinates = "" + restaurant.getLatitude() + "," + restaurant.getLongitude();
        params.put("location", coordinates);
        params.put("radius", "10");
        params.put("keyword",restaurant.getName());

        final Call<RestaurantId> restaurantIdResponse = retrofitService
                .getRestaurantId(params);

        restaurantIdResponse.enqueue(new Callback<RestaurantId>() {
            @Override
            public void onResponse(Call<RestaurantId> call, Response<RestaurantId> response) {
                Timber.d(call.request().toString());
                String placeId = "";
                for (RestaurantId.Candidate candidate : response.body().getCandidates()) {
                    placeId = candidate.getPlace_id();
                }

                Map<String, String> infoParams = new HashMap<>();
                infoParams.put("key", getResources().getString(R.string.google_maps_api));
                infoParams.put("placeid", placeId);

                Call<RestaurantInfo> restaurantInfoResponse = retrofitService
                        .getRestaurantInfo(infoParams);

                restaurantInfoResponse.enqueue(new Callback<RestaurantInfo>() {
                    @Override
                    public void onResponse(Call<RestaurantInfo> call, Response<RestaurantInfo> response) {
                        Timber.d(call.request().toString());
                        List<RestaurantInfo.PhotoReference> photoReferences;
                        photos = new ArrayList<>();
                        reviews = response.body().getCandidates().getReviews();
                        photoReferences = response.body().getCandidates().getPhotos();
                        for(RestaurantInfo.PhotoReference reference : photoReferences){
                            photos.add(new Photo(getPhotoUrl(reference.getPhotoReference())));
                        }
                        restaurant.setPhotos(photos);
                        restaurant.setReviews(reviews);
                        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), restaurant));
                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                        tabLayout.addOnTabSelectedListener(new TabAdapter(viewPager));
                    }

                    @Override
                    public void onFailure(Call<RestaurantInfo> call, Throwable t) {
                        Timber.d("restaurantInfoResponse: Response Failed");
                    }
                });
            }

            @Override
            public void onFailure(Call<RestaurantId> call, Throwable t) {
                Timber.d("restaurantIdResponse: Response Failed");
            }
        });
    }

    public void favoriteOnClick(View view) {
        ViewDialog.showSelectFavoriteDialog(this);
    }

    public void directionsOnClick(View view) {
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + restaurant
                .getLatitude() + "," + restaurant.getLongitude());
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private String getPhotoUrl(String reference) {
        return BASE_URL + PHOTO_QUERY + reference + "&key=" + getResources().getString(
                R.string.google_maps_api);
    }

}
