package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.searchify.BookObj;
import com.example.searchify.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GridElementAdapter extends BaseAdapter {
    Context context;

    private List<BookObj> bookObjList;

    public GridElementAdapter(List<BookObj> bookObjList, Context

            context) {
        this.context = context;
        this.bookObjList = bookObjList;

    }

    @Override
    public int getCount() {
        return this.bookObjList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BookObj book = bookObjList.get(position);

        View v = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_element, parent,

                    false);
        } else {
            v = convertView;
        }





        Typeface mTfRegular = Typeface.createFromAsset(v.getContext().getAssets(), "OpenSans-Regular.ttf");

        ImageView bookImageView = v.findViewById(R.id.grid_img);
        TextView bookNameText = v.findViewById(R.id.grid_text);


        //Book Image
        String image = book.getImageuri();
        if(!image.equals("noimageuri"))
        {
            Picasso.with(bookImageView.getContext()).load(image).into(bookImageView);
        }
        else {
            bookImageView.setImageResource(R.drawable.library);
        }
        bookImageView.setVisibility(View.VISIBLE);


//        bookImageView.setImageResource(R.drawable.library);
//        bookImageView.setVisibility(View.VISIBLE);

        //Book name
        bookNameText.setTypeface(mTfRegular);
        bookNameText.setText(book.getName());
        bookNameText.setVisibility(View.VISIBLE);


        return v;
    }



}