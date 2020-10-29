package com.example.donation;

import java.util.Map;

public class Upload {
    private String productName;
    private String ImageUrl;
    private String user_name;

    public Upload()
    {
        //default constructor is needed
    }

    //to upload profile image
    public Upload(String imageUrl){
        ImageUrl=imageUrl;
    }
    //to upload donation product
    public Upload(String name,String imageUrl){
        if(name.trim().equals("")){
            name = "No Name";
        }
        productName=name;
        ImageUrl=imageUrl;
    }

    public Upload(Map<String, Object> data){
        productName=data.get("product_name").toString();
        ImageUrl=data.get("imageurl").toString();
        user_name=data.get("name").toString();
    }
    public String getName(){
        return productName;
    }
    public String getUser_name(){
        return user_name;
    }
    public void setName(String name){
        productName=name;
    }
    public String getImageUrl(){
        return ImageUrl;
    }
    public void setImageUrl(String imageUrl){
        ImageUrl=imageUrl;
    }
}
