package com.salajim.musab.myrestaurants.adapter;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.salajim.musab.myrestaurants.R;
import com.salajim.musab.myrestaurants.models.Restaurant;
import com.salajim.musab.myrestaurants.util.ItemTouchHelperViewHolder;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class FirebaseRestaurantViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    public ImageView mRestaurantImageView;

    View mView;
    Context mContext;

    public FirebaseRestaurantViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
    }

    // we first bind the views and then set the image and text views.
    public void bindRestaurant(Restaurant restaurant) {
        mRestaurantImageView = (ImageView) mView.findViewById(R.id.restaurantImageView);
        TextView nameTextView = (TextView) mView.findViewById(R.id.restaurantNameTextView);
        TextView categoryTextView = (TextView) mView.findViewById(R.id.categoryTextView);
        TextView ratingTextView = (TextView) mView.findViewById(R.id.ratingTextView);

        if (!restaurant.getImageUrl().contains("http")) {
            try {
                Bitmap imageBitmap = decodeFromFirebaseBase64(restaurant.getImageUrl());
                mRestaurantImageView.setImageBitmap(imageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
            } else{



                Picasso.with(mContext)
                        .load(restaurant.getImageUrl())
                        .resize(MAX_WIDTH, MAX_HEIGHT)
                        .centerCrop()
                        .into(mRestaurantImageView);


                nameTextView.setText(restaurant.getName());
                categoryTextView.setText(restaurant.getCategories().get(0));
                ratingTextView.setText("Rating: " + restaurant.getRating() + "/5");
        }
    }

    //decoding Base64
    public static Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodeByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodeByteArray, 0, decodeByteArray.length);
    }


    @Override
    public void onItemSelected() {
        /*
        itemView.animate()
                .alpha(0.7f)//alters the alpha level of an object (its transparency and/or opaqueness)
                .scaleX(0.9f)//sets the horizontal scale of the item
                .scaleY(0.9f)//sets the vertical scale of the item
                .setDuration(500);//Determines how long this animation will last
                */
        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.drag_scale_on);
        set.setTarget(itemView);
        set.start();
    }

    @Override
    public void onItemClear() {
        /*
        itemView.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f);
                */
        AnimatorSet set =(AnimatorSet) AnimatorInflater.loadAnimator(mContext, R.animator.drag_scale_off);
        set.setTarget(itemView);
        set.start();
    }

}
