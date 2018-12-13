package com.example.jeffr.capstone_stage2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jeffr.capstone_stage2.R;

public class FavoriteCategoryListFragment extends Fragment {

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
    recyclerView = container.findViewById(R.id.category_list_recyclerview);
    return inflater.inflate(R.layout.fragment_favorite_category_list, container, false);
  }

}
