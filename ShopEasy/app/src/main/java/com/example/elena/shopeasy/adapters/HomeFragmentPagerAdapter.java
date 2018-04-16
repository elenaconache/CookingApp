package com.example.elena.shopeasy.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.elena.shopeasy.R;
import com.example.elena.shopeasy.fragments.CategoriesFragment;
import com.example.elena.shopeasy.fragments.HomeFragment;

/**
 * Created by elena on 1/28/2018.
 */

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public HomeFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else{
            return new CategoriesFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.home_info);
           // case 1:
              //  return mContext.getString(R.string.categories_info);
            default:
                return mContext.getString(R.string.categories_info);
        }
    }

}
