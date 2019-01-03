package com.example.jeffr.capstone_stage2.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.TintableBackgroundView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.jeffr.capstone_stage2.FirebaseDatabaseContract;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.models.FavoriteCategory;
import com.example.jeffr.capstone_stage2.models.Restaurant;
import com.example.jeffr.capstone_stage2.ui.DetailRestaurantActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class ListViewWidgetService extends RemoteViewsService {

  public static int categoryIndex = 0;
  public static String categoryName;
  public static FavoriteCategory currentCategory;

  @Override
  public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new ListViewRemoteViewsFactory(this.getApplicationContext());
  }

  public class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    Context mContext;
    FirebaseAuth firebaseAuth;
    DatabaseReference categoryReference;
    List<FavoriteCategory> categories;
    FirebaseUser user;

    public ListViewRemoteViewsFactory(Context applicationContext) {
      mContext = applicationContext;
      firebaseAuth = FirebaseAuth.getInstance();
      categories = new ArrayList<>();
      user = firebaseAuth.getCurrentUser();
      if (user != null) {
        categoryReference = FirebaseDatabase.getInstance()
            .getReference()
            .child(FirebaseDatabaseContract.USERS_CHILD)
            .child(user.getUid())
            .child(FirebaseDatabaseContract.FAVORITE_CATEGORY_CHILD);
      }
    }

    @Override
    public void onCreate() {
      if (categoryReference != null) {
        categoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
              FavoriteCategory favoriteCategory = snapshot.getValue(FavoriteCategory.class);
              categories.add(favoriteCategory);
            }
            Timber.d("Successfully loaded categories in widget");
            AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
            final ComponentName cn = new ComponentName(mContext, FavoriteListWidget.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(cn),
                R.id.resturant_list_view);
          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {
            Timber.d(databaseError.toException(), "Failed loaded categories in widget");
          }
        });
      }
    }

    @Override
    public void onDataSetChanged() {
      if (categories.size() != 0) {
        currentCategory = categories.get(Math.abs(categoryIndex) % categories.size());
        categoryName = categories.get(Math.abs(categoryIndex) % categories.size()).getTitle();
      }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
      if (currentCategory != null && currentCategory.getRestaurants() != null) {
        if(currentCategory.getRestaurants().size() > 10){
          return 10;
        }
        else{
          return currentCategory.getRestaurants().size();
        }
      }
      else {
        return  0;
      }
    }

    @Override
    public RemoteViews getViewAt(int position) {
      RemoteViews remoteViews =
          new RemoteViews(mContext.getPackageName(), R.layout.widget_restaurant_list_item);
      if (currentCategory != null) {
        Restaurant restaurant = currentCategory.getRestaurants().get(position);
        try {
          Bitmap bitmap = Picasso.get().load(restaurant.getImageUrl()).get();
          remoteViews.setImageViewBitmap(R.id.resturant_imageview, bitmap);
        } catch (IOException e) {
          e.printStackTrace();
        }
        remoteViews.setTextViewText(R.id.restaurant_title_textview, restaurant.getName());
        remoteViews.setTextViewText(R.id.restaurant_rating_textview,
            String.valueOf(restaurant.getRating()));
        remoteViews.setTextViewText(R.id.restaurant_price_textview, restaurant.getPrice());
        remoteViews.setTextViewText(R.id.restaurant_address_textview, restaurant.getAddress());
        Intent intent = new Intent(mContext, DetailRestaurantActivity.class);
      }
      return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
      return null;
    }

    @Override
    public int getViewTypeCount() {
      return 1;
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public boolean hasStableIds() {
      return false;
    }
  }
}
