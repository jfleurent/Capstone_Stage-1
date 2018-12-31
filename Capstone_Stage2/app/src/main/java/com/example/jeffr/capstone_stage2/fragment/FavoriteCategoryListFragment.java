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
import android.widget.Button;
import com.example.jeffr.capstone_stage2.FirebaseDatabaseContract;
import com.example.jeffr.capstone_stage2.ui.DetailCategoryFavoriteActivity;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.models.FavoriteCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import timber.log.Timber;

public class FavoriteCategoryListFragment extends Fragment implements RecyclerViewOnClick {

  private DatabaseReference favoriteCategoryReference;
  private List<FavoriteCategory> deletionList;
  private Button deleteButton;

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
    deletionList = new ArrayList<>();
    deleteButton = rootView.findViewById(R.id.delete_button);
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        deleteOnClick();
      }
    });
    GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
    recyclerView.setLayoutManager(gridLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);

    favoriteCategoryReference = FirebaseDatabase.getInstance()
        .getReference()
        .child(FirebaseDatabaseContract.USERS_CHILD)
        .child(
            FirebaseDatabaseContract.USER_ID)
        .child(FirebaseDatabaseContract.FAVORITE_CATEGORY_CHILD);
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
    startActivity(intent);
  }

  @Override public void deleteSelectedRow(Object obj) {
    deleteButton.setVisibility(View.VISIBLE);
    deletionList.add((FavoriteCategory) obj);
  }

  public void deleteOnClick() {
    for (FavoriteCategory category : deletionList) {
      Query query =
          favoriteCategoryReference.orderByChild(FirebaseDatabaseContract.CATEGORY_REFERENCE)
              .equalTo(category.getCategoryReference());
      query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if (dataSnapshot.exists()) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              favoriteCategoryReference.child(snapshot.getKey()).removeValue();
              Timber.d("Successfully deleted item from the database");
            }
          }
        }

        @Override public void onCancelled(@NonNull DatabaseError databaseError) {
          Timber.d("Failed to delete item from the database");
        }
      });
    }
  }
}
