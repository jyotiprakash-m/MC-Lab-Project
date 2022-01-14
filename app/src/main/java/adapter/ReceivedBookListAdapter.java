package adapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.searchify.BookObj;
import com.example.searchify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;

//public class ReceivedBookListAdapter extends BaseAdapter {
//    public List<BookObj> bookObjList;
//    String from;
//    Context context;
//    public String ss;
//
//    public ReceivedBookListAdapter(List<BookObj> bookObjList, String from, Context
//
//            context) {
//        this.context = context;
//        this.bookObjList = bookObjList;
//        this.from = from;
//
//    }
//
//    @Override
//    public int getCount() {
//        return bookObjList.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View v = null;
//        final BookObj book = bookObjList.get(position);
//        if (convertView == null) {
//            LayoutInflater inflater = (LayoutInflater)
//                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            v = inflater.inflate(R.layout.received_book_list_adapter_layout, parent,
//
//                    false);
//        } else {
//            v = convertView;
//        }
//
//        Typeface mTfRegular = Typeface.createFromAsset(v.getContext().getAssets(), "OpenSans-Regular.ttf");
//
//        ImageView bookImageView = v.findViewById(R.id.book_img);
//        TextView senderNameText = v.findViewById(R.id.req_sender_name);
//        TextView bookNameText = v.findViewById(R.id.book_name);
//        TextView authorNameText = v.findViewById(R.id.author_name);
//        final Button allowButton = v.findViewById(R.id.allow_req_btn);
//        final Button rejectButton = v.findViewById(R.id.reject_req_btn);
//
//        FirebaseAuth mAuth;
//
//        mAuth = FirebaseAuth.getInstance();
//        final String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
//
//
//
//        if (book.getAvailability().equals("no")) {
//            allowButton.setClickable(false);
//            allowButton.setText("Not Available");
//        } else if (book.getAvailability().equals("yes")) {
//            allowButton.setText("ALLOW");
//            allowButton.setClickable(true);
//        }
//
//        allowButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                allowButton.setClickable(false);
//                allowButton.setText("Allowed");
//                rejectButton.setVisibility(v.INVISIBLE);
//                //send a request to book owner
//
//                DatabaseReference uid_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                        child("UID").child(user_id).child("username");
//
//                uid_ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        final String user_name = (String) dataSnapshot.getValue();
//
//                        final DatabaseReference allowed_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                                child("UID").child(user_id).child("allowed").child(book.getBook_id());
//
//                        allowed_ref.child("name").setValue(book.getName());
//                        allowed_ref.child("category").setValue(book.getCategory());
//                        allowed_ref.child("writer").setValue(book.getWriter());
//                        allowed_ref.child("availability").setValue(book.getAvailability());
//                        allowed_ref.child("bookid").setValue(book.getBook_id());
//                        allowed_ref.child("owner").setValue(book.getOwner());
//
//
//                        assert user_name != null;
//                        final DatabaseReference user_name_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                                child("username").child(user_name).child("allowed").child(book.getBook_id());
//
//                        user_name_ref.child("name").setValue(book.getName());
//                        user_name_ref.child("category").setValue(book.getCategory());
//                        user_name_ref.child("writer").setValue(book.getWriter());
//                        user_name_ref.child("availability").setValue(book.getAvailability());
//                        user_name_ref.child("bookid").setValue(book.getBook_id());
//                        user_name_ref.child("owner").setValue(book.getOwner());
//
//
//                        final DatabaseReference from_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                                child("username").child(from).child("granted").child(book.getBook_id());
//
//                        from_ref.child("name").setValue(book.getName());
//                        from_ref.child("category").setValue(book.getCategory());
//                        from_ref.child("writer").setValue(book.getWriter());
//                        from_ref.child("availability").setValue(book.getAvailability());
//                        from_ref.child("bookid").setValue(book.getBook_id());
//                        from_ref.child("owner").setValue(book.getOwner());
//
//                        final DatabaseReference from_uid_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                                child("username").child(from).child("UID");
//
//                        from_uid_ref.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                final String from_uid = (String) dataSnapshot.getValue();
//
//                                assert from_uid != null;
//                                final DatabaseReference from_uid_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                                        child("UID").child(from_uid).child("granted").child(book.getBook_id());
//
//                                from_uid_ref.child("name").setValue(book.getName());
//                                from_uid_ref.child("category").setValue(book.getCategory());
//                                from_uid_ref.child("writer").setValue(book.getWriter());
//                                from_uid_ref.child("availability").setValue(book.getAvailability());
//                                from_uid_ref.child("bookid").setValue(book.getBook_id());
//                                from_uid_ref.child("owner").setValue(book.getOwner());
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
//
//
//
//
//
//            }
//        });
//
//        rejectButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //current user e book read list e thakle unread banate hobe else read
//                final DatabaseReference update_req_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                        child("UID").child(user_id).child("receiverequest");
//
//                update_req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        HashMap<String, Object> received_req = (HashMap<String, Object>) dataSnapshot.getValue();
//
//                        for (HashMap.Entry<String, Object> entry : received_req.entrySet()){
//                            HashMap<String, Object> singleBook = (HashMap<String, Object>) entry.getValue();
//                            System.out.println(singleBook);
//                            HashMap singleBook1 = new HashMap();
//
//                            int i = 0;
//                            for (HashMap.Entry<String, Object> entry2 : singleBook.entrySet()) {
//                                System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE     " + i++);
//                                System.out.println(entry2.getValue() + "        " + entry2.getKey());
//                                System.out.println(book.getBook_id());
//                                if (!entry2.getKey().equals("from")) {
//                                    singleBook1 = (HashMap) entry2.getValue();
//                                    if(singleBook1.get("bookid").equals(book.getBook_id())) {
//                                        update_req_ref.child(entry.getKey()).setValue(null);
//                                        System.out.println("dddddddddddd        " + entry2.getKey() + " " + entry.getKey());
//                                    }
//                                }
//                                else
//                                {
//                                    from = (String) entry2.getValue();
//                                }
//                            }
//
//                            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//                            System.out.println(singleBook1);
//                        }
//
//
//                        final DatabaseReference update_username_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                                child("UID").child(user_id).child("username");
//
//                        update_username_ref.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                String user_name = (String) dataSnapshot.getValue();
//
//                                assert user_name != null;
//                                final DatabaseReference update_req_ref_username = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
//                                        child("username").child(user_name).child("receiverequest");
//
//                                update_req_ref_username.addListenerForSingleValueEvent(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        HashMap<String, Object> received_req_username = (HashMap<String, Object>) dataSnapshot.getValue();
//
//
//
//                                        for (HashMap.Entry<String, Object> entry_user : received_req_username.entrySet()){
//                                            HashMap<String, Object> singleBook_user = (HashMap<String, Object>) entry_user.getValue();
//                                            System.out.println(singleBook_user);
//                                            HashMap singleBook_user1 = new HashMap();
//
//                                            int i = 0;
//                                            for (HashMap.Entry<String, Object> entry_user2 : singleBook_user.entrySet()) {
//                                                System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE     " + i++);
//                                                System.out.println(entry_user2.getValue() + "        " + entry_user2.getKey());
//                                                System.out.println(book.getBook_id());
//                                                if (!entry_user2.getKey().equals("from")) {
//                                                    singleBook_user1 = (HashMap) entry_user2.getValue();
//                                                    if(singleBook_user1.get("bookid").equals(book.getBook_id())) {
//                                                        update_req_ref_username.child(entry_user.getKey()).setValue(null);
//                                                        System.out.println("dddddddddddd        " + entry_user2.getKey() + " " + entry_user.getKey());
//                                                    }
//                                                }
//                                                else
//                                                {
//                                                    from = (String) entry_user2.getValue();
//                                                }
//                                            }
//
//                                            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
//                                            System.out.println(singleBook_user1);
//                                        }
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//            }
//        });
//
//        //Book Image
//        bookImageView.setImageResource(R.drawable.library);
//        bookImageView.setVisibility(View.VISIBLE);
//
//        //Book name
//        bookNameText.setTypeface(mTfRegular);
//        bookNameText.setText(book.getName());
//        bookNameText.setVisibility(View.VISIBLE);
//
//        //author name
//        authorNameText.setTypeface(mTfRegular);
//        authorNameText.setText(book.getWriter());
//        authorNameText.setVisibility(View.VISIBLE);
//
//
//        return v;
//    }
//}
//
//
//package adapter;

public class ReceivedBookListAdapter extends RecyclerView.Adapter<ReceivedBookListAdapter.ViewHolder> {
    public List<BookObj> bookObjList;
    String from;
    public String ss;

    public ReceivedBookListAdapter(List<BookObj> bookObjList, String from) {
        this.bookObjList = bookObjList;
        this.from = from;

    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView bookImageView;;
        public TextView senderNameText, bookNameText, authorNameText;
        public final Button allowButton, rejectButton;
        public Typeface mTfRegular;

        public ViewHolder(View itemView) {
            super(itemView);
            mTfRegular = Typeface.createFromAsset(itemView.getContext().getAssets(), "OpenSans-Regular.ttf");

            bookImageView = itemView.findViewById(R.id.book_img);
            senderNameText = itemView.findViewById(R.id.req_sender_name);
            bookNameText = itemView.findViewById(R.id.book_name);
            authorNameText = itemView.findViewById(R.id.author_name);
            allowButton = itemView.findViewById(R.id.allow_req_btn);
            rejectButton = itemView.findViewById(R.id.reject_req_btn);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();


                }
            });
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.received_book_list_adapter_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final BookObj book = bookObjList.get(i);
        FirebaseAuth mAuth;

        mAuth = FirebaseAuth.getInstance();
        final String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();



        if (book.getAvailability().equals("no")) {
            viewHolder.allowButton.setClickable(false);
            viewHolder.allowButton.setText("Not Available");
        } else if (book.getAvailability().equals("yes")) {
            viewHolder.allowButton.setText("ALLOW");
            viewHolder.allowButton.setClickable(true);
        }

        viewHolder.allowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.allowButton.setClickable(false);
                viewHolder.allowButton.setText("Allowed");
                viewHolder.rejectButton.setVisibility(v.INVISIBLE);
                //send a request to book owner

                DatabaseReference uid_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                        child("UID").child(user_id).child("username");

                uid_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String user_name = (String) dataSnapshot.getValue();

                        final DatabaseReference allowed_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                child("UID").child(user_id).child("allowed").child(book.getBook_id());

                        allowed_ref.child("name").setValue(book.getName());
                        allowed_ref.child("category").setValue(book.getCategory());
                        allowed_ref.child("writer").setValue(book.getWriter());
                        allowed_ref.child("availability").setValue(book.getAvailability());
                        allowed_ref.child("bookid").setValue(book.getBook_id());
                        allowed_ref.child("owner").setValue(book.getOwner());
                        allowed_ref.child("imageuri").setValue(book.getImageuri());


                        assert user_name != null;
                        final DatabaseReference user_name_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                child("username").child(user_name).child("allowed").child(book.getBook_id());

                        user_name_ref.child("name").setValue(book.getName());
                        user_name_ref.child("category").setValue(book.getCategory());
                        user_name_ref.child("writer").setValue(book.getWriter());
                        user_name_ref.child("availability").setValue(book.getAvailability());
                        user_name_ref.child("bookid").setValue(book.getBook_id());
                        user_name_ref.child("owner").setValue(book.getOwner());
                        user_name_ref.child("imageuri").setValue(book.getImageuri());


                        final DatabaseReference from_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                child("username").child(from).child("granted").child(book.getBook_id());

                        from_ref.child("name").setValue(book.getName());
                        from_ref.child("category").setValue(book.getCategory());
                        from_ref.child("writer").setValue(book.getWriter());
                        from_ref.child("availability").setValue(book.getAvailability());
                        from_ref.child("bookid").setValue(book.getBook_id());
                        from_ref.child("owner").setValue(book.getOwner());
                        from_ref.child("imageuri").setValue(book.getImageuri());

                        final DatabaseReference from_uid_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                child("username").child(from).child("UID");

                        from_uid_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String from_uid = (String) dataSnapshot.getValue();

                                assert from_uid != null;
                                final DatabaseReference from_uid_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                        child("UID").child(from_uid).child("granted").child(book.getBook_id());

                                from_uid_ref.child("name").setValue(book.getName());
                                from_uid_ref.child("category").setValue(book.getCategory());
                                from_uid_ref.child("writer").setValue(book.getWriter());
                                from_uid_ref.child("availability").setValue(book.getAvailability());
                                from_uid_ref.child("bookid").setValue(book.getBook_id());
                                from_uid_ref.child("owner").setValue(book.getOwner());
                                from_uid_ref.child("imageuri").setValue(book.getImageuri());
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







            }
        });

        viewHolder.rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.rejectButton.setClickable(false);
                viewHolder.rejectButton.setText("Rejected");
                viewHolder.allowButton.setVisibility(v.INVISIBLE);
                //current user e book read list e thakle unread banate hobe else read
                final DatabaseReference update_req_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                        child("UID").child(user_id).child("receiverequest");

                update_req_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap<String, Object> received_req = (HashMap<String, Object>) dataSnapshot.getValue();

                        for (HashMap.Entry<String, Object> entry : received_req.entrySet()){
                            HashMap<String, Object> singleBook = (HashMap<String, Object>) entry.getValue();
                            System.out.println(singleBook);
                            HashMap singleBook1 = new HashMap();

                            int i = 0;
                            for (HashMap.Entry<String, Object> entry2 : singleBook.entrySet()) {

                                if (!entry2.getKey().equals("from")) {
                                    singleBook1 = (HashMap) entry2.getValue();
                                    if(singleBook1.get("bookid").equals(book.getBook_id())) {
                                        update_req_ref.child(entry.getKey()).setValue(null);
                                    }
                                }
                                else
                                {
                                    from = (String) entry2.getValue();
                                }
                            }

                        }


                        final DatabaseReference update_username_ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                child("UID").child(user_id).child("username");

                        update_username_ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String user_name = (String) dataSnapshot.getValue();

                                assert user_name != null;
                                final DatabaseReference update_req_ref_username = FirebaseDatabase.getInstance().getReference().child("Users").child("Owners").
                                        child("username").child(user_name).child("receiverequest");

                                update_req_ref_username.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        HashMap<String, Object> received_req_username = (HashMap<String, Object>) dataSnapshot.getValue();



                                        for (HashMap.Entry<String, Object> entry_user : received_req_username.entrySet()){
                                            HashMap<String, Object> singleBook_user = (HashMap<String, Object>) entry_user.getValue();
                                            System.out.println(singleBook_user);
                                            HashMap singleBook_user1 = new HashMap();

                                            int i = 0;
                                            for (HashMap.Entry<String, Object> entry_user2 : singleBook_user.entrySet()) {
                                                System.out.println("EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE     " + i++);
                                                System.out.println(entry_user2.getValue() + "        " + entry_user2.getKey());
                                                System.out.println(book.getBook_id());
                                                if (!entry_user2.getKey().equals("from")) {
                                                    singleBook_user1 = (HashMap) entry_user2.getValue();
                                                    if(singleBook_user1.get("bookid").equals(book.getBook_id())) {
                                                        update_req_ref_username.child(entry_user.getKey()).setValue(null);
                                                        System.out.println("dddddddddddd        " + entry_user2.getKey() + " " + entry_user.getKey());
                                                    }
                                                }
                                                else
                                                {
                                                    from = (String) entry_user2.getValue();
                                                }
                                            }

                                            System.out.println("SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS");
                                            System.out.println(singleBook_user1);
                                        }


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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        //Book Image
        String image = book.getImageuri();
        if(!image.equals("noimageuri")) {
            Picasso.with(viewHolder.bookImageView.getContext()).load(image).into(viewHolder.bookImageView);
        }
        else {
            viewHolder.bookImageView.setImageResource(R.drawable.library);
        }
        viewHolder.bookImageView.setVisibility(View.VISIBLE);

        //Book name
        viewHolder.bookNameText.setTypeface(viewHolder.mTfRegular);
        viewHolder.bookNameText.setText(book.getName());
        viewHolder.bookNameText.setVisibility(View.VISIBLE);

        //author name
        //viewHolder.authorNameText.setTypeface(viewHolder.mTfRegular);
        viewHolder.authorNameText.setText(book.getWriter());
        viewHolder.authorNameText.setVisibility(View.VISIBLE);

        //sender name
        viewHolder.senderNameText.setTypeface(viewHolder.mTfRegular);
        viewHolder.senderNameText.setText("New request from " + from );
        viewHolder.senderNameText.setVisibility(View.VISIBLE);


    }



    @Override
    public int getItemCount() {
        return bookObjList.size();
    }

    protected static String getSaltString() {
        int length = 6;
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();

    }
}