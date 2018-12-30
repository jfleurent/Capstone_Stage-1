package com.example.jeffr.capstone_stage2.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.models.FavoriteCategory;
import com.example.jeffr.capstone_stage2.fragment.RestaurantListFragment;

public class DetailCategoryFavoriteActivity extends AppCompatActivity {
  FavoriteCategory favoriteCategory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_category_favorite);
    favoriteCategory = (FavoriteCategory) getIntent().getExtras().getSerializable("Category");
    Fragment fragment = new RestaurantListFragment();
    fragment.setArguments(getIntent().getExtras());
    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_relativelayout,
        fragment, fragment.getTag()).commit();
    getSupportActionBar().setTitle(favoriteCategory.getTitle());
  }
}
