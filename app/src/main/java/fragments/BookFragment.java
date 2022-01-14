package fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.searchify.BookObj;
import com.example.searchify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;

import adapter.BookListAdapter;

public class BookFragment extends Fragment {
    private ArrayList<BookObj> books;

//    //List Adapter Init
//    private ListView bookListView;
//    private BookListAdapter bookListAdapter;
//    private List<String> new_user = new ArrayList<>();



    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    public BookFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_book, container, false);

        books = new ArrayList<>();

        //bookListView = view.findViewById(R.id.all_book_list_view);


        DatabaseReference public_book_ref = FirebaseDatabase.getInstance().getReference().child("Books");

        public_book_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final HashMap<String, Object> all_public_books = (HashMap<String, Object>) dataSnapshot.getValue();
                assert all_public_books != null;

                FirebaseAuth mAuth;

//            BookObj aBook = new BookObj();
//
//            //Get user HashMap
//            HashMap singleBook = (HashMap) entry.getValue();

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


                        recyclerView = view.findViewById(R.id.books_recycler_view);
                        layoutManager = new LinearLayoutManager(getContext());
                        recyclerView.setLayoutManager(layoutManager);

                        adapter = new BookListAdapter(books);
                        recyclerView.setAdapter(adapter);



//                        //List Adapter
//                        bookListAdapter = new BookListAdapter(books, getContext());
//
//                        bookListView.setAdapter(bookListAdapter);
//                        //bookListView.setClickable(true);
//
//                        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(AdapterView<?> adapterView, View view, int itemNumber, long l) {
//                                Object obj = bookListView.getAdapter().getItem(itemNumber);
//
//                            }
//                        });

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

}
