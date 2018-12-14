package com.example.jeffr.capstone_stage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.jeffr.capstone_stage2.fragment.FavoriteCategoryListFragment;
import com.example.jeffr.capstone_stage2.fragment.HomePageFragment;
import com.example.jeffr.capstone_stage2.fragment.SearchFragment;

public class NavigationActivity extends AppCompatActivity {

  FragmentManager manager;
  Fragment fragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation);
    getSupportActionBar().hide();
    manager = getSupportFragmentManager();
    fragment = new HomePageFragment();
    manager.beginTransaction().replace(R.id.fragment_relativelayout,fragment,fragment.getTag()).commit();
    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(new NavigationListener());
  }

  public void fabOnclick(View view){
    if(fragment instanceof HomePageFragment){
      Intent intent = new Intent(this,CustomizePageActivity.class);
      startActivity(intent);
    }
    else if(fragment instanceof  FavoriteCategoryListFragment){

    }
    else {
      Intent intent = new Intent(this,SearchResultsActivity.class);
      startActivity(intent);
    }
  }



  private class NavigationListener implements BottomNavigationView.OnNavigationItemSelectedListener{
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          fragment = new HomePageFragment();
          manager.beginTransaction()
              .replace(R.id.fragment_relativelayout,fragment,fragment.getTag())
              .commit();
          return true;
        case R.id.navigation_dashboard:
          fragment = new FavoriteCategoryListFragment();
          manager.beginTransaction().
              replace(R.id.fragment_relativelayout,fragment,fragment.getTag())
              .commit();
          return true;
        case R.id.navigation_notifications:
          fragment = new SearchFragment();
          manager.beginTransaction()
              .replace(R.id.fragment_relativelayout,fragment,fragment.getTag())
              .commit();
          return true;
      }
      return false;
    }
  }
}
