package com.example.donation;

import java.io.Serializable;
import java.util.Map;

public class Upload implements Serializable {
    private String productName;
    private String ImageUrl;
    private String user_name;
    private String prodDescription;
    private String prodCategory;
    private String donation_id;
    private String uid;
    private String donated;
    private String email;

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
        prodCategory=data.get("categories").toString();
        uid=data.get("uid").toString();
        donated=data.get("donated").toString();
        prodDescription=data.get("product_description").toString();
        donation_id=data.get("donation_id").toString();
    }
    public String getName(){
        return productName;
    }
    public String getUser_name(){
        return user_name;
    }
    public String getProdDescription(){return prodDescription;}

    public String getDonated() {
        return donated;
    }

    public String getUid() {
        return uid;
    }

    public String getDonation_id() {
        return donation_id;
    }

    public String getProdCategory() {
        return prodCategory;
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
