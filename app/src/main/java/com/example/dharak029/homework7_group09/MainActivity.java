package com.example.dharak029.homework7_group09;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    SignInButton gsignInButton;
    GoogleApiClient googleApiClient;
    private final int SIGN_IN = 9001;

    EditText txtEmail;
    EditText txtPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.friends_list);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        sharedpreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        if(!sharedpreferences.getString("UID","").equals("") && !sharedpreferences.getString("NAME","").equals("")){
            Intent intent = new Intent(MainActivity.this,Home.class);
            intent.putExtra("UID",sharedpreferences.getString("UID",""));
            intent.putExtra("NAME",sharedpreferences.getString("NAME",""));
            startActivity(intent);
        }

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();

        findViewById(R.id.txtSignup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnGoogleSignIn).setOnClickListener(this);

        txtEmail = (EditText)findViewById(R.id.editUname);
        txtPassword = (EditText)findViewById(R.id.editPwd);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("demo", "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w("demo", "signInWithEmail:failed", task.getException());
                                    Toast.makeText(MainActivity.this,"SignIn Failed",
                                            Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("UID", mAuth.getCurrentUser().getUid());
                                    editor.commit();
                                    Intent intent = new Intent(MainActivity.this,Home.class);
                                    intent.putExtra("UID",mAuth.getCurrentUser().getUid());
                                    intent.putExtra("NAME",mAuth.getCurrentUser().getDisplayName());
                                    startActivity(intent);
                                }

                                // ...
                            }
                        });

            }
        });
    }

    public void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,SIGN_IN);
    }

    @Override
    public void onClick(View view) {
        signIn();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    public void handleSignInResult(GoogleSignInResult result){

        if(result.isSuccess()){
            final GoogleSignInAccount account = result.getSignInAccount();
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            final User user = new User(account.getGivenName(), account.getFamilyName(), account.getEmail());
            final DatabaseReference userRef = rootRef.child(account.getId());
            rootRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    int flag = 0;
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        if(dataSnapshot1.getKey().equals(account.getId())){
                            flag = 1;
                        }

                    }

                    if(flag==0){
                        userRef.setValue(user);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("UID", account.getId());
            editor.putString("NAME", account.getGivenName());
            editor.commit();
            Intent intent = new Intent(MainActivity.this,Home.class);
            intent.putExtra("UID",account.getId());
            intent.putExtra("NAME",account.getGivenName());
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
