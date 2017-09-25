package com.salajim.musab.myrestaurants.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.salajim.musab.myrestaurants.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = CreateAccountActivity.class.getSimpleName();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Bind(R.id.createUserButton) Button mCreateUserButton;
    @Bind(R.id.nameEditText) EditText mNameEditText;
    @Bind(R.id.emailEditText) EditText mEmailEditText;
    @Bind(R.id.passwordEditText) EditText mPasswordEditText;
    @Bind(R.id.confirmPasswordEditText) EditText mConfirmPasswordEditText;
    @Bind(R.id.loginTextView) TextView mLoginTextView;
    private ProgressDialog mAuthProgressDialog;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();

        mCreateUserButton.setOnClickListener(this);
        mLoginTextView.setOnClickListener(this);

        createAuthStateListener();
        createAuthProgressDialog();
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authentication with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View view) {
        if(view == mLoginTextView) {
            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//Called intent flags to manage our back stack of tasks.
            startActivity(intent);
            finish();
        }
        if(view == mCreateUserButton) {
            createNewUser();
        }
    }

    private void createNewUser() {
        //We fetch the contents of our registration form and transform each value into a string
         mName = mNameEditText.getText().toString().trim();
        final String email = mEmailEditText.getText().toString().trim();
        String password = mPasswordEditText.getText().toString().trim();
        String confirmPassword = mConfirmPasswordEditText.getText().toString().trim();

        boolean validEmail = isValidEmail(email);
        boolean validName = isValidName(mName);
        boolean validPassword = isValidPassword(password, confirmPassword);
  //The if statement will halt our createNewUser() method, and the validation method(s) will display errors
        //if the user info is not valid
        if(!validEmail || !validName || !validPassword) return;

        mAuthProgressDialog.show();

      //We create a new user account in Firebase, passing in the user's email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthProgressDialog.dismiss();
                        if (task.isSuccessful()) {

                            Log.d(TAG, "Authentication successful");
                            //We grab the result from the Task object returned in onComplete().
                            //then retrieve the specific user by calling Firebase's getUser() method.
                            createFirebaseUserProfile(task.getResult().getUser());
                        }else {
                            Toast.makeText(CreateAccountActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

//We are adding the AuthStateListener to the FirebaseAuth
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);// Then We associate them
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);//We remove the listener before the activity is destroyed
    }

    // User Email Validations
    private boolean isValidEmail(String email) {
        boolean isGoodEmail = (email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());

        if(!isGoodEmail) {
            mEmailEditText.setError("Please enter a valid email address!");
            return false;
        }
        return isGoodEmail;
    }
    // Validating new user's name
    private boolean isValidName(String name) {
        if(name.equals("")) {
            mNameEditText.setError("Please enter your name");
            return false;
        }
        return true;
    }
    // New password validations
    private boolean isValidPassword(String password, String confirmPassword) {
        if(password.length() < 6) {
            mPasswordEditText.setError("Please enter a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            mPasswordEditText.setError("Passwords do not match");
            return false;
        }
        return true;
    }

    // User profile
    private void createFirebaseUserProfile(final FirebaseUser user) {
        //This is a Firebase object used to request updates to user profile information.
        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(mName)//We attach the user-entered name to the user's profile.
                .build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, user.getDisplayName());
                        }
                    }
                });
    }

   //This interface listens to changes in the current AuthState. When there is a change
    private void createAuthStateListener() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            //This method returns FirebaseAuth data.
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //Using this data, we create a FirebaseUser by calling the getCurrentUser()
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                //We double-check that this user is not null before traveling to the MainActivity.
                if(user != null) {
                    Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }
}
