package com.example.elena.shopeasy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.elena.shopeasy.R;

/**
 * Created by Absolute on 1/30/2018.
 */

public class HomeGridAdapter extends BaseAdapter {
    private Context mContext;

    // references to our images
    private Integer[] mThumbIds = {
            R.drawable.main1,
            R.drawable.main2,
            R.drawable.main3,
            R.drawable.main4
    };

    private String[] mTextContent = {
            "Personalize",
            "Find the best ingredients",
            "Get inspired",
            "Brands"
    };

    public HomeGridAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.item_grid_home, parent, false);
        }

        textView = convertView.findViewById(R.id.text_view_home_item);
        textView.setBackgroundResource(mThumbIds[position]);
        textView.setText(mTextContent[position]);
        return convertView;
    }

}
