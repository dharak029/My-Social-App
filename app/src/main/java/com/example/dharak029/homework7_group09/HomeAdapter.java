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

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dharak029 on 11/16/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder>{

    ArrayList<Post> mData;
    String uid;
    Activity activity;
    String name;

    public HomeAdapter(ArrayList<Post> mData,String uid,String name,Activity activity) {
        this.mData = mData;
        this.uid = uid;
        this.activity = activity;
        this.name = name;
    }

    public HomeAdapter(ArrayList<Post> mData,String uid) {
        this.mData = mData;
        this.uid = uid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout, parent, false);
        HomeAdapter.ViewHolder viewHolder = new HomeAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Post post = (Post) mData.get(position);
        holder.txtName.setText(post.getName());
        holder.txtPost.setText(post.getPost());
        holder.txtUID.setText(post.getUid());
        PrettyTime prettyTime = new PrettyTime();
        Date date  = post.getTime();
        long timeMillis = date.getTime();
        holder.txtTime.setText(prettyTime.format(new Date(timeMillis )));

        holder.txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.finish();
                Intent intent = new Intent(activity,FriendsWall.class);
                intent.putExtra("NAME",holder.txtName.getText().toString());
                intent.putExtra("UID",holder.txtUID.getText().toString());
                intent.putExtra("USERUID",uid);
                intent.putExtra("USERNAME",name);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtTime;
        TextView txtPost;
        TextView txtUID;

        public ViewHolder(final View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtPost = (TextView) itemView.findViewById(R.id.txtPost);
            txtUID = (TextView)itemView.findViewById(R.id.txtWallUID);
        }
    }



}
