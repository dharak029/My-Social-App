package com.example.dharak029.homework7_group09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Home extends AppCompatActivity {

    EditText editPost;
    private String uid;
    private String name;
    private TextView txtName;
    private RecyclerView recyclerView;
    private ArrayList<Post> postArrayList;
    private DatabaseReference userRef;
    private DatabaseReference postsRef;
    DatabaseReference rootRef;
    final ArrayList<Post> arrayList = new ArrayList<Post>();
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        editPost = (EditText)findViewById(R.id.editPost);
        txtName = (TextView)findViewById(R.id.txtUserName);

        if(getIntent().getExtras()!=null){
            uid = getIntent().getExtras().getString("UID");
            name = getIntent().getExtras().getString("NAME");
            txtName.setText(name);
        }


        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(uid);

        findViewById(R.id.imgFriendList).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Home.this,ManageFriends.class);
                intent.putExtra("UID",uid);
                intent.putExtra("NAME",name);
                startActivity(intent);
            }
        });


        findViewById(R.id.imgSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post(name,editPost.getText().toString(), Calendar.getInstance().getTime(),uid);
                DatabaseReference postRef = postsRef.child(editPost.getText().toString());
                postRef.setValue(post);
            }
        });

        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(Home.this,UserWall.class);
                intent.putExtra("NAME",name);
                intent.putExtra("UID",uid);
                startActivity(intent);
            }
        });

    }

    protected void onStart() {
        super.onStart();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if(dataSnapshot1.getKey().equals("fname") && name == null){
                        name = (String) dataSnapshot1.getValue();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("NAME", name);
                        editor.commit();
                        txtName.setText(name);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child("Friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String friendUID = dataSnapshot1.getKey();
                    rootRef.child(friendUID).child("Posts").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                Post post = dataSnapshot1.getValue(Post.class);
                                arrayList.add(post);
                            }
                            Log.d("seize", arrayList.size() + "");
                            recyclerView = (RecyclerView) findViewById(R.id.recyclerHome);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Home.this);
                            recyclerView.setLayoutManager(mLayoutManager);
                            HomeAdapter homeContactAdapter = new HomeAdapter(arrayList, uid,name,Home.this);
                            recyclerView.setAdapter(homeContactAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        postsRef = userRef.child("Posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Post post = dataSnapshot1.getValue(Post.class);
                    arrayList.add(post);
                }
                recyclerView = (RecyclerView) findViewById(R.id.recyclerHome);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Home.this);
                recyclerView.setLayoutManager(mLayoutManager);

                Comparator<Post> comparator = new Comparator<Post>() {
                    @Override
                    public int compare(Post o, Post o1) {
                        Date date1 = o.getTime();
                        Date date2 = o1.getTime();
                        int compare = date2.compareTo(date1);
                        return compare;
                    }
                };

                Collections.sort(arrayList,comparator);
                HomeAdapter homeContactAdapter = new HomeAdapter(arrayList,uid,name,Home.this);
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
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.clear().commit();
                finish();
                Intent intent = new Intent(Home.this,MainActivity.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
