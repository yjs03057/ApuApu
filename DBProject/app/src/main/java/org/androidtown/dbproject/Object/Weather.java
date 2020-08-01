package org.androidtown.dbproject.Object;

public class Weather {

    public static String Today_Name = "";

    private String Location;  //위치
    private String Date;    // YYYYMMDD날짜
    private String Name;    // 날씨 이름
    private int TMax;   // 최고온도
    private int TMin;   // 최저온도
    private double Wind;    //풍속
    private double Rain;    //강수확률
    private int R_case; // case

    private Boolean rain_condition;

    public String getLocation() {
        return this.Location;
    }

    public String getDate() {
        return this.Date;
    }

    public String getName() {
        return this.Name;
    }

    public int getTMax() {
        return this.TMax;
    }

    public int getTMin() {
        return this.TMin;
    }

    public double getWind() {
        return this.Wind;
    }

    public double getRain() {
        return this.Rain;
    }

    public int getR_case() {
        return this.R_case;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public void setTMax(int tmax) {
        this.TMax = tmax;
    }

    public void setTMin(int tmin) {
        this.TMin = tmin;
    }

    public void setWind(double wind) {
        this.Wind = wind;
    }

    public void setRain(double rain) {
        this.Rain = rain;
    }


    public Weather() {
        Location = "";
        Date = "";
        Name = "";
        TMax = 0;
        TMin = 0;
        Wind = 0;
        Rain = 0;
        R_case = 0;
    }

    public Weather(String date, String name, int tmax, int tmin, double wind, double rain) {
        Location = "몰랑";
        Date = date;
        Name = name;
        TMax = tmax;
        TMin = tmin;
        Wind = wind;
        Rain = rain;
        setR_case();
    }

    public void setR_case() {
        firstCondition();

        if(secondCondition() || thirdCondition()){
            if (R_case == 1 || R_case == 2){ R_case += 10;}
            else if (R_case == 3 || R_case == 4){ R_case += 10;}
            else if (R_case == 5 || R_case == 6){ R_case += 10;}
        }

        if(lastCondition()){R_case += 100;}
    }

    //set basic case with TMax
    private void firstCondition() {
        if (TMax >= 28) {
            R_case = 1;
        } else if (TMax >= 23) {
            R_case = 2;
        } else if (TMax >= 20) {
            R_case = 3;
        } else if (TMax >= 17) {
            R_case = 4;
        } else if (TMax >= 12) {
            R_case = 5;
        } else if (TMax >= 9) {
            R_case = 6;
        } else if (TMax >= 5) {
            R_case = 7;
        } else {
            R_case = 8;
        }
    }

    //set advanced case with daytime difference
    private boolean secondCondition() {
        if (TMax - TMin >= 8) {
            return true;
        } else if (TMin < 22) {
            return true;
        }
        return false;
    }

    //set advanced case with Wind
    private boolean thirdCondition() {
        return (Wind >= 10.0);
    }

    //set advanced case with precipitation
    private boolean lastCondition() {
        setrainCondtion();
        if (rain_condition) {
            return true;
        } else return (Rain >= 20.0);
    }

    //set is the weather : rain or snow
    private void setrainCondtion() {
        if (Name.equals("구름많고 비") || Name.equals("구름많고 눈") ||
                Name.equals("구름많고 비 또는 눈") || Name.equals("흐리고 비") ||
                Name.equals("흐리고 눈") || Name.equals("흐리고 비 또는 눈") ||
                Name.equals("뇌우/비") || Name.equals("뇌우/눈") ||
                Name.equals("뇌우/비 또는 눈")) {
            rain_condition = true;
        } else {
            rain_condition = false;
        }
    }


}
