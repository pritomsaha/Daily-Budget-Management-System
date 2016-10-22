package com.example.akash.homebudgetmanagement;

/**
 * Created by Akash on 6/8/2015.
 */
public class ContactModel {

    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public ContactModel(String name, String phoneNumber, String email, String address){

        this.name=name;
        this.phoneNumber=phoneNumber;
        this.email=email;
        this.address=address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
