package com.example.loving_essentials.Domain.Entity;

public class Category {
    private int Id;
    private String Name;
    public Category(){
    }
    public Category(int Id, String Name){
        this.Id = Id;
        this.Name = Name;
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
}
