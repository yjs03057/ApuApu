package org.androidtown.dbproject.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidtown.dbproject.Activity.MainActivity;
import org.androidtown.dbproject.Object.Cloth;
import org.androidtown.dbproject.R;

import java.util.ArrayList;

public class Fragment_Brand extends Fragment {

    private LinearLayout Top_Layout = null;
    private LinearLayout Pants_Layout = null;
    private LinearLayout Outer_Layout = null;
    private LinearLayout Other_Layout = null;

    private TextView Top_TextView = null;
    private TextView Pants_TextView = null;
    private TextView Outer_TextView = null;
    private TextView Other_TextView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_brand, container, false);

        Top_Layout = view.findViewById(R.id.fragment_brand_cloth_top_layout);
        Pants_Layout = view.findViewById(R.id.fragment_brand_cloth_pants_layout);
        Outer_Layout = view.findViewById(R.id.fragment_brand_cloth_outer_layout);
        Other_Layout = view.findViewById(R.id.fragment_brand_cloth_other_layout);

        Top_TextView = view.findViewById(R.id.fragment_brand_cloth_top_name_textview);
        Pants_TextView = view.findViewById(R.id.fragment_brand_cloth_pants_name_textview);
        Outer_TextView = view.findViewById(R.id.fragment_brand_cloth_outer_name_textview);
        Other_TextView = view.findViewById(R.id.fragment_brand_cloth_other_name_textview);

        if (MainActivity.setlist) {
            setview();
        }else{
            WeatherThread thread = new WeatherThread();
            thread.start();
        }

        return view;
    }

    private void setview() {

        Top_Layout.setVisibility(View.VISIBLE);
        Pants_Layout.setVisibility(View.VISIBLE);
        Outer_Layout.setVisibility(View.VISIBLE);
        Other_Layout.setVisibility(View.VISIBLE);

        ArrayList<Cloth> list = MainActivity.shop_result;
        if (list.get(0) == null) {
            Top_Layout.setVisibility(View.GONE);
        } else {
            Top_TextView.setText(list.get(0).getName());
        }

        if (list.get(1) == null) {
            Pants_Layout.setVisibility(View.GONE);
        } else {
            Pants_TextView.setText(list.get(1).getName());
        }
        if (list.get(2) == null) {
            Outer_Layout.setVisibility(View.GONE);
        } else {
            Outer_TextView.setText(list.get(2).getName());
        }
        if (list.get(3) == null) {
            Other_Layout.setVisibility(View.GONE);
        } else {
            Other_TextView.setText(list.get(3).getName());
        }

    }

    class WeatherThread extends Thread {

        @Override
        public void run() {
            while (!MainActivity.setlist) {
                //wait
            }
            setview();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MainActivity.setlist){
            setview();

        }
    }

}
