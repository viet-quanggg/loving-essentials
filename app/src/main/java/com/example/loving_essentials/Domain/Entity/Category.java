package com.example.loving_essentials.Domain.Entity;

public class Category {
    private int Id;
    private String Name;
    private String imgUrl;
    public Category(){
    }

    public Category(int Id, String Name, String imgUrl){
        this.Id = Id;
        this.Name = Name;
        this.imgUrl = imgUrl;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
