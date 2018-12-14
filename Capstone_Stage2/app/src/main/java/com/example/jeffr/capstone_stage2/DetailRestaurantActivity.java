package com.example.jeffr.capstone_stage2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DetailRestaurantActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_detail_restaurant);
  }

  public  void favoriteOnClick(View view){
    ViewDialog.showSelectFavoriteDialog(this);
  }
}
