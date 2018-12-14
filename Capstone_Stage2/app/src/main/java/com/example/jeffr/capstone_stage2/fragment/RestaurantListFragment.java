package com.example.jeffr.capstone_stage2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jeffr.capstone_stage2.DetailCategoryFavoriteActivity;
import com.example.jeffr.capstone_stage2.DetailRestaurantActivity;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.data.Restaurant;
import java.util.ArrayList;
import java.util.List;

public class RestaurantListFragment extends Fragment implements RecyclerViewOnClick {

  public RestaurantListFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_restaurant_list, container, false);
    RecyclerView recyclerView = rootView.findViewById(R.id.restaurant_recyclerview);
    List<Restaurant> restaurants = new ArrayList<>();
    restaurants.add(new Restaurant("Pizza Place",3,3,"8512 Avonshire RD, Orlando, FL 32817"));
    restaurants.add(new Restaurant("Pizza Place",3,3,"8512 Avonshire RD, Orlando, FL 32817"));
    restaurants.add(new Restaurant("Pizza Place",3,3,"8512 Avonshire RD, Orlando, FL 32817"));
    restaurants.add(new Restaurant("Pizza Place",3,3,"8512 Avonshire RD, Orlando, FL 32817"));
    restaurants.add(new Restaurant("Pizza Place",3,3,"8512 Avonshire RD, Orlando, FL 32817"));
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(new SimpleRecyclerViewAdapter(restaurants,R.layout.restaurant_item, this));
    return rootView;
  }

  @Override public void rowSelected(Object obj) {
    Intent intent = new Intent(getActivity(),DetailRestaurantActivity.class);
    startActivity(intent);
  }
}