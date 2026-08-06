package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expenseflow.Entity.ParsedSms;

import java.util.List;

@Dao
public interface ParsedSmsDAO {

    @Insert
    void insert(ParsedSms sms);

    @Query("SELECT * FROM ParsedSms WHERE conversionStatus = :conversionStatus ORDER BY transactionTime ASC")
    List<ParsedSms> getAll(String conversionStatus);

    @Query("SELECT * FROM ParsedSms where id = :id")
    ParsedSms getParsedSms(Long id);

}
