package com.example.jeffr.capstone_stage2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.jeffr.capstone_stage2.fragment.RestaurantListFragment;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {
  Fragment fragment;
  FragmentManager manager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_search_results);
    fragment = new RestaurantListFragment();
    manager = getSupportFragmentManager();
    fragment.setArguments(getIntent().getExtras());
    manager.beginTransaction()
        .replace(R.id.fragment_relativelayout, fragment, fragment.getTag())
        .commit();
  }
}
