package com.example.jeffr.capstone_stage2.fragment;


import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.data.User;
import com.example.jeffr.capstone_stage2.databinding.FragmentHomePageBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class HomePageFragment extends Fragment {
    private Fragment restaurantListFragment;
    private DatabaseReference userReference;
    private StorageReference mStorageReference;


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

        mStorageReference = FirebaseStorage.getInstance().getReference(getActivity().getIntent().getExtras().getString("UserId"));
        userReference = FirebaseDatabase.getInstance().getReference().child("users").child(
                getActivity().getIntent().getExtras().getString("UserId"));
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //TODO Test if adding just user will work
                User user = dataSnapshot.getValue(User.class);
                String name = (user.getName() != null) ? user.getName() : "";
                String city = (user.getCity() != null) ? user.getCity() : "";
                String state = (user.getState() != null) ? user.getState() : "";
                List<String> favorites = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child("favorites").getChildren()) {
                    String favorite = snapshot.getValue(String.class);
                    favorites.add(favorite);
                }
                int seenTotal = user.getSeenTotal();
                int favoriteTotal = user.getFavoriteTotal();
                String photoUrl = (user.getPhoto_url() != null) ? user.getPhoto_url() : "";
                Timber.d("Photo Url: "+photoUrl);
                String backgroundUrl = (user.getBackground_url() != null) ? user.getBackground_url() : "";
                Timber.d("Background Url: "+photoUrl);
                boolean hasBackgroundImage = user.isHasBackgroundImage();
                long backgroundColor = user.getBackgroundColor();
                User bindUser = new User(name, city, favorites, state, seenTotal, favoriteTotal,photoUrl,backgroundUrl,
                        hasBackgroundImage, backgroundColor);
                binding.setUser(bindUser);
                Timber.d("Successfully added user to HomepageFragment");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Timber.d(databaseError.toException(), "Could not read User object data");
            }
        });
        return rootView;
    }


}
