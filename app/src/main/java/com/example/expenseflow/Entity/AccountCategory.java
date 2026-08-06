package com.example.expenseflow.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.expenseflow.Enum.AccountType;

@Entity
public class AccountCategory {

    @PrimaryKey
    private Long id;

    private String name;

    private String accountType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
