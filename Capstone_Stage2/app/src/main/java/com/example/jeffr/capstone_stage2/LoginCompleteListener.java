package com.example.jeffr.capstone_stage2;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.jeffr.capstone_stage2.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginCompleteListener implements OnCompleteListener<AuthResult> {

    private Context mContext;
    private Intent intent;
    private DatabaseReference mDatabase;


    public LoginCompleteListener(Context context) {
        this.mContext = context;
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (!task.isSuccessful()) {
            Toast.makeText(mContext,task.getException().getMessage(),Toast.LENGTH_LONG).show();
        } else {
            String userId = task.getResult().getUser().getUid();
            if(mContext instanceof SignUpActivity){
                User user = new User();
                mDatabase.child("users").child(userId).setValue(user);
                intent = new Intent(mContext, CustomizePageActivity.class);
                intent.putExtra("UserId",userId);
                mContext.startActivity(intent);
            }
            else if(mContext instanceof MainActivity){
                intent = new Intent(mContext, NavigationActivity.class);
                intent.putExtra("UserId",userId);
                mContext.startActivity(intent);
            }

        }
    }
}
