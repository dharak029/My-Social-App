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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserWall extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Post> postArrayList;
    private DatabaseReference userRef;
    private DatabaseReference postsRef;
    private String uid;
    private String name;
    private TextView txtName;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wall);

        txtName = (TextView)findViewById(R.id.txtUserNameMyPosts);

        if(getIntent().getExtras()!=null){
            uid = getIntent().getExtras().getString("UID");
            name = getIntent().getExtras().getString("NAME");
            txtName.setText(name);
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(uid);

        findViewById(R.id.imgEditProfile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(UserWall.this,EditUser.class);
                intent.putExtra("UID",uid);
                startActivity(intent);
            }
        });

        findViewById(R.id.imgFriendListMyPosts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(UserWall.this,ManageFriends.class);
                intent.putExtra("UID",uid);
                intent.putExtra("NAME",name);
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
                recyclerView = (RecyclerView) findViewById(R.id.recyclerMyPosts);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(UserWall.this);
                recyclerView.setLayoutManager(mLayoutManager);
                MyPostsAdapter myPostsAdapter = new MyPostsAdapter(arrayList, uid);
                recyclerView.setAdapter(myPostsAdapter);
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
                Intent intent = new Intent(UserWall.this,MainActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
