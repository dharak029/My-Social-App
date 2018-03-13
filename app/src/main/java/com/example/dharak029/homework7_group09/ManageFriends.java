package com.example.dharak029.homework7_group09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ManageFriends extends AppCompatActivity implements FriendsFragment.OnFriendFragmentInteractionListener,RequestPendingFragment.OnRequestPendingFragmentInteractionListener,AddNewFriendFragment.OnAddNewFragmentInteractionListener{

    String uid;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        if(getIntent().getExtras()!=null){
            uid = getIntent().getExtras().getString("UID");
            name = getIntent().getExtras().getString("NAME");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Add New Friends"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending Requests"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        findViewById(R.id.imgManageFriendHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(ManageFriends.this,Home.class);
                intent.putExtra("UID",uid);
                intent.putExtra("NAME",name);
                startActivity(intent);
            }
        });
    }

    @Override
    public String getUID() {
        return uid;
    }

    @Override
    public String getAddNewFriendUID() {
        return uid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUIDPendingRequest() {
        return uid;
    }

    @Override
    public String getNamePendingRequest() {
        return name;
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
                Intent intent = new Intent(ManageFriends.this,MainActivity.class);
                startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }
}
