package com.salajim.musab.myrestaurants;


import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;

public class MainActivityInstrumentationTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    // Testing with Espresso
    /*
    @Test
    public void validateEditText() {
        onView(withId(R.id.locationEditText)).perform(typeText("Portland")).check(matches(withText("Portland")));
    }

    @Test
    public void locationIsSentToRestaurantsActivity() {
        String location = "Portland";
        onView(withId(R.id.locationEditText)).perform(typeText(location));
        onView(withId(R.id.findRestaurantsButton)).perform(click());
        onView(withId(R.id.locationTextView)).check(matches(withText("Here are all restaurants near:" + location)));
    }
    */
}
