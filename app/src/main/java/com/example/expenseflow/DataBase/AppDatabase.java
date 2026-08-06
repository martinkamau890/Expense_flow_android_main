package com.example.expenseflow.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.expenseflow.Entity.AccountCategory;
import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Entity.RawSms;
import com.example.expenseflow.Entity.UserAccount;

@Database(entities = {RawSms.class, ParsedSms.class, AccountCategory.class, UserAccount.class}, version = 3)
public abstract class AppDatabase extends RoomDatabase {

    public abstract RawSmsDAO rawSmsDAO();

    public abstract ParsedSmsDAO parsedSmsDAO();

    public abstract UserAccountDAO userAccountDAO();

    public abstract AccountCategoryDAO accountCategoryDAO();

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {

        if(instance == null) {

            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    AppDatabase.class,
                    "Expense_Flow.db"
            ).build();
        }

        return instance;
    }
}

