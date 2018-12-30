package com.example.jeffr.capstone_stage2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jeffr.capstone_stage2.ui.DetailCategoryFavoriteActivity;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.models.FavoriteCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class FavoriteCategoryListFragment extends Fragment implements RecyclerViewOnClick {

  private RecyclerView recyclerView;
  private DatabaseReference favoriteCategoryReference;

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
    final RecyclerView recyclerView = rootView.findViewById(R.id.category_list_recyclerview);

    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
    recyclerView.setLayoutManager(gridLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);

    favoriteCategoryReference = FirebaseDatabase.getInstance().getReference().child("users").child(
        getActivity().getIntent().getExtras().getString("UserId")).child("favorite_categories");
    favoriteCategoryReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        final List<FavoriteCategory> favoriteCategories = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          FavoriteCategory category = snapshot.getValue(FavoriteCategory.class);
          favoriteCategories.add(category);
        }
        Timber.d("Categories found:" + favoriteCategories.toString());
        recyclerView.setAdapter(
            new SimpleRecyclerViewAdapter<>(favoriteCategories, R.layout.favorite_category_item,
                FavoriteCategoryListFragment.this));
        Timber.d("Successfully added categories to FavoriteCategoryListFragment");
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Timber.d(databaseError.toException(), "Could not read categories object data");
      }
    });

    return rootView;
  }

  @Override public void rowSelected(Object obj) {
    Intent intent = new Intent(getActivity(), DetailCategoryFavoriteActivity.class);
    intent.putExtra("Category", (FavoriteCategory) obj);
    intent.putExtra("UserId", getActivity().getIntent().getExtras().getString("UserId"));
    startActivity(intent);
  }
}
