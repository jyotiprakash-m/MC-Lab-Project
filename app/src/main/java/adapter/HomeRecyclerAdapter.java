package adapter;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.searchify.R;
import com.example.searchify.UserObj;

import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    List<UserObj> userObjList;


    public HomeRecyclerAdapter(List<UserObj>userObjList) {
        this.userObjList = userObjList;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView itemImage;
        public TextView itemName;



        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.goal_image);
            itemName = itemView.findViewById(R.id.goal_name);


            Typeface mTfRegular = Typeface.createFromAsset(itemView.getContext().getAssets(),"OpenSans-Regular.ttf");
            itemName.setTypeface(mTfRegular);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position = getAdapterPosition();

                    /*Snackbar.make(v, "Click detected on item " + position,
                            Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();*/
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemName.setText(userObjList.get(i).getName());
        viewHolder.itemImage.setImageResource(R.drawable.man);
        

    }

    @Override
    public int getItemCount() {
        return userObjList.size();
    }
}