package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;

import com.example.expenseflow.Entity.JournalLine;

@Dao
public interface JournalLineDAO {
    @Insert
    void insert(JournalLine journalLine);
}
