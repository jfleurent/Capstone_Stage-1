package com.example.jeffr.capstone_stage2.data;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.example.jeffr.capstone_stage2.R;
import com.squareup.picasso.Picasso;

public class Photo {

   private String photoUrl;

    public Photo(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @BindingAdapter({"bind:photoUrl"})
    public static void loadImage(ImageView view, String photoUrl) {
        if (!photoUrl.equals(""))
            Picasso.get().load(photoUrl).placeholder(R.drawable.gary).error(R
                    .drawable.gary).fit().into(view);
    }
}
