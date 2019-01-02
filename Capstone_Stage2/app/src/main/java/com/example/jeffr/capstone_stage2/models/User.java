package com.example.jeffr.capstone_stage2.models;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.jeffr.capstone_stage2.R;
import com.google.firebase.database.IgnoreExtraProperties;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class User implements Serializable {
  private String name;
  private String city;
  private List<String> favorite;
  private String state;
  private int seenTotal;
  private int favoriteTotal;
  private String photo_url;
  private String background_url;
  private boolean hasBackgroundImage;
  private Long backgroundColor;
  private List<Restaurant> restaurantHistory;

  public User() {

  }

  public User(String name, String city, List<String> favorite, String state, int seenTotal,
      int favoriteTotal, String photo_url, String background_url, boolean hasBackgroundImage,
      Long backgroundColor) {
    this.name = name;
    this.city = city;
    this.favorite = favorite;
    this.state = state;
    this.seenTotal = seenTotal;
    this.favoriteTotal = favoriteTotal;
    this.photo_url = photo_url;
    this.background_url = (hasBackgroundImage) ? background_url : String.valueOf(backgroundColor);
    this.hasBackgroundImage = hasBackgroundImage;
    this.backgroundColor = backgroundColor;
  }

  public List<Restaurant> getRestaurantHistory() {
    return restaurantHistory;
  }

  public void setRestaurantHistory(
      List<Restaurant> restaurantHistory) {
    this.restaurantHistory = restaurantHistory;
  }

  public String getPhoto_url() {
    return photo_url;
  }

  public void setPhoto_url(String photo_url) {
    this.photo_url = photo_url;
  }

  public String getBackground_url() {
    return background_url;
  }

  public void setBackground_url(String background_url) {
    this.background_url = background_url;
  }

  public boolean isHasBackgroundImage() {
    return hasBackgroundImage;
  }

  public void setHasBackgroundImage(boolean hasBackgroundImage) {
    this.hasBackgroundImage = hasBackgroundImage;
  }

  public List<String> getFavorite() {
    return favorite;
  }

  public void setFavorite(List<String> favorite) {
    this.favorite = favorite;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Long getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(Long
      backgroundColor) {
    this.backgroundColor = backgroundColor;
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
    String favoritesString = "";
    if (favorite != null && favorite.size() != 0) {
      for (String f : favorite) {
        favoritesString += f + " | ";
      }
      return favoritesString.substring(0, favoritesString.length() - 2);
    }
    return favoritesString;
  }

  public String getCityString() {
    return String.format("%s, %s", city, state);
  }

  @BindingAdapter({ "bind:photo_url" })
  public static void loadProfileImage(ImageView view, String photo_url) {
    if (photo_url != null && !photo_url.equals("")) {
      Picasso.get().load(photo_url).placeholder(R.drawable.findndin_logo).error(R
          .drawable.findndin_logo).fit().into(view);
    }
  }

  @BindingAdapter({ "bind:background_url" })
  public static void loadBackgroundImage(ImageView view, String background_url) {
    if (background_url != null && !background_url.equals("")) {
      try {
        int backgroundColor = Integer.valueOf(background_url);
        view.setBackgroundColor(backgroundColor);
      } catch (Exception e) {
        Picasso.get().load(background_url).placeholder(R.drawable.findndin_logo).error(R
            .drawable.findndin_logo).fit().into(view);
      }
    }
  }
}
