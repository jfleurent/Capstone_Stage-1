package com.example.jeffr.capstone_stage2.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.example.jeffr.capstone_stage2.models.Restaurant;
import com.example.jeffr.capstone_stage2.fragment.PhotoFragment;
import com.example.jeffr.capstone_stage2.fragment.ReviewFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
  private Restaurant restaurant;

  public SectionsPagerAdapter(FragmentManager fm, Restaurant restaurant) {
    super(fm);
    this.restaurant = restaurant;
  }

  @Override public Fragment getItem(int i) {
    return (i == 0) ? ReviewFragment.newInstance(restaurant) : PhotoFragment.newInstance(restaurant);
  }

  @Override public int getCount() {
    return 2;
  }
}
