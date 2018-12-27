package com.example.jeffr.capstone_stage2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.jeffr.capstone_stage2.data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    Bitmap profileImageBitmap;
    Bitmap backgroundImageBitmap;

    private static final String[] stateList =
            {"AK", "AL", "AR", "AZ", "CA", "CO", "CT", "DC", "DE", "FL", "GA", "GU", "HI", "IA",
                    "ID", "IL", "IN", "KS", "KY", "LA", "MA", "MD", "ME", "MH", "MI", "MN",
                    "MO", "MS", "MT", "NC", "ND", "NE", "NH", "NJ", "NM", "NV", "NY", "OH",
                    "OK", "OR", "PA", "PR", "PW", "RI", "SC", "SD", "TN", "TX", "UT", "VA",
                    "VI", "VT", "WA", "WI", "WV", "WY"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_page);
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
        ArrayAdapter<String> favoriteListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, loadCategoriesFromRaw());
        stateSpinner.setAdapter(stateListAdapter);
        favorite1Spinner.setAdapter(favoriteListAdapter);
        favorite2Spinner.setAdapter(favoriteListAdapter);
        favorite3Spinner.setAdapter(favoriteListAdapter);
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

        //TODO Possibly add code to prevent empty strings in name and city
        String name = (TextUtils.isEmpty(nameEditText.getText())) ? ""
                : nameEditText.getText().toString();
        String city = (TextUtils.isEmpty(cityEditText.getText())) ? ""
                : cityEditText.getText().toString();
        String state = (TextUtils.isEmpty(cityEditText.getText())) ? ""
                : stateSpinner.getSelectedItem().toString();

        String favorite1 = favorite1Spinner.getSelectedItem().toString();
        String favorite2 = favorite2Spinner.getSelectedItem().toString();
        String favorite3 = favorite3Spinner.getSelectedItem().toString();
        List<String> favorites = new ArrayList<>();
        favorites.add(favorite1);
        favorites.add(favorite2);
        favorites.add(favorite3);
        Timber.d("Got to here");

        mDatabase.child("users").child(getIntent().getExtras().getString("UserId")).child(
                "name").setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
               Timber.d("Successfully added name to user");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.d(e,"Failed to place user's name");
                    }
                });

        mDatabase.child("users").child(getIntent().getExtras().getString("UserId")).child(
                "state").setValue(state).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.d("Successfully added state to user");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.d(e,"Failed to place user's state");
                    }
                });

        mDatabase.child("users").child(getIntent().getExtras().getString("UserId")).child(
                "city").setValue(city).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.d("Successfully added name to city");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.d(e,"Failed to place user's city");
                    }
                });

        mDatabase.child("users").child(getIntent().getExtras().getString("UserId")).child(
                "favorites").setValue(favorites).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Timber.d("Successfully added name to favorites");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Timber.d(e,"Failed to place user's favorites");
                    }
                });;

        if (profileImageBitmap != null) {
            profileStorageRef = storage.getReference().child(
                    String.format("%s/profile_image", getIntent().getExtras().getString("UserId")));
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
                }
            });

        }

        if (backgroundImageBitmap != null) {
            backgroundStorageRef = storage.getReference().child(String.format("%s/background_image",
                    getIntent().getExtras().getString("UserId")));
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
                    mDatabase.child("users").child(
                            getIntent().getExtras().getString("UserId")).child(
                            "hasBackgroundImage").setValue(true);
                }
            });
        }
    }

    //TODO Fix string for dialog type
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

    public List<String> loadCategoriesFromRaw() {
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
        return categories;
    }

    public DatabaseReference getDatabase() {
        return mDatabase;
    }
}
