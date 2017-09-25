package com.salajim.musab.myrestaurants.adapter;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.salajim.musab.myrestaurants.models.Restaurant;
import com.salajim.musab.myrestaurants.util.ItemTouchHelperAdapter;
import com.salajim.musab.myrestaurants.util.OnStartDragListener;

public class FirebaseRestaurantListAdapter extends FirebaseRecyclerAdapter<Restaurant,
        FirebaseRestaurantViewHolder> implements ItemTouchHelperAdapter {
    private DatabaseReference mRef;
    private OnStartDragListener mOnStartDragListener;
    private Context mContext;

  public FirebaseRestaurantListAdapter(Class<Restaurant> modelClass, int modelLayout, Class<FirebaseRestaurantViewHolder>
                                       viewHolderClass, Query ref, OnStartDragListener onStartDragListener, Context context) {
      super(modelClass, modelLayout, viewHolderClass, ref);
      mRef = ref.getRef();
      mOnStartDragListener = onStartDragListener;
      mContext = context;
  }

  @Override
    protected void populateViewHolder(final FirebaseRestaurantViewHolder viewHolder, Restaurant model, int position) {
      viewHolder.bindRestaurant(model);
      viewHolder.mRestaurantImageView.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
              if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                  mOnStartDragListener.onStartDrag(viewHolder);
              }
              return false;
          }
      });
  }

 @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
     return false;
 }

 @Override
    public void onItemDismiss(int position) {

 }
}

