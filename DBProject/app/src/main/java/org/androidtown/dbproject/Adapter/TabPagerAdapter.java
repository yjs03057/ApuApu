package org.androidtown.dbproject.Adapter;

import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import org.androidtown.dbproject.Fragment.Fragment1;
import org.androidtown.dbproject.Fragment.Fragment2;
import org.androidtown.dbproject.Fragment.Fragment3;
import org.androidtown.dbproject.Fragment.Fragment4;
import org.androidtown.dbproject.Fragment.Fragment5;


public class TabPagerAdapter extends FragmentStatePagerAdapter {

    public static final int ALL = 10;
    public static final int TOP = 11;
    public static final int PANTS = 12;
    public static final int OUTER = 13;
    public static final int OTHER = 14;

    // Count number of tabs
    private int tabCount;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    private Fragment5 fragment5;

    private Handler mhandler = new Handler(){
        @Override
        public void handleMessage(final Message msg) {
            final int num = msg.what;
            switch (num) {
                case ALL:
                    fragment2.ReFresh();
                    fragment3.ReFresh();
                    fragment4.ReFresh();
                    fragment5.ReFresh();
                    break;
                default:
                    fragment1.ReFresh();
                    break;
            }
        }
    };


    public TabPagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {

        // Returning the current tabs
        switch (position) {
            case 0:
                Fragment1 tabFragment1 = new Fragment1();
                tabFragment1.setHandler(mhandler);
                return tabFragment1;
            case 1:
                Fragment2 tabFragment2 = new Fragment2();
                tabFragment2.setHandler(mhandler);
                return tabFragment2;
            case 2:
                Fragment3 tabFragment3 = new Fragment3();
                tabFragment3.setHandler(mhandler);
                return tabFragment3;
            case 3:
                Fragment4 tabFragment4 = new Fragment4();
                tabFragment4.setHandler(mhandler);
                return tabFragment4;
            case 4:
                Fragment5 tabFragment5 = new Fragment5();
                tabFragment5.setHandler(mhandler);
                return tabFragment5;
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                fragment1 = (Fragment1) createdFragment;
                break;
            case 1:
                fragment2 = (Fragment2) createdFragment;
                break;
            case 2:
                fragment3 = (Fragment3) createdFragment;
                break;
            case 3:
                fragment4 = (Fragment4) createdFragment;
                break;
            case 4:
                fragment5 = (Fragment5) createdFragment;
                break;
        }
        return createdFragment;
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}