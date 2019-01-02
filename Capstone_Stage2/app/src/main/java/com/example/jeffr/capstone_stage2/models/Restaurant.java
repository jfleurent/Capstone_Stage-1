package com.example.jeffr.capstone_stage2.models;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.example.jeffr.capstone_stage2.R;
import com.google.firebase.database.IgnoreExtraProperties;
import com.squareup.picasso.Picasso;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Restaurant implements Serializable, Comparable<Restaurant>, Parcelable {

    private String imageUrl;
    private String name;
    private float rating;
    private String price;
    private String address;
    private float distance;
    private float latitude;
    private float longitude;
    private List<Photo> photos;
    private String distanceString;
    private List<Review> reviews;
    private String categoryReference;

    public Restaurant(){

    }

    public Restaurant(String imageUrl, String name, float rating, String
            price, String address, float distance, float latitude, float
            longitude) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.rating = rating;
        this.price = price;
        this.address = address;
        this.distance = distance;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distanceString = String.format("%.1f mi", distance);
        photos = new ArrayList<>();
    }

    protected Restaurant(Parcel in) {
        imageUrl = in.readString();
        name = in.readString();
        rating = in.readFloat();
        price = in.readString();
        address = in.readString();
        distance = in.readFloat();
        latitude = in.readFloat();
        longitude = in.readFloat();
        distanceString = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getCategoryReference() {
        return categoryReference;
    }

    public void setCategoryReference(String categoryReference) {
        this.categoryReference = categoryReference;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getDistanceString() {
        return distanceString;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
        if (imageUrl != null && !imageUrl.equals(""))
            Picasso.get().load(imageUrl).placeholder(R.drawable.gary).error(R
                    .drawable.gary).fit().into(view);
    }


    @Override
    public int compareTo(Restaurant restaurant) {
        return this.price.length() - restaurant.price.length();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(name);
        dest.writeFloat(rating);
        dest.writeString(price);
        dest.writeString(address);
        dest.writeFloat(distance);
        dest.writeFloat(latitude);
        dest.writeFloat(longitude);
        dest.writeString(distanceString);
    }
}