package com.salajim.musab.myrestaurants.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.salajim.musab.myrestaurants.Constants;
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

public class RestaurantListActivity extends AppCompatActivity {
    public static final String TAG = RestaurantListActivity.class.getSimpleName();

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
     private RestaurantListAdapter mAdapter;
     private SharedPreferences mSharedPreferences;
     private String mRecentAddress;
    private SharedPreferences.Editor mEditor;

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

    //we inflate and bind our Views, define our mSharedPreferences and mEditor member variables
    //To retrieve a user’s search from the SearchView, we must grab the action_search menu item from our new layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        ButterKnife.bind(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

       //This method is run automatically when the user submits a query into our SearchView
            @Override
            public boolean onQueryTextSubmit(String query) {
                addToSharedPreferences(query);
                getRestaurants(query);
                return false;
            }

            //This method is run whenever any changes to the SearchView contents occur.
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

//All functionality from the parent class (referred to here as super) will still apply despite us manually overriding portions of the menu's functionality.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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

                RestaurantListActivity.this.runOnUiThread(new Runnable() { // We are switching to UI thread, Runnable helps sharing code between threads
                    @Override
                    public void run() {//Runnable method which contains the code for the UI thread
                        mAdapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantListActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);

                        }
                    });
                }
            });
    }
    //This method will be responsible for writing data to Shared Preferences:
    public void addToSharedPreferences(String location) {
        mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
    }
}