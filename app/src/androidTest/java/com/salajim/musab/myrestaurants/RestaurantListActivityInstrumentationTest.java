package com.salajim.musab.myrestaurants;


import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.salajim.musab.myrestaurants.ui.RestaurantListActivity;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

public class RestaurantListActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<RestaurantListActivity> activityTestRule = new ActivityTestRule<>(RestaurantListActivity.class);

    @Test
    public void listItemClickDisplaysToastWithCorrectRestaurant() {
        View activityDecorView = activityTestRule.getActivity().getWindow().getDecorView();
        String restaurantName = "Mi Mero Mole";
        onData(anything())
                .inAdapterView(withId(R.id.recyclerView))
                .atPosition(0)
                .perform(click());
        onView(withText(restaurantName)).inRoot(withDecorView(not(activityDecorView)))
                .check(matches(withText(restaurantName)));
    }
}