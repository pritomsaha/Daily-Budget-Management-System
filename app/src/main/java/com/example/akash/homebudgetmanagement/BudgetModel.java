package com.example.akash.homebudgetmanagement;

/**
 * Created by Akash on 6/12/2015.
 */
public class BudgetModel {

    private String category;
    private String budget_amount;
    private String spent_amount;
    private String left_amount;
    private String notes;

    public BudgetModel(String category,String budget_amount,String spent_amount,
                       String left_amount,String notes){

        this.category=category;
        this.budget_amount=budget_amount;
        this.spent_amount=spent_amount;
        this.left_amount=left_amount;
        this.notes=notes;
    }

    public String getCategory() {
        return category;
    }

    public String getBudget_amount() {
        return budget_amount;
    }

    public String getSpent_amount() {
        return spent_amount;
    }

    public String getLeft_amount() {
        return left_amount;
    }

    public String getNotes() {
        return notes;
    }
}
