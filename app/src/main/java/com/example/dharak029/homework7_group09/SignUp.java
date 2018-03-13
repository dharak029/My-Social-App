package com.example.dharak029.homework7_group09;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class SignUp extends AppCompatActivity {

    EditText txtSignupEmail;
    EditText txtSignupFirstName;
    EditText txtSignupLastName;
    EditText txtSignupPassword;
    EditText txtSignupRepeatPwd;
    EditText txtDOB;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        txtSignupEmail = (EditText)findViewById(R.id.editSignupEmail);
        txtSignupFirstName = (EditText)findViewById(R.id.editSignupFname);
        txtSignupLastName = (EditText)findViewById(R.id.editSignupLname);
        txtSignupPassword = (EditText)findViewById(R.id.editSignupPassword);
        txtSignupRepeatPwd = (EditText)findViewById(R.id.editRepeatPassword);
        txtDOB = (EditText)findViewById(R.id.editDOB);
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btnSignUpRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = txtSignupEmail.getText().toString();
                final String pwd = txtSignupPassword.getText().toString();


                if(txtSignupFirstName.getText().toString().equals("") || txtSignupLastName.getText().toString().equals("")|| email.equals("")|| pwd.equals("") || txtSignupRepeatPwd.getText().toString().equals("")){
                    Toast.makeText(SignUp.this, "All inputs are mandatory", Toast.LENGTH_SHORT).show();
                }
                else if(!email.matches(EMAIL_PATTERN))
                {
                    Toast.makeText(SignUp.this, "Email should be in correct format", Toast.LENGTH_SHORT).show();
                }
                else if (pwd.length() < 6)
                {
                    Toast.makeText(SignUp.this, "Password should be greater than 6 character", Toast.LENGTH_SHORT).show();
                }
                else{

                    if(pwd.equals(txtSignupRepeatPwd.getText().toString())){
                        mAuth.createUserWithEmailAndPassword(email,pwd )
                                .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        Log.d("demo", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                        // If sign in fails, display a message to the user. If sign in succeeds
                                        // the auth state listener will be notified and logic to handle the
                                        // signed in user can be handled in the listener.
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(SignUp.this,"Failed Signup",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else{

                                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                            User user = new User(txtSignupFirstName.getText().toString(), txtSignupLastName.getText().toString(), email,txtDOB.getText().toString());
                                            DatabaseReference userRef = rootRef.child(mAuth.getCurrentUser().getUid());
                                            userRef.setValue(user);
                                            Toast.makeText(SignUp.this, "User has been created successfully",
                                                    Toast.LENGTH_SHORT).show();
                                            finish();
                                            Intent intent = new Intent(SignUp.this,MainActivity.class);
                                            startActivity(intent);
                                        }

                                        // ...
                                    }
                                });
                    }
                    else{
                        Toast.makeText(SignUp.this,"Password Not Matched",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent = new Intent(SignUp.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
