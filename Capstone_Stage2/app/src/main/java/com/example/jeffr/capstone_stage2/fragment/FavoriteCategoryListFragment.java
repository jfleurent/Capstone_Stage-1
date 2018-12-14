package com.example.jeffr.capstone_stage2.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.example.jeffr.capstone_stage2.data.FavoriteCategory;
import java.util.ArrayList;
import java.util.List;

public class FavoriteCategoryListFragment extends Fragment implements RecyclerViewOnClick {

  private RecyclerView recyclerView;

  public FavoriteCategoryListFragment() {

  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_favorite_category_list, container, false);
    RecyclerView recyclerView = rootView.findViewById(R.id.category_list_recyclerview);
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
    recyclerView.setLayoutManager(gridLayoutManager);
    List<FavoriteCategory> favoriteCategories = new ArrayList<>();
    favoriteCategories.add(new FavoriteCategory("Pizza"));
    favoriteCategories.add(new FavoriteCategory("Pizza"));
    favoriteCategories.add(new FavoriteCategory("Pizza"));
    favoriteCategories.add(new FavoriteCategory("Pizza"));
    favoriteCategories.add(new FavoriteCategory("Pizza"));
    favoriteCategories.add(new FavoriteCategory("Pizza"));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(new SimpleRecyclerViewAdapter<>(favoriteCategories,R.layout.favorite_category_item, this));
    return rootView;
  }

  @Override public void rowSelected(Object obj) {
    Intent intent = new Intent(getActivity(),DetailCategoryFavoriteActivity.class);
    startActivity(intent);
  }
}