package com.example.jeffr.capstone_stage2.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@IgnoreExtraProperties
public class Review implements Serializable, Parcelable {

  @SerializedName("text")
  String reviewString;

  @SerializedName("author_name")
  String userName;

  @SerializedName("rating")
  float rating;

  public Review() {

  }

  public Review(String reviewString, String userName, float rating) {
    this.reviewString = reviewString;
    this.userName = userName;
    this.rating = rating;
  }

  protected Review(Parcel in) {
    reviewString = in.readString();
    userName = in.readString();
    rating = in.readFloat();
  }

  public static final Creator<Review> CREATOR = new Creator<Review>() {
    @Override
    public Review createFromParcel(Parcel in) {
      return new Review(in);
    }

    @Override
    public Review[] newArray(int size) {
      return new Review[size];
    }
  };

  public String getReviewString() {
    return reviewString;
  }

  public void setReviewString(String reviewString) {
    this.reviewString = reviewString;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public float getRating() {
    return rating;
  }

  public void setRating(float rating) {
    this.rating = rating;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(reviewString);
    dest.writeString(userName);
    dest.writeFloat(rating);
  }
}
