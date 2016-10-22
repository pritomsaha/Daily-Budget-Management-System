package com.example.akash.homebudgetmanagement;

/**
 * Created by Akash on 6/9/2015.
 */
public class AccountModel {

    private String accountName;
    private String openingBalance;
    private String currentBalance;
    private String notes;

    public AccountModel(String accountName,String openingBalance,String currentBalance,String notes){

        this.accountName=accountName;
        this.openingBalance=openingBalance;
        this.currentBalance=currentBalance;
        this.notes=notes;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
