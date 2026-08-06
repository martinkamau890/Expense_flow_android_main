package com.example.expenseflow.DTO;

import com.example.expenseflow.Enum.EntryType;

import java.math.BigDecimal;

public class JournalLineDTO {

    private Long accountId;

    private EntryType entryType;

    private BigDecimal amount;

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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
