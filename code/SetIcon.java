/**
 * Set Icon Algorithm
 * Have to change return value to actual image address
 */

public class SetIcon {
    public String IconAddress(String weather_type){
        //해 아이콘
        if (weather_type.equals("맑음")) {
            return "Climacons.fw 4.JPEG";
        }

        //해랑 구름 같이 있는 아이콘
        if (weather_type.equals("구름조금")) {
            return "Climacons.fw 2.JPEG";
        }

        //구름만 있는 아이콘
        if (weather_type.equals("구름많음")) {
            return "Climacons.fw.JPEG";
        }

        //비 오는 아이콘
        if (weather_type.equals("구름많고 비") ||
                weather_type.equals("구름많고 비 또는 눈") ||
                weather_type.equals("흐리고 비") ||
                weather_type.equals("흐리고 비 또는 눈") ||
                weather_type.equals("뇌우/비") ||
                weather_type.equals("뇌우/비 또는 눈")) {
            return "Climacons.fw 3.JPEG";
        }

        //눈 내리는 아이콘
        if (weather_type.equals("구름많고 눈") ||
                weather_type.equals("흐리고 눈")||
                weather_type.equals("뇌우/눈")) {
            return "Climacons.fw 6.JPEG";
        }

        //바람 표시된 아이콘
        if (weather_type.equals("흐림") ||
            weather_type.equals("흐리고 낙뢰")) {
            return "Climacons.fw 7.JPEG";
        }
        return "";
    }
}
