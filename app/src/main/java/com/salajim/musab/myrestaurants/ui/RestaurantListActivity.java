package com.salajim.musab.myrestaurants.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.salajim.musab.myrestaurants.R;

import butterknife.ButterKnife;

public class RestaurantListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

    }
}