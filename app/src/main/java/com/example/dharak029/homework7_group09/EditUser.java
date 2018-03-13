package com.example.dharak029.homework7_group09;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditUser extends AppCompatActivity {

    EditText editFname,editLname,editDOB,editEmail;
    String uid;
    DatabaseReference userRef;
    Button btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        editEmail = (EditText) findViewById(R.id.editEmail);
        editEmail.setEnabled(false);
        editFname = (EditText)findViewById(R.id.editFname);
        editLname = (EditText)findViewById(R.id.editLname);
        editDOB = (EditText)findViewById(R.id.editupdateUserDOB);
        btnUpdate = (Button)findViewById(R.id.btnUpdateUser);

        if(getIntent().getExtras()!=null){
            uid = getIntent().getExtras().getString("UID");
        }
        if(getIntent().getExtras().getString("ACTIVITY")!=null){
            editFname.setEnabled(false);
            editLname.setEnabled(false);
            editDOB.setEnabled(false);
            btnUpdate.setVisibility(View.GONE);
        }
        if(getIntent().getExtras().getString("USERUID")!=null){
            uid = getIntent().getExtras().getString("USERUID");
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        userRef = rootRef.child(uid);



        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child("fname").setValue(editFname.getText().toString());
                userRef.child("lname").setValue(editLname.getText().toString());
                userRef.child("dob").setValue(editDOB.getText().toString());
                Toast.makeText(EditUser.this,"Profile updated successfully",Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.imgEditUserHome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(EditUser.this,Home.class);
                intent.putExtra("UID",uid);
                startActivity(intent);
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
                finish();
                Intent intent = new Intent(EditUser.this,MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals("email")){
                        editEmail.setText(dataSnapshot1.getValue().toString());
                    }
                    else if(dataSnapshot1.getKey().equals("fname")){
                        editFname.setText(dataSnapshot1.getValue().toString());
                    }
                    else if(dataSnapshot1.getKey().equals("lname")){
                        editLname.setText(dataSnapshot1.getValue().toString());
                    }
                    else if(dataSnapshot1.getKey().equals("dob")){
                        editDOB.setText(dataSnapshot1.getValue().toString());
                    }
                    else{

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
