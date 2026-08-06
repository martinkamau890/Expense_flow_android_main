package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.expenseflow.Entity.RawSms;
import com.example.expenseflow.Entity.UserAccount;

import java.util.List;

@Dao
public interface UserAccountDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(UserAccount userAccount);

    @Query("SELECT * FROM UserAccount")
    List<UserAccount> getUserAccount();
}
