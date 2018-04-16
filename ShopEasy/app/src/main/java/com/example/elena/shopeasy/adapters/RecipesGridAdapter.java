package com.example.elena.shopeasy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.model.Recipe;

import java.util.List;

/**
 * Created by Absolute on 3/5/2018.
 */

public class RecipesGridAdapter extends BaseAdapter {
    private Context mContext;

    // references to our images
    private List<Recipe> mRecipes;

    public RecipesGridAdapter(Context c, List<Recipe> recipes) {
        mContext = c;
        mRecipes = recipes;
        notifyDataSetChanged();
    }

    public int getCount() {
        return mRecipes.size();
    }

    public Object getItem(int position) {
        return mRecipes.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textViewRecipeName;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).
                    inflate(R.layout.item_recipe, parent, false);
        }

        textViewRecipeName = convertView.findViewById(R.id.recipe_name_text);
        textViewRecipeName.setText(mRecipes.get(position).getName());
        return convertView;
    }

}
