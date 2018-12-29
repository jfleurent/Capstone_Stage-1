package com.example.jeffr.capstone_stage2;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.jeffr.capstone_stage2.data.FavoriteCategory;
import com.example.jeffr.capstone_stage2.fragment.RestaurantListFragment;
import timber.log.Timber;

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
