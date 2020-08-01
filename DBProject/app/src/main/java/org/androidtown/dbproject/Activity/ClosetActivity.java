package org.androidtown.dbproject.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import org.androidtown.dbproject.Adapter.TabPagerAdapter;
import org.androidtown.dbproject.ConnectDB;
import org.androidtown.dbproject.Fragment.Fragment1;
import org.androidtown.dbproject.Fragment.Fragment2;
import org.androidtown.dbproject.Fragment.Fragment3;
import org.androidtown.dbproject.Fragment.Fragment4;
import org.androidtown.dbproject.Fragment.Fragment5;
import org.androidtown.dbproject.Object.Cloth;
import org.androidtown.dbproject.Object.User;
import org.androidtown.dbproject.R;

import java.util.ArrayList;

public class ClosetActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabPagerAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closet);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageButton button = (ImageButton) findViewById(R.id.button);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("전체"));
        tabLayout.addTab(tabLayout.newTab().setText("상의"));
        tabLayout.addTab(tabLayout.newTab().setText("하의"));
        tabLayout.addTab(tabLayout.newTab().setText("아우터"));
        tabLayout.addTab(tabLayout.newTab().setText("기타"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.pager);

        pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ItemAddActivity.class);
                startActivityForResult(intent, 100);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }



}
