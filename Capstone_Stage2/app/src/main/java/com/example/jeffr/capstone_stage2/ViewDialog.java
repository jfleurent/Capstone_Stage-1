package com.example.jeffr.capstone_stage2;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.jeffr.capstone_stage2.models.FavoriteCategory;
import com.example.jeffr.capstone_stage2.models.Restaurant;
import com.example.jeffr.capstone_stage2.models.User;
import com.example.jeffr.capstone_stage2.ui.CustomizePageActivity;
import com.example.jeffr.capstone_stage2.ui.DetailRestaurantActivity;
import com.example.jeffr.capstone_stage2.ui.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;
import java.util.List;
import timber.log.Timber;

public class ViewDialog {
  public static final String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
  private static Dialog dialog;

  public static void showCustomizePageDialog(final Activity activity, String takeString,
      String chooseString, final String changeName, final int[] requestCodes) {
    dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.customize_page_dialog);
    Button button1 = dialog.findViewById(R.id.take_photo_button);
    Button button2 = dialog.findViewById(R.id.choose_photo_button);
    Button cancelButton = dialog.findViewById(R.id.cancel_button);
    button1.setText(takeString);
    button2.setText(chooseString);
    button1.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (changeName.equals("picture")) {
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          activity.startActivityForResult(intent, requestCodes[0]);
          dialog.dismiss();
        } else {
          final ColorPicker cp = new ColorPicker(activity, 126, 126, 126);
          cp.setCallback(new ColorPickerCallback() {
            @Override
            public void onColorChosen(int color) {
              ImageView imageView = activity.findViewById(
                  R.id.customize_background_imageview);
              imageView.setImageBitmap(null);
              imageView.setBackgroundColor(cp.getColor());

              ((CustomizePageActivity) activity).getDatabase()
                  .child(FirebaseDatabaseContract.USERS_CHILD)
                  .child(
                      USER_ID)
                  .child(
                      FirebaseDatabaseContract.HAS_BG_CHILD)
                  .setValue(false);
              ((CustomizePageActivity) activity).getDatabase()
                  .child(FirebaseDatabaseContract.USERS_CHILD)
                  .child(
                      USER_ID)
                  .child(
                      FirebaseDatabaseContract.BG_COLOR_CHILD)
                  .setValue(cp.getColor());
              cp.dismiss();
            }
          });
          cp.show();

          dialog.dismiss();
        }
      }
    });
    button2.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (changeName.equals("picture")) {
          openFileImages(activity, requestCodes[1]);
          dialog.dismiss();
        } else {
          openFileImages(activity, requestCodes[1]);
          dialog.dismiss();
        }
      }
    });
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
      }
    });

    dialog.show();
  }

  public static void showNewCategoryDialog(final Activity activity) {
    dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.restaurant_category_dialog);
    final EditText categoryEditText = dialog.findViewById(R.id.category_title_edittext);
    Button okButton = dialog.findViewById(R.id.ok_button);
    Button cancelButton = dialog.findViewById(R.id.cancel_button);
    okButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (!TextUtils.isEmpty(categoryEditText.getText())) {
          final DatabaseReference mDatabase = FirebaseDatabase.getInstance()
              .getReference()
              .child(FirebaseDatabaseContract.USERS_CHILD)
              .child(USER_ID)
              .child(FirebaseDatabaseContract.FAVORITE_CATEGORY_CHILD);
          mDatabase
              .push()
              .child(FirebaseDatabaseContract.CATEGORY_TITLE)
              .setValue(categoryEditText.getText().toString());
          //TODO impose limits on how much categories could be made
          mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                if(snapshot.getKey() != null){
                  mDatabase.child(snapshot.getKey()).child(FirebaseDatabaseContract.CATEGORY_REFERENCE).setValue(snapshot.getKey());
                }
                Timber.d("Successfully added reference to category");
              }
            }

            @Override public void onCancelled(@NonNull DatabaseError databaseError) {
              Timber.d("Failed to add reference to category");
            }
          });
        }
        dialog.dismiss();
      }
    });
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
      }
    });
    dialog.show();
  }

  public static void showSelectFavoriteDialog(final Activity activity) {
    dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.select_favorite_dialog);
    final ListView listView = dialog.findViewById(R.id.checkbox_listview);
    listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    final DatabaseReference userReference =
        FirebaseDatabase
            .getInstance()
            .getReference()
            .child(FirebaseDatabaseContract.USERS_CHILD)
            .child(USER_ID);
    final DatabaseReference favoriteCategoryReference =
        userReference.child(FirebaseDatabaseContract.FAVORITE_CATEGORY_CHILD);
    final List<String> favoriteCategories = new ArrayList<>();
    final List<String> favoriteCategoryRefs = new ArrayList<>();
    final List<Integer> favoritesCategorySizes = new ArrayList<>();
    favoriteCategoryReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
          FavoriteCategory category = snapshot.getValue(FavoriteCategory.class);
          favoriteCategories.add(category.getTitle());
          favoriteCategoryRefs.add(snapshot.getKey());
          favoritesCategorySizes.add(
              (category.getRestaurants() != null) ? category.getRestaurants().size() : 0);
        }
        Timber.d("Successfully added categories to Dialog");
      }

      @Override
      public void onCancelled(@NonNull DatabaseError databaseError) {
        Timber.d(databaseError.toException(), "Could not read categories object data");
      }
    });

    listView.setAdapter(
        new ArrayAdapter<>(activity,
            android.R.layout.simple_list_item_multiple_choice,
            favoriteCategories));
    Button okButton = dialog.findViewById(R.id.ok_button);

    okButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Timber.d("ListView Size:" + listView.getCheckedItemPositions().size());
        //Checks selected items
        for (int i = 0; i < favoriteCategories.size(); i++) {
          if (listView.getCheckedItemPositions().get(i)) {
            Timber.d("Reference key:" + favoriteCategoryRefs.get(i));
            Restaurant restaurant = ((DetailRestaurantActivity) activity).getRestaurant();
            restaurant.setCategoryReference(favoriteCategoryRefs.get(i));
            favoriteCategoryReference.child(favoriteCategoryRefs.get(i))
                .child(FirebaseDatabaseContract.RESTAURANTS_CHILD)
                .child(String.valueOf(favoritesCategorySizes.get(i)))
                .setValue(restaurant)
                .addOnSuccessListener(
                    new OnSuccessListener<Void>() {
                      @Override public void onSuccess(Void aVoid) {
                        Timber.d("Successfully add restaurant to category");
                        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                          @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            int favoriteTotal = user.getFavoriteTotal();
                            userReference.child(FirebaseDatabaseContract.FAVORITE_TOTAL_CHILD)
                                .setValue(++favoriteTotal);
                            Timber.d("Successfully add to favorite total");
                          }

                          @Override public void onCancelled(@NonNull DatabaseError databaseError) {
                            Timber.d("Failed to add to favorite total");
                          }
                        });
                      }
                    })
                .addOnFailureListener(new OnFailureListener() {
                  @Override public void onFailure(@NonNull Exception e) {
                    Timber.d(e, "Failed to add restaurant to category");
                  }
                });
          }
        }

        dialog.dismiss();
      }
    });
    dialog.show();
  }

  public static  void showLogoutDialog(final Activity activity){
    new AlertDialog.Builder(activity)
        .setTitle("Log Out")
        .setMessage("Are you sure you want to logout")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
        {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(activity,MainActivity.class);
            activity.startActivity(intent);
            activity.finish();
          }
        })
        .setNegativeButton("No", null)
        .show();
  }

  private static void openFileImages(Activity activity, int requestCode) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"),
        requestCode);
  }
}
