package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expenseflow.Entity.JournalLine;

import java.util.List;

@Dao
public interface JournalLineDAO {
    @Insert
    void insert(JournalLine journalLine);

    @Query("SELECT * from journalline where transactionId = :id")
    List<JournalLine> getPendingJournalLines(long id);
}
