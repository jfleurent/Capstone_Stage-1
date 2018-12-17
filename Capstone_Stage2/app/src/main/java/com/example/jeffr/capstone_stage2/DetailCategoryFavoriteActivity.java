package com.example.jeffr.capstone_stage2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.jeffr.capstone_stage2.data.FavoriteCategory;

public class DetailCategoryFavoriteActivity extends AppCompatActivity {
  FavoriteCategory favoriteCategory;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_category_favorite);
    favoriteCategory = (FavoriteCategory) getIntent().getExtras().getSerializable("Category");
    getSupportActionBar().setTitle(favoriteCategory.getTitle());
  }
}
