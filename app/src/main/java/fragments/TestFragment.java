package fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.searchify.BookObj;
import com.example.searchify.R;
import com.example.searchify.ShowProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import adapter.GridElementAdapter;
import adapter.ImageAdapter;

public class TestFragment extends Fragment {
    private GridElementAdapter gridElementAdapter;
    private GridView bookListView;
    private ArrayList<BookObj> books;
    private ImageAdapter adapterView;

    ViewPager mViewPager;


    public TestFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_fargment, container, false);
        books = new ArrayList<>();

        bookListView = view.findViewById(R.id.gridView1);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPage);
        adapterView = new ImageAdapter(getContext());
        mViewPager.setAdapter(adapterView);
        pageSwitcher(3);




        DatabaseReference public_book_ref = FirebaseDatabase.getInstance().getReference().child("Books");
        public_book_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final HashMap<String, Object> all_public_books = (HashMap<String, Object>) dataSnapshot.getValue();

                FirebaseAuth mAuth;


                mAuth = FirebaseAuth.getInstance();
                final String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                DatabaseReference my_sent_req_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                        child("UID").child(user_id);

                my_sent_req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, Object> uid_object = (HashMap<String, Object>) dataSnapshot.getValue();

                        assert uid_object != null;
                        if (uid_object.containsKey("sentrequest")) {
                            HashMap<String, Object> sent_req_book = (HashMap<String, Object>) uid_object.get("sentrequest");

                            for (HashMap.Entry<String, Object> entry : all_public_books.entrySet()) {
                                BookObj aBook = new BookObj();

                                //Get user HashMap
                                HashMap singleBook = (HashMap) entry.getValue();

                                if (!(sent_req_book.containsKey(singleBook.get("bookid")))) {
                                    aBook.setBook_id((String) singleBook.get("bookid"));
                                    aBook.setName((String) singleBook.get("name"));
                                    aBook.setAvailability((String) singleBook.get("availability"));
                                    aBook.setCategory((String) singleBook.get("category"));
                                    aBook.setOwner((String) singleBook.get("owner"));
                                    aBook.setWriter((String) singleBook.get("writer"));


                                    if(singleBook.containsKey("imageuri"))
                                    {
                                        aBook.setImageuri((String) singleBook.get("imageuri"));
                                    }
                                    else {
                                        aBook.setImageuri("noimageuri");

                                    }


                                    //phoneNumbers.add((Long) singleUser.get("phone"));
                                    //System.out.println("book      " + aBook.toString());
                                    books.add(aBook);
                                }
                            }
                        } else {
                            for (HashMap.Entry<String, Object> entry : all_public_books.entrySet()) {
                                BookObj aBook = new BookObj();

                                //Get user HashMap
                                HashMap singleBook = (HashMap) entry.getValue();


                                aBook.setBook_id((String) singleBook.get("bookid"));
                                aBook.setName((String) singleBook.get("name"));
                                aBook.setAvailability((String) singleBook.get("availability"));
                                aBook.setCategory((String) singleBook.get("category"));
                                aBook.setOwner((String) singleBook.get("owner"));
                                aBook.setWriter((String) singleBook.get("writer"));

                                if(singleBook.containsKey("imageuri"))
                                {
                                    aBook.setImageuri((String) singleBook.get("imageuri"));
                                }
                                else {
                                    aBook.setImageuri("noimageuri");

                                }

                                //phoneNumbers.add((Long) singleUser.get("phone"));
                                //System.out.println("book      " + aBook.toString());
                                books.add(aBook);

                            }
                        }
                        bookListView.setNumColumns(books.size());
                        //Grid Adapter

                        gridElementAdapter = new GridElementAdapter(books, getContext());

                        bookListView.setAdapter(gridElementAdapter);


                        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int itemNumber, long l) {
                                Object obj = bookListView.getAdapter().getItem(itemNumber);


                                Intent intent = new Intent(getContext(), ShowProfileActivity.class);
                                intent.putExtra("username", books.get(itemNumber).getOwner());
                                intent.putExtra("fullname", books.get(itemNumber).getOwner());
                                startActivity(intent);


                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //bookListView.setClickable(true);

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    Timer timer;
    int page = 0;

    public void pageSwitcher(int seconds) {
        timer = new Timer(); // At this line a new Thread will be created
        timer.scheduleAtFixedRate(new RemindTask(), 0, seconds * 1000); // delay
        // in
        // milliseconds
    }

    // this is an inner class...
    class RemindTask extends TimerTask {

        @Override
        public void run() {

            // As the TimerTask run on a seprate thread from UI thread we have
            // to call runOnUiThread to do work on UI thread.
            if(getActivity()!=null)
            {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        if (page > 2) { // In my case the number of pages are 5
                            //timer.cancel();
                            page = 0;
                            mViewPager.setCurrentItem(page++);
                        } else {
                            mViewPager.setCurrentItem(page++);
                        }
                    }
                });
            }
            else {
                timer.cancel();
            }


        }
    }

}
