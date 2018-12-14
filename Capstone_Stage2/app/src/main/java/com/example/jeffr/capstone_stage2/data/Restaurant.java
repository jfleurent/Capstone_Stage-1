package com.example.jeffr.capstone_stage2.data;

public class Restaurant {

  //TODO add variable for image
  private String name;
  private float rating;
  private int price;
  private String address;

  public Restaurant(String name, float rating, int price, String address) {
    this.name = name;
    this.rating = rating;
    this.price = price;
    this.address = address;
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

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
