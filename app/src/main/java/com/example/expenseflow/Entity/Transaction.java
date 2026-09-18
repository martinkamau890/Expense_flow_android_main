package com.example.expenseflow.Entity;

import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.expenseflow.Enum.SyncStatus;
import com.example.expenseflow.Enum.TransactionType;

import java.util.List;

@Entity(indices = {
        @Index(value = {"refNumber"}, unique = true)
})
public class Transaction {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String description;

    private String transactionDate;

    private TransactionType transactionType;

    private String currencyCode;

    private String refNumber;

    private SyncStatus parsedStatus;

    public SyncStatus getParsedStatus() {
        return parsedStatus;
    }

    public void setParsedStatus(SyncStatus parsedStatus) {
        this.parsedStatus = parsedStatus;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }
}
