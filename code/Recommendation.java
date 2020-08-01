/**
 * Recommendation Algorithm
 * set shop result, user result
 * picking small class which match today's case -> pick actual clothes randomly
 * ++ if top is onepiece, than don't pick any clothes to bottom
 */

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

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

    /**
     * Connect to the test.db database
     * @return the Connection object
     **/
    private Connection connect(){
        // SQLite connection string
        Connection conn = null;

        try {
            String url = "jdbc:sqlite:C://Users/USER/Desktop/데이터베이스/프로젝트/database/apuapu2.db";
            conn = DriverManager.getConnection(url);
            //System.out.println("Connection to SQLite has been established.");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     *  Select today's r_case from CLIMATE table
     **/
    private void getType(){
        Date Today = new Date();
        SimpleDateFormat today = new SimpleDateFormat("yyyyMMdd");
        String key = today.format(Today);

        // select query
        String sql = "SELECT R_CASE FROM CLIMATE WHERE DATE = ?";

        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, key);
            ResultSet rs = pstmt.executeQuery();

            //loop through the result set
            while (rs.next()){
                r_case = rs.getInt("R_CASE");
                System.out.println(r_case);
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private void selectCase(){
        //System.out.println("들어왔당!");
        String sql = "SELECT * FROM RECOMMENDATION WHERE R_CASE = ?";

        try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            // set the value
            pstmt.setInt(1, r_case);

            ResultSet rs = pstmt.executeQuery();
            // loop through the result set
            while (rs.next()) {
                r_case = rs.getInt("R_CASE");
                cloth_type = rs.getString("CLOTH_TYPE");
                ref_case = rs.getInt("REF_CASE");

//                System.out.println(rs.getInt("R_CASE") + "\t"
//                + rs.getString("CLOTH_TYPE") + "\t"
//                + rs.getInt("REF_CASE"));
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
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
        if (outer.size() == 0){check_outer = true
        ;}

        //find upper value of clothes
        for(int i=0;i<temp.length;i++){
            String sql = "SELECT CLOTH_UPPER FROM MATCH WHERE CLOTH_SMALL = ?";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)){
                // set the value
                pstmt.setString(1, temp[i]);

                ResultSet rs = pstmt.executeQuery();
                // loop through the result set
                while (rs.next()) {
                    upper = rs.getString("CLOTH_UPPER");
                }
            }catch (SQLException e) {
                System.out.println(e.getMessage());
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

        String sql;
        if(sex == "남자"){
            sql = "SELECT CLOTH_NAME FROM SHOP_CLOTHES_MAN WHERE CLOTH_SMALL = ?";
        }
        else{
            sql = "SELECT CLOTH_NAME FROM SHOP_CLOTHES_WOMAN WHERE CLOTH_SMALL = ?";
        }

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            //find all items which has same cloth_small value
            for(int i = 0; i < clothes.size(); i++){
                pstmt.setString(1, clothes.get(i));
                ResultSet rs = pstmt.executeQuery();
                //loop through the result set
                while (rs.next()){
                    temp.add(rs.getString("CLOTH_NAME"));
                }
            }

            //shuffle the list and get random result
            Collections.shuffle(temp);
            if(temp.size() > 0){shop_result[index] = (temp.get(0));}
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Select actual shop clothes from table & set umbrella
     */
    public void selectShopClothes(String sex){
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

        System.out.println("상의 : " + shop_result[0]);
        System.out.println(returnURL(sex, shop_result[0]));
        System.out.println("하의 : " + shop_result[1]);
        System.out.println(returnURL(sex, shop_result[1]));
        System.out.println("아우터 : " + shop_result[2]);
        System.out.println(returnURL(sex, shop_result[2]));
        System.out.println("기타 : " + shop_result[3]);
        System.out.println(returnURL(sex, shop_result[3]));
        System.out.println("우산은 : " + shop_result[4]);
    }

    /**
     * get url address info of shop cloth
     */
    private String returnURL(String sex, String cloth_name){
        String sql;
        if(sex == "남자"){
            sql = "SELECT PRODUCT_ADDRESS FROM SHOP_CLOTHES_MAN WHERE CLOTH_NAME = ?";
        }
        else{
            sql = "SELECT PRODUCT_ADDRESS FROM SHOP_CLOTHES_WOMAN WHERE CLOTH_NAME = ?";
        }

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, cloth_name);
            ResultSet rs = pstmt.executeQuery();
            //loop through the result set
            while (rs.next()) {
                return(rs.getString("PRODUCT_ADDRESS"));
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return "";
    }
    /**
     * Select random cloth name which match with list from user_clothes
     */
    private void setUserResults(String user_id, ArrayList<String> clothes, int index){
        ArrayList<String> temp = new ArrayList<>();

        String sql = "SELECT CLOTH_NAME FROM USER_CLOTHES WHERE CLOTH_SMALL = ? AND USER_ID = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(2, user_id);
            //find all items which has same cloth_small value
            for(int i = 0; i < clothes.size(); i++){
                pstmt.setString(1, clothes.get(i));
                ResultSet rs = pstmt.executeQuery();
                //loop through the result set
                while (rs.next()){
                    temp.add(rs.getString("CLOTH_NAME"));
                }
            }

            //shuffle the list and get random result
            Collections.shuffle(temp);
            if(temp.size() > 0){user_result[index] = (temp.get(0));}
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Select actual user clothes from table & set umbrella
     */
    public void selectUserClothes(String user_id){
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

        System.out.println("상의 : " + user_result[0]);
        System.out.println("하의 : " + user_result[1]);
        System.out.println("아우터 : " + user_result[2]);
        System.out.println("기타 : " + user_result[3]);
        System.out.println("우산은 : " + user_result[4]);
    }

    private void checkOnePiece(String cloth_name){
        String sql = "SELECT CLOTH_SMALL FROM SHOP_CLOTHES_WOMAN WHERE CLOTH_NAME = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, cloth_name);
            ResultSet rs = pstmt.executeQuery();
            //loop through the result set
            while (rs.next()) {
                if(rs.getString("CLOTH_SMALL").equals("원피스")){
                    bottom.clear();
                }
            }
        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
}
