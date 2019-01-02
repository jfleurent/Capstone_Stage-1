package com.example.jeffr.capstone_stage2.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeffr.capstone_stage2.listener.LoginCompleteListener;
import com.example.jeffr.capstone_stage2.R;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
  private FirebaseAuth auth;
  private EditText password;
  private EditText email;
  private EditText confirmPassword;
  private Toolbar toolbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    auth = FirebaseAuth.getInstance();
    setContentView(R.layout.activity_sign_up);
    toolbar = findViewById(R.id.sign_up_toolbar);
    password = findViewById(R.id.sign_up_password_edittext);
    email = findViewById(R.id.sign_up_email_edittext);
    confirmPassword = findViewById(R.id.sign_up_confirm_edittext);
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
  }

  public void signUpOnClick(View view) {
    switch (view.getId()) {
      case R.id.sign_up_button:
        if (!TextUtils.isEmpty(password.getText())
            && !TextUtils.isEmpty(email.getText())
            && !TextUtils.isEmpty(confirmPassword.getText())) {
          if (password.getText().equals(confirmPassword)) {
            auth.createUserWithEmailAndPassword(email.getText().toString(),
                password.getText().toString()).addOnCompleteListener(this,
                new LoginCompleteListener(this));
          } else {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
          }
        } else {
          Toast.makeText(this, "One of the fields is empty", Toast.LENGTH_SHORT).show();
        }
        break;
    }
  }
}
