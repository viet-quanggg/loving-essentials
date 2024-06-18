package com.example.loving_essentials.Domain.Entity;

public class Category {
    private int id;
    private String name;
    public Category(){
    }

    public Category(int Id, String Name){
        this.id = Id;
        this.name = Name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
