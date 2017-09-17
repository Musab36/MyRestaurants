package com.salajim.musab.myrestaurants.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.salajim.musab.myrestaurants.Constants;
import com.salajim.musab.myrestaurants.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = MainActivity.class.getSimpleName();
    //private SharedPreferences mSharedPreferences;
    //private SharedPreferences.Editor mEditor;
    private DatabaseReference mSearchedLocationReference;
    @Bind(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
    @Bind(R.id.locationEditText) EditText mLocationEditText;
    @Bind(R.id.appNameTextView) TextView mAppNameTextView;

    private ValueEventListener mSearchedLocationReferenceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSearchedLocationReference = FirebaseDatabase
                .getInstance() // This method accesses our database
                .getReference() // This method gets a specific reference within our database
                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);

       mSearchedLocationReferenceListener = mSearchedLocationReference.addValueEventListener(new ValueEventListener() {
           //we'll snag the values returned in the dataSnapshot, loop through each of the children with the getChildren()
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 for(DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                     String location = locationSnapshot.getValue().toString();
                     Log.d("Locations updated", "location: " + location);
                 }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        Typeface ostrichFont = Typeface.createFromAsset(getAssets(), "fonts/ostrich-regular.ttf");
        mAppNameTextView.setTypeface(ostrichFont);

        mFindRestaurantsButton.setOnClickListener(this);

        //        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //       mEditor = mSharedPreferences.edit();



    }

        @Override
        public void onClick(View v) {
            if (v == mFindRestaurantsButton) {
                String location = mLocationEditText.getText().toString();

                saveLocationToFirebase(location);
                /*
                if(!(location).equals("")) {
                    addToSharedPreferences(location);
                }
                */
                Intent intent = new Intent(MainActivity.this, RestaurantListActivity.class);
                intent.putExtra("location", location);
                startActivity(intent);
            }
        }

        public void saveLocationToFirebase(String location) {
            mSearchedLocationReference.push().setValue(location);//we're providing a value that corresponds to the key of searchedLocation
        }
    // We are removing the event listener when the user is not interacting with the app
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSearchedLocationReference.removeEventListener(mSearchedLocationReferenceListener);
    }
          /*
        private void addToSharedPreferences(String location) {
            mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
        }
        */
}
