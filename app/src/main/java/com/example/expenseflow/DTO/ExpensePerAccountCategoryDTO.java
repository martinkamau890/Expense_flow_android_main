package com.example.expenseflow.DTO;

import java.math.BigDecimal;

public class ExpensePerAccountCategoryDTO {

    private String accountCategory;
    private BigDecimal amount;

    public String getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(String accountCategory) {
        this.accountCategory = accountCategory;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
