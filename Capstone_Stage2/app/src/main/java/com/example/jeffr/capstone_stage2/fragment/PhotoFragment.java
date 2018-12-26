package com.example.jeffr.capstone_stage2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.RetrofitService;
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.data.Photo;
import com.example.jeffr.capstone_stage2.data.Restaurant;
import com.example.jeffr.capstone_stage2.data.RetrofitParsePhotos;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.gson.Gson;

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

public class PhotoFragment extends Fragment implements RecyclerViewOnClick {

    private static final String BASE_URL = "https://maps.googleapis.com";
    private static final String PHOTO_QUERY = "/maps/api/place/photo?maxwidth=400&photoreference=";
    private Restaurant restaurant;
    private RecyclerView recyclerView;

    public PhotoFragment() {

    }

    public static PhotoFragment newInstance(Restaurant restaurant) {
        PhotoFragment fragment = new PhotoFragment();
        Bundle args = new Bundle();
        args.putSerializable("Restaurant", restaurant);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
            Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_photo, container,
                false);

        recyclerView = rootView.findViewById(R.id.photos_recyclerview);

        restaurant = (Restaurant) getArguments().get("Restaurant");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService retrofitService = retrofit
                .create(RetrofitService.class);

        Map<String, String> params = new HashMap<>();
        params.put("key", getResources().getString(R.string.google_maps_api));
        String coordinates = "" + restaurant.getLatitude() + "," + restaurant.getLongitude();
        params.put("location", coordinates);
        params.put("radius", "49999");
        params.put("keyword",restaurant.getName());

        Call<RetrofitParsePhotos> photoResponse = retrofitService
                .getRestaurantPhotosReferences(params);

        photoResponse.enqueue(new Callback<RetrofitParsePhotos>() {
            @Override
            public void onResponse(Call<RetrofitParsePhotos> call,
                    Response<RetrofitParsePhotos> response) {
                Timber.d(call.request().toString());
                List<Photo> photos = new ArrayList<>();
                for (RetrofitParsePhotos.Candidate candidate : response.body().getCandidates()) {
                    if (candidate.getPhotos() != null) {
                        for (RetrofitParsePhotos.PhotoReference reference : candidate.getPhotos()) {
                            photos.add(new Photo(getPhotoUrl(reference.getPhoto_reference())));
                        }
                    }
                }
                restaurant.setPhotos(photos);
                recyclerView.setAdapter(new SimpleRecyclerViewAdapter<>(restaurant
                        .getPhotos(), R.layout.photo_list_item, PhotoFragment.this));
            }

            @Override
            public void onFailure(Call<RetrofitParsePhotos> call, Throwable t) {
                Timber.d("Response Failed");
            }
        });

        restaurant = (Restaurant) getArguments().getSerializable
                ("Restaurant");

        StaggeredGridLayoutManager staggeredGridLayoutManager = new
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager
                .VERTICAL);

        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager
                .GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(new SimpleRecyclerViewAdapter<>(restaurant
                .getPhotos(), R.layout.photo_list_item, PhotoFragment.this));

        return rootView;
    }

    @Override
    public void rowSelected(Object obj) {

    }

    private String getPhotoUrl(String reference) {
        return BASE_URL + PHOTO_QUERY + reference + "&key=" + getResources().getString(
                R.string.google_maps_api);
    }
}
