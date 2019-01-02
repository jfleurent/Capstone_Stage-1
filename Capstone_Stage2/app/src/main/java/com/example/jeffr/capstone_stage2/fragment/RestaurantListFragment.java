package com.example.jeffr.capstone_stage2.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.jeffr.capstone_stage2.FirebaseDatabaseContract;
import com.example.jeffr.capstone_stage2.ui.DetailCategoryFavoriteActivity;
import com.example.jeffr.capstone_stage2.ui.DetailRestaurantActivity;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.models.FavoriteCategory;
import com.example.jeffr.capstone_stage2.models.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class RestaurantListFragment extends Fragment implements RecyclerViewOnClick {
  public static final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
  private RelativeLayout toolbarLayout;
  private Toolbar toolbar;
  private List<Restaurant> deletionList;
  private Button deleteButton;
  private FavoriteCategory category;
  private RecyclerView recyclerView;
  List<Restaurant> restaurants;

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
    deletionList = new ArrayList<>();
    deleteButton = rootView.findViewById(R.id.delete_button);
    recyclerView = rootView.findViewById(R.id.restaurant_recyclerview);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);

    if (getArguments() != null) {

      restaurants = (ArrayList<Restaurant>) getArguments().get("Restaurants");
      if (getArguments().get("Category") != null) {
        category = (FavoriteCategory) getArguments().get("Category");
        restaurants = category.getRestaurants();
        if (getActivity() instanceof DetailCategoryFavoriteActivity) {
          toolbarLayout = rootView.findViewById(R.id.toolbar_relativelayout);
          toolbarLayout.setVisibility(View.VISIBLE);
          toolbar = rootView.findViewById(R.id.favorite_category_toolbar);
          toolbar.setTitle(category.getTitle());
          deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
              deleteOnClick();
            }
          });
        }
      } else if (getArguments().get("History") != null) {
        restaurants = (List<Restaurant>) getArguments().get("History");
      }
      restaurants = (restaurants != null) ? restaurants : new ArrayList<Restaurant>();
      recyclerView.setAdapter(
          new SimpleRecyclerViewAdapter(restaurants, R.layout.restaurant_item, this));
    }
    return rootView;
  }

  @Override
  public void rowSelected(Object obj) {
    Intent intent = new Intent(getActivity(), DetailRestaurantActivity.class);
    intent.putExtra("Restaurant", (Serializable) obj);
    startActivity(intent);
  }

  @Override public void deleteSelectedRow(Object obj) {
    deleteButton.setVisibility(View.VISIBLE);
    deletionList.add((Restaurant) obj);
  }

  private void deleteOnClick() {
    final DatabaseReference restaurantReference = FirebaseDatabase.getInstance()
        .getReference()
        .child(FirebaseDatabaseContract.USERS_CHILD)
        .child(USER_ID)
        .child(FirebaseDatabaseContract.FAVORITE_CATEGORY_CHILD);
    for (Restaurant deletingRestaurant : deletionList) {
      restaurants.remove(deletingRestaurant);
      Query
          query = restaurantReference.child(category.getCategoryReference())
          .child(FirebaseDatabaseContract.RESTAURANTS_CHILD)
          .orderByChild(FirebaseDatabaseContract.NAME_CHILD)
          .equalTo(deletingRestaurant.getName());
      query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
          if (dataSnapshot.exists()) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              restaurantReference.child(category.getCategoryReference())
                  .child(FirebaseDatabaseContract.RESTAURANTS_CHILD)
                  .child(snapshot.getKey())
                  .removeValue();
              break;
            }
            Timber.d("Successfully deleted item from the database");
            recyclerView.setAdapter(
                new SimpleRecyclerViewAdapter(restaurants, R.layout.restaurant_item,
                    RestaurantListFragment.this));
          }
        }

        @Override public void onCancelled(@NonNull DatabaseError databaseError) {
          Timber.d(databaseError.toException(),"Failed to delete item from the database");
        }
      });
    }
  }
}

