package com.example.loving_essentials.Domain.Entity;

public class User {
    private int Id;
    private String Name;
    private String Email;
    private String Phone;
    private int Role;
    private String Password;

    public User() {}

    public User(int id, String name, String email, String phone, int role, String password) {
        Id = id;
        Name = name;
        Email = email;
        Phone = phone;
        Role = role;
        Password = password;
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

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int role) {
        Role = role;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
