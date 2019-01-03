package com.example.jeffr.capstone_stage2.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.ViewDialog;
import com.example.jeffr.capstone_stage2.models.Restaurant;
import com.example.jeffr.capstone_stage2.fragment.FavoriteCategoryListFragment;
import com.example.jeffr.capstone_stage2.fragment.HomePageFragment;
import com.example.jeffr.capstone_stage2.fragment.SearchFragment;
import com.example.jeffr.capstone_stage2.models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class NavigationActivity extends AppCompatActivity {

  private static final int LOCATION_PERMISSION = 843;
  private static final String HOME = "home";
  private static final String FAVORITE = "favorite";
  private static final String SEARCH = "search";
  private static FragmentManager manager;
  private Fragment fragment;
  private FloatingActionButton fab;
  private Location location;
  private LocationManager lm;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_navigation);
    getSupportActionBar().hide();
    fab = findViewById(R.id.navigation_action_button);
    manager = getSupportFragmentManager();
    if (savedInstanceState != null) {
      switch (savedInstanceState.getString("Fragment")) {
        case HOME:
          fragment = new HomePageFragment();
          fab.setImageResource(R.drawable.ic_edit_profile);
          fab.setContentDescription(
              getResources().getString(R.string.edit_profile_fab_content_description));
          break;
        case FAVORITE:
          fragment = new FavoriteCategoryListFragment();
          fab.setImageResource(R.drawable.ic_add_favorite);
          fab.setContentDescription(getResources().getString(R.string.add_fab_content_description));
          break;
        case SEARCH:
          fragment = new SearchFragment();
          fab.setImageResource(R.drawable.ic_search);
          fab.setContentDescription(
              getResources().getString(R.string.search_fab_content_description));
          break;
        default:
          fragment = new HomePageFragment();
          fab.setImageResource(R.drawable.ic_edit_profile);
          fab.setContentDescription(
              getResources().getString(R.string.edit_profile_fab_content_description));
      }
    } else {
      fragment = new HomePageFragment();
    }

    fab.setImageResource(R.drawable.ic_edit_profile);
    manager.beginTransaction().replace(R.id.fragment_relativelayout, fragment,
        fragment.getTag()).commit();
    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(new NavigationListener
        ());
    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission
        .ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest
        .permission.ACCESS_COARSE_LOCATION) != PackageManager
        .PERMISSION_GRANTED) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(new String[] {Manifest.permission
            .ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
      }
      return;
    }
    location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
  }

  public void fabOnclick(View view) {
    if (fragment instanceof HomePageFragment) {
      Intent intent = new Intent(this, CustomizePageActivity.class);
      startActivity(intent);
    } else if (fragment instanceof FavoriteCategoryListFragment) {
      ViewDialog.showNewCategoryDialog(this);
    } else {
      search();
    }
  }

  private void search() {
    final Intent intent = new Intent(this, SearchResultsActivity.class);
    YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
    YelpFusionApi yelpFusionApi = null;
    try {
      yelpFusionApi = apiFactory.createAPI(getResources().getString(R
          .string.yelp_api_key));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Map<String, String> params = new HashMap<>();

    String search = ((SearchFragment) fragment).getSearchText().getText()
        .toString().toLowerCase();
    for (String category : loadCategoriesFromRaw()) {
      if (category.contains(search) && !search.equals("")) {
        params.put("categories", category);
        Timber.d("Categories: " + category);
        break;
      }
    }

    if (!search.equals("") && params.get("categories") == null) {
      startActivity(intent);
      return;
    }

    params.put("latitude", String.valueOf(location.getLatitude()));
    params.put("longitude", String.valueOf(location.getLongitude()));
    params.put("limit", "40");
    params.put("radius", String.valueOf(fragment.getArguments().getInt
        ("Distance")));
    params.put("price", fragment.getArguments().getString("Price"));
    if (!fragment.getArguments().getString("Sort").equals("price")) {
      params.put("sort_by", fragment.getArguments().getString("Sort"));
    }

    Timber.d(params.toString());
    Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
    call.enqueue(new Callback<SearchResponse>() {
      @Override
      public void onResponse(Call<SearchResponse> call,
          Response<SearchResponse> response) {
        SearchResponse searchResponse = response.body();
        ArrayList<Restaurant> queriedRestaurants = convertBusinesses
            (searchResponse.getBusinesses());
        if (fragment.getArguments().getInt("Amount") == 0) {
          queriedRestaurants = randomizeResult(queriedRestaurants);
        } else {
          queriedRestaurants = filterByRating(queriedRestaurants,
              fragment.getArguments().getInt("Rating"));
          if (fragment.getArguments().getString("Sort").equals
              ("price")) {
            Collections.sort(queriedRestaurants);
          }
        }
        intent.putExtra("Restaurants", queriedRestaurants);
        startActivity(intent);
      }

      @Override
      public void onFailure(Call<SearchResponse> call, Throwable t) {
        Timber.d(t, "Query Failed");
      }
    });
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[]
      permissions, @NonNull int[] grantResults) {
    switch (requestCode) {
      case LOCATION_PERMISSION: {
        if (grantResults.length > 0 && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED) {

        } else {
          Timber.d("Permission Denied");
        }
      }
    }
  }

  private ArrayList<Restaurant> convertBusinesses(List<Business> businesses) {
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    for (Business business : businesses) {
      restaurants.add(new Restaurant(business.getImageUrl(), business
          .getName(), (float) business.getRating(), business
          .getPrice(), String.format("%s %s, %s %s", business
          .getLocation().getAddress1(), business.getLocation()
          .getCity(), business.getLocation().getState(), business
          .getLocation().getZipCode()), Math.round((business
          .getDistance() / 1609.344) * 10) / 10, (float) business
          .getCoordinates().getLatitude(), (float) business
          .getCoordinates().getLongitude()));
    }
    return restaurants;
  }

  private ArrayList<Restaurant> filterByRating(ArrayList<Restaurant>
      restaurants, int
      rating) {

    ArrayList<Restaurant> filteredRestaurants = new ArrayList<>();
    if (rating == 0) {
      return restaurants;
    } else {
      for (Restaurant restaurant : restaurants) {
        if (restaurant.getRating() >= rating) {
          filteredRestaurants.add(restaurant);
        }
      }
      return filteredRestaurants;
    }
  }

  private ArrayList<Restaurant> randomizeResult(ArrayList<Restaurant>
      restaurants) {
    if (restaurants.size() == 0) {
      return restaurants;
    }
    Random random = new Random();
    int randomNumber = random.nextInt(restaurants.size());
    ArrayList<Restaurant> randomResult = new ArrayList<>();
    randomResult.add(restaurants.get(randomNumber));
    return randomResult;
  }

  public List<String> loadCategoriesFromRaw() {
    List<String> categories = new ArrayList<>();
    String json = null;
    try {
      InputStream is = getResources().openRawResource(getResources()
          .getIdentifier("categories", "raw", getPackageName()));
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, "UTF-8");
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    try {
      JSONArray jsonArray = new JSONArray(json);
      for (int i = 0; i < jsonArray.length(); i++) {
        try {
          JSONObject jsonObject = jsonArray.getJSONObject(i);
          String parentCategory = jsonObject.getJSONArray("parents").getString(0);
          if (parentCategory.equals("restaurants") || parentCategory.equals("bar")
              || parentCategory.equals("caribbean") || parentCategory.equals("food")
              || parentCategory.equals("nightlife")) {
            categories.add(jsonObject.getString("alias"));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return categories;
  }

  private class NavigationListener implements BottomNavigationView
      .OnNavigationItemSelectedListener {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          fragment = new HomePageFragment();
          fab.setImageResource(R.drawable.ic_edit_profile);
          fab.setContentDescription(
              getResources().getString(R.string.edit_profile_fab_content_description));
          manager.beginTransaction().replace(R.id.fragment_relativelayout, fragment,
              fragment.getTag()).commit();
          return true;
        case R.id.navigation_dashboard:
          fragment = new FavoriteCategoryListFragment();
          fab.setImageResource(R.drawable.ic_add_favorite);
          fab.setContentDescription(getResources().getString(R.string.add_fab_content_description));
          manager.beginTransaction().replace(R.id.fragment_relativelayout, fragment,
              fragment.getTag()).commit();
          return true;
        case R.id.navigation_notifications:
          fragment = new SearchFragment();
          fab.setImageResource(R.drawable.ic_search);
          fab.setContentDescription(
              getResources().getString(R.string.search_fab_content_description));
          manager.beginTransaction().replace(R.id.fragment_relativelayout, fragment,
              fragment.getTag()).commit();
          return true;
        case R.id.navigation_logout:
          ViewDialog.showLogoutDialog(NavigationActivity.this);
      }
      return false;
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    if (fragment instanceof HomePageFragment) {
      outState.putString("Fragment", HOME);
    } else if (fragment instanceof FavoriteCategoryListFragment) {
      outState.putString("Fragment", FAVORITE);
    } else {
      outState.putString("Fragment", SEARCH);
    }
  }

  public static void loadHistoryFragment(Fragment fragment) {
    manager
        .beginTransaction()
        .replace(R.id.restaurant_history_fragment, fragment, fragment.getTag())
        .commit();
  }
}
