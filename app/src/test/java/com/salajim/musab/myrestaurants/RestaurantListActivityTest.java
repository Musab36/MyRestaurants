package com.salajim.musab.myrestaurants;

import android.os.Build;
import android.widget.ListView;

import com.salajim.musab.myrestaurants.ui.RestaurantListActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)

public class RestaurantListActivityTest {
    private RestaurantListActivity activity;
    private ListView mRestaurantsListView;

    @Before
    public void setup() {
        activity = Robolectric.setupActivity(RestaurantListActivity.class);
        mRestaurantsListView = (ListView) activity.findViewById(R.id.recyclerView);
    }

    @Test
    public void restaurantListViewPopulates() {
        assertNotNull(mRestaurantsListView.getAdapter());
        assertEquals(mRestaurantsListView.getAdapter().getCount(), 15);
    }
}

