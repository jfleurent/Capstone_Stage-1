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
  private GoogleSignInClient mGoogleSignInClient;
  private static final int RC_SIGN_IN = 9001;

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
        Timber.d("Got to here");
        GoogleSignInOptions gso =
            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_web_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        break;
      default:
        Toast.makeText(this, "Unknown Button", Toast.LENGTH_SHORT).show();
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
      } catch (ApiException e) {
        Timber.d(e, "Google sign in failed");
      }
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mFirebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
              FirebaseUser user = mFirebaseAuth.getCurrentUser();
            }
          }
        });
  }
}
