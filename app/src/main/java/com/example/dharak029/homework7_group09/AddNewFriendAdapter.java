package com.example.dharak029.homework7_group09;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class AddNewFriendAdapter  extends RecyclerView.Adapter<AddNewFriendAdapter.ViewHolder> {

    ArrayList<FriendsFragment.Friend> mData;
    String uid;
    String username;

    static class Request{
        String status;
        String name;
    }

    public AddNewFriendAdapter(ArrayList<FriendsFragment.Friend> mData, String uid,String username) {
        this.mData = mData;
        this.uid = uid;
        this.username = username;
    }

    @Override
    public AddNewFriendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layoutaddnewfriend, parent, false);
        AddNewFriendAdapter.ViewHolder viewHolder = new AddNewFriendAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AddNewFriendAdapter.ViewHolder holder, final int position) {
        final FriendsFragment.Friend friend = mData.get(position);
        holder.txtName.setText(friend.friendName);
        holder.txtUID.setText(friend.frindUID);
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        holder.imgAddNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference userRef = rootRef.child(uid);
                DatabaseReference userSendRef = userRef.child("Requests");
                DatabaseReference sendrequestRef = userSendRef.child(friend.frindUID);
                Request sendrequest = new Request();
                sendrequest.status = "send";
                sendrequest.name = holder.txtName.getText().toString();
                sendrequestRef.setValue(sendrequest);

                DatabaseReference friendRef = rootRef.child(friend.frindUID);
                DatabaseReference friendreceiveReqRef = friendRef.child("Requests");
                DatabaseReference receiveReqRef = friendreceiveReqRef.child(uid);
                Request recrequest = new Request();
                recrequest.status = "receive";
                recrequest.name = username;
                receiveReqRef.setValue(recrequest);

                Log.d("size",mData.size()+"");
                mData.remove(position);
                Log.d("size",mData.size()+"");
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
        ImageView imgAddNewFriend;

        public ViewHolder(final View itemView) {
            super(itemView);
            imgAddNewFriend = (ImageView)itemView.findViewById(R.id.imgAddFriend);
            txtName = (TextView) itemView.findViewById(R.id.txtFriendNameInAddNew);
            txtUID = (TextView)itemView.findViewById(R.id.txtUIDInAddNewFriend);
        }
    }
}
