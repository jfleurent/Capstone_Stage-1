package com.example.jeffr.capstone_stage2.models;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.jeffr.capstone_stage2.R;
import com.google.firebase.database.IgnoreExtraProperties;
import com.squareup.picasso.Picasso;
import java.io.Serializable;

@IgnoreExtraProperties
public class Photo implements Serializable {

  private String photoUrl;

  public Photo() {

  }

  public Photo(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  public String getPhotoUrl() {
    return photoUrl;
  }

  public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
  }

  @BindingAdapter({ "bind:photoUrl" })
  public static void loadImage(ImageView view, String photoUrl) {
    if (!photoUrl.equals("")) {
      Picasso.get().load(photoUrl).placeholder(R.drawable.gary).error(R
          .drawable.gary).fit().into(view);
    }
  }
}
