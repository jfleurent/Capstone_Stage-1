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
import com.example.jeffr.capstone_stage2.data.RestaurantId;
import com.example.jeffr.capstone_stage2.data.RestaurantInfo;

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


}
