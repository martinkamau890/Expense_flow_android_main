package com.example.expenseflow.Services;

import android.content.Context;

import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.TransactionDate;

public class TransactionDateService {
    private Context context;
    private AppDatabase appDatabase;



    public TransactionDateService(Context context) {
        this.context = context;
        this.appDatabase = AppDatabase.getInstance(context);
    }

    public void signupDate(TransactionDate transactionDate){
        appDatabase.transactionDateDAO().insert(transactionDate);
    }

    public String getRegDate(){
        return appDatabase.transactionDateDAO().getRegDate();
    }

    public void insert(TransactionDate transactionDate){
        appDatabase.transactionDateDAO().insert(transactionDate);
    }

}
