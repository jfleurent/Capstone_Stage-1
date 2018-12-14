package com.example.jeffr.capstone_stage2;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import java.util.ArrayList;
import java.util.List;

public class ViewDialog {

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
      @Override public void onClick(View view) {
        if (changeName.equals("picture")) {
          Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
          activity.startActivityForResult(intent, requestCodes[0]);
          dialog.dismiss();
        } else {
          final ColorPicker cp = new ColorPicker(activity, 0, 126, 255);
          cp.setCallback(new ColorPickerCallback() {
            @Override public void onColorChosen(int color) {
              ImageView imageView =activity.findViewById(R.id.customize_background_imageview);
              imageView.setBackgroundColor(cp.getColor());
              cp.dismiss();
            }
          });
          cp.show();

          dialog.dismiss();
        }
      }
    });
    button2.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
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
      @Override public void onClick(View view) {
        dialog.dismiss();
      }
    });

    dialog.show();
  }

  public static void showNewCategoryDialog(Activity activity) {
    dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.restaurant_category_dialog);
    Button okButton = dialog.findViewById(R.id.ok_button);
    Button cancelButton = dialog.findViewById(R.id.cancel_button);
    okButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        //TODO Add code for adding new category
        dialog.dismiss();
      }
    });
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        dialog.dismiss();
      }
    });
    dialog.show();
  }

  public static void showSelectFavoriteDialog(Activity activity) {
    dialog = new Dialog(activity);
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    dialog.setCancelable(false);
    dialog.setContentView(R.layout.select_favorite_dialog);
    final ListView listView = dialog.findViewById(R.id.checkbox_listview);
    listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    List<String> types = new ArrayList<>();
    types.add("Pizza");
    types.add("Pizza");
    types.add("Pizza");
    types.add("Pizza");
    types.add("Pizza");
    listView.setAdapter(
        new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_multiple_choice,
            types));
    Button okButton = dialog.findViewById(R.id.ok_button);

    okButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        //TODO Add code for selecting checkboxes

        //Checks selected items
        //listView.getCheckedItemPositions();

        dialog.dismiss();
      }
    });
    dialog.show();
  }

  private static void openFileImages(Activity activity, int requestCode) {
    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"),
        requestCode);
  }
}
