package com.example.jeffr.capstone_stage2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.jeffr.capstone_stage2.data.User;
import com.example.jeffr.capstone_stage2.fragment.FavoriteCategoryListFragment;
import com.example.jeffr.capstone_stage2.fragment.HomePageFragment;
import com.example.jeffr.capstone_stage2.fragment.SearchFragment;
import timber.log.Timber;

public class NavigationActivity extends AppCompatActivity {

  FragmentManager manager;
  Fragment fragment;
  FloatingActionButton fab;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation);
    getSupportActionBar().hide();
    fab = findViewById(R.id.navigation_action_button);
    manager = getSupportFragmentManager();
    fragment = new HomePageFragment();
    fab.setImageResource(R.drawable.ic_edit_profile);
    Bundle bundle = new Bundle();
    String[] favorites = {"Mexican", "Italian", "Polish"};
    bundle.putSerializable("User",new User("Tomas Maxx", "Fort Myers, Florida",favorites,33,54));
    fragment.setArguments(bundle);
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
        ViewDialog.showNewCategoryDialog(this);
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
          fab.setImageResource(R.drawable.ic_edit_profile);
          Bundle bundle = new Bundle();
          String[] favorites = {"Mexican", "Italian", "Polish"};
          bundle.putSerializable("User",new User("Tomas Maxx", "Fort Myers, Florida",favorites,33,54));
          fragment.setArguments(bundle);
          manager.beginTransaction()
              .replace(R.id.fragment_relativelayout,fragment,fragment.getTag())
              .commit();
          return true;
        case R.id.navigation_dashboard:
          fragment = new FavoriteCategoryListFragment();
          fab.setImageResource(R.drawable.ic_add_favorite);
          manager.beginTransaction().
              replace(R.id.fragment_relativelayout,fragment,fragment.getTag())
              .commit();
          return true;
        case R.id.navigation_notifications:
          fragment = new SearchFragment();
          fab.setImageResource(R.drawable.ic_search);
          manager.beginTransaction()
              .replace(R.id.fragment_relativelayout,fragment,fragment.getTag())
              .commit();
          return true;
      }
      return false;
    }
  }

}
