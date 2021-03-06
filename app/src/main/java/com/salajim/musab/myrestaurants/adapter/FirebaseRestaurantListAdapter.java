package com.salajim.musab.myrestaurants.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.salajim.musab.myrestaurants.Constants;
import com.salajim.musab.myrestaurants.R;
import com.salajim.musab.myrestaurants.models.Restaurant;
import com.salajim.musab.myrestaurants.ui.RestaurantDetailActivity;
import com.salajim.musab.myrestaurants.ui.RestaurantDetailFragment;
import com.salajim.musab.myrestaurants.util.ItemTouchHelperAdapter;
import com.salajim.musab.myrestaurants.util.OnStartDragListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

public class FirebaseRestaurantListAdapter extends FirebaseRecyclerAdapter<Restaurant,
        FirebaseRestaurantViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;
    private int mOrientation;

    private ChildEventListener mChildEventListener;
    private ArrayList<Restaurant> mRestaurants = new ArrayList<>();

  public FirebaseRestaurantListAdapter(Class<Restaurant> modelClass, int modelLayout, Class<FirebaseRestaurantViewHolder>
                                       viewHolderClass, Query ref, OnStartDragListener onStartDragListener, Context context) {
      super(modelClass, modelLayout, viewHolderClass, ref);
      mRef = ref.getRef();
      mOnStartDragListener = onStartDragListener;
      mContext = context;

      mChildEventListener = mRef.addChildEventListener(new ChildEventListener() {
          @Override
          public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              mRestaurants.add(dataSnapshot.getValue(Restaurant.class));
          }
          @Override
          public void onChildChanged(DataSnapshot dataSnapshot, String s) {

          }
          @Override
          public void onChildRemoved(DataSnapshot dataSnapshot) {

          }
          @Override
          public void onChildMoved(DataSnapshot dataSnapshot, String s) {

          }
          @Override
          public void onCancelled(DatabaseError databaseError) {

          }
      });
  }

  @Override
    protected void populateViewHolder(final FirebaseRestaurantViewHolder viewHolder, Restaurant model, final int position) {
      viewHolder.bindRestaurant(model);
      mOrientation = viewHolder.itemView.getResources().getConfiguration().orientation;
      if(mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
          createDetailFragment(0);
      }
      viewHolder.mRestaurantImageView.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                  mOnStartDragListener.onStartDrag(viewHolder);
              }
              return false;
          }
      });

      viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

          @Override
          public void onClick(View v) {
              int itemPosition = viewHolder.getAdapterPosition();
              if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                  createDetailFragment(itemPosition);
              } else {
              Intent intent = new Intent(mContext, RestaurantDetailActivity.class);
              intent.putExtra(Constants.EXTRA_KEY_POSITION, itemPosition);//To get the current position of the click item
              intent.putExtra(Constants.EXTRA_KEY_RESTAURANTS, Parcels.wrap(mRestaurants));
              intent.putExtra(Constants.KEY_SOURCE, Constants.SOURCE_SAVED);
              mContext.startActivity(intent);
          }
      }
      });
  }

 @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
     //We update the order of our mRestaurants ArrayList items passing in the ArrayList of items and the starting and ending positions.
     Collections.swap(mRestaurants, fromPosition, toPosition);
     notifyItemMoved(fromPosition, toPosition);//We're notifying the underlying data has changed.
     return false;
 }

 @Override
    public void onItemDismiss(int position) {
     mRestaurants.remove(position);//Removes the item from mRestaurants at the given position.
     getRef(position).removeValue();//To delete the dismissed item from Firebase, we can call the getRef()
                                    //removeValue() method to delete that object from Firebase
 }
//This method will be in charge of re-assigning the "index" property
// for each restaurant object in our array list and then save it to Firebase:
 private void setIndexInFirebase() {
     for(Restaurant restaurant : mRestaurants) {
         int index = mRestaurants.indexOf(restaurant);// Grabs the index of each restaurant
         DatabaseReference ref = getRef(index);//We grab the reference of each item using the getRef()
         restaurant.setIndex(Integer.toString(index));//Updates the index property for each item.
         ref.child("index").setValue(Integer.toString(index));
     }

 }
 @Override
  public void cleanup() {
     super.cleanup();
     setIndexInFirebase();
     mRef.removeEventListener(mChildEventListener);
 }

 private void createDetailFragment(int position) {
     RestaurantDetailFragment detailFragment = RestaurantDetailFragment.newInstance(mRestaurants, position, Constants.SOURCE_SAVED);
     FragmentTransaction ft = ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction();
     ft.replace(R.id.restaurantDetailContainer, detailFragment);
     ft.commit();
 }
}

