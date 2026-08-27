package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.expenseflow.Entity.MpesaAssignmentRule;

import java.util.List;

@Dao
public interface MpesaAssignmentRuleDAO {
    @Insert
    void insert(MpesaAssignmentRule sms);

    @Query("SELECT * from MpesaAssignmentRule")
    List<MpesaAssignmentRule> getAll();
}
