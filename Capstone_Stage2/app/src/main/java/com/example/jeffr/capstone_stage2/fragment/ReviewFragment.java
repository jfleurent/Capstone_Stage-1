package com.example.jeffr.capstone_stage2.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.adapters.RecyclerViewOnClick;
import com.example.jeffr.capstone_stage2.adapters.SimpleRecyclerViewAdapter;
import com.example.jeffr.capstone_stage2.data.Restaurant;
import com.example.jeffr.capstone_stage2.data.Review;
import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment implements RecyclerViewOnClick {

  public ReviewFragment() {

  }

  // TODO: Rename and change types and number of parameters
  public static ReviewFragment newInstance(Restaurant restaurant) {
    ReviewFragment fragment = new ReviewFragment();
    Bundle args = new Bundle();
    args.putSerializable("Restaurant",restaurant);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    //TODO perform query based on restaurant info
    View rootView = inflater.inflate(R.layout.fragment_review, container, false);
    RecyclerView recyclerView = rootView.findViewById(R.id.reviews_recyclerview);
    List<Review> reviews = new ArrayList<>();
    reviews.add(new Review("Very nice selections of authentic taste of Italy...\n"
        + "brought back good memories from Italy and \n"
        + "even NYC. \n"
        + "\n"
        + "Pricing is definitely on high end but people who can \n"
        + "afford it would only step in here, as I sat in parking \n"
        + "lot shoving my pesto chicken sandwich (yummy!!!!), \n"
        + "all high end cars drive in and out.","Tomas Maxx", 4));
    reviews.add(new Review("Very nice selections of authentic taste of Italy...\n"
        + "brought back good memories from Italy and \n"
        + "even NYC. \n"
        + "\n"
        + "Pricing is definitely on high end but people who can \n"
        + "afford it would only step in here, as I sat in parking \n"
        + "lot shoving my pesto chicken sandwich (yummy!!!!), \n"
        + "all high end cars drive in and out.","Tomas Maxx", 4));
    reviews.add(new Review("Very nice selections of authentic taste of Italy...\n"
        + "brought back good memories from Italy and \n"
        + "even NYC. \n"
        + "\n"
        + "Pricing is definitely on high end but people who can \n"
        + "afford it would only step in here, as I sat in parking \n"
        + "lot shoving my pesto chicken sandwich (yummy!!!!), \n"
        + "all high end cars drive in and out.","Tomas Maxx", 4));
    reviews.add(new Review("Very nice selections of authentic taste of Italy...\n"
        + "brought back good memories from Italy and \n"
        + "even NYC. \n"
        + "\n"
        + "Pricing is definitely on high end but people who can \n"
        + "afford it would only step in here, as I sat in parking \n"
        + "lot shoving my pesto chicken sandwich (yummy!!!!), \n"
        + "all high end cars drive in and out.","Tomas Maxx", 4));
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setAdapter(new SimpleRecyclerViewAdapter<>(reviews,R.layout.review_list_item, this));
    return rootView;
  }

  @Override public void rowSelected(Object obj) {

  }
}
