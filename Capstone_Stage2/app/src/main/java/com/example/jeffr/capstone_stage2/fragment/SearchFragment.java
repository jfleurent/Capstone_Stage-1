package com.example.jeffr.capstone_stage2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TableLayout;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.adapters.TabAdapter;

public class SearchFragment extends Fragment {

  private EditText searchText;
  private TabLayout numberOfReturnsLayout;
  private TabLayout sortByLayout;
  private TabLayout distanceLayout;
  private TabLayout priceLayout;
  private TabLayout ratingLayout;

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
    numberOfReturnsLayout = rootView.findViewById(R.id.tabLayout);
    sortByLayout = rootView.findViewById(R.id.sortby_tablayout);
    distanceLayout = rootView.findViewById(R.id.distance_tablayout);
    priceLayout = rootView.findViewById(R.id.price_tablayout);
    ratingLayout = rootView.findViewById(R.id.rating_tablayout);
    searchText = rootView.findViewById(R.id.search_bar);
    Bundle bundle = new Bundle();
    bundle.putInt("Amount", 0);
    bundle.putString("Sort", "best_match");
    bundle.putInt("Distance", 5);
    bundle.putString("Price", "1,2,3,4");
    bundle.putInt("Rating", 1);
    setArguments(bundle);
    numberOfReturnsLayout.addOnTabSelectedListener(new TabAdapter("Amount",this));
    sortByLayout.addOnTabSelectedListener(new TabAdapter("Sort",this));
    distanceLayout.addOnTabSelectedListener(new TabAdapter("Distance",this));
    priceLayout.addOnTabSelectedListener(new TabAdapter("Price",this));
    ratingLayout.addOnTabSelectedListener(new TabAdapter("Rating",this));
    return rootView;
  }

  public EditText getSearchText() {
    return searchText;
  }
}
