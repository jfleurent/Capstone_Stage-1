package com.example.jeffr.capstone_stage2.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.view.View;

import com.example.jeffr.capstone_stage2.FirebaseDatabaseContract;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.RetrofitService;
import com.example.jeffr.capstone_stage2.ViewDialog;
import com.example.jeffr.capstone_stage2.adapters.SectionsPagerAdapter;
import com.example.jeffr.capstone_stage2.adapters.TabAdapter;
import com.example.jeffr.capstone_stage2.models.Photo;
import com.example.jeffr.capstone_stage2.models.Restaurant;
import com.example.jeffr.capstone_stage2.models.RestaurantId;
import com.example.jeffr.capstone_stage2.models.RestaurantInfo;
import com.example.jeffr.capstone_stage2.models.Review;
import com.example.jeffr.capstone_stage2.models.User;
import com.example.jeffr.capstone_stage2.databinding
    .ActivityDetailRestaurantBinding;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

//TODO remove toolbar and add the one in layout to make it move
//TODO add OnCompleteListener for using progressbar when loading fragments for viewpager
//https://stackoverflow.com/questions/43445169/how-can-i-register-a-username-in-firebase
public class DetailRestaurantActivity extends AppCompatActivity {
  public static final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
  private static final String BASE_URL = "https://maps.googleapis.com";
  private static final String PHOTO_QUERY = "/maps/api/place/photo?maxwidth=400&photoreference=";
  private ViewPager viewPager;
  private TabLayout tabLayout;
  private Restaurant restaurant;
  private List<Review> reviews;
  private List<Photo> photos;
  private DatabaseReference userReference;
  private DatabaseReference historyReference;
  private ConstraintLayout constraintLayout;
  private FloatingActionButton floatingActionButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_restaurant);
    getSupportActionBar().hide();
    constraintLayout = findViewById(R.id.restaurant_info);
    floatingActionButton = findViewById(R.id.favorite_fab);

    restaurant = (Restaurant) getIntent().getExtras().getSerializable("Restaurant");
    ActivityDetailRestaurantBinding binding = DataBindingUtil.setContentView(this,
        R.layout.activity_detail_restaurant);
    binding.setRestaurant(restaurant);
    viewPager = findViewById(R.id.detail_restaurant_viewpager);
    tabLayout = findViewById(R.id.tabs);

    userReference = FirebaseDatabase.getInstance()
        .getReference()
        .child(FirebaseDatabaseContract.USERS_CHILD)
        .child(USER_ID);

    historyReference = userReference.child("restaurantHistory");

    updateSeenTotal();
    updateViewHistory();
    loadReviewPhotos();
    //new FetchColors().execute();
  }

  public void favoriteOnClick(View view) {
    ViewDialog.showSelectFavoriteDialog(this);
  }

  public void directionsOnClick(View view) {
    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + restaurant
        .getLatitude() + "," + restaurant.getLongitude());
    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
    mapIntent.setPackage("com.google.android.apps.maps");
    startActivity(mapIntent);
  }

  private String getPhotoUrl(String reference) {
    return BASE_URL + PHOTO_QUERY + reference + "&key=" + getResources().getString(
        R.string.google_maps_api);
  }

  public Restaurant getRestaurant() {
    return restaurant;
  }

  private void updateSeenTotal() {
    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        int seenTotal = (user != null) ? user.getSeenTotal() : 0;
        userReference.child(FirebaseDatabaseContract.SEEN_TOTAL_CHILD).setValue(++seenTotal);
        Timber.d("Successfully increased seen total");
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Timber.d("Failed to increase seen total");
      }
    });
  }

  private void updateViewHistory() {
    historyReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        if (dataSnapshot.getChildrenCount() < 20) {
          historyReference.child(String.valueOf(dataSnapshot.getChildrenCount()))
              .setValue(restaurant)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                  Timber.d("Successfully added restaurant to history");
                }
              }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Timber.d("Failed to add restaurant to history");
            }
          });
        } else {
          List<Restaurant> historyRestaurants = new ArrayList<>();
          for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            Restaurant restaurantEntry = snapshot.getValue(Restaurant.class);
            historyRestaurants.add(restaurantEntry);
          }
          Collections.reverse(historyRestaurants);
          historyRestaurants.remove(19);
          historyRestaurants.add(0, restaurant);
          Collections.reverse(historyRestaurants);
          historyReference.setValue(historyRestaurants)
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                  Timber.d("Successfully added the list of history restaurants");
                }
              }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Timber.d("Failed to add the list of history restaurants");
            }
          });
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Timber.d(databaseError.toException(), "Failed to add data to restaurantHistory");
      }
    });
  }

  private void loadReviewPhotos() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    final RetrofitService retrofitService = retrofit
        .create(RetrofitService.class);

    Map<String, String> params = new HashMap<>();
    params.put("key", getResources().getString(R.string.google_maps_api));
    String coordinates = "" + restaurant.getLatitude() + "," + restaurant.getLongitude();
    params.put("location", coordinates);
    params.put("radius", "10");
    params.put("keyword", restaurant.getName());

    final Call<RestaurantId> restaurantIdResponse = retrofitService
        .getRestaurantId(params);

    restaurantIdResponse.enqueue(new Callback<RestaurantId>() {
      @Override
      public void onResponse(Call<RestaurantId> call, Response<RestaurantId> response) {
        Timber.d(call.request().toString());
        String placeId = "";
        for (RestaurantId.Candidate candidate : response.body().getCandidates()) {
          placeId = candidate.getPlace_id();
        }

        Map<String, String> infoParams = new HashMap<>();
        infoParams.put("key", getResources().getString(R.string.google_maps_api));
        infoParams.put("placeid", placeId);

        Call<RestaurantInfo> restaurantInfoResponse = retrofitService
            .getRestaurantInfo(infoParams);

        restaurantInfoResponse.enqueue(new Callback<RestaurantInfo>() {
          @Override
          public void onResponse(Call<RestaurantInfo> call, Response<RestaurantInfo> response) {
            Timber.d(call.request().toString());
            List<RestaurantInfo.PhotoReference> photoReferences;
            photos = new ArrayList<>();
            reviews = new ArrayList<>();
            if (response.body().getCandidates() != null) {
              reviews = response.body().getCandidates().getReviews();
              photoReferences = response.body().getCandidates().getPhotos();
              for (RestaurantInfo.PhotoReference reference : photoReferences) {
                photos.add(new Photo(getPhotoUrl(reference.getPhotoReference())));
              }
            }
            restaurant.setPhotos(photos);
            restaurant.setReviews(reviews);
            viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(), restaurant));
            viewPager.addOnPageChangeListener(
                new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabAdapter(viewPager));
          }

          @Override
          public void onFailure(Call<RestaurantInfo> call, Throwable t) {
            Timber.d("restaurantInfoResponse: Response Failed");
          }
        });
      }

      @Override
      public void onFailure(Call<RestaurantId> call, Throwable t) {
        Timber.d("restaurantIdResponse: Response Failed");
      }
    });
  }

  private HashMap<String, Integer> processPalette(Palette palette) {
    HashMap<String, Integer> map = new HashMap<>();
    if (palette.getDarkMutedSwatch() != null) {
      map.put("DarkMuted", palette.getDarkMutedSwatch().getRgb());
    }
    if (palette.getDarkVibrantSwatch() != null) {
      map.put("DarkVibrant", palette.getDarkVibrantSwatch().getRgb());
    }
    if (palette.getDominantSwatch() != null) {
      map.put("Dominant", palette.getDominantSwatch().getRgb());
    }
    if (palette.getLightMutedSwatch() != null) {
      map.put("LightMuted", palette.getLightMutedSwatch().getRgb());
    }
    if (palette.getLightVibrantSwatch() != null) {
      map.put("LightVibrant", palette.getLightVibrantSwatch().getRgb());
    }
    if (palette.getMutedSwatch() != null) {
      map.put("Muted", palette.getMutedSwatch().getRgb());
    }
    if (palette.getVibrantSwatch() != null) {
      map.put("Vibrant", palette.getVibrantSwatch().getRgb());
    }
    return map;
  }

  //private class FetchColors extends AsyncTask<Void, Void, HashMap<String, Integer>> {
  //
  //  @Override protected HashMap<String, Integer> doInBackground(Void... voids) {
  //    final HashMap<String, Integer>[] map = new HashMap[1];
  //    try {
  //      Bitmap bitmap = Picasso.get().load(restaurant.getImageUrl()).get();
  //      Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
  //        @Override public void onGenerated(@Nullable Palette palette) {
  //          if (palette != null) {
  //            map[0] = processPalette(palette);
  //            Timber.d("Colors: " + map[0].toString());
  //            constraintLayout.setBackgroundColor(Color.GREEN);
  //            floatingActionButton.setBackgroundColor(map[0].get("Vibrant"));
  //          }
  //        }
  //      });
  //    } catch (IOException e) {
  //      e.printStackTrace();
  //    }
  //
  //    return map[0];
  //  }
  //}
}
