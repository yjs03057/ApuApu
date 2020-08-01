package org.androidtown.dbproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.androidtown.dbproject.Object.Cloth;
import org.androidtown.dbproject.Object.User;
import org.androidtown.dbproject.Object.Weather;

import java.util.ArrayList;

public class ConnectDB extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "apuapu.db";

    // 안드로이드에서 SQLite 데이터 베이스를 쉽게 사용할 수 있도록 도와주는 클래스
    public ConnectDB(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 최초에 DB 가 없을경우, DB생성을 위해 호출됨.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table mytable(id integer primary key autoincrement, name text);";
        db.execSQL(sql);
    }

    //DB 버전이 바뀌었을때 호출되는 메소드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table mytable;";
        db.execSQL(sql);
        onCreate(db);
    }

    //오늘날씨 가져오기
    public Weather Select_Weather(String monthly) {
        Weather weather = null;
        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("select * from CLIMATE where DATE = \'%s\';",monthly);
        Cursor c = DB.rawQuery(sql, null);
        while (c.moveToNext()) {
            weather = new Weather(c.getString(0),c.getString(1),
                    c.getInt(2),c.getInt(3),
                    c.getDouble(4),c.getDouble(5));
        }

        return weather;
    }

    //일치하는 유저 찾기 : Login
    public User Select_User(String id, String pw) {
        User user = null;
        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("select * from user where id = \'%s\' and password = \'%s\';",id,pw);

        Cursor c = DB.rawQuery(sql, null);
        while (c.moveToNext()) {
            user = new User(c.getString(0),c.getString(2));
        }

        return user;
    }

    //유저의 옷 찾기
    public ArrayList<Cloth> Select_User_Cloth(String id) {
        ArrayList<Cloth> cloth_list = new ArrayList<Cloth>();
        Cloth cloth;
        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("select u.cloth_name, m.cloth_upper, u.cloth_small from match m, user_clothes u where m.cloth_small = u.cloth_small and u.user_id = \'%s\' ;",id);
        Cursor c = DB.rawQuery(sql, null);
        while (c.moveToNext()) {
            cloth = new Cloth(c.getString(0),c.getString(1),c.getString(2));
            cloth_list.add(cloth);
        }

        return cloth_list;
    }

    //유저의 옷 이름에 따라 찾기
    public Cloth Select_User_Cloth_Name(String name) {

        Cloth cloth =null;
        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("select u.cloth_name, m.cloth_upper, u.cloth_small from match m, user_clothes u where m.cloth_small = u.cloth_small and u.user_id = \'%s\' and u.cloth_name = \'%s\' ;",User.MainUser_Id, name);
        Cursor c = DB.rawQuery(sql, null);
        while (c.moveToNext()) {
            cloth = new Cloth(c.getString(0),c.getString(1),c.getString(2));
        }

        return cloth;
    }

    //유저의 옷 카테고리에 따라 찾기
    public ArrayList<Cloth> Select_User_Cloth(String id, String main) {
        ArrayList<Cloth> cloth_list = new ArrayList<Cloth>();
        Cloth cloth;
        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("select u.cloth_name, m.cloth_upper, u.cloth_small from match m, user_clothes u where m.cloth_small = u.cloth_small and u.user_id = \'%s\' and m.cloth_upper = \'%s\' ;",id,main);
        Cursor c = DB.rawQuery(sql, null);
        while (c.moveToNext()) {
            cloth = new Cloth(c.getString(0),c.getString(1),c.getString(2));
            cloth_list.add(cloth);
        }

        return cloth_list;
    }

    //유저의 옷 서브카테고리에 따라 찾기
    public Cursor Select_User_Cloth_Name(String id, String sub) {

        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("SELECT CLOTH_NAME FROM USER_CLOTHES WHERE CLOTH_SMALL = \'%s\' AND USER_ID = \'%s\';",sub,id);
        Cursor c = DB.rawQuery(sql, null);

        return c;
    }


    //유저의 옷 이름에 따라 찾기
    public Cloth Select_Brand_Cloth_Name(String name) {

        Cloth cloth =null;
        SQLiteDatabase DB = this.getWritableDatabase();

        String sql;
        if(User.MainUser_Sex.equals("남자")){
            sql = String.format("select s.cloth_name, m.cloth_upper, s.cloth_small from SHOP_CLOTHES_MAN s , MATCH m where m.cloth_small = s.cloth_small and s.cloth_name = \'%s\' ;",name);
        }else{
            sql = String.format("select s.cloth_name, m.cloth_upper, s.cloth_small from SHOP_CLOTHES_WOMAN s , MATCH m where m.cloth_small = s.cloth_small and s.cloth_name = \'%s\' ;",name);
        }

        Cursor c = DB.rawQuery(sql, null);
        while (c.moveToNext()) {
            cloth = new Cloth(c.getString(0),c.getString(1),c.getString(2));
        }

        return cloth;
    }

    //브랜드 옷이름 찾기
    public Cursor Select_Brand_Cloth_Name(String sex, String cloth_small) {


        SQLiteDatabase DB = this.getWritableDatabase();
        String sql;
        if(sex.equals("남자")){
            sql = String.format("SELECT CLOTH_NAME FROM SHOP_CLOTHES_MAN WHERE CLOTH_SMALL = \'%s\'",cloth_small);
        }else{
            sql = String.format("SELECT CLOTH_NAME FROM SHOP_CLOTHES_WOMAN WHERE CLOTH_SMALL = \'%s\'",cloth_small);
        }

        Cursor c = DB.rawQuery(sql, null);

        return c;
    }

    //브랜드 옷URL 찾기
    public Cursor Select_Brand_Cloth_Url(String sex, String cloth_name) {


        SQLiteDatabase DB = this.getWritableDatabase();
        String sql;
        if(sex.equals("남자")){
            sql = String.format("SELECT PRODUCT_ADDRESS FROM SHOP_CLOTHES_MAN WHERE CLOTH_NAME = \'%s\'",cloth_name);
        }else{
            sql = String.format("SELECT PRODUCT_ADDRESS FROM SHOP_CLOTHES_WOMAN WHERE CLOTH_NAME = \'%s\'",cloth_name);
        }

        Cursor c = DB.rawQuery(sql, null);

        return c;
    }

    //원피스 유무 확인
    public Cursor Select_Brand_Cloth_Onepiece(String cloth_name) {


        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("SELECT PRODUCT_ADDRESS FROM SHOP_CLOTHES_WOMAN WHERE CLOTH_NAME = \'%s\'",cloth_name);

        Cursor c = DB.rawQuery(sql, null);

        return c;
    }

    //추천 형식 가져오기
    public Cursor Select_Recommendation(int r_case) {

        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("SELECT * FROM RECOMMENDATION WHERE R_CASE = %d",r_case);

        Cursor c = DB.rawQuery(sql, null);
        return c;
    }

    //옷 대분류 가져오기
    public Cursor Select_Match(String cloth_small) {

        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("SELECT CLOTH_UPPER FROM MATCH WHERE CLOTH_SMALL = \'%s\'",cloth_small);

        Cursor c = DB.rawQuery(sql, null);
        return c;
    }



    //Weather DB에 집어넣기
    public void Insert_Weather(Weather weather) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String date = weather.getDate();
        String name  = weather.getName();
        double tmax = weather.getTMax();
        double tmin = weather.getTMin();
        double wind = weather.getWind();
        double rain = weather.getRain();
        int r_case = weather.getR_case();

        String sql = String.format("insert into CLIMATE values(\'%s\',\'%s\',%f,%f,%f,%f,%d);",date,name,tmax,tmin,wind,rain,r_case);
        Log.d("쿼리확인", sql);

        try{
            DB.execSQL(sql);
        }
        catch (Exception e){
            Log.d("에러발생","insert에러");
        }
    }


    //User DB에 집어넣기
    public void Insert_User(String id, String pw, String sex) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("insert into USER (ID,PASSWORD,SEX) values(\"%s\",\"%s\",\"%s\");",id,pw,sex);

        try{
            DB.execSQL(sql);
        }
        catch (Exception e){
            Log.d("에러발생","insert에러");
        }
    }

    //User_cloth DB에 집어넣기
    public void Insert_User_Cloth(String name, String sub) {

        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("insert into USER_CLOTHES (CLOTH_NAME, CLOTH_SMALL, USER_ID) values(\"%s\",\"%s\",\"%s\");",name, sub, User.MainUser_Id);

        try{
            DB.execSQL(sql);
        }
        catch (Exception e){
            Log.d("에러발생","insert에러");
        }
    }

    public void Delete_User(String id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("delete from user where id=\"%s\";",id);
        DB.execSQL(sql);
    }

    public void Delete_User_Cloth(String cloth_name) {
        SQLiteDatabase DB = this.getWritableDatabase();
        String sql = String.format("delete from user_clothes where user_id =\"%s\" and cloth_name =\"%s\";",User.MainUser_Id,cloth_name);
        DB.execSQL(sql);
    }

//    void delete() {
//        String sql = "delete from mytable where id=%d;";
//
//        Cursor c = db.rawQuery("select * from mytable;", null);
//        while (c.moveToNext()) {
//            int id = c.getInt(0);
//            String name = c.getString(1);
//            Log.d(tag, "id:" + id + ",name:" + name);
//            db.execSQL(String.format(sql, id));
//        }
//
//        Log.d(tag, "delete 완료");
//    }
//
//    void update() {
//        db.execSQL("update mytable set name='Park' where id=5;");
//        Log.d(tag, "update 완료");
//    }



}
