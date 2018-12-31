package com.example.jeffr.capstone_stage2.listener;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.jeffr.capstone_stage2.FirebaseDatabaseContract;
import com.example.jeffr.capstone_stage2.models.User;
import com.example.jeffr.capstone_stage2.ui.CustomizePageActivity;
import com.example.jeffr.capstone_stage2.ui.MainActivity;
import com.example.jeffr.capstone_stage2.ui.NavigationActivity;
import com.example.jeffr.capstone_stage2.ui.SignUpActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginCompleteListener implements OnCompleteListener<AuthResult> {

  private Context mContext;
  private DatabaseReference mDatabase;

  public LoginCompleteListener(Context context) {
    this.mContext = context;
    mDatabase = FirebaseDatabase.getInstance().getReference();
  }

  @Override
  public void onComplete(@NonNull Task<AuthResult> task) {
    Intent intent;
    if (!task.isSuccessful()) {
      Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_LONG).show();
    } else {
      if (mContext instanceof SignUpActivity) {
        User user = new User();
        mDatabase.child(FirebaseDatabaseContract.USERS_CHILD)
            .child(FirebaseDatabaseContract.USER_ID)
            .setValue(user);
        intent = new Intent(mContext, CustomizePageActivity.class);
        ((SignUpActivity) mContext).finish();
        mContext.startActivity(intent);
      } else if (mContext instanceof MainActivity) {
        intent = new Intent(mContext, NavigationActivity.class);
        mContext.startActivity(intent);
        ((MainActivity) mContext).finish();
      }
    }
  }
}
