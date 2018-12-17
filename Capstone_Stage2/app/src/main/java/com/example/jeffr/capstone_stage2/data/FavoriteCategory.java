package com.example.jeffr.capstone_stage2.data;

import java.io.Serializable;
import java.util.List;

public class FavoriteCategory implements Serializable {
  //TODO Add variable for image
  private String title;
  private List<Restaurant> restaurants;

  public FavoriteCategory(String title,
      List<Restaurant> restaurants) {
    this.title = title;
    this.restaurants = restaurants;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<Restaurant> getRestaurants() {
    return restaurants;
  }

  public void setRestaurants(List<Restaurant> restaurants) {
    this.restaurants = restaurants;
  }
}
