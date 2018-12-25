package com.example.jeffr.capstone_stage2.adapters;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import timber.log.Timber;


public class TabAdapter implements TabLayout.OnTabSelectedListener {
  private ViewPager viewPager;
  private String bundleKey;
  private Fragment fragment;

  public TabAdapter(ViewPager viewPager) {
    this.viewPager = viewPager;
  }

  public TabAdapter(String bundleKey, Fragment fragment){
    this.bundleKey = bundleKey;
    this.fragment = fragment;
  }

  @Override public void onTabSelected(TabLayout.Tab tab) {
    if(viewPager!=null){
      viewPager.setCurrentItem(tab.getPosition());
    }
    else{
      addToBundle(tab.getPosition());
    }
  }

  @Override public void onTabUnselected(TabLayout.Tab tab) {

  }

  @Override public void onTabReselected(TabLayout.Tab tab) {

  }

  private void addToBundle(int i){
    switch (bundleKey){
      case "Amount":
        fragment.getArguments().putInt(bundleKey,i);
        break;
      case "Sort":
        fragment.getArguments().putString(bundleKey,getSortBy(i));
        break;
      case "Distance":
        fragment.getArguments().putInt(bundleKey,(i+1)*5*1600);
        break;
      case "Price":
        fragment.getArguments().putString(bundleKey,(i == 0) ? "1,2,3,4" : String.valueOf(i));
        break;
      case "Rating":
        fragment.getArguments().putInt(bundleKey,i+1);
      default:
        Timber.d(new Exception(),"Wrong bundle key was placed");
    }

  }

  private String getSortBy(int i){
    switch (i){
      case 0:
        return "best_match";
      case 1:
        return "price";
      case 2:
        return "distance";
      case 4:
        return "rating";
      default:
        return "best_match";
    }
  }

}
