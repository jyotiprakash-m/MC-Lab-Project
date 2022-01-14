package com.example.searchify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText mEmail, mPassword;
    private Button mLogin;
    private TextView mRegistrationText;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    //private DatabaseReference mUserDatabase;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        //mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child("UID");
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);

        mLogin = findViewById(R.id.btn_login);
        mRegistrationText = findViewById(R.id.link_signup);

        mRegistrationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("enter a valid email address");
                    return;
                } else {
                    mEmail.setError(null);
                }

                if(password.isEmpty() || password.length() < 6) {
                    mPassword.setError("atleast 6 alphanumeric characters");
                    return;
                } else {
                    mPassword.setError(null);
                }


                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
//                            progressDialog.dismiss();
                            mPassword.setError("email or password did not match");
                        } else {
                            mPassword.setError(null);
                            final String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            final String current_user = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                            //mUserDatabase.child(current_user).child("device_token").setValue(deviceToken);
                        }
                    }
                });
              /*  progressDialog = ProgressDialog.show(OwnerSigninActivity.this, "Please wait.",
                        "Logging in..!", true);*/
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }
}
