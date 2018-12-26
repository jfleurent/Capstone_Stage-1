package com.example.jeffr.capstone_stage2;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.jeffr.capstone_stage2.data.Restaurant;
import com.example.jeffr.capstone_stage2.fragment.RestaurantListFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;

import java.util.List;

public class SearchResultsActivity extends AppCompatActivity{
    private Fragment fragment;
    private FragmentManager manager;
    private TextView noResultsTextView;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        if (getIntent().getExtras() == null || ((List<Restaurant>) getIntent
                ().getExtras().get("Restaurants")).size() == 0) {
            noResultsTextView = findViewById(R.id.no_results_textview);
            noResultsTextView.setVisibility(View.VISIBLE);
        } else {
            fragment = new RestaurantListFragment();
            manager = getSupportFragmentManager();
            fragment.setArguments(getIntent().getExtras());
            manager.beginTransaction().replace(R.id.fragment_relativelayout,
                    fragment, fragment.getTag()).commit();
        }
    }
}
