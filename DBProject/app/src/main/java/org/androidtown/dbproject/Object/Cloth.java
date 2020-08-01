package org.androidtown.dbproject.Object;

public class Cloth
{
    private String name;
    private String main_category;
    private String sub_category;

    public Cloth(String name, String main_category, String sub_category)
    {
        this.name = name;
        this.main_category = main_category;
        this.sub_category = sub_category;
    }
    public void setName(String name)
    {
        this.name = name;
    }
    public void setMain_category(String main_category) { this.main_category = main_category;}
    public void setSub_category(String sub_category) { this.sub_category = sub_category;}
    public String getName()
    {
        return name;
    }
    public String getMain_category()
    {
        return main_category;
    }
    public String getSub_category() { return sub_category; }
}