package com.example.expenseflow.Entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.expenseflow.Enum.EntryType;

import java.math.BigDecimal;

@Entity(
        foreignKeys = @ForeignKey(
                entity = Transaction.class,
                parentColumns = "id",
                childColumns = "transactionId",
                onDelete = ForeignKey.SET_NULL
        ))
public class JournalLine {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    private Long transactionId;
    private Long accountId;

    private EntryType entryType;

    private String amount;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public EntryType getEntryType() {
        return entryType;
    }

    public void setEntryType(EntryType entryType) {
        this.entryType = entryType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
