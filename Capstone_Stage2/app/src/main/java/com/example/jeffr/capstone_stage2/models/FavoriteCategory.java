package com.example.jeffr.capstone_stage2.models;

import android.databinding.BindingAdapter;
import android.widget.ImageView;
import com.example.jeffr.capstone_stage2.R;
import com.google.firebase.database.IgnoreExtraProperties;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class FavoriteCategory implements Serializable {
  //TODO Add variable for image
  private String title;
  private List<Restaurant> restaurants;
  private String categoryReference;

  public FavoriteCategory() {

  }

  public FavoriteCategory(String title,
      List<Restaurant> restaurants) {
    this.title = title;
    this.restaurants = restaurants;
  }

  public String getCategoryReference() {
    return categoryReference;
  }

  public void setCategoryReference(String categoryReference) {
    this.categoryReference = categoryReference;
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

  @BindingAdapter({ "bind:category_image_url" })
  public static void loadBackgroundImage(ImageView view, String category_image_url) {
    Picasso.get().load(category_image_url).placeholder(R.drawable.gary).error(R
        .drawable.gary).fit().into(view);
  }
}
