package com.example.jeffr.capstone_stage2;

import android.databinding.DataBindingUtil;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.example.jeffr.capstone_stage2.adapters.SectionsPagerAdapter;
import com.example.jeffr.capstone_stage2.adapters.TabAdapter;
import com.example.jeffr.capstone_stage2.data.Restaurant;
import com.example.jeffr.capstone_stage2.databinding.ActivityDetailRestaurantBinding;

public class DetailRestaurantActivity extends AppCompatActivity {

  private ViewPager viewPager;
  private TabLayout tabLayout;
  private Restaurant restaurant;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_restaurant);
    getSupportActionBar().hide();
    restaurant = (Restaurant) getIntent().getExtras().getSerializable("Restaurant");
    ActivityDetailRestaurantBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_detail_restaurant);
    binding.setRestaurant(restaurant);
    viewPager = findViewById(R.id.detail_restaurant_viewpager);
    tabLayout = findViewById(R.id.tabs);
    viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(),restaurant));
    viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    tabLayout.addOnTabSelectedListener(new TabAdapter(viewPager));

  }

  public  void favoriteOnClick(View view){
    ViewDialog.showSelectFavoriteDialog(this);
  }

}
