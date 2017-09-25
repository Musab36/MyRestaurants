package com.salajim.musab.myrestaurants.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.salajim.musab.myrestaurants.Constants;
import com.salajim.musab.myrestaurants.R;
import com.salajim.musab.myrestaurants.adapter.FirebaseRestaurantViewHolder;
import com.salajim.musab.myrestaurants.models.Restaurant;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SavedRestaurantListActivity extends AppCompatActivity {
    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        //Retrieving User-Specific Data
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        //Pin pointing to the correct location in firebase
        mRestaurantReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS)
                .child(uid);

        setUpFirebaseAdapter();
    }
    //We create a method to set up the FirebaseAdapter which takes the model class,
    // the list item layout, the view holder, and the database reference as parameters.
    private void setUpFirebaseAdapter() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Restaurant, FirebaseRestaurantViewHolder>
                (Restaurant.class, R.layout.restaurant_list_item_drag, FirebaseRestaurantViewHolder.class, mRestaurantReference) {
            //We set the appropriate text and image with the given restaurant using bindRestaurant() method
            @Override
            protected void populateViewHolder(FirebaseRestaurantViewHolder viewHolder, Restaurant model, int position) {
                viewHolder.bindRestaurant(model);
            }
        };
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }
    //We clean up the FirebaseAdapter. When the activity is destroyed,
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }
}
