package com.example.expenseflow.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.expenseflow.Enum.AccountType;

@Entity(
        foreignKeys = @ForeignKey(
                entity = AccountCategory.class,
                parentColumns = "id",
                childColumns = "accountCategory",
                onDelete = ForeignKey.SET_NULL
        )
)
public class UserAccount {

    @PrimaryKey
    private Long id;

    private String name;

    private String accountType;

    private Long accountCategory;

    private String currencyCode;

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

    public Long getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(Long accountCategory) {
        this.accountCategory = accountCategory;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
