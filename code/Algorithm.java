/**
 * set case by algorithm
 *
 * case 1 : 민소매, 반팔, 반바지, 원피스, 치마
 * case 2 : 반팔, 셔츠, 블라우스, 원피스, 반바지, 면바지, 슬랙스, 치마
 * case 3 : 가디건, 블라우스, 긴팔, 원피스, 면바지, 청바지, 슬랙스, 치마
 * case 4 : 가디건, 후드집업, 니트, 맨투맨, 면바지, 청바지
 * case 5 : 자켓, 후드집업, 가디건, 니트, 청바지, 면바지
 * case 6 : 자켓, 환절기 코트, 니트, 청바지
 * case 7 : 코트, 자켓, 니트, 청바지
 * case 8 : case 7 + 패딩, 헤비 아우터, 목도리
 *
 *
 * case 11, 12 : case 1 or case 2 + case 3's 아우터
 * case 13, 14 : case 3 or case 4 + case 5's 아우터
 * case 15, 16 : case 5 or case 6 + case 7's 아우터
 *
 * 비가 내리는 경우 : original case + 100
 *
 **/


public class Algorithm extends Getweather {
    private String date;
    private String weather_type;
    private double max_temp;
    private double min_temp;
    private double wind_velocity;
    private double precipitation;
    private int r_case;
    boolean rain_condition;

    public Algorithm(){
        Getweather getweather = new Getweather();
        getweather.Run();

        this.date = getweather.date;
        this.weather_type = getweather.weather_type;
        this.max_temp = getweather.max_temp;
        this.min_temp = getweather.min_temp;
        this.wind_velocity = getweather.wind_velocity;
        this.precipitation = getweather.precipitation;
    }

    //insert whole data to database
    public void insertData(){
        setR_case();
        InsertData insertData = new InsertData();
        insertData.insert(date, weather_type, max_temp, min_temp, wind_velocity, precipitation, r_case);
    }

    private void setR_case(){
        firstCondition();

        if(secondCondition() || thirdCondition()){
            if (r_case == 1 || r_case == 2){ r_case += 10;}
            else if (r_case == 3 || r_case == 4){ r_case += 10;}
            else if (r_case == 5 || r_case == 6){ r_case += 10;}
        }

        if(lastCondition()){r_case += 100;}

    }

    //set basic case with max_temp
    private void firstCondition(){
        if (max_temp >= 28){r_case = 1;}
        else if (max_temp >= 23 && 27 >= max_temp){r_case = 2;}
        else if (max_temp >= 22 && 20 >= max_temp){r_case = 3;}
        else if (max_temp >= 19 && 17 >= max_temp){r_case = 4;}
        else if (max_temp >= 16 && 12 >= max_temp){r_case = 5;}
        else if (max_temp >= 11 && 9 >= max_temp){r_case = 6;}
        else if (max_temp >= 5 && 8 >= max_temp){r_case = 7;}
        else {r_case = 8;}
    }

    //set advanced case with daytime difference
    private boolean secondCondition(){
        if(max_temp - min_temp >=8){return true;}
        else if(min_temp < 22){return true;}
        return false;
    }

    //set advanced case with wind_velocity
    private boolean thirdCondition(){
        return(wind_velocity >= 10.0 );
    }

    //set advanced case with precipitation
    private  boolean lastCondition(){
        setrainCondtion();
        if (rain_condition){
            return true;
        }
        else return (precipitation >=20.0);
    }

    //set is the weather : rain or snow
    private void setrainCondtion(){
        if(weather_type.equals("구름많고 비") || weather_type.equals("구름많고 눈") ||
                weather_type.equals("구름많고 비 또는 눈") || weather_type.equals("흐리고 비") ||
                weather_type.equals("흐리고 눈") || weather_type.equals("흐리고 비 또는 눈")||
                weather_type.equals("뇌우/비")|| weather_type.equals("뇌우/눈")||
                weather_type.equals("뇌우/비 또는 눈")){rain_condition = true;}
        else {rain_condition = false;}
    }
}
