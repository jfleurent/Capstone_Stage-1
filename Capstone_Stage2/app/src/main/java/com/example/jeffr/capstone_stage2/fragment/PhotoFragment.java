package com.example.jeffr.capstone_stage2.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.models.Restaurant;

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
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
    recyclerView.setLayoutManager(gridLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(new SimpleRecyclerViewAdapter<>(restaurant
        .getPhotos(), R.layout.photo_list_item, PhotoFragment.this));

    return rootView;
  }

  @Override
  public void rowSelected(Object obj) {

  }

  @Override public void deleteSelectedRow(Object obj) {

  }
}
