package com.example.jeffr.capstone_stage2;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import com.example.jeffr.capstone_stage2.data.Restaurant;
import com.example.jeffr.capstone_stage2.data.User;
import com.example.jeffr.capstone_stage2.fragment.FavoriteCategoryListFragment;
import com.example.jeffr.capstone_stage2.fragment.HomePageFragment;
import com.example.jeffr.capstone_stage2.fragment.SearchFragment;
import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.SearchResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

import static com.example.jeffr.capstone_stage2.fragment.SearchFragment.bundle;

public class NavigationActivity extends AppCompatActivity {

  private static final int LOCATION_PERMISSION = 843;
  private FragmentManager manager;
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
    fragment = new HomePageFragment();
    fab.setImageResource(R.drawable.ic_edit_profile);
    Bundle bundle = new Bundle();
    String[] favorites = { "Mexican", "Italian", "Polish" };
    bundle.putSerializable("User",
        new User("Tomas Maxx", "Fort Myers, Florida", favorites, 33, 54));
    fragment.setArguments(bundle);
    manager.beginTransaction()
        .replace(R.id.fragment_relativelayout, fragment, fragment.getTag())
        .commit();
    BottomNavigationView navigation = findViewById(R.id.navigation);
    navigation.setOnNavigationItemSelectedListener(new NavigationListener());

    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
            LOCATION_PERMISSION);
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

  private class NavigationListener
      implements BottomNavigationView.OnNavigationItemSelectedListener {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
      switch (item.getItemId()) {
        case R.id.navigation_home:
          fragment = new HomePageFragment();
          fab.setImageResource(R.drawable.ic_edit_profile);
          Bundle bundle = new Bundle();
          String[] favorites = { "Mexican", "Italian", "Polish" };
          bundle.putSerializable("User",
              new User("Tomas Maxx", "Fort Myers, Florida", favorites, 33, 54));
          fragment.setArguments(bundle);
          manager.beginTransaction()
              .replace(R.id.fragment_relativelayout, fragment, fragment.getTag())
              .commit();
          return true;
        case R.id.navigation_dashboard:
          fragment = new FavoriteCategoryListFragment();
          fab.setImageResource(R.drawable.ic_add_favorite);
          manager.beginTransaction().
              replace(R.id.fragment_relativelayout, fragment, fragment.getTag())
              .commit();
          return true;
        case R.id.navigation_notifications:
          fragment = new SearchFragment();
          fab.setImageResource(R.drawable.ic_search);
          manager.beginTransaction()
              .replace(R.id.fragment_relativelayout, fragment, fragment.getTag())
              .commit();
          return true;
      }
      return false;
    }
  }

  private void search() {
    YelpFusionApiFactory apiFactory = new YelpFusionApiFactory();
    YelpFusionApi yelpFusionApi = null;
    try {
      yelpFusionApi = apiFactory.createAPI(getResources().getString(R.string.yelp_api_keu));
    } catch (IOException e) {
      e.printStackTrace();
    }

    Map<String, String> params = new HashMap<>();

    // general params
    params.put("latitude", String.valueOf(location.getLatitude()));
    params.put("longitude", String.valueOf(location.getLongitude()));
    params.put("limit", "40");
    params.put("sort_by", bundle.getString("Sort"));
    params.put("radius", String.valueOf(bundle.getInt("Distance")));
    params.put("price", bundle.getString("Price"));

    Timber.d(params.toString());
    //TODO Add code for sorting price
    Call<SearchResponse> call = yelpFusionApi.getBusinessSearch(params);
    call.enqueue(new Callback<SearchResponse>() {
      @Override
      public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
        SearchResponse searchResponse = response.body();
        Timber.d(String.valueOf(searchResponse.getBusinesses().size()));
        Intent intent = new Intent(NavigationActivity.this, SearchResultsActivity.class);
        ArrayList<Restaurant> queriedRestaurants = convertBusinesses(searchResponse.getBusinesses());
        if(bundle.getInt("Amount") == 0){
          queriedRestaurants = randomizeResult(queriedRestaurants);
        }
        else {
          queriedRestaurants = filterByRating(queriedRestaurants,bundle.getInt("Rating"));
        }

        intent.putExtra("Restaurants",queriedRestaurants );
        startActivity(intent);
      }

      @Override
      public void onFailure(Call<SearchResponse> call, Throwable t) {
        Timber.d(t, "Query Failed");
        // HTTP error happened, do something to handle it.
      }
    });
  }

  @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    switch (requestCode) {
      case LOCATION_PERMISSION: {
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

        } else {

          Timber.d("Rermission Denied");
        }
        return;
      }
    }
  }

  private ArrayList<Restaurant> convertBusinesses(List<Business> businesses) {
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    for (Business business : businesses) {
      restaurants.add(
          new Restaurant(business.getImageUrl(), business.getName(), (float) business.getRating(),
              business.getPrice(),business.getLocation().getAddress1(),(float) business.getDistance()));
    }
    return restaurants;
  }

  private ArrayList<Restaurant> filterByRating(ArrayList<Restaurant> restaurants,int rating){
    ArrayList<Restaurant> filteredRestaurants = new ArrayList<>();
    if(rating == 0){
      return restaurants;
    }
    else{
      for(Restaurant restaurant : restaurants){
        if(restaurant.getRating() >= rating){
          filteredRestaurants.add(restaurant);
        }
      }
      return filteredRestaurants;
    }
  }

  private ArrayList<Restaurant> randomizeResult(ArrayList<Restaurant> restaurants){
    if(restaurants.size() == 0){
      return restaurants;
    }
    Random random = new Random();
    int randomNumber = random.nextInt(restaurants.size()) + 1;
    ArrayList<Restaurant> randomResult = new ArrayList<>();
    randomResult.add(restaurants.get(randomNumber));
    return  randomResult;
  }
}
