package com.example.searchify;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import de.hdodenhof.circleimageview.CircleImageView;

import adapter.BookListAdapter;

public class ShowProfileActivity extends AppCompatActivity {
    private ArrayList<BookObj> books;


    private ImageView coverImage;
    private CircleImageView profilePic;
    private TextView profileName;
    private Typeface mTfLight, mTfRegular, mTfBold;
    private String userName, fullName;

//    //List Adapter Init
//    private ListView bookListView;
//    private BookListAdapter bookListAdapter;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);


        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        userName = bundle.getString("username");
        fullName = bundle.getString("fullname");

        books = new ArrayList<>();
        //bookListView = findViewById(R.id.book_list_view);


        coverImage = findViewById(R.id.header_cover_image);
        profilePic = findViewById(R.id.profile_pic);
        profileName = findViewById(R.id.name);


        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        mTfBold = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");

        profileName.setTypeface(mTfBold);
        profileName.setText(fullName);

        DatabaseReference private_book_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                child("username").child(userName).child("books");

        private_book_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() instanceof String) {

                } else {
                    final Map<String, Object> all_private_books = (Map<String, Object>) dataSnapshot.getValue();
                    assert all_private_books != null;

                    System.out.println(all_private_books);


                    FirebaseAuth mAuth;

                    mAuth = FirebaseAuth.getInstance();
                    final String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                    DatabaseReference my_sent_req_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                            child("UID").child(user_id);

                    my_sent_req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> uid_object = (Map<String, Object>) dataSnapshot.getValue();

                            assert uid_object != null;
                            if (uid_object.containsKey("sentrequest")) {
                                Map<String, Object> sent_req_book = (Map<String, Object>) uid_object.get("sentrequest");

                                for (Map.Entry<String, Object> entry : all_private_books.entrySet()) {
                                    BookObj aBook = new BookObj();

                                    //Get user map
                                    Map singleBook = (Map) entry.getValue();
                                    if (!(sent_req_book.containsKey(singleBook.get("bookid")))) {
                                        //Get phone field and append to list
                                        aBook.setBook_id((String) singleBook.get("bookid"));
                                        aBook.setName((String) singleBook.get("name"));
                                        aBook.setAvailability((String) singleBook.get("availability"));
                                        aBook.setCategory((String) singleBook.get("category"));
                                        aBook.setOwner(userName);
                                        //aBook.setOwner((String) singleBook.get("owner"));
                                        aBook.setWriter((String) singleBook.get("writer"));

                                        if(singleBook.containsKey("imageuri"))
                                        {
                                            aBook.setImageuri((String) singleBook.get("imageuri"));
                                        }
                                        else {
                                            aBook.setImageuri("noimageuri");

                                        }

                                        //phoneNumbers.add((Long) singleUser.get("phone"));
                                        System.out.println("book      " + aBook.toString());
                                        books.add(aBook);
                                    }
                                }

                            } else {
                                for (Map.Entry<String, Object> entry : all_private_books.entrySet()) {
                                    BookObj aBook = new BookObj();

                                    //Get user map
                                    Map singleBook = (Map) entry.getValue();

                                    //Get phone field and append to list
                                    aBook.setBook_id((String) singleBook.get("bookid"));
                                    aBook.setName((String) singleBook.get("name"));
                                    aBook.setAvailability((String) singleBook.get("availability"));
                                    aBook.setCategory((String) singleBook.get("category"));
                                    aBook.setOwner(userName);
                                    //aBook.setOwner((String) singleBook.get("owner"));
                                    aBook.setWriter((String) singleBook.get("writer"));

                                    if(singleBook.containsKey("imageuri"))
                                    {
                                        aBook.setImageuri((String) singleBook.get("imageuri"));
                                    }
                                    else {
                                        aBook.setImageuri("noimageuri");

                                    }

                                    //phoneNumbers.add((Long) singleUser.get("phone"));
                                    System.out.println("book      " + aBook.toString());
                                    books.add(aBook);

                                }
                            }
                            recyclerView = findViewById(R.id.books_recycler_view);
                            layoutManager = new LinearLayoutManager(getApplicationContext());
                            recyclerView.setLayoutManager(layoutManager);

                            adapter = new BookListAdapter(books);
                            recyclerView.setAdapter(adapter);

//                            //List Adapter
//                            bookListAdapter = new BookListAdapter(books, getApplicationContext());
//
//                            bookListView.setAdapter(bookListAdapter);
//                            //bookListView.setClickable(true);
//
//                            bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                @Override
//                                public void onItemClick(AdapterView<?> adapterView, View view, int itemNumber, long l) {
//                                    Object obj = bookListView.getAdapter().getItem(itemNumber);
//                                }
//                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    //collectBookData(all_private_books, userName);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
