package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expenseflow.Entity.TransactionDate;

@Dao
public interface TransactionDateDAO {

    @Insert
    void insert(TransactionDate transactionDate);

    @Query("SELECT regDate FROM transactiondate")
    String getRegDate();

}
