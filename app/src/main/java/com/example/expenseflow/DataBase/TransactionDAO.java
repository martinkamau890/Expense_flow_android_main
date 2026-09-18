package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expenseflow.Entity.Transaction;
import com.example.expenseflow.Enum.SyncStatus;

import java.util.List;

@Dao
public interface TransactionDAO {

    @Insert
    long insert(Transaction transaction);

    @Query("SELECT * FROM `transaction` where parsedStatus = :syncStatus")
    List<Transaction> getPendingTransactions(SyncStatus syncStatus);

    @Query("UPDATE `transaction` set parsedStatus = :syncStatus where id = :id")
    void updateTransactionStatus(SyncStatus syncStatus, Long id);
}
