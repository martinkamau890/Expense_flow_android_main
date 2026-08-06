package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.expenseflow.Entity.RawSms;

import java.util.List;

@Dao
public interface RawSmsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RawSms sms);

    @Query("SELECT * FROM RawSms")
    List<RawSms> findAll();

    @Query("Select * from RawSms where providerSmsId = :id")
    RawSms findByProviderId(Long id);

    @Query("Select * from RawSms where conversionStatus = :conversionStatus ORDER BY smsDate ASC")
    List<RawSms> getPendingSms(String conversionStatus);

    @Query("UPDATE RawSms set conversionStatus = :conversionStatus where id = :id")
    void updateRawSms(String conversionStatus, long id);

}
