package com.example.expenseflow.DataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.expenseflow.Entity.AccountCategory;
import com.example.expenseflow.Entity.RawSms;

import java.util.List;

@Dao
public interface AccountCategoryDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(AccountCategory accountCategory);

    @Query("SELECT * FROM AccountCategory WHERE accountType = :accountType")
    List<AccountCategory> getAccountCategoryList(String accountType);
}
