package com.example.jeffr.capstone_stage2.data;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import com.example.jeffr.capstone_stage2.R;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Restaurant implements Serializable {

  private  String imageUrl;
  private String name;
  private float rating;
  private String price;
  private String address;
  private float distance;

  //TODO change when working with the database
  private List<Integer> photos;

  public Restaurant(String imageUrl, String name, float rating, String price, String address,
      float distance) {
    this.imageUrl = imageUrl;
    this.name = name;
    this.rating = rating;
    this.price = price;
    this.address = address;
    this.distance = distance;

    //TODO Remove when database is added
    photos = new ArrayList<>();
    photos.add(R.drawable.gary);
    photos.add(R.drawable.gary);
    photos.add(R.drawable.gary);
    photos.add(R.drawable.gary);
    photos.add(R.drawable.gary);
    photos.add(R.drawable.gary);
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public List<Integer> getPhotos() {
    return photos;
  }

  public void setPhotos(List<Integer> photos) {
    this.photos = photos;
  }

  public float getDistance() {
    return distance;
  }

  public void setDistance(float distance) {
    this.distance = distance;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  public String getPrice() {
    return price;
  }

  public void setPrice(String price) {
    this.price = price;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  @BindingAdapter({"bind:imageUrl"})
  public static void loadImage(ImageView view, String imageUrl) {
    Picasso.get()
        .load(imageUrl)
        .placeholder(R.drawable.gary)
        .fit()
        .into(view);
  }


}
