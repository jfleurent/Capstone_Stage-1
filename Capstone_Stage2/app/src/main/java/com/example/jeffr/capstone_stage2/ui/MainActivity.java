package com.example.jeffr.capstone_stage2.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeffr.capstone_stage2.listener.LoginCompleteListener;
import com.example.jeffr.capstone_stage2.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private EditText emailEditText;
    private EditText passwordEditText;
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                }
                break;
            case R.id.sign_in_button:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.google_button:
                break;
            case R.id.facebook_button:
                break;
            case R.id.twitter_button:
                break;
            default:
                Toast.makeText(this, "Unknown Button", Toast.LENGTH_SHORT);

        }
    }
}
