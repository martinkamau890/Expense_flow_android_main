package com.example.expenseflow.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long debitAccount;

    private String refNumber;

    private Long creditAccount;

    private Long transactionDate;

    private String description;

    private String amount;



}
