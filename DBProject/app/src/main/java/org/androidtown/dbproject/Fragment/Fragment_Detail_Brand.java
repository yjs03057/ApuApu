package org.androidtown.dbproject.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.androidtown.dbproject.Activity.MainActivity;
import org.androidtown.dbproject.Object.Cloth;
import org.androidtown.dbproject.Object.Weather;
import org.androidtown.dbproject.R;

import java.util.ArrayList;

public class Fragment_Detail_Brand extends Fragment {

    private TextView Warning_TextView = null;
    private TextView Top_TextView = null;
    private TextView Top_Main_Category_TextView = null;
    private TextView Top_Sub_Category_TextView = null;
    private TextView Pants_TextView = null;
    private TextView Pants_Main_Category_TextView = null;
    private TextView Pants_Sub_Category_TextView = null;
    private TextView Outer_TextView = null;
    private TextView Outer_Main_Category_TextView = null;
    private TextView Outer_Sub_Category_TextView = null;
    private TextView Other_TextView = null;
    private TextView Other_Main_Category_TextView = null;
    private TextView Other_Sub_Category_TextView = null;

    private Button Top_Link_Button = null;
    private Button Pants_Link_Button = null;
    private Button Outer_Link_Button = null;
    private Button Other_Link_Button = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_brand, container, false);

        Warning_TextView = view.findViewById(R.id.fragment_detail_brand_warning);
        Top_TextView = view.findViewById(R.id.fragment_detail_brand_cloth_top_name_textview);
        Top_Main_Category_TextView = view.findViewById(R.id.fragment_detail_brand_top_main_category);
        Top_Sub_Category_TextView = view.findViewById(R.id.fragment_detail_brand_top_sub_category);
        Pants_TextView = view.findViewById(R.id.fragment_detail_brand_cloth_pants_name_textview);
        Pants_Main_Category_TextView = view.findViewById(R.id.fragment_detail_brand_pants_main_category);
        Pants_Sub_Category_TextView = view.findViewById(R.id.fragment_detail_brand_pants_sub_category);
        Outer_TextView = view.findViewById(R.id.fragment_detail_brand_cloth_outer_name_textview);
        Outer_Main_Category_TextView = view.findViewById(R.id.fragment_detail_brand_outer_main_category);
        Outer_Sub_Category_TextView = view.findViewById(R.id.fragment_detail_brand_outer_sub_category);
        Other_TextView = view.findViewById(R.id.fragment_detail_brand_cloth_other_name_textview);
        Other_Main_Category_TextView = view.findViewById(R.id.fragment_detail_brand_other_main_category);
        Other_Sub_Category_TextView = view.findViewById(R.id.fragment_detail_brand_other_sub_category);

        Top_Link_Button = view.findViewById(R.id.fragment_detail_brand_top_link_button);
        Pants_Link_Button = view.findViewById(R.id.fragment_detail_brand_pants_link_button);
        Outer_Link_Button = view.findViewById(R.id.fragment_detail_brand_outer_link_button);
        Other_Link_Button = view.findViewById(R.id.fragment_detail_brand_other_link_button);


        if (MainActivity.setlist) {
            setview();
        }else{
            WeatherThread thread = new WeatherThread();
            thread.start();
        }

        return view;
    }

    private void setview() {

        ArrayList<Cloth> list = MainActivity.shop_result;
        final String[] cloth_url = MainActivity.shop_result_url;

        Warning_TextView.setText("오늘의 날씨는 " + "\""+Weather.Today_Name+ "\"");

        if (list.get(0) == null) {
            Top_TextView.setText("날씨에 맞는 물품이 없네요..");
            Top_Main_Category_TextView.setText("#..");
            Top_Sub_Category_TextView.setText("#..");
        } else {
            Top_TextView.setText(list.get(0).getName());
            Top_Main_Category_TextView.setText("#" +list.get(0).getMain_category());
            Top_Sub_Category_TextView.setText("#" +list.get(0).getSub_category());
            Top_Link_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cloth_url[0]));
                    startActivity(myIntent);
                }
            });
        }

        if (list.get(1) == null) {
            Pants_TextView.setText("날씨에 맞는 물품이 없네요..");
            Pants_Main_Category_TextView.setText("#..");
            Pants_Sub_Category_TextView.setText("#..");
        } else {
            Pants_TextView.setText(list.get(1).getName());
            Pants_Main_Category_TextView.setText("#" +list.get(1).getMain_category());
            Pants_Sub_Category_TextView.setText("#" +list.get(1).getSub_category());
            Pants_Link_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cloth_url[1]));
                    startActivity(myIntent);
                }
            });
        }

        if (list.get(2) == null) {
            Outer_TextView.setText("날씨에 맞는 물품이 없네요..");
            Outer_Main_Category_TextView.setText("#..");
            Outer_Sub_Category_TextView.setText("#..");
        } else {
            Outer_TextView.setText(list.get(2).getName());
            Outer_Main_Category_TextView.setText("#" +list.get(2).getMain_category());
            Outer_Sub_Category_TextView.setText("#" +list.get(2).getSub_category());
            Outer_Link_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cloth_url[2]));
                    startActivity(myIntent);
                }
            });
        }

        if (list.get(3) == null) {
            Other_TextView.setText("날씨에 맞는 물품이 없네요..");
            Other_Main_Category_TextView.setText("#..");
            Other_Sub_Category_TextView.setText("#..");
        } else {
            Other_TextView.setText(list.get(3).getName());
            Other_Main_Category_TextView.setText("#" +list.get(3).getMain_category());
            Other_Sub_Category_TextView.setText("#" +list.get(3).getSub_category());
            Other_Link_Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cloth_url[3]));
                    startActivity(myIntent);
                }
            });
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
