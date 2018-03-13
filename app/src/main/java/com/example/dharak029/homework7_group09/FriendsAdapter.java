package com.example.dharak029.homework7_group09;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by dharak029 on 11/17/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

    ArrayList<FriendsFragment.Friend> mData;
    String uid;
    Activity activity;

    public FriendsAdapter(ArrayList<FriendsFragment.Friend> mData, String uid,Activity activity) {
        this.mData = mData;
        this.uid = uid;
        this.activity = activity;
    }

    @Override
    public FriendsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layoutfriends, parent, false);
        FriendsAdapter.ViewHolder viewHolder = new FriendsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendsAdapter.ViewHolder holder, final int position) {
        final FriendsFragment.Friend friened = mData.get(position);
        holder.txtName.setText(friened.friendName);
        holder.txtUID.setText(friened.frindUID);

        holder.imgRemoveFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userRef = rootRef.child(uid);
                DatabaseReference friendsRef = userRef.child("Friends");
                friendsRef.child(friened.frindUID).removeValue();

                DatabaseReference friendsrootRef = rootRef.child(friened.frindUID);
                DatabaseReference friendsOfFriendsRef = friendsrootRef.child("Friends");
                friendsOfFriendsRef.child(uid).removeValue();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                Intent intent = new Intent(activity,EditUser.class);
                intent.putExtra("UID",friened.frindUID);
                intent.putExtra("ACTIVITY","Friends");
                intent.putExtra("USERUID",uid);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtUID;
        ImageView imgRemoveFriend;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgRemoveFriend = (ImageView)itemView.findViewById(R.id.imgRemoveFriend);
            txtName = (TextView) itemView.findViewById(R.id.txtFriendNameInFriends);
            txtUID = (TextView)itemView.findViewById(R.id.txtFriendLayotUID);
        }
    }
}
