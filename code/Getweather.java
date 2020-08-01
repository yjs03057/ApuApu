/**
 * get weather information from weather api
 * insert data to database at Algorithm after choose r_case
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Getweather extends InsertData {
    private String result3days = "";
    private String resultSum = "";
    private JSONObject daysObj;
    private JSONObject summaryObj;
    private  JSONObject todayObject;
    private JSONObject fcstObj;

    protected String date;
    protected String weather_type;
    protected double max_temp;
    protected double min_temp;
    protected double wind_velocity;
    protected double precipitation;

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
            bf1 = new BufferedReader(new InputStreamReader(url1.openStream()));
            bf2 = new BufferedReader(new InputStreamReader(url2.openStream()));

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
        JSONParser jsonParser = new JSONParser();
        try {
            daysObj = (JSONObject) jsonParser.parse(result3days);
            daysObj = (JSONObject) daysObj.get("weather");
            JSONArray weather3days = (JSONArray) daysObj.get("forecast3days");
            daysObj = (JSONObject) weather3days.get(0);
            fcstObj = (JSONObject) daysObj.get("fcst3hour");

            summaryObj = (JSONObject) jsonParser.parse(resultSum);
            summaryObj = (JSONObject) summaryObj.get("weather");
            JSONArray sumweather = (JSONArray) summaryObj.get("summary");
            summaryObj = (JSONObject) sumweather.get(0);
            todayObject = (JSONObject) summaryObj.get("today");


        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void setLocation(){
        JSONObject gridObj = (JSONObject) daysObj.get("grid");
        System.out.println("위치 : "+gridObj.get("city")+" "+gridObj.get("county"));
    }

    private void setDate(){
        Date Today = new Date();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
        date = today.format(Today);
        System.out.println("날짜 : "+date);
    }

    private void setWeatherType(){
        JSONObject statusObj = (JSONObject) todayObject.get("sky");
        System.out.println("날씨 : "+statusObj.get("name"));
        weather_type = statusObj.get("name").toString();
    }

    private void setTemp(){
        JSONObject todaytemp = (JSONObject) todayObject.get("temperature");

        System.out.println("최고기온 : "+ todaytemp.get("tmax"));
        System.out.println("최저기온 : "+ todaytemp.get("tmin"));

        max_temp = Double.parseDouble(todaytemp.get("tmax").toString());
        min_temp = Double.parseDouble(todaytemp.get("tmin").toString());
    }

    private void setWind(){
        JSONObject windObj = (JSONObject) fcstObj.get("wind");
        Set windKeys = windObj.keySet();
        List windList = new ArrayList<String>(windKeys);

        for(Iterator<String> it = windList.iterator(); it.hasNext();){
            String value = it.next();
            if(value.startsWith("wdir")) {
                it.remove();
            }
        }

        Collections.sort(windList);
        List todayWind = new ArrayList<Float>();

        int hour = 4;
        for(int i=0;i<22;i++){
            hour = hour + 3;
            String s = String.valueOf(hour);
            s = "wspd" + s + "hour";
            if(hour<=24){todayWind.add(windObj.get(s));}
        }

        System.out.println("풍속 : " + Collections.max(todayWind));
        wind_velocity = Double.parseDouble(Collections.max(todayWind).toString());
    }

    private void setPrec(){
        JSONObject precObj = (JSONObject) fcstObj.get("precipitation");
        Set precKeys = precObj.keySet();
        List precList = new ArrayList<String>(precKeys);

        Collections.sort(precList);
        List todayPrec = new ArrayList<Float>();
        List todayType = new ArrayList<Float>();

        int hour = 4;
        String type;
        String prob;

        for(int i=0;i<22;i++){
            hour = hour + 3;
            String s = String.valueOf(hour);
            type = "type" + s + "hour";
            prob = "prob" + s + "hour";

            if(hour<=24){
                todayType.add(precObj.get(type));
                todayPrec.add(precObj.get(prob));
            }
        }

        System.out.println("강수량 : " + Collections.max(todayType)+" / "+Collections.max(todayPrec));
        precipitation = Double.parseDouble(Collections.max(todayPrec).toString());
    }

    public void Run(){
        getInfo();
        createObj();
        setLocation();
        setDate();
        setWeatherType();
        setTemp();
        setWind();
        setPrec();
    }
}