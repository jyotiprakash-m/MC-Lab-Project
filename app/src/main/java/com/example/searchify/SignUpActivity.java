package com.example.searchify;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class    SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    private EditText mNameRegistrationOwner;
    private EditText mEmailRegistrationOwner;
    private EditText mPasswordRegistrationOwner;
    private EditText mContactRegistrationOwner;
    private EditText mUsernameRegistrationOwner;
    private TextView mLoginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mNameRegistrationOwner = findViewById(R.id.input_name);
        mEmailRegistrationOwner = findViewById(R.id.input_email);
        mPasswordRegistrationOwner = findViewById(R.id.input_password);
        mContactRegistrationOwner = findViewById(R.id.input_contact);
        mUsernameRegistrationOwner = findViewById(R.id.input_username);
        mLoginText = findViewById(R.id.link_login);
        Button mButtonRegistration = findViewById(R.id.btn_signup);

        mLoginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mButtonRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mNameRegistrationOwner.getText().toString();
                final String email = mEmailRegistrationOwner.getText().toString();
                final String password = mPasswordRegistrationOwner.getText().toString();
                final String username = mUsernameRegistrationOwner.getText().toString();
                final String contact = mContactRegistrationOwner.getText().toString();

                if(name.isEmpty()) {
                    mNameRegistrationOwner.setError("enter your name");
                    return;
                } else if(!isValid(name, "name")) {
                    mNameRegistrationOwner.setError("only alphanumeric characters");
                    return;
                } else {
                    mNameRegistrationOwner.setError(null);
                }

                if(username.isEmpty()|| username.length() < 3) {
                    mUsernameRegistrationOwner.setError("at least 3 characters");
                    return;
                } else if(!isValid(username, "username")) {
                    mUsernameRegistrationOwner.setError("only alphanumeric characters");
                    return;
                }
                else {
                    mUsernameRegistrationOwner.setError(null);
                }

                if(contact.isEmpty() || contact.length() < 11) {
                    mContactRegistrationOwner.setError("at least 11 numbers(0-9)");
                    return;
                } else {
                    mContactRegistrationOwner.setError(null);
                }

                if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmailRegistrationOwner.setError("enter a valid email address");
                    return;
                } else {
                    mEmailRegistrationOwner.setError(null);
                }

                if(password.isEmpty() || password.length() < 6) {
                    mPasswordRegistrationOwner.setError("at least 6 alphanumeric characters");
                    return;
                } else {
                    mPasswordRegistrationOwner.setError(null);
                }

                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").child("UID").orderByChild("username").equalTo(username);

                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() > 0) {
                            mUsernameRegistrationOwner.setError("choose a different username");
                        } else {
                            mUsernameRegistrationOwner.setError(null);
                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        mEmailRegistrationOwner.setError("this email is already registered");
                                        Toast.makeText(SignUpActivity.this, "sign up error", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mEmailRegistrationOwner.setError(null);
                                        String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                        final DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").child("username").child(username);

                                        final String deviceToken = FirebaseInstanceId.getInstance().getToken();

                                        Map newPost = new HashMap();
                                        newPost.put("UID", user_id);
                                        newPost.put("name", name);
                                        newPost.put("contact no", contact);
                                        newPost.put("image","true");
                                        newPost.put("books", "none");
                                        current_user_db.setValue(newPost);

                                        final DatabaseReference current_user_db2 = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").child("UID").child(user_id);

                                        Map newPost2 = new HashMap();
                                        newPost2.put("username", username);
                                        newPost2.put("name", name);
                                        newPost2.put("contact no", contact);
                                        newPost2.put("device_token", deviceToken);
                                        newPost2.put("image","true");
                                        newPost2.put("books", "none");

                                        current_user_db2.setValue(newPost2);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private boolean isValid(String string, String id) {
        boolean valid = true;
        if(id.equals("username")) {
            for (int i = 0; i < string.length(); i++) {
                if (!(string.charAt(i) >= 'a' && string.charAt(i) <= 'z')) {
                    if (!(string.charAt(i) >= 'A' && string.charAt(i) <= 'Z')) {
                        if (!(string.charAt(i) >= '0' && string.charAt(i) <= '9')) {
                            valid = false;
                        }
                    }
                }
            }
            return valid;
        } else {
            for(int i = 0; i < string.length(); i++) {
                if(!(string.charAt(i) >= 'a' && string.charAt(i) <= 'z')) {
                    if(!(string.charAt(i) >= 'A' && string.charAt(i) <= 'Z')) {
                        if(!(string.charAt(i) >= '0' && string.charAt(i) <= '9')) {
                            valid = false;
                        }
                    }
                }
                if(string.charAt(i) == ' ') {
                    valid = true;
                }
            }
            return valid;
        }
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
