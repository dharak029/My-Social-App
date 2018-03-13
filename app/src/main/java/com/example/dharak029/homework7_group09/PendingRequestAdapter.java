package com.example.dharak029.homework7_group09;

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

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ViewHolder> {

    ArrayList<RequestPendingFragment.Friend> mData;
    String uid;
    String username;

    static class Request{
        String status;
        String name;
    }

    public PendingRequestAdapter(ArrayList<RequestPendingFragment.Friend> mData, String uid,String username) {
        this.mData = mData;
        this.uid = uid;
        this.username = username;
    }

    @Override
    public PendingRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layoutpendingrequest, parent, false);
        PendingRequestAdapter.ViewHolder viewHolder = new PendingRequestAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PendingRequestAdapter.ViewHolder holder, final int position) {
        final RequestPendingFragment.Friend friend = mData.get(position);
        holder.txtName.setText(friend.friendName);
        holder.txtUID.setText(friend.friendUID);

        if(friend.status.equals("send")){
            holder.imgAccept.setVisibility(View.GONE);
        }

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userRef = rootRef.child(uid);


        holder.imgAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userSendRef = userRef.child("Friends");
                DatabaseReference friendRef = userSendRef.child(friend.friendUID);
                friendRef.setValue(friend.friendName);
                userRef.child("Requests").child(friend.friendUID).removeValue();

                DatabaseReference friendReqRef = rootRef.child(friend.friendUID);
                DatabaseReference friendsRef = friendReqRef.child("Friends");
                DatabaseReference friends = friendsRef.child(uid);
                friends.setValue(username);

                DatabaseReference requestRef = friendReqRef.child("Requests");
                requestRef.child(uid).removeValue();

                mData.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.imgDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference userrequestRef = userRef.child("Requests");
                userrequestRef.child(friend.friendUID).removeValue();

                DatabaseReference friendReqRef = rootRef.child(friend.friendUID);
                DatabaseReference requestRef = friendReqRef.child("Requests");
                requestRef.child(uid).removeValue();
                mData.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName,txtUID;
        ImageView imgAccept;
        ImageView imgDecline;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgAccept = (ImageView)itemView.findViewById(R.id.imgAccept);
            imgDecline = (ImageView)itemView.findViewById(R.id.imgDecline);
            txtName = (TextView) itemView.findViewById(R.id.txtNewPersonName);
            txtUID = (TextView)itemView.findViewById(R.id.txtPendingUID);
        }
    }

}
