package com.example.akash.homebudgetmanagement;

/**
 * Created by Akash on 6/7/2015.
 */
public class CategoryModel {

    private String date;
    private String category;
    private String name;
    private String amount;

    public CategoryModel(String date, String category, String name, String amount){

        if(category=="") this.category="No category";
        else{
            this.date=date;
            this.category=category;
            this.name=name;
            this.amount=amount;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
