package org.androidtown.dbproject;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import org.androidtown.dbproject.Activity.MainActivity;
import org.androidtown.dbproject.Object.Weather;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class Weather_API {


    private String result3days = "";
    private String resultSum = "";
    private JSONObject daysObj;
    private JSONObject summaryObj;
    private  JSONObject todayObject;
    private JSONObject fcstObj;

    private Weather weather = new Weather();

    private Handler mainHandler;

    public Weather_API(Handler handler) {
        mainHandler = handler;
    }

    public void getWeather(){
        WeatherThread thread = new WeatherThread();
        thread.start();
    }


    class WeatherThread extends Thread {

        @Override
        public void run() {
            Run();
            Message message = mainHandler.obtainMessage();
            message.what = MainActivity.SEND_SUCCESS;
            message.obj = weather;
            mainHandler.sendMessage(message);
        }
    }

    private void getInfo(){
        String version = "1";
        String lat = "37.2482";
        String lon = "127.0762";
        String appKey = "44ff0bad-9e0e-4be6-aed1-cf7f453a996b";

        String url3days = "https://api2.sktelecom.com/weather/forecast/3days?" +
                "+version="+version+"&lat="+lat+"&lon="+lon+"&appKey="+appKey;
        String urlSummary = "https://api2.sktelecom.com/weather/summary?" +
                "+version="+version+"&lat="+lat+"&lon="+lon+"&appKey="+appKey;

        BufferedReader bf1;
        BufferedReader bf2;
        String line;

        try {
            URL url1 = new URL(url3days);
            URL url2 = new URL(urlSummary);

            URLConnection conn1 = url1.openConnection();
            URLConnection conn2 = url2.openConnection();

            bf1 = new BufferedReader(new InputStreamReader(conn1.getInputStream()));
            bf2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));

            while((line = bf1.readLine())!=null){
                result3days = result3days.concat(line);
            }
            while((line = bf2.readLine())!=null){
                resultSum = resultSum.concat(line);
            }

            bf1.close();
            bf2.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void createObj(){

        try {
            JSONObject jsonParser = new JSONObject(result3days);
            daysObj = jsonParser.getJSONObject("weather");
            String forecast3days = daysObj.getString("forecast3days");
            JSONArray weather3days = new JSONArray(forecast3days);
            daysObj = weather3days.getJSONObject(0);
            fcstObj = daysObj.getJSONObject("fcst3hour");

            jsonParser = new JSONObject(resultSum);

            summaryObj = jsonParser.getJSONObject("weather");
            String summary = summaryObj.getString("summary");
            JSONArray sumweather = new JSONArray(summary);
            summaryObj = sumweather.getJSONObject(0);
            todayObject = summaryObj.getJSONObject("today");


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void setLocation(){
        JSONObject gridObj = null;
        try {
            gridObj = daysObj.getJSONObject("grid");
            Log.d("데이터 정보 : ", "위치 : "+gridObj.get("city")+" "+gridObj.get("county"));
            weather.setLocation(gridObj.get("city")+" "+gridObj.get("county"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDate(){

        Date Today = new Date();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd",Locale.KOREA);
        String date = today.format(Today);
        weather.setDate(date);

    }

    private void setWeatherType(){
        JSONObject statusObj = null;
        try {
            statusObj = todayObject.getJSONObject("sky");
            weather.setName((String)statusObj.get("name"));
            Log.d("데이터 정보 : ", "날씨 : "+statusObj.get("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setTemp(){

        JSONObject todaytemp = null;
        try {
            todaytemp = todayObject.getJSONObject("temperature");
            Log.d("데이터 정보 : ", "최고기온 : "+todaytemp.get("tmax"));
            Log.d("데이터 정보 : ", "최저기온 : "+todaytemp.get("tmin"));
            weather.setTMax(todaytemp.getInt("tmax"));
            weather.setTMin(todaytemp.getInt("tmin"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setWind(){
        JSONObject windObj = null;
        try {
            windObj = fcstObj.getJSONObject("wind");
            Iterator windKeys = windObj.keys();

            ArrayList<String> windList = new ArrayList<String>();


            while(windKeys.hasNext())
            {
                String value = windKeys.next().toString();
                if(!value.startsWith("wdir")){
                    windList.add(value);
                }
            }
            Collections.sort(windList);
            ArrayList<Double> todayWind = new ArrayList<Double>();


            int hour = 4;
            for(int i=0;i<22;i++){
                hour = hour + 3;
                String s = String.valueOf(hour);
                s = "wspd" + s + "hour";
                if(hour<=24){todayWind.add(windObj.getDouble(s));}
            }

            weather.setWind(Collections.max(todayWind));
            Log.d("데이터 정보 : ", "풍속 : "+Collections.max(todayWind));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setPrec(){
        JSONObject precObj = null;
        try {
            precObj = fcstObj.getJSONObject("precipitation");

            Iterator precKeys = precObj.keys();
            ArrayList<String> precList = new ArrayList<String>();

            while(precKeys.hasNext())
            {
                String value = precKeys.next().toString();
                precList.add(value);
            }

            ArrayList<Double> todayPrec = new ArrayList<Double>();
            ArrayList<Double> todayType = new ArrayList<Double>();

            int hour = 4;
            String type;
            String prob;

            for(int i=0;i<22;i++){
                hour = hour + 3;
                String s = String.valueOf(hour);
                type = "type" + s + "hour";
                prob = "prob" + s + "hour";

                if(hour<=24){
                    todayType.add(precObj.getDouble(type));
                    todayPrec.add(precObj.getDouble(prob));
                }
            }
            Log.d("데이터 정보 : ", "강수량 : "+Collections.max(todayType)+" / "+Collections.max(todayPrec));

            weather.setWind(Collections.max(todayPrec));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void Run(){
        getInfo();
        createObj();
        setLocation();
        setDate();
        setWeatherType();
        setTemp();
        setWind();
        setPrec();
        weather.setR_case();
    }
}
