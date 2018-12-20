package com.example.jeffr.capstone_stage2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.adapters.TabAdapter;

public class SearchFragment extends Fragment {

  public static Bundle bundle;

  public SearchFragment() {
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_search, container, false);
    TabLayout numberOfReturnsLayout = rootView.findViewById(R.id.tabLayout);
    TabLayout sortByLayout = rootView.findViewById(R.id.sortby_tablayout);
    TabLayout distanceLayout = rootView.findViewById(R.id.distance_tablayout);
    TabLayout priceLayout = rootView.findViewById(R.id.price_tablayout);
    TabLayout ratingLayout = rootView.findViewById(R.id.rating_tablayout);
    bundle = new Bundle();
    bundle.putInt("Amount", 0);
    bundle.putString("Sort", "best_match");
    bundle.putInt("Distance", 5);
    bundle.putString("Price", "1,2,3,4");
    bundle.putInt("Rating", 1);
    numberOfReturnsLayout.addOnTabSelectedListener(new TabAdapter("Amount"));
    sortByLayout.addOnTabSelectedListener(new TabAdapter("Sort"));
    distanceLayout.addOnTabSelectedListener(new TabAdapter("Distance"));
    priceLayout.addOnTabSelectedListener(new TabAdapter("Price"));
    ratingLayout.addOnTabSelectedListener(new TabAdapter("Rating"));
    return rootView;
  }
}
