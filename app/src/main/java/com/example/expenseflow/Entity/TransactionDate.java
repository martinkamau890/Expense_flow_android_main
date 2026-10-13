package com.example.expenseflow.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class TransactionDate {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String regDate;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
