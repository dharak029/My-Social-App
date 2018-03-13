package com.example.dharak029.homework7_group09;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dharak029 on 11/16/2017.
 */

public class MyPostsAdapter  extends RecyclerView.Adapter<MyPostsAdapter.ViewHolder> {

    ArrayList<Post> mData;
    String uid;
    Activity activity;

    public MyPostsAdapter(ArrayList<Post> mData,String uid) {
        this.mData = mData;
        this.uid = uid;
    }

    public MyPostsAdapter(ArrayList<Post> mData,String uid,Activity activity) {
        this.mData = mData;
        this.uid = uid;
        this.activity = activity;
    }

    @Override
    public MyPostsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layoutmyposts, parent, false);
        MyPostsAdapter.ViewHolder viewHolder = new MyPostsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyPostsAdapter.ViewHolder holder, final int position) {
        Post post = (Post) mData.get(position);
        holder.txtName.setText(post.getName());
        holder.txtPost.setText(post.getPost());
        PrettyTime prettyTime = new PrettyTime();
        Date date  = post.getTime();
        long timeMillis = date.getTime();
        holder.txtTime.setText(prettyTime.format(new Date(timeMillis )));
        if(activity!=null && activity.toString().contains("FriendsWall")){
            holder.imageView.setVisibility(View.GONE);
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference userRef = rootRef.child(uid);
                DatabaseReference postsRef = userRef.child("Posts");
                postsRef.child(mData.get(position).getPost()).removeValue();
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
        TextView txtName;
        TextView txtTime;
        TextView txtPost;
        ImageButton imageView;

        public ViewHolder(final View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtMyPostsName);
            txtTime = (TextView) itemView.findViewById(R.id.txtMyPostsTime);
            txtPost = (TextView) itemView.findViewById(R.id.txtMyPost);
            imageView = (ImageButton)itemView.findViewById(R.id.imgBtnDelete);
        }
    }



}
