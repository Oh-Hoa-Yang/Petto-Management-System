package com.example.pettomanagementsystem;

public class ContactModel {
    String name;
    String phoneNo;
    String key;

    public ContactModel() {}
    public ContactModel(String name, String phoneNo, String key) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getKey() {
        return key;
    }
}
