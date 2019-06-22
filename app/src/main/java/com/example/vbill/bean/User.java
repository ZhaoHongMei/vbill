package com.example.vbill.bean;

public class User {
    private int id;
    private String telephoneNumber;
    private String name;
    private String password;
    private String photopath;
    public User() {
    }

    public User(int id, String name, String password, String photopath) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.photopath = photopath;
    }

    public User(int id, String telephoneNumber, String name, String password, String photopath) {
        this.id = id;
        this.telephoneNumber = telephoneNumber;
        this.name = name;
        this.password = password;
        this.photopath = photopath;
    }

    public User(String telephoneNumber, String name, String password, String photopath) {
        this.telephoneNumber = telephoneNumber;
        this.name = name;
        this.password = password;
        this.photopath = photopath;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }
}
