package com.example.jeffr.capstone_stage2.data;

public class Review {
  String reviewString;
  String userName;
  float rating;

  public Review(String reviewString, String userName, float rating) {
    this.reviewString = reviewString;
    this.userName = userName;
    this.rating = rating;
  }

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
}
