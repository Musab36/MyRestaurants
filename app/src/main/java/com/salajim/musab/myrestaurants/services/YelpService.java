package com.salajim.musab.myrestaurants.services;


import com.salajim.musab.myrestaurants.Constants;
import com.salajim.musab.myrestaurants.models.Restaurant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YelpService {
    // The following method build, signs, and sends an OAuth API request using OkHttp and Signpost:
    public static void findRestaurants(String location, Callback callback) {
        // SignPost responsible for creating our OAuth signature
       // OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(Constants.YELP_CONSUMER_KEY, Constants.YELP_CONSUMER_SECRET);
        //consumer.setTokenWithSecret(Constants.YELP_TOKEN, Constants.YELP_TOKEN_SECRET);

        // Here we are creating OKHttpClient to create and send request
        OkHttpClient client = new OkHttpClient();
               // .addInterceptor(new SigningInterceptor(consumer)) // Then we tie it to the SignPost consumer object
                //.build();

        // Building the request URL with OKHttp
        HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.YELP_BASE_URL).newBuilder(); // Creating a new URL
        urlBuilder.addQueryParameter(Constants.YELP_LOCATION_QUERY_PARAMETER, location); // The location the user searches for

        String url = urlBuilder.build().toString(); // Turns the finished URL into a String

        // Creating a new Request with OKHttp using the new URL
        Request request = new Request.Builder()
                .header("Authorization", Constants.YELP_TOKEN)
                .url(url)
                .build();

        // Here we are excuting our request
        Call call = client.newCall(request); // We created a Call object and placed our request in it
        call.enqueue((Callback) callback); // Then we excute our request
    }

    // This method will return an array list of Restaurant objects which we can then display.
    public ArrayList<Restaurant> processResults(Response response) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        try {
            String jsonData = response.body().string(); // Transforms the API response into a String in order to double-check the response was successful.
            if (response.isSuccessful()) {
                JSONObject yelpJSON = new JSONObject(jsonData);
                /*
                Log.v("jsonData", jsonData);
                */
                JSONArray businessesJSON = yelpJSON.getJSONArray("businesses");
                for (int i = 0; i < businessesJSON.length(); i++) {
                    JSONObject restaurantJSON = businessesJSON.getJSONObject(i);
                    String name = restaurantJSON.getString("name");
                    String phone = restaurantJSON.optString("display_phone", "Phone not available");
                    String website = restaurantJSON.getString("url");
                    double rating = restaurantJSON.getDouble("rating");
                    String imageUrl = restaurantJSON.getString("image_url");
                    double latitude = restaurantJSON
                            .getJSONObject("coordinates").getDouble("latitude");
                    double longitude = restaurantJSON
                            .getJSONObject("coordinates").getDouble("longitude");
                    ArrayList<String> address = new ArrayList<>();
                    JSONArray addressJSON = restaurantJSON.getJSONObject("location")
                            .getJSONArray("display_address");
                    for (int y = 0; y < addressJSON.length(); y++) {
                        address.add(addressJSON.get(y).toString());
                    }

                    ArrayList<String> categories = new ArrayList<>();
                    JSONArray categoriesJSON = restaurantJSON.getJSONArray("categories");

                    for (int y = 0; y < categoriesJSON.length(); y++) {
                       // categories.add(categoriesJSON.getJSONArray(y).get(0).toString());
                        JSONObject jsonObject = categoriesJSON.getJSONObject(y);
                        String category = jsonObject.getString("alias");
                        categories.add(category);
                    }
                    Restaurant restaurant = new Restaurant(name, phone, website, rating,
                            imageUrl, address, latitude, longitude, categories);
                    restaurants.add(restaurant);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return restaurants;
    }
}
