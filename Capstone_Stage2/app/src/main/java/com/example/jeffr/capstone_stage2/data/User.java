package com.example.jeffr.capstone_stage2.data;

import java.io.Serializable;

public class User implements Serializable {
  private String name;
  private String city;
  private String[] favorite;
  private String favoritesString;
  private int seenTotal;
  private int favoriteTotal;

  public User(String name, String city, String[] favorite, int seenTotal, int favoriteTotal) {
    this.name = name;
    this.city = city;
    this.favorite = favorite;
    this.seenTotal = seenTotal;
    this.favoriteTotal = favoriteTotal;
    setFavoritesString();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String[] getFavorite() {
    return favorite;
  }

  public void setFavorite(String[] favorite) {
    this.favorite = favorite;
  }

  public int getSeenTotal() {
    return seenTotal;
  }

  public void setSeenTotal(int seenTotal) {
    this.seenTotal = seenTotal;
  }

  public int getFavoriteTotal() {
    return favoriteTotal;
  }

  public void setFavoriteTotal(int favoriteTotal) {
    this.favoriteTotal = favoriteTotal;
  }

  public String getFavoritesString() {
    return favoritesString;
  }

  private void setFavoritesString() {
    favoritesString= "";
    for (String f : favorite){
      favoritesString+=f+" | ";
    }
    favoritesString = favoritesString.substring(0,favoritesString.length()-2);
  }
}
