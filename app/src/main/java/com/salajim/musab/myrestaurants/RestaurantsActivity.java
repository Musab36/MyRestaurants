package com.salajim.musab.myrestaurants;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RestaurantsActivity extends AppCompatActivity {
    public static final String TAG = RestaurantsActivity.class.getSimpleName();

    @Bind(R.id.locationTextView)
    TextView mLocationTextView;
    @Bind(R.id.listView)
    ListView mListView;

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        mLocationTextView.setText("Here are all the restaurants near: " + location);

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
                        // String array returning list of restaurants that we want to display for the users
                        String[] restaurantNames = new String[mRestaurants.size()];
                        for (int i = 0; i < restaurantNames.length; i++) {
                            restaurantNames[i] = mRestaurants.get(i).getName();
                        }

                        // An ArrayAdapter passing data to the view
                        ArrayAdapter adapter = new ArrayAdapter(RestaurantsActivity.this,
                                android.R.layout.simple_list_item_1, restaurantNames);
                        mListView.setAdapter(adapter);

                        for (Restaurant restaurant : mRestaurants) {
                            Log.d(TAG, "Name: " + restaurant.getName());
                            Log.d(TAG, "Phone: " + restaurant.getPhone());
                            Log.d(TAG, "Website: " + restaurant.getWebsite());
                            Log.d(TAG, "Image url: " + restaurant.getImageUrl());
                            Log.d(TAG, "Rating: " + Double.toString(restaurant.getRating()));
                            Log.d(TAG, "Address: " + android.text.TextUtils.join(", ", restaurant.getAddress()));
                            Log.d(TAG, "Categories: " + restaurant.getCategories().toString());
                        }
                    }
                });
            }
        });
    }
}