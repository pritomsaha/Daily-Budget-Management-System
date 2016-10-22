package com.example.akash.homebudgetmanagement;

/**
 * Created by Akash on 6/10/2015.
 */
public class Income_ExpenseModel {

    private String date;
    private String time;
    private String amount;
    private String account;
    private String contact;
    private String category;
    private String notes;

    public Income_ExpenseModel(String date, String time, String amount, String account, String contact,
                               String category, String notes){
        this.date=date;
        this.time=time;
        this.amount=amount;
        this.account=account;
        this.contact=contact;
        this.category=category;
        this.notes=notes;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getAmount() {
        return amount;
    }

    public String getAccount() {
        return account;
    }

    public String getContact() {
        return contact;
    }

    public String getCategory() {
        return category;
    }

    public String getNotes() {
        return notes;
    }

    public String getDay(){

        String[] date=this.date.split(" ");
        return date[0];
    }
}
