package com.example.jeffr.capstone_stage2.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.jeffr.capstone_stage2.FirebaseDatabaseContract;
import com.example.jeffr.capstone_stage2.R;
import com.example.jeffr.capstone_stage2.ViewDialog;
import com.example.jeffr.capstone_stage2.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class CustomizePageActivity extends AppCompatActivity {
  private static final int REQUEST_IMAGE_CAPTURE = 685;
  private static final int REQUEST_PICK_IMAGE = 956;
  private static String dialogType;
  private ImageView profileImage;
  private ImageView backgroundImage;
  private EditText nameEditText;
  private EditText cityEditText;
  private Spinner stateSpinner;
  private Spinner favorite1Spinner;
  private Spinner favorite2Spinner;
  private Spinner favorite3Spinner;
  private DatabaseReference mDatabase;
  private FirebaseStorage storage;
  private StorageReference profileStorageRef;
  private StorageReference backgroundStorageRef;
  private Bitmap profileImageBitmap;
  private Bitmap backgroundImageBitmap;
  private User user;
  private float rotation = 1;

  private static final String[] stateList =
      {"State", "AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "GU", "HI", "IA",
          "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MH", "MI", "MN",
          "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH",
          "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT", "VA",
          "VI", "VT", "WA", "WI", "WV", "WY"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customize_page);
    getSupportActionBar().hide();
    profileImage = findViewById(R.id.profile_imageview);
    backgroundImage = findViewById(R.id.customize_background_imageview);
    stateSpinner = findViewById(R.id.state_spinner);
    favorite1Spinner = findViewById(R.id.favorite1_spinner);
    favorite2Spinner = findViewById(R.id.favorite2_spinner);
    favorite3Spinner = findViewById(R.id.favoirte3_spinner);
    nameEditText = findViewById(R.id.name_edittext);
    cityEditText = findViewById(R.id.city_edittext);
    mDatabase = FirebaseDatabase.getInstance().getReference();
    storage = FirebaseStorage.getInstance();
    ArrayAdapter<String> stateListAdapter = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, stateList);
    ArrayAdapter<String> favoriteListAdapter1 = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, loadCategoriesFromRaw(1));
    ArrayAdapter<String> favoriteListAdapter2 = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, loadCategoriesFromRaw(2));
    ArrayAdapter<String> favoriteListAdapter3 = new ArrayAdapter<String>(this,
        android.R.layout.simple_spinner_item, loadCategoriesFromRaw(3));
    stateSpinner.setAdapter(stateListAdapter);
    favorite1Spinner.setAdapter(favoriteListAdapter1);
    favorite2Spinner.setAdapter(favoriteListAdapter2);
    favorite3Spinner.setAdapter(favoriteListAdapter3);
    user = (User) getIntent().getExtras().get("User");
    Picasso.get().load(user.getPhoto_url()).placeholder(R.drawable.gary).fit().into(profileImage);
    Timber.d(user.getBackground_url());
    try {
      backgroundImage.setBackgroundColor(Integer.valueOf(user.getBackground_url()));
    } catch (Exception e) {
      Picasso.get()
          .load(user.getBackground_url())
          .placeholder(R.drawable.gary)
          .into(backgroundImage);
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if (resultCode == RESULT_OK) {
      if (requestCode == REQUEST_IMAGE_CAPTURE) {
        Bundle extras = data.getExtras();
        profileImageBitmap = (Bitmap) extras.get("data");
        profileImage.setImageBitmap(profileImageBitmap);
      } else if (requestCode == REQUEST_PICK_IMAGE) {
        InputStream inputStream = null;
        try {
          inputStream = getContentResolver().openInputStream(data.getData());
        } catch (FileNotFoundException e) {
          e.printStackTrace();
        }

        if (dialogType.equals("picture")) {
          profileImageBitmap = BitmapFactory.decodeStream(inputStream);
          profileImage.setImageBitmap(profileImageBitmap);
        } else {
          backgroundImageBitmap = BitmapFactory.decodeStream(inputStream);
          backgroundImage.setImageBitmap(backgroundImageBitmap);
        }
      }
    }
  }

  public void updateProfile(View view) {
    updateFavorites();
    updateUsername();
    updateCity();
    updateState();
    if (profileImageBitmap != null) {
      uploadProfileImage();
    }
    if (backgroundImageBitmap != null) {
      uploadBackgroundImage();
    }
    Toast.makeText(this, "Profile Updated", Toast.LENGTH_LONG).show();
    finish();
  }

  public void showDialog(View view) {
    int[] requestCodes = {REQUEST_IMAGE_CAPTURE, REQUEST_PICK_IMAGE};
    switch (view.getId()) {
      case R.id.change_background_button:
        dialogType = "background";
        ViewDialog.showCustomizePageDialog(this,
            getResources().getString(R.string.choose_color_text),
            getResources().getString(R.string.choose_background_text), dialogType,
            requestCodes);
        break;
      case R.id.change_photo_button:
        dialogType = "picture";
        ViewDialog.showCustomizePageDialog(this,
            getResources().getString(R.string.take_photo_text),
            getResources().getString(R.string.choose_photo_text), dialogType,
            requestCodes);
        break;
      default:
        Toast.makeText(this, "Unknown Button", Toast.LENGTH_SHORT);
    }
  }

  public List<String> loadCategoriesFromRaw(int number) {
    List<String> categories = new ArrayList<>();
    String json = null;
    try {
      InputStream is = getResources().openRawResource(getResources()
          .getIdentifier("categories", "raw", getPackageName()));
      int size = is.available();
      byte[] buffer = new byte[size];
      is.read(buffer);
      is.close();
      json = new String(buffer, "UTF-8");
    } catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }
    try {
      JSONArray jsonArray = new JSONArray(json);
      for (int i = 0; i < jsonArray.length(); i++) {
        try {
          JSONObject jsonObject = jsonArray.getJSONObject(i);
          String parentCategory = jsonObject.getJSONArray("parents").getString(0);
          if (parentCategory.equals("restaurants") || parentCategory.equals("bar")
              || parentCategory.equals("caribbean") || parentCategory.equals("food")
              || parentCategory.equals("nightlife")) {
            categories.add(jsonObject.getString("alias"));
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    categories.add(0, String.format("Favorite %d", number));
    return categories;
  }

  public DatabaseReference getDatabase() {
    return mDatabase;
  }

  public void rotateOnClick(View view) {
    profileImage.setRotation(90 * (rotation % 4));
    Matrix matrix = new Matrix();
    matrix.postRotate(90 * (rotation % 4));
    rotation++;
    profileImageBitmap = ((BitmapDrawable) profileImage.getDrawable()).getBitmap();
    profileImageBitmap =
        Bitmap.createBitmap(profileImageBitmap, 0, 0, profileImageBitmap.getWidth(),
            profileImageBitmap.getHeight(), matrix, true);
    if (profileImageBitmap != null) {
      uploadProfileImage();
    }
  }

  private void uploadBackgroundImage() {
    backgroundStorageRef = storage.getReference().child(String.format("%s/background_image",
        FirebaseDatabaseContract.USER_ID));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    backgroundImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] data = baos.toByteArray();

    UploadTask uploadTask = backgroundStorageRef.putBytes(data);
    uploadTask.addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception exception) {
        Timber.d(exception, "Failed to upload background image");
      }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
      @Override
      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Timber.d("Successfully upload background image");
        mDatabase.child(FirebaseDatabaseContract.USERS_CHILD).child(
            FirebaseDatabaseContract.USER_ID).child(
            FirebaseDatabaseContract.HAS_BG_CHILD).setValue(true);
        backgroundStorageRef.getDownloadUrl().addOnCompleteListener(
            new OnCompleteListener<Uri>() {
              @Override
              public void onComplete(@NonNull Task<Uri> task) {
                String backgroundUrl = task.getResult().toString();
                mDatabase.child(FirebaseDatabaseContract.USERS_CHILD)
                    .child(FirebaseDatabaseContract.USER_ID)
                    .child(
                        FirebaseDatabaseContract.BG_URL_CHILD)
                    .setValue(backgroundUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                        Timber.d("Successfully added background_url to user");
                      }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                        Timber.d(e, "Failed to place user's background_url");
                      }
                    });
              }
            }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Timber.d(e, "Failed to download background url");
          }
        });
      }
    });
  }

  private void uploadProfileImage() {
    profileStorageRef = storage.getReference().child(
        String.format("%s/profile_image", FirebaseDatabaseContract.USER_ID));
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    profileImageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
    byte[] data = baos.toByteArray();

    UploadTask uploadTask = profileStorageRef.putBytes(data);
    uploadTask.addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception exception) {
        Timber.d(exception, "Failed to upload profile image");
      }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
      @Override
      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Timber.d("Successfully upload profile image");
        profileStorageRef.getDownloadUrl().addOnCompleteListener(
            new OnCompleteListener<Uri>() {
              @Override
              public void onComplete(@NonNull Task<Uri> task) {
                String photoUrl = task.getResult().toString();
                mDatabase.child(FirebaseDatabaseContract.USERS_CHILD)
                    .child(FirebaseDatabaseContract.USER_ID)
                    .child(
                        FirebaseDatabaseContract.PHOTO_URL_CHILD)
                    .setValue(photoUrl)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                        Timber.d("Successfully added photo_url to user");
                      }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                        Timber.d(e, "Failed to place user's photo_url");
                      }
                    });
              }
            }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Timber.d(e, "Failed to download photo url");
          }
        });
      }
    });
  }

  private void updateFavorites() {
    String favorite1 = " ";
    String favorite2 = " ";
    String favorite3 = " ";
    List<String> favorites = new ArrayList<>();
    if (favorite1Spinner.getSelectedItem().toString().endsWith("1")) {
      if (user.getFavorite() == null || user.getFavorite().size() < 1) {
        favorites.add(favorite1);
      } else {
        favorite1 = user.getFavorite().get(0);
        favorites.add(favorite1);
      }
    } else {
      favorite1 = favorite1Spinner.getSelectedItem().toString();
      favorites.add(favorite1);
    }
    if (favorite2Spinner.getSelectedItem().toString().endsWith("2")) {
      if (user.getFavorite() == null || user.getFavorite().size() < 2) {
        favorites.add(favorite2);
      } else {
        favorite2 = user.getFavorite().get(1);
        favorites.add(favorite2);
      }
    } else {
      favorite2 = favorite2Spinner.getSelectedItem().toString();
      favorites.add(favorite2);
    }
    if (favorite3Spinner.getSelectedItem().toString().endsWith("3")) {
      if (user.getFavorite() == null || user.getFavorite().size() < 3) {
        favorites.add(favorite3);
      } else {
        favorite3 = user.getFavorite().get(2);
        favorites.add(favorite3);
      }
    } else {
      favorite3 = favorite3Spinner.getSelectedItem().toString();
      favorites.add(favorite3);
    }

    mDatabase.child(FirebaseDatabaseContract.USERS_CHILD)
        .child(FirebaseDatabaseContract.USER_ID)
        .child(
            FirebaseDatabaseContract.FAVORITES_CHILD)
        .setValue(favorites)
        .addOnSuccessListener(new OnSuccessListener<Void>() {
          @Override
          public void onSuccess(Void aVoid) {
            Timber.d("Successfully added name to favorites");
          }
        })
        .addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
            Timber.d(e, "Failed to place user's favorites");
          }
        });
  }

  private void updateUsername() {
    String name = (TextUtils.isEmpty(nameEditText.getText())) ? ""
        : nameEditText.getText().toString();
    if (!name.isEmpty()) {
      mDatabase.child(FirebaseDatabaseContract.USERS_CHILD)
          .child(FirebaseDatabaseContract.USER_ID)
          .child(
              FirebaseDatabaseContract.NAME_CHILD)
          .setValue(name)
          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              Timber.d("Successfully added name to user");
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Timber.d(e, "Failed to place user's name");
            }
          });
    }
  }

  private void updateCity() {
    String city = (TextUtils.isEmpty(cityEditText.getText())) ? ""
        : cityEditText.getText().toString();
    if (!city.isEmpty()) {
      mDatabase.child(FirebaseDatabaseContract.USERS_CHILD)
          .child(FirebaseDatabaseContract.USER_ID)
          .child(
              FirebaseDatabaseContract.CITY_CHILD)
          .setValue(city)
          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              Timber.d("Successfully added city to user");
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Timber.d(e, "Failed to place user's city");
            }
          });
    }
  }

  private void updateState() {
    String state = (TextUtils.isEmpty(cityEditText.getText())) ? ""
        : stateSpinner.getSelectedItem().toString();
    if (!state.isEmpty() && !state.equals("State")) {
      mDatabase.child(FirebaseDatabaseContract.USERS_CHILD)
          .child(FirebaseDatabaseContract.USER_ID)
          .child(
              FirebaseDatabaseContract.STATE_CHILD)
          .setValue(state)
          .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
              Timber.d("Successfully added state to user");
            }
          })
          .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              Timber.d(e, "Failed to place user's state");
            }
          });
    }
  }
}
