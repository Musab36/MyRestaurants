package com.salajim.musab.myrestaurants.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.salajim.musab.myrestaurants.R;
import com.salajim.musab.myrestaurants.models.Restaurant;
import com.salajim.musab.myrestaurants.ui.RestaurantDetailActivity;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder> {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    private ArrayList<Restaurant> mRestaurants = new ArrayList<>();
    private Context mContext;

    public RestaurantListAdapter(Context context, ArrayList<Restaurant> restaurants) {
        mContext = context;
        mRestaurants = restaurants;
    }

    // onCreateViewHolder method which inflates the layout, and creates the ViewHolder object required from the adapter
    @Override
    public RestaurantListAdapter.RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item, parent, false);
        RestaurantViewHolder viewHolder = new RestaurantViewHolder(view);
        return viewHolder;
    }

    // onBindViewHolder method which updates the contents of the ItemView to reflect the restaurant in the given position
    @Override
    public void onBindViewHolder(RestaurantListAdapter.RestaurantViewHolder holder, int position) {
        holder.bindRestaurant(mRestaurants.get(position));
    }

    // getItemCount method which sets the number of items the adapter will display
    @Override
    public int getItemCount() {
        return mRestaurants.size();
    }

    // viewHolder class as an inner/nested class
    public class RestaurantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.restaurantImageView) ImageView mRestaurantImageView;
        @Bind(R.id.restaurantNameTextView) TextView mNameTextView;
        @Bind(R.id.categoryTextView) TextView mCategoryTextView;
        @Bind(R.id.ratingTextView) TextView mRatingTextView;

        private Context mContext;

        public RestaurantViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getLayoutPosition(); //Retrieves the position of the specific list item clicked
            Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
            intent.putExtra("position", itemPosition);
            intent.putExtra("restaurants", Parcels.wrap(mRestaurants));
            mContext.startActivity(intent);
        }

        public void bindRestaurant(Restaurant restaurant) {
            Picasso.with(mContext)
                    .load(restaurant.getImageUrl())
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(mRestaurantImageView); // Picasso wil handle image displaying
            mNameTextView.setText(restaurant.getName());
            mCategoryTextView.setText(restaurant.getCategories().get(0));
            mRatingTextView.setText("Rating: " + restaurant.getRating() + "/5");
        }
    }
}