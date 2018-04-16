package com.example.elena.shopeasy.adapters;

/**
 * Created by Absolute on 2/1/2018.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.customViews.AnimatedExpandableListView;
import com.example.elena.shopeasy.ui.RecipesActivity;
import com.example.elena.shopeasy.utils.PrefUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Absolute on 2/1/2018.
 */

import java.util.Set;

/**
 * Adapter for our list of {GroupItem}s.
 */
public class CategoriesAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {

    private List<String> mHeaders;
    private HashMap<String,Set<String>> mDataChild;
    private Context mContext ;

    private Activity mActivity;

    public CategoriesAdapter(Context context, List<String> listDataHeader,
                             HashMap<String, Set<String>> listChildData, Activity activity) {
        this.mContext = context;
        this.mHeaders= listDataHeader;
        this.mDataChild = listChildData;

        this.mActivity = activity;

        notifyDataSetChanged();
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        Set<String> childSet = mDataChild.get(mHeaders.get(groupPosition));
        List<String> childList = new ArrayList<>();
        childList.addAll(childSet);
        return childList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (infalInflater != null) {
                convertView = infalInflater.inflate(R.layout.categories_list_item, null);
            }
        }

        if (convertView != null) {
            TextView txtListChild = convertView
                    .findViewById(R.id.child_category_text);
            txtListChild.setText(childText);



            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent openRecipesActivity = new Intent(mContext,RecipesActivity.class);
                    openRecipesActivity.putExtra(RecipesActivity.EXTRA_INGREDIENT, childText);
                    mContext.startActivity(openRecipesActivity);
                }
            });

        }

        return convertView;

    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if (mDataChild.get(mHeaders.get(groupPosition))==null) return 0;
        return this.mDataChild.get(this.mHeaders.get(groupPosition))
                .size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return this.mHeaders.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mHeaders.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        final PrefUtils prefUtils = new PrefUtils(mActivity);
        int fontColor = prefUtils.getFontColor();
        Log.wtf("fontcolor",fontColor+"");
        int backgroundColor = prefUtils.getBackgroundColor();

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (infalInflater != null) {
                convertView = infalInflater.inflate(R.layout.categories_list_group, null);
            }
        }

        if (convertView != null) {

            if (backgroundColor!=0){
                convertView.setBackgroundColor(backgroundColor);
            }

            TextView lblListHeader = convertView
                    .findViewById(R.id.text_header_category);
          //  lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
            if (isExpanded) lblListHeader.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    mContext.getResources().getDrawable(R.drawable.indicator_expanded),
                    null);
            else lblListHeader.setCompoundDrawablesWithIntrinsicBounds(
                    null,
                    null,
                    mContext.getResources().getDrawable(R.drawable.indicator_collapsed),
                    null);

            if (fontColor!=0){
                lblListHeader.setTextColor(fontColor);
            }

            Drawable normalDrawable = mActivity.getResources().getDrawable(R.drawable.group_indicator);
            Drawable wrapDrawable = DrawableCompat.wrap(normalDrawable);
            DrawableCompat.setTint(wrapDrawable, prefUtils.getFontColor());
            if (prefUtils.getFontColor()!=0){
                lblListHeader.setCompoundDrawablesWithIntrinsicBounds(null,null,wrapDrawable,null);
                // expListView.
                // expListView.setGroupIndicator(getResources().getDrawable(R.drawable.group_indicator).setTint(prefUtils.getFontColor()););
            }else {

                lblListHeader.setCompoundDrawablesWithIntrinsicBounds(null,null,normalDrawable,null);

            }
        }

        return convertView;

    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

}