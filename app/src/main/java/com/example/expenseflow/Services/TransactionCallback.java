package com.example.expenseflow.Services;

public interface TransactionCallback {
    void onSuccess();
    void onFailure(String error);
}
