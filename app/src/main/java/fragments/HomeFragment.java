package fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenu;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.searchify.AddBookActivity;
import com.example.searchify.R;
import com.example.searchify.ShowProfileActivity;
import com.example.searchify.UserObj;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adapter.UserListAdapter;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    private ListView userListView;
    private UserListAdapter userListAdapter;
    private List<UserObj> userObjList = new ArrayList<>();
    private Typeface mTfRegular, mTfLight, mtfBold;

    private ArrayList<UserObj> new_user;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new_user = new ArrayList<>();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userListView = view.findViewById(R.id.user_list_view);

        new_user = new ArrayList<>();
        System.out.println("Entering homeeeeeeeeeeeeeeeeeeeeeeeee");

//        recyclerView = view.findViewById(R.id.goal_recycler_view);
//        layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);




        DatabaseReference user_ref = FirebaseDatabase.getInstance().getReference().child("Users").
                child("Owners").child("UID");

        user_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> all_user = (Map<String, Object>) dataSnapshot.getValue();
                assert all_user != null;
                collectUserData(all_user);


                //List Adapter
                userListAdapter = new UserListAdapter(new_user, getContext());

                userListView.setAdapter(userListAdapter);
                userListView.setClickable(true);

                userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int itemNumber, long l) {
                        Object obj = userListView.getAdapter().getItem(itemNumber);
                        final String userName = new_user.get(itemNumber).getUser_name();
                        final String fullName = new_user.get(itemNumber).getName();
                        Intent intent = new Intent(getContext(), ShowProfileActivity.class);
                        intent.putExtra("username", userName);
                        intent.putExtra("fullname", fullName);
                        startActivity(intent);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        //Fav Button
        FabSpeedDial fabAddFood = (FabSpeedDial) view.findViewById(R.id.fab_add_food);
        fabAddFood.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onPrepareMenu(NavigationMenu navigationMenu) {
                return true;
            }
        });
        fabAddFood.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {


                switch (menuItem.getItemId()) {
                    case R.id.action_addbook: {
                        Intent intent = new Intent(getContext(), AddBookActivity.class);
                        startActivity(intent);
                        break;
                    }

                }
                return false;
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

    private void collectUserData(Map<String, Object> users) {


        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){
            UserObj aUser = new UserObj();

            //Get user map
            Map singleUser = (Map) entry.getValue();

            //Get phone field and append to list
            aUser.setName((String) singleUser.get("name"));
            aUser.setUser_name((String) singleUser.get("username"));
            //phoneNumbers.add((Long) singleUser.get("phone"));
            System.out.println("PPPPPPP      " + aUser.toString());
            new_user.add(aUser);
        }
        //System.out.println("qqqqqq   " + new_user.size());

        System.out.println("rrrrr   " + new_user.toString());
    }

}
