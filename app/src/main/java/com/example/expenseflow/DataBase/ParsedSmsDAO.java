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
    List<ParsedSms> getPendingSms(String conversionStatus);

    @Query("SELECT * FROM ParsedSms where id = :id")
    ParsedSms getParsedSms(Long id);

    @Query("UPDATE parsedsms set conversionStatus = :conversionStatus where id = :id")
    void updateConversionStatus(String conversionStatus, Long id);

    @Query("SELECT * from parsedsms where counterparty = :counterparty AND smsType = :smsType AND conversionStatus = :conversionStatus")
    List<ParsedSms> searchBasedOnMpesaAssignmentRule(String counterparty, String smsType, String conversionStatus);

}
