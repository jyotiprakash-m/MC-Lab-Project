package adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.searchify.R;
import com.example.searchify.UserObj;

import java.util.List;

public class UserListAdapter extends BaseAdapter {
    public List<UserObj> userObjList;
    Context context;

    public UserListAdapter(List<UserObj> mchannelList, Context

            context) {
        this.context = context;
        this.userObjList = mchannelList;
    }

    @Override
    public int getCount() {
        return userObjList.size();
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
        View v = null;
        UserObj channelList = userObjList.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.spinner_value_layout, parent,

                    false);
        } else {
            v = convertView;
        }

        Typeface mTfRegular = Typeface.createFromAsset(v.getContext().getAssets(), "OpenSans-Regular.ttf");

        ImageView channelImageView = v.findViewById(R.id.spinnerImages);
        TextView channelNameText = v.findViewById(R.id.spinnerTextView);
        TextView oneLetterText = v.findViewById(R.id.name_char_text);

        channelNameText.setTypeface(mTfRegular);
        oneLetterText.setTypeface(mTfRegular);

        System.out.println("---------------------1111111111-----------");
        System.out.println(channelList.getName() + " " + channelList.getName().charAt(0));
        channelNameText.setText(channelList.getName());
        String one = String.valueOf(channelList.getName().charAt(0));
        oneLetterText.setText(one);

        channelImageView.setImageResource(R.drawable.circleshapepink);

        java.util.Random rand = new java.util.Random();
        int value = rand.nextInt(3);
        android.graphics.drawable.Drawable background = channelImageView.getBackground();

        if (value == 1) {

            if (background instanceof android.graphics.drawable.ShapeDrawable) {
                ((android.graphics.drawable.ShapeDrawable) background).getPaint().setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.pink));
            } else if (background instanceof android.graphics.drawable.GradientDrawable) {
                ((android.graphics.drawable.GradientDrawable) background).setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.pink));
            } else if (background instanceof android.graphics.drawable.ColorDrawable) {
                ((android.graphics.drawable.ColorDrawable) background).setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.pink));
            }

        } else if (value == 2) {

            if (background instanceof android.graphics.drawable.ShapeDrawable) {
                ((android.graphics.drawable.ShapeDrawable) background).getPaint().setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.blue));
            } else if (background instanceof android.graphics.drawable.GradientDrawable) {
                ((android.graphics.drawable.GradientDrawable) background).setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.blue));
            } else if (background instanceof android.graphics.drawable.ColorDrawable) {
                ((android.graphics.drawable.ColorDrawable) background).setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.blue));
            }

        } else {

            if (background instanceof android.graphics.drawable.ShapeDrawable) {
                ((android.graphics.drawable.ShapeDrawable) background).getPaint().setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.green));
            } else if (background instanceof android.graphics.drawable.GradientDrawable) {
                ((android.graphics.drawable.GradientDrawable) background).setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.green));
            } else if (background instanceof android.graphics.drawable.ColorDrawable) {
                ((android.graphics.drawable.ColorDrawable) background).setColor(android.support.v4.content.ContextCompat.getColor(context, R.color.green));
            }

        }

        channelImageView.setVisibility(View.VISIBLE);
        channelNameText.setVisibility(View.VISIBLE);
        oneLetterText.setVisibility(View.VISIBLE);


        return v;
    }
}