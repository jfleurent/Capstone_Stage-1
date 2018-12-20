package com.example.jeffr.capstone_stage2.adapters;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import com.example.jeffr.capstone_stage2.fragment.SearchFragment;
import timber.log.Timber;

import static com.example.jeffr.capstone_stage2.fragment.SearchFragment.bundle;

public class TabAdapter implements TabLayout.OnTabSelectedListener {
  private ViewPager viewPager;
  private String bundleKey;

  public TabAdapter(ViewPager viewPager) {
    this.viewPager = viewPager;
  }

  public TabAdapter(String bundleKey){
    this.bundleKey = bundleKey;
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
        bundle.putInt(bundleKey,i);
        break;
      case "Sort":
        bundle.putString(bundleKey,getSortBy(i));
        break;
      case "Distance":
        bundle.putInt(bundleKey,(i+1)*5*1600);
        break;
      case "Price":
        bundle.putString(bundleKey,(i == 0) ? "1,2,3,4" : String.valueOf(i));
        break;
      case "Rating":
        bundle.putInt(bundleKey,i+1);
      default:
        Timber.d(new Exception(),"Wrong bundle key was placed");
    }

  }

  private String getSortBy(int i){
    switch (i){
      case 0:
        return "best_match";
      case 1:
        return "best_match";
      case 2:
        return "distance";
      case 4:
        return "rating";
      default:
        return "best_match";
    }
  }

}
