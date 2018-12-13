package com.example.jeffr.capstone_stage2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Intent intent;
    private EditText userNameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userNameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext);
    }

    public void mainActivityButtonsOnClick(View view){
        switch (view.getId()){
            case R.id.login_button:
                //TODO Add authorization code
                intent = new Intent(this,NavigationActivity.class);
                startActivity(intent);
                break;
            case  R.id.sign_in_button:
                intent = new Intent(this,SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.google_button:
                break;
            case R.id.facebook_button:
                break;
            case R.id.twitter_button:
                break;
            default:
                Toast.makeText(this,"Unknown Button",Toast.LENGTH_SHORT);

        }
    }
}
