package com.example.expenseflow.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.expenseflow.Enum.TransactionType;

@Entity
public class MpesaAssignmentRule {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String recipient;
    private String smsType;

    private TransactionType transactionType;

    private long debitAccount;

    private long creditAccount;

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSmsType() {
        return smsType;
    }

    public void setSmsType(String smsType) {
        this.smsType = smsType;
    }

    public long getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(long debitAccount) {
        this.debitAccount = debitAccount;
    }

    public long getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(long creditAccount) {
        this.creditAccount = creditAccount;
    }
}
