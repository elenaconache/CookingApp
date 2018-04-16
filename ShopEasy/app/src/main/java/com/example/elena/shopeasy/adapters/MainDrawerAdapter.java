package com.example.elena.shopeasy.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.elena.shopeasy.R;

/**
 * Created by Absolute on 3/7/2018.
 */

public class MainDrawerAdapter extends ArrayAdapter<String>
{
    private final Context context;
    private final int layoutResourceId;
    private String data[];

    public MainDrawerAdapter(Context context, int layoutResourceId, String[] data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        TextView textView;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(layoutResourceId, parent, false);
        }
        textView = convertView.findViewById(R.id.text_drawer_option);
        textView.setText(data[position]);
        return convertView;
    }
}