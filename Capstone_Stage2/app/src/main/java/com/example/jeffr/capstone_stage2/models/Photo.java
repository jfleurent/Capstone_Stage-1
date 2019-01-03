package com.example.jeffr.capstone_stage2.models;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.example.jeffr.capstone_stage2.R;
import com.google.firebase.database.IgnoreExtraProperties;
import com.squareup.picasso.Picasso;
import java.io.Serializable;

@IgnoreExtraProperties
public class Photo implements Serializable, Parcelable {

  private String photoUrl;

  public Photo() {

  }

  public Photo(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  protected Photo(Parcel in) {
    photoUrl = in.readString();
  }

  public static final Creator<Photo> CREATOR = new Creator<Photo>() {
    @Override
    public Photo createFromParcel(Parcel in) {
      return new Photo(in);
    }

    @Override
    public Photo[] newArray(int size) {
      return new Photo[size];
    }
  };

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  @BindingAdapter({"bind:photoUrl"})
  public static void loadImage(ImageView view, String photoUrl) {
    if (!photoUrl.equals("")) {
      Picasso.get().load(photoUrl).placeholder(R.drawable.findndin_logo).error(R
          .drawable.findndin_logo).fit().into(view);
    }
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(photoUrl);
  }
}
