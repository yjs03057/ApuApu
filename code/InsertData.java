/**
 * Insert data at Database
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertData {
    /**
     * Connect to the test.db database
     *
     * @return the Connection object
     **/
    private Connection connect(){
        // SQLite connection string
        Connection conn = null;

        try {
            String url = "jdbc:sqlite:C://Users/USER/Desktop/데이터베이스/프로젝트/database/apuapu2.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Insert a new row into the CLIMATE table
     *
     * @param date
     * @param weather_type
     * @param max_temp
     * @param min_temp
     * @param wind_velocity
     * @param precipitation
     * @param r_case
     */
    public void insert(String date, String weather_type,
                       double max_temp, double min_temp, double wind_velocity,
                       double precipitation, int r_case){
        String sql = "INSERT INTO CLIMATE(date, weather_type, max_temp," +
                "min_temp, wind_velocity, precipitation, r_case) " +
                "VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, date);
            pstmt.setString(2, weather_type);
            pstmt.setDouble(3, max_temp);
            pstmt.setDouble(4, min_temp);
            pstmt.setDouble(5, wind_velocity);
            pstmt.setDouble(6, precipitation);
            pstmt.setInt(7, r_case);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Insert a new row into the CLIMATE table
     *
     * @param r_case
     * @param cloth_type
     *
     */
    public void insert_recommendation(int r_case, String cloth_type){
        String sql = "INSERT INTO RECOMMENDATION(r_case, cloth_type) " +
                "VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setInt(1, r_case);
            pstmt.setString(2, cloth_type);
            pstmt.executeUpdate();
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
}
