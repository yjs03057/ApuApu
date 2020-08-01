package org.androidtown.dbproject.Object;

public class User {

    public static String MainUser_Id;
    public static String MainUser_Sex;

    public static void setMainUser(User user){
        MainUser_Id = user.getId();
        MainUser_Sex = user.getSex();
    }

    private String Id;
    private String Sex;

    public User(String id, String sex){
        this.Id = id;
        this.Sex = sex;
    }

    public String getId() {
        return Id;
    }

    public String getSex() {
        return Sex;
    }

}
