package com.example.searchify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddBookActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText mBookName, mWriter, mCategory, mAvailability;
    private Button mSubmit;
    private FirebaseAuth mAuth;



    //Image
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    ImageView imageView;

    public String photoLink = "noimageuri";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        //Image
        imageView = (ImageView) findViewById(R.id.book_image_view);
        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();
        //creates a storage reference
        storageRef = storage.getReference();


        mBookName = findViewById(R.id.input_book);
        mWriter = findViewById(R.id.input_writer);
        mCategory = findViewById(R.id.input_category);
        mSubmit = findViewById(R.id.btn_submit);
        mAvailability = findViewById(R.id.input_availability);

        mAuth = FirebaseAuth.getInstance();

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String book_name = mBookName.getText().toString();
                final String writer_name = mWriter.getText().toString();
                final String category = mCategory.getText().toString();
                final String availability = mAvailability.getText().toString();

                final String user_uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                if(book_name.isEmpty()) {
                    mBookName.setError("enter a book name");
                    return;
                } else {
                    mBookName.setError(null);
                }

                if(writer_name.isEmpty()) {
                    mWriter.setError("enter a writer name");
                    return;
                } else {
                    mWriter.setError(null);
                }

                if(category.isEmpty()) {
                    mCategory.setError("enter a edition");
                    return;
                } else {
                    mCategory.setError(null);
                }

                if(availability.isEmpty()) {
                    mAvailability.setError("enter yes/no");
                    return;
                } else {
                    mAvailability.setError(null);
                }



                //Image
                //create reference to images folder and assing a name to the file that will be uploaded
                System.out.println("______________selected image__________________");
                System.out.println(selectedImage);

                if(selectedImage!=null)
                {
                    imageRef = storageRef.child("images/" + selectedImage.getLastPathSegment());

//                //creating and showing progress dialog
//                progressDialog = new ProgressDialog(getApplicationContext());
//                progressDialog.setMax(100);
//                progressDialog.setMessage("Uploading...");
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                progressDialog.show();
//                progressDialog.setCancelable(false);

                    //starting upload
                    uploadTask = imageRef.putFile(selectedImage);

//                // Observe state change events such as progress, pause, and resume
//                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//
//                    @Override
//                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                        //sets and increments value of progressbar
//                        progressDialog.incrementProgressBy((int) progress);
//
//                    }
//                });

                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Handle unsuccessful uploads
                            Toast.makeText(AddBookActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                            //progressDialog.dismiss();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.


                            Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    photoLink = uri.toString();
                                    Toast.makeText(AddBookActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                    // progressDialog.dismiss();

                                    //showing the uploaded image in ImageView using the download url
                                    Picasso.with(AddBookActivity.this).load(photoLink).into(imageView);



                                    final DatabaseReference uid_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                            child("UID").child(user_uid).child("username");

                                    uid_ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final String user_name = (String) dataSnapshot.getValue();
                                            assert user_name != null;

                                            final String book_id = user_name + book_name;

                                            final DatabaseReference book_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                                    child("UID").child(user_uid).child("books").child(book_id);

                                            book_ref.child("name").setValue(book_name);
                                            book_ref.child("writer").setValue(writer_name);
                                            book_ref.child("category").setValue(category);
                                            book_ref.child("availability").setValue(availability);
                                            book_ref.child("bookid").setValue(book_id);
                                            book_ref.child("owner").setValue(user_name);
                                            book_ref.child("imageuri").setValue(photoLink);






                                            final DatabaseReference user_name_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                                    child("username").child(user_name).child("books").child(book_id);

                                            user_name_ref.child("name").setValue(book_name);
                                            user_name_ref.child("writer").setValue(writer_name);
                                            user_name_ref.child("category").setValue(category);
                                            user_name_ref.child("availability").setValue(availability);
                                            user_name_ref.child("bookid").setValue(book_id);
                                            user_name_ref.child("owner").setValue(user_name);
                                            user_name_ref.child("imageuri").setValue(photoLink);


                                            System.out.println("**********PhotoLink************************");
                                            System.out.println(photoLink);


                                            final DatabaseReference publc_book_ref = FirebaseDatabase.getInstance().getReference().child("Books").
                                                    child(book_id);
                                            publc_book_ref.child("name").setValue(book_name);
                                            publc_book_ref.child("writer").setValue(writer_name);
                                            publc_book_ref.child("category").setValue(category);
                                            publc_book_ref.child("availability").setValue(availability);
                                            publc_book_ref.child("owner").setValue(user_name);
                                            publc_book_ref.child("bookid").setValue(book_id);
                                            publc_book_ref.child("imageuri").setValue(photoLink);


                                            Intent intent = new Intent(AddBookActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });






                                }
                            });


                        }
                    });

                }
                else
                {
                    Toast.makeText(AddBookActivity.this, "Please upload an image", Toast.LENGTH_SHORT).show();
                }


                System.out.println("*****************Outof LOOP*******************");




//                final DatabaseReference book_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                        child("UID").child(user_uid).child("books").child(book_name);
//
//                book_ref.child("name").setValue(book_name);
//                book_ref.child("writer").setValue(writer_name);
//                book_ref.child("category").setValue(category);
//                book_ref.child("availability").setValue(availability);


            }
        });
    }

    public void selectImage(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {

                    Toast.makeText(AddBookActivity.this,"Image selected, click on upload button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                }
        }
    }


    public void uploadImage(View view) {

        //create reference to images folder and assing a name to the file that will be uploaded
        imageRef = storageRef.child("images/" + selectedImage.getLastPathSegment());

        //creating and showing progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);

        //starting upload
        uploadTask = imageRef.putFile(selectedImage);

        // Observe state change events such as progress, pause, and resume
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //sets and increments value of progressbar
                progressDialog.incrementProgressBy((int) progress);

            }
        });

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {

                // Handle unsuccessful uploads
                Toast.makeText(AddBookActivity.this, "Error in uploading!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.


                Task<Uri> task = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String photoLink = uri.toString();
                        Toast.makeText(AddBookActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        //showing the uploaded image in ImageView using the download url
                        Picasso.with(AddBookActivity.this).load(photoLink).into(imageView);
                    }
                });


            }
        });
    }


    }