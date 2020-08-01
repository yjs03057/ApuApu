package org.androidtown.dbproject.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.androidtown.dbproject.Object.Cloth;
import org.androidtown.dbproject.Object.Recommendation;
import org.androidtown.dbproject.Object.User;
import org.androidtown.dbproject.Object.Weather;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidtown.dbproject.ConnectDB;
import org.androidtown.dbproject.R;
import org.androidtown.dbproject.Adapter.DetailViewPagerAdapter;
import org.androidtown.dbproject.Adapter.ViewPagerAdapter;
import org.androidtown.dbproject.Weather_API;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public final static int SIMPLE = 10;
    public final static int DETAIL = 11;

    public final static int SEND_SUCCESS = 21;
    public final static int SEND_FAIL = 22;

    public static String[] shop_result_url ;
    public static ArrayList<Cloth> shop_result =new ArrayList<>();
    public static ArrayList<Cloth> user_result = new ArrayList<>();
    public static Boolean setlist = false;

    private ConnectDB DB;
    private Recommendation Recommen;
    String tag = "SQLite";

    private LinearLayout Scroll_Down_Layout = null;
    private LinearLayout Scroll_Up_Layout = null;

    private ImageView Scroll_Down_Closet = null;
    private ImageView Scroll_Up_Closet = null;
    private ImageView Scroll_Down_Weather_icon = null;
    private ImageView Scroll_Up_Weather_icon = null;

    private ViewPager Main_ViewPager = null;
    private ViewPager Main_ViewPager_Detail = null;
    private ViewPagerAdapter Simple_PagerAdpter = null;
    private DetailViewPagerAdapter Detail_PagerAdapter = null;
    private int ViewPager_state = 0;

    private TextView Scroll_Down_Monthly = null;
    private TextView Scroll_Up_Monthly = null;
    private TextView Scroll_Down_Temperture = null;
    private TextView Scroll_Up_Temperture = null;

    private Weather_API mWeatherAPI = null;

    final private Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEND_SUCCESS:
                    DB.Insert_Weather((Weather) msg.obj);
                    setWeather((Weather) msg.obj);
                    getCloth();
                    break;
                case SEND_FAIL:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DB = new ConnectDB(this); // DB 연결
        Recommen = new Recommendation(DB);


        Scroll_Down_Layout = findViewById(R.id.scroll_down_layout);
        Scroll_Up_Layout = findViewById(R.id.scroll_up_layout);

        Scroll_Down_Closet = findViewById(R.id.scroll_down_closet_icon);
        Scroll_Up_Closet = findViewById(R.id.scroll_up_closet_icon);
        Scroll_Down_Weather_icon = findViewById(R.id.scroll_down_weather_icon);
        Scroll_Up_Weather_icon = findViewById(R.id.scroll_up_weather_icon);

        Main_ViewPager = findViewById(R.id.viewpager);

        Main_ViewPager_Detail = findViewById(R.id.viewpager_detail);
        Simple_PagerAdpter = new ViewPagerAdapter(getSupportFragmentManager(),this);
        Detail_PagerAdapter = new DetailViewPagerAdapter(getSupportFragmentManager(),this);

        Main_ViewPager.setAdapter(Simple_PagerAdpter);
        Main_ViewPager_Detail.setAdapter(Detail_PagerAdapter);

        Scroll_Down_Monthly = findViewById(R.id.scroll_down_monthly);
        Scroll_Up_Monthly = findViewById(R.id.scroll_up_monthly);
        Scroll_Down_Temperture = findViewById(R.id.scroll_down_temperture);
        Scroll_Up_Temperture = findViewById(R.id.scroll_up_temperture);


        //제스쳐 셋팅
        ViewPager_state = SIMPLE;
        final GestureDetector gestureDetector = new GestureDetector(Main_ViewPager.getContext(), scroll_event);
        final GestureDetector gestureDetector_detail = new GestureDetector(Main_ViewPager_Detail.getContext(), scroll_event);


        Main_ViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });

        Main_ViewPager_Detail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector_detail.onTouchEvent(event);
                return false;
            }
        });

        Scroll_Down_Closet.setOnClickListener(Closet_ClickListener);
        Scroll_Up_Closet.setOnClickListener(Closet_ClickListener);

        //날씨 설정 : 엑티비티들이 다 배치된 후 사용해야함.
        getWeather();

    }

    // 특정 제스처 동작 이벤트 설정 : scroll
    private GestureDetector.OnGestureListener scroll_event = new GestureDetector.OnGestureListener() {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
            if (distanceY > 100) {
                if (ViewPager_state == SIMPLE) {

                    Main_ViewPager.animate().translationY(-500).setDuration(100)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    Scroll_Down_Layout.setVisibility(View.GONE);
                                    Scroll_Up_Layout.setVisibility(View.VISIBLE);
                                    ViewPager_state = DETAIL;
                                    Main_ViewPager.animate().translationY(0);
                                }
                            });
                    int view_number = Main_ViewPager.getCurrentItem();
                    Main_ViewPager_Detail.setCurrentItem(view_number);
                }
            } else if (distanceY < -100) {
                if (ViewPager_state == DETAIL) {

                    Main_ViewPager_Detail.animate().translationY(500).setDuration(100)
                            .withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    Scroll_Up_Layout.setVisibility(View.GONE);
                                    Scroll_Down_Layout.setVisibility(View.VISIBLE);
                                    ViewPager_state = SIMPLE;
                                    Main_ViewPager_Detail.animate().translationY(0);
                                }
                            });
                    int view_number = Main_ViewPager_Detail.getCurrentItem();
                    Main_ViewPager.setCurrentItem(view_number);
                }
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

    //날씨 상세정보 가져오기
    private void getWeather() {

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMdd", Locale.KOREA);
        String monthly = formatter.format(date);

        Weather today_weather = DB.Select_Weather(monthly);

        if (today_weather == null) {
            mWeatherAPI = new Weather_API(mhandler);
            mWeatherAPI.getWeather();
        } else {
            setWeather(today_weather);
            getCloth();
        }

    }

    // 날씨정보 입력
    private void setWeather(Weather weather) {

        Weather.Today_Name = weather.getName();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd", Locale.KOREA);
        String monthly = formatter.format(date);

        Scroll_Down_Monthly.setText(monthly);
        Scroll_Up_Monthly.setText(monthly);

        String Temperture = weather.getTMax() + "℃/" + weather.getTMin() + "℃";

        Scroll_Down_Temperture.setText(Temperture);
        Scroll_Up_Temperture.setText(Temperture);

        setWeather_icon(weather.getName());
    }

    private void setWeather_icon(String weather_type) {

        if (weather_type.equals("맑음")) {
            Scroll_Down_Weather_icon.setImageResource(R.drawable.icon_sun);
            Scroll_Up_Weather_icon.setImageResource(R.drawable.icon_sun);
        }

        //해랑 구름 같이 있는 아이콘
        if (weather_type.equals("구름조금")) {
            Scroll_Down_Weather_icon.setImageResource(R.drawable.icon_cloudsun);
            Scroll_Up_Weather_icon.setImageResource(R.drawable.icon_cloudsun);
        }

        //구름만 있는 아이콘
        if (weather_type.equals("구름많음")) {
            Scroll_Down_Weather_icon.setImageResource(R.drawable.icon_cloud);
            Scroll_Up_Weather_icon.setImageResource(R.drawable.icon_cloud);
        }

        //비 오는 아이콘
        if (weather_type.equals("구름많고 비") ||
                weather_type.equals("구름많고 비 또는 눈") ||
                weather_type.equals("흐리고 비") ||
                weather_type.equals("흐리고 비 또는 눈") ||
                weather_type.equals("뇌우/비") ||
                weather_type.equals("뇌우/비 또는 눈")) {
            Scroll_Down_Weather_icon.setImageResource(R.drawable.icon_rain);
            Scroll_Up_Weather_icon.setImageResource(R.drawable.icon_rain);
        }

        //눈 내리는 아이콘
        if (weather_type.equals("구름많고 눈") ||
                weather_type.equals("흐리고 눈") ||
                weather_type.equals("뇌우/눈")) {
            Scroll_Down_Weather_icon.setImageResource(R.drawable.icon_snow);
            Scroll_Up_Weather_icon.setImageResource(R.drawable.icon_snow);
        }

        //바람 표시된 아이콘
        if (weather_type.equals("흐림") ||
                weather_type.equals("흐리고 낙뢰")) {
            Scroll_Down_Weather_icon.setImageResource(R.drawable.icon_wind);
            Scroll_Up_Weather_icon.setImageResource(R.drawable.icon_wind);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(setlist){
            getCloth();
        }
    }

    private void getCloth() {

        resetstatic();

        String[] shop = Recommen.selectShopClothes(User.MainUser_Sex);
        String[] user = Recommen.selectUserClothes(User.MainUser_Id);

        for (int i = 0; i < shop.length - 1; i++) {
            if (shop[i].equals("없음")) {
                shop_result.add(null);
            } else {
                shop_result.add(DB.Select_Brand_Cloth_Name(shop[i]));
            }
        }

        for (int i = 0; i < user.length - 1; i++) {
            if (user[i].equals("없음")) {
                user_result.add(null);
            } else {
                user_result.add(DB.Select_User_Cloth_Name(user[i]));
            }
        }

        shop_result_url = Recommen.selectShopClothesUrl(User.MainUser_Sex);

        setlist = true;
    }


    // 옷장이동
    private View.OnClickListener Closet_ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), ClosetActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetstatic();
    }

    private void resetstatic(){
        shop_result_url = new String[1];
        shop_result =new ArrayList<>();
        user_result = new ArrayList<>();
        setlist = false;
    }

}



