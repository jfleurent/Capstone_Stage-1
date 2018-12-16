package com.example.jeffr.capstone_stage2.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

public class TabAdapter implements TabLayout.OnTabSelectedListener {
  private ViewPager viewPager;

  public TabAdapter(ViewPager viewPager) {
    this.viewPager = viewPager;
  }

  @Override public void onTabSelected(TabLayout.Tab tab) {
    viewPager.setCurrentItem(tab.getPosition());
  }

  @Override public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override public void onTabReselected(TabLayout.Tab tab) {

  }
}
