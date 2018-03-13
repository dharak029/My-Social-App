package com.example.dharak029.homework7_group09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class FriendsWall extends AppCompatActivity {

    private String uid;
    private String name;
    private TextView txtName;
    private RecyclerView recyclerView;
    private ArrayList<Post> postArrayList;
    private DatabaseReference userRef;
    private DatabaseReference postsRef;
    private TextView txtPostTitle;
    private String userUID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_wall);

        txtName = (TextView)findViewById(R.id.txtFriendName);
        txtPostTitle = (TextView)findViewById(R.id.txtFriendPosts);

        if(getIntent().getExtras()!=null){
            uid = getIntent().getExtras().getString("UID");
            name = getIntent().getExtras().getString("NAME");
            userUID = getIntent().getExtras().getString("USERUID");
            userName = getIntent().getExtras().getString("USERNAME");
            txtName.setText(name);
            txtPostTitle.setText(name + "'s" + " Posts");
        }



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(uid);

        findViewById(R.id.imgHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(FriendsWall.this,Home.class);
                intent.putExtra("UID",userUID);
                intent.putExtra("NAME",userName);
                startActivity(intent);
            }
        });

    }

    protected void onStart() {
        super.onStart();

        postsRef = userRef.child("Posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Post> arrayList = new ArrayList<Post>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Post post = dataSnapshot1.getValue(Post.class);
                    arrayList.add(post);
                }
                Log.d("seize", arrayList.size() + "");
                recyclerView = (RecyclerView) findViewById(R.id.recyclerFriendWall);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FriendsWall.this);
                recyclerView.setLayoutManager(mLayoutManager);
                MyPostsAdapter homeContactAdapter = new MyPostsAdapter(arrayList, uid,FriendsWall.this);
                recyclerView.setAdapter(homeContactAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.action_logout:
                SharedPreferences sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear().commit();
                finish();
                Intent intent = new Intent(FriendsWall.this,MainActivity.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
