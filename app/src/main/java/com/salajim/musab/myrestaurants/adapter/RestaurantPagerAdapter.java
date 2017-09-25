package com.salajim.musab.myrestaurants.adapter;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.salajim.musab.myrestaurants.models.Restaurant;
import com.salajim.musab.myrestaurants.ui.RestaurantDetailFragment;

import java.util.ArrayList;

public class RestaurantPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Restaurant> mRestaurants;

    //A constructor where we set the required FragmentManager and array list of restaurants we will be swiping through.
    public RestaurantPagerAdapter(FragmentManager fm, ArrayList<Restaurant> restaurants) {
        super(fm);
        mRestaurants = restaurants;
    }

 //Returns an instance of the RestaurantDetailFragment for the restaurant in the position provided as an argument
    @Override
    public Fragment getItem(int position) {
        return RestaurantDetailFragment.newInstance(mRestaurants.get(position));
    }

//Determines how many restaurants are in our Array List. This lets our adapter know how many fragments it must create.
    @Override
    public int getCount() {
        return mRestaurants.size();
    }

//Updates the title that appears in the scrolling tabs at the top of the screen
    @Override
    public CharSequence getPageTitle(int position) {
        return mRestaurants.get(position).getName();
    }
}
