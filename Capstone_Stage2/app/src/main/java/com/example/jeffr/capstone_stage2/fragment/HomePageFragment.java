package com.example.jeffr.capstone_stage2.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeffr.capstone_stage2.FirebaseDatabaseContract;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.models.Restaurant;
import com.example.jeffr.capstone_stage2.models.User;
import com.example.jeffr.capstone_stage2.databinding.FragmentHomePageBinding;
import com.example.jeffr.capstone_stage2.ui.NavigationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;

import static com.example.jeffr.capstone_stage2.ui.NavigationActivity.loadHistoryFragment;

public class HomePageFragment extends Fragment {
  public static final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
  private DatabaseReference userReference;
  private DatabaseReference historyReference;

  public HomePageFragment() {

  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final FragmentHomePageBinding binding = DataBindingUtil.inflate(
        inflater, R.layout.fragment_home_page, container, false);
    setHasOptionsMenu(true);
    View rootView = binding.getRoot();
    userReference = FirebaseDatabase.getInstance()
        .getReference()
        .child(FirebaseDatabaseContract.USERS_CHILD)
        .child(
            USER_ID);
    historyReference = userReference.child(FirebaseDatabaseContract.RESTAURANT_HISTORY_CHILD);
    final Bundle bundle = new Bundle();
    final Fragment fragment = new RestaurantListFragment();
    userReference.addValueEventListener(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        User user = dataSnapshot.getValue(User.class);
        String name = (user.getName() != null) ? user.getName() : "";
        String city = (user.getCity() != null) ? user.getCity() : "";
        String state = (user.getState() != null) ? user.getState() : "";
        List<String> favorites = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.child(FirebaseDatabaseContract.FAVORITES_CHILD)
            .getChildren()) {
          String favorite = snapshot.getValue(String.class);
          favorites.add(favorite);
        }
        int seenTotal = user.getSeenTotal();
        int favoriteTotal = user.getFavoriteTotal();
        String photoUrl = (user.getPhoto_url() != null) ? user.getPhoto_url() : "";
        Timber.d("Photo Url: " + photoUrl);
        String backgroundUrl = (user.getBackground_url() != null) ? user.getBackground_url() : "";
        Timber.d("Background Url: " + photoUrl);
        boolean hasBackgroundImage = user.isHasBackgroundImage();
        long backgroundColor = (user.getBackgroundColor()!= null) ? user.getBackgroundColor() : 0;
        User bindUser = new User(name, city, favorites, state, seenTotal, favoriteTotal, photoUrl,
            backgroundUrl,
            hasBackgroundImage, backgroundColor);
        if (getActivity() != null) {
          ((NavigationActivity)getActivity()).setUser(bindUser);
          binding.setUser(bindUser);
        }

        Timber.d("Successfully added user to HomepageFragment");
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Timber.d(databaseError.toException(), "Could not read User object data");
      }
    });

    historyReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ArrayList<Restaurant> restaurantHistory = new ArrayList<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          Restaurant restaurant = snapshot.getValue(Restaurant.class);
          restaurantHistory.add(restaurant);
        }
        Collections.reverse(restaurantHistory);
        bundle.putParcelableArrayList("History", restaurantHistory);
        Timber.d("Successfully added restaurant history to homepage");
        fragment.setArguments(bundle);
        loadHistoryFragment(fragment);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Timber.d("Failed to add restaurant history to homepage");
      }
    });
    return rootView;
  }
}
