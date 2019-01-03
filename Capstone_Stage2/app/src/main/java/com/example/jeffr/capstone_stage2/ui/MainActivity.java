package com.example.jeffr.capstone_stage2.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeffr.capstone_stage2.listener.LoginCompleteListener;
import com.example.jeffr.capstone_stage2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
  private Intent intent;
  private EditText emailEditText;
  private EditText passwordEditText;
  private FirebaseAuth mFirebaseAuth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().hide();
    mFirebaseAuth = FirebaseAuth.getInstance();
    emailEditText = findViewById(R.id.email_edittext);
    passwordEditText = findViewById(R.id.password_edittext);
  }

  public void mainActivityButtonsOnClick(View view) {
    switch (view.getId()) {
      case R.id.login_button:
        if (!TextUtils.isEmpty(emailEditText.getText()) && !TextUtils.isEmpty(
            passwordEditText.getText())) {
          mFirebaseAuth.signInWithEmailAndPassword(emailEditText.getText().toString(),
              passwordEditText.getText().toString()).addOnCompleteListener(this,
              new LoginCompleteListener(this));
        } else {
          Toast.makeText(this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
        }
        break;
      case R.id.sign_in_button:
        intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        break;
      default:
        Toast.makeText(this, "Unknown Button", Toast.LENGTH_SHORT).show();
    }
  }

}
