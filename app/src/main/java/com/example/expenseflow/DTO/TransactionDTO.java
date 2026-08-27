package com.example.expenseflow.DTO;

import com.example.expenseflow.Entity.JournalLine;

import java.util.List;

public class TransactionDTO {
    private String description;

    List<JournalLineDTO> transactionEntryDTOList;

    private String transactionDate;

    private String transactionType;

    private String currencyCode;

    private String refNumber;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<JournalLineDTO> getTransactionEntryDTOList() {
        return transactionEntryDTOList;
    }

    public void setTransactionEntryDTOList(List<JournalLineDTO> transactionEntryDTOList) {
        this.transactionEntryDTOList = transactionEntryDTOList;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
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

