package com.example.donation;

class RequestData{
    private String title,city,description,category,name,number,id,locality;
    RequestData(String title,String description,String category,String name,String number,String id,String locality,String city){
        this.title = title;
        this.description = description;
        this.category = category;
        this.name = name;
        this.number = number;
        this.id = id;
        this.locality = locality;
        this.city = city;
    }

    RequestData(){
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLocality() {
        return locality;
    }

    public String getCity() {
        return city;
    }
}

