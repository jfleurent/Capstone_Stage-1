package com.example.jeffr.capstone_stage2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CustomizePageActivity extends AppCompatActivity {
  private static final int REQUEST_IMAGE_CAPTURE = 685;
  private static final int REQUEST_PICK_IMAGE = 956;
  private static String dialogType;
  private ImageView profileImage;
  private ImageView backgroundImage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customize_page);
    profileImage = findViewById(R.id.profile_imageview);
    backgroundImage = findViewById(R.id.customize_background_imageview);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if(resultCode == RESULT_OK){
      if (requestCode == REQUEST_IMAGE_CAPTURE) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        profileImage.setImageBitmap(imageBitmap);
      }
      else if (requestCode == REQUEST_PICK_IMAGE) {
        InputStream inputStream = null;
        try {
          inputStream = getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }
        Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
        if(dialogType.equals("picture")){
          profileImage.setImageBitmap(imageBitmap);
        }
        else{
          backgroundImage.setImageBitmap(imageBitmap);
        }
      }
    }

  }

  public void showDialog(View view) {
    int[] requestCodes = {REQUEST_IMAGE_CAPTURE, REQUEST_PICK_IMAGE};
    switch (view.getId()) {
      case R.id.change_background_button:
        dialogType= "background";
        ViewDialog.showCustomizePageDialog(this, getResources().getString(R.string.choose_color_text),
            getResources().getString(R.string.choose_background_text), dialogType, requestCodes);
        break;
      case R.id.change_photo_button:
        dialogType = "picture";
        ViewDialog.showCustomizePageDialog(this, getResources().getString(R.string.take_photo_text),
            getResources().getString(R.string.choose_photo_text), dialogType, requestCodes);
        break;
      default:
        Toast.makeText(this, "Unknown Button", Toast.LENGTH_SHORT);
    }
  }
}
