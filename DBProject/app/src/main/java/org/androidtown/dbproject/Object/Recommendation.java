package org.androidtown.dbproject.Object;

import android.database.Cursor;
import android.util.Log;

import org.androidtown.dbproject.ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class Recommendation {

    private ArrayList<String> outer = new ArrayList<>();
    private ArrayList<String> top = new ArrayList<>();
    private ArrayList<String> bottom = new ArrayList<>();
    private ArrayList<String> etc = new ArrayList<>();

    //result array [top, bottom, outer, etc, umbrella]
    private String[] shop_result = {"없음", "없음", "없음", "없음", "필요없어요!"};
    private String[] user_result = {"없음", "없음", "없음", "없음", "필요없어요!"};

    private String cloth_type;
    private int ref_case = 1;
    private int r_case;

    private ConnectDB DB = null;

    public Recommendation(ConnectDB db){
        this.DB = db;
    }
    /**
     * Connect to the test.db database
     * @return the Connection object
     **/
//    private Connection connect(){
//        // SQLite connection string
//        Connection conn = null;
//
//        try {
//            String url = "jdbc:sqlite:C://Users/USER/Desktop/데이터베이스/프로젝트/database/apuapu2.db";
//            conn = DriverManager.getConnection(url);
//            //System.out.println("Connection to SQLite has been established.");
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//        return conn;
//    }

    /**
     *  Select today's r_case from CLIMATE table
     **/
    private void getType(){
        Date Today = new Date();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        String key = today.format(Today);

        Weather weather = DB.Select_Weather(key);

        if(weather !=null){
            r_case = weather.getR_case();
        }
    }

    private void selectCase(){
        //System.out.println("들어왔당!");
        Cursor c = DB.Select_Recommendation(r_case);

        while (c.moveToNext()) {
            r_case = c.getInt(0);
            cloth_type = c.getString(1);
            ref_case = c.getInt(2);
        }
    }

    /**
     * actual function which select Clothes
     * find clothes which match with case until ref_case is zero
     */
    private void setClothes(){
        getType();
        while(ref_case != 0){
            selectCase();
            setArray(cloth_type);
            r_case = ref_case;
        }
    }

    /**
     * Processing cloth_type
     * String -> Array
     * Also find Upper Value
     */
    private void setArray(String clothes){
        //System.out.println(clothes);
        String[] temp = clothes.split(",");
        String upper="";
        boolean check_outer = false;

        //check if there is other outer already which from other r_case
        if (outer.size() == 0){check_outer = true;}

        //find upper value of clothes
        for(int i=0;i<temp.length;i++){
            Cursor c = DB.Select_Match(temp[i]);

            while (c.moveToNext()) {
                upper = c.getString(0);
            }

            //put items in each array
            if (upper.equals("상의")){top.add(temp[i]);}
            if (upper.equals("하의")){bottom.add(temp[i]);}

            //if outer has value which from other r_case, than skip this step
            if(check_outer) {
                if (upper.equals("아우터")){outer.add(temp[i]);}
            }
            if (upper.equals("기타")){etc.add(temp[i]);}
        }
    }

    /**
     * Select random cloth name which match with list from shop_clothes
     */
    private void setShopResults(String sex, ArrayList<String> clothes, int index){
        ArrayList<String> temp = new ArrayList<>();

        //find all items which has same cloth_small value
        for(int i = 0; i < clothes.size(); i++){
            Cursor c = DB.Select_Brand_Cloth_Name(sex, clothes.get(i));

            //loop through the result set
            while (c.moveToNext()) {
                temp.add(c.getString(0));
            }
        }

        //shuffle the list and get random result
        Collections.shuffle(temp);
        if(temp.size() > 0){shop_result[index] = (temp.get(0));}

    }

    /**
     * Select actual shop clothes from table & set umbrella
     */
    public String[] selectShopClothes(String sex){
        setClothes();
        setShopResults(sex, top, 0);
        checkOnePiece(shop_result[0]);
        setShopResults(sex, bottom, 1);
        setShopResults(sex, outer, 2);
        setShopResults(sex, etc, 3);

        for(int i = 0; i < etc.size(); i++){
            String temp = etc.get(i);
            if(temp.equals("우산")){
                shop_result[4] = "챙기세요!";
            }
        }

        Log.d( "옷검색확인" , "상의 : " + shop_result[0]);
        Log.d( "옷검색확인" ,""+returnURL(sex, shop_result[0]));
        Log.d( "옷검색확인" , "하의 : " + shop_result[1]);
        Log.d( "옷검색확인" ,""+returnURL(sex, shop_result[1]));
        Log.d( "옷검색확인" , "아우터 : " + shop_result[2]);
        Log.d( "옷검색확인" ,""+returnURL(sex, shop_result[2]));
        Log.d( "옷검색확인" , "기타 : " + shop_result[3]);
        Log.d( "옷검색확인" ,""+returnURL(sex, shop_result[3]));
        Log.d( "옷검색확인" , "우산은 : " + shop_result[4]);

        return shop_result;

    }

    public String[] selectShopClothesUrl(String sex){
        String[] url = {returnURL(sex, shop_result[0]),returnURL(sex, shop_result[1]),returnURL(sex, shop_result[2]),returnURL(sex, shop_result[3]),returnURL(sex, shop_result[4])};
        return url;
    }

    /**
     * get url address info of shop cloth
     */
    private String returnURL(String sex, String cloth_name){

        Cursor c = DB.Select_Brand_Cloth_Url(sex, cloth_name);
        //loop through the result set
        while (c.moveToNext()) {
            return(c.getString(0));
        }
        return "";
    }
    /**
     * Select random cloth name which match with list from user_clothes
     */
    private void setUserResults(String user_id, ArrayList<String> clothes, int index){
        ArrayList<String> temp = new ArrayList<>();

        //find all items which has same cloth_small value
        for(int i = 0; i < clothes.size(); i++){

            Cursor c = DB.Select_User_Cloth_Name(user_id, clothes.get(i));

            //loop through the result set
            while (c.moveToNext()) {
                temp.add(c.getString(0));
            }

        }

        //shuffle the list and get random result
        Collections.shuffle(temp);
        if(temp.size() > 0){user_result[index] = (temp.get(0));}

    }

    /**
     * Select actual user clothes from table & set umbrella
     */
    public String[] selectUserClothes(String user_id){
        setClothes();
        setUserResults(user_id, top, 0);
        checkOnePiece(user_result[0]);
        setUserResults(user_id, bottom, 1);
        setUserResults(user_id, outer, 2);
        setUserResults(user_id, etc, 3);

        for(int i = 0; i < etc.size(); i++){
            String temp = etc.get(i);
            if(temp.equals("우산")){
                user_result[4] = "챙기세요!";
            }
        }

        Log.d( "옷검색확인" , "상의 : " + user_result[0]);
        Log.d( "옷검색확인" , "하의 : " + user_result[1]);
        Log.d( "옷검색확인" , "아우터 : " + user_result[2]);
        Log.d( "옷검색확인" , "기타 : " + user_result[3]);
        Log.d( "옷검색확인" , "우산은 : " + user_result[4]);

        return user_result;
    }

    private void checkOnePiece(String cloth_name){
        Cursor c = DB.Select_Brand_Cloth_Onepiece(cloth_name);

        //loop through the result set
        while (c.moveToNext()) {
            if(c.getString(0).equals("원피스")){
                bottom.clear();
            }
        }
    }

}
