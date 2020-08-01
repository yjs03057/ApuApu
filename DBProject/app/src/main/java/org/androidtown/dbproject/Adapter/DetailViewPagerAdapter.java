package org.androidtown.dbproject.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidtown.dbproject.Fragment.Fragment_Brand;
import org.androidtown.dbproject.Fragment.Fragment_Detail_Brand;
import org.androidtown.dbproject.Fragment.Fragment_Detail_User;
import org.androidtown.dbproject.Fragment.Fragment_User;
import org.androidtown.dbproject.R;

public class DetailViewPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext = null;

    public DetailViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment_Detail_User tab1 = new Fragment_Detail_User();
                return tab1;
            case 1:
                Fragment_Detail_Brand tab2 = new Fragment_Detail_Brand();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }



}
