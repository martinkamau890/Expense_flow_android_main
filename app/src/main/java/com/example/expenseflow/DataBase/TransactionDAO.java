package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.expenseflow.Entity.Transaction;

@Dao
public interface TransactionDAO {

    @Insert
    long insert(Transaction transaction);
}
