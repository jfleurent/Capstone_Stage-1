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
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.data.Restaurant;

public class PhotoFragment extends Fragment implements RecyclerViewOnClick {
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    //TODO perform query based on restaurant info
    View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
    RecyclerView recyclerView = rootView.findViewById(R.id.photos_recyclerview);
    Restaurant restaurant = (Restaurant) getArguments().getSerializable("Restaurant");
    StaggeredGridLayoutManager staggeredGridLayoutManager =
        new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
    staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
    recyclerView.setLayoutManager(staggeredGridLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setHasFixedSize(false);
    recyclerView.setAdapter(
        new SimpleRecyclerViewAdapter<>(restaurant.getPhotos(), R.layout.photo_list_item, this));
    return rootView;
  }

  @Override public void rowSelected(Object obj) {

  }
}
