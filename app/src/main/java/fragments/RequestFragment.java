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
import android.widget.AdapterView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import adapter.BookListAdapter;
import adapter.ReceivedBookListAdapter;

public class RequestFragment extends Fragment {
    private ArrayList<BookObj> books, allowed_books, shown_books;

    //List Adapter Init
//    private ListView bookListView;
//    private ReceivedBookListAdapter bookListAdapter;


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    private List<String> new_user = new ArrayList<>();
    private FirebaseAuth mAuth;
    private String from;

    public RequestFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_requests, container, false);

        books = new ArrayList<>();
        allowed_books = new ArrayList<>();
        shown_books = new ArrayList<>();

        //bookListView = view.findViewById(R.id.received_req_book_list_view);

        mAuth = FirebaseAuth.getInstance();


        System.out.println("Entering analysissssssssssssssssssss");

        final String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

        DatabaseReference receive_req_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                child("UID").child(user_id);

        receive_req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final HashMap<String, Object> all_received_req = (HashMap<String, Object>) dataSnapshot.getValue();
                assert all_received_req != null;
                System.out.println("ALLLLLLLLLLLAAAAAAAAAAAAAALLLLLLLLLLLLLLLLLLAAAAAAAAAAAAAAAAAAAAALLLLLLLLLLLLL                ");
                //System.out.println(all_received_req);

                if(all_received_req.containsKey("receiverequest")) {
                    collectReqData(all_received_req);
                    System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmm " + books);



                    DatabaseReference allowed_req_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                            child("UID").child(user_id);

                    allowed_req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            HashMap<String, Object> all_allowed_req = (HashMap<String, Object>) dataSnapshot.getValue();

                            if(all_received_req.containsKey("allowed")) {
                                assert all_allowed_req != null;
                                collectAllowedData(all_allowed_req);
                                collectShowData();


                                recyclerView = view.findViewById(R.id.reveived_books_recycler_view);
                                layoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(layoutManager);

                                adapter = new ReceivedBookListAdapter(shown_books, from);
                                recyclerView.setAdapter(adapter);
                                //List Adapter
//                                bookListAdapter = new ReceivedBookListAdapter(shown_books, from, getContext());
//
//                                bookListView.setAdapter(bookListAdapter);
//                                //bookListView.setClickable(true);
//
//                                bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int itemNumber, long l) {
//                                        Object obj = bookListView.getAdapter().getItem(itemNumber);
//                                    }
//                                });
                            } else {
                                recyclerView = view.findViewById(R.id.reveived_books_recycler_view);
                                layoutManager = new LinearLayoutManager(getContext());
                                recyclerView.setLayoutManager(layoutManager);

                                adapter = new ReceivedBookListAdapter(books, from);
                                recyclerView.setAdapter(adapter);
                                //List Adapter
//                                bookListAdapter = new ReceivedBookListAdapter(books, from, getContext());
//
//                                bookListView.setAdapter(bookListAdapter);
//                                //bookListView.setClickable(true);
//
//                                bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                    @Override
//                                    public void onItemClick(AdapterView<?> adapterView, View view, int itemNumber, long l) {
//                                        Object obj = bookListView.getAdapter().getItem(itemNumber);
////
//
//                                    }
//                                });
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else
                {
                    System.out.println("no booooooooooooks fffffffffound");
                }


//
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

    private void collectReqData(HashMap<String, Object> users) {

        System.out.println(users);


        HashMap<String, Object> req_book = (HashMap<String, Object>) users.get("receiverequest");

        System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
        System.out.println(req_book);

        //iterate through each user, ignoring their UID
        for (HashMap.Entry<String, Object> entry : req_book.entrySet()){
            BookObj aBook = new BookObj();

            //Get user map
            HashMap<String, Object> singleBook = (HashMap<String, Object>) entry.getValue();

            HashMap singleBook1 = new HashMap();



            for (HashMap.Entry<String, Object> entry2 : singleBook.entrySet()) {

                if (!entry2.getKey().equals("from")) {
                    singleBook1 = (HashMap) entry2.getValue();
                }
                else
                {
                    from = (String) entry2.getValue();
                }
            }



            //Get phone field and append to list
            aBook.setName((String) singleBook1.get("name"));
            aBook.setAvailability((String) singleBook1.get("availability"));
            aBook.setCategory((String) singleBook1.get("category"));
            aBook.setOwner((String) singleBook1.get("owner"));
            aBook.setWriter((String) singleBook1.get("writer"));
            aBook.setBook_id((String) singleBook1.get("bookid"));


            if(singleBook1.containsKey("imageuri"))
            {
                aBook.setImageuri((String) singleBook1.get("imageuri"));
            }
            else {
                aBook.setImageuri("noimageuri");

            }

            books.add(aBook);
        }
        //System.out.println("qqqqqq   " + new_user.size());

    }

    private void collectAllowedData(HashMap<String, Object> allowed) {
        HashMap<String, Object> allowed_book = (HashMap<String, Object>) allowed.get("allowed");

        //iterate through each user, ignoring their UID
        for (HashMap.Entry<String, Object> entry : allowed_book.entrySet()){
            BookObj aBook = new BookObj();

            //Get user map
            HashMap<String, Object> singleBook = (HashMap<String, Object>) entry.getValue();

            //Get phone field and append to list
            aBook.setName((String) singleBook.get("name"));
            aBook.setAvailability((String) singleBook.get("availability"));
            aBook.setCategory((String) singleBook.get("category"));
            aBook.setOwner((String) singleBook.get("owner"));
            aBook.setWriter((String) singleBook.get("writer"));
            aBook.setBook_id((String) singleBook.get("bookid"));

            if(singleBook.containsKey("imageuri"))
            {
                aBook.setImageuri((String) singleBook.get("imageuri"));
            }
            else {
                aBook.setImageuri("noimageuri");

            }

            allowed_books.add(aBook);
        }
        //System.out.println("qqqqqq   " + new_user.size());

        System.out.println("allowed          bookssssssss   " + allowed_books.toString());
    }

    private void collectShowData() {

        for(int i = 0; i < books.size(); i++) {
            boolean flag = true;
            for(int j = 0; j < allowed_books.size(); j++) {

                if(books.get(i).getBook_id().equals(allowed_books.get(j).getBook_id())) {
                    flag = false;
                }
            }
            if(flag)
            {
                BookObj aShownBook = new BookObj();
                aShownBook.setBook_id(books.get(i).getBook_id());
                aShownBook.setAvailability(books.get(i).getAvailability());
                aShownBook.setCategory(books.get(i).getCategory());
                aShownBook.setName(books.get(i).getName());
                aShownBook.setOwner(books.get(i).getOwner());
                aShownBook.setWriter(books.get(i).getWriter());
                aShownBook.setImageuri(books.get(i).getImageuri());

                shown_books.add(aShownBook);
            }
        }
    }
}
