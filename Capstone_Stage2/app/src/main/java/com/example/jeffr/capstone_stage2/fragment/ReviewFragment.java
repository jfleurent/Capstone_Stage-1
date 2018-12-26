package com.example.jeffr.capstone_stage2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.RetrofitService;
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.data.Photo;
import com.example.jeffr.capstone_stage2.data.Restaurant;
import com.example.jeffr.capstone_stage2.data.RestaurantInfo;
import com.example.jeffr.capstone_stage2.data.Review;
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

public class ReviewFragment extends Fragment implements RecyclerViewOnClick {

  private Restaurant restaurant;
  private RecyclerView recyclerView;

  public ReviewFragment() {

  }

  // TODO: Rename and change types and number of parameters
  public static ReviewFragment newInstance(Restaurant restaurant) {
    ReviewFragment fragment = new ReviewFragment();
    Bundle args = new Bundle();
    args.putSerializable("Restaurant",restaurant);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    View rootView = inflater.inflate(R.layout.fragment_review, container, false);
    recyclerView = rootView.findViewById(R.id.reviews_recyclerview);
    restaurant = (Restaurant) getArguments().get("Restaurant");
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(new SimpleRecyclerViewAdapter<>(restaurant.getReviews(),R.layout.review_list_item, this));
    return rootView;
  }

  @Override public void rowSelected(Object obj) {

  }
}
