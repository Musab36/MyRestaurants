package com.salajim.musab.myrestaurants.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.salajim.musab.myrestaurants.R;
import com.salajim.musab.myrestaurants.adapter.RestaurantListAdapter;
import com.salajim.musab.myrestaurants.models.Restaurant;
import com.salajim.musab.myrestaurants.services.YelpService;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsActivity extends AppCompatActivity {
    public static final String TAG = RestaurantsActivity.class.getSimpleName();
    /*
    @Bind(R.id.locationTextView)
    TextView mLocationTextView;
    @Bind(R.id.listView)
    ListView mListView;
    */
    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        //mLocationTextView.setText("Here are all the restaurants near: " + location);

        getRestaurants(location);
    }

    // Callback method for desplaying response data
    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService(); // Creating new instance of Yelp
        yelpService.findRestaurants(location, new Callback() { // new Callback represents the second argument in findRestaurants() method

            // This method is run when our request fails
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace(); // prints details of the error to the output console
            }

            // This method is run if our request is successful
            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);// We are triggering .processResults() and collect its return value

                RestaurantsActivity.this.runOnUiThread(new Runnable() { // We are switching to UI thread, Runnable helps sharing code between threads
                    @Override
                    public void run() {//Runnable method which contains the code for the UI thread
                        mAdapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantsActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);

                        }
                    });
                }
            });
    }
}