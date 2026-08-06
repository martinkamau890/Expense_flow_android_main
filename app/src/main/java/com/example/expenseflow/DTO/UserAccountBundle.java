package com.example.expenseflow.DTO;

import com.example.expenseflow.Entity.AccountCategory;

import java.util.List;

public class UserAccountBundle {
    public List<UserAccountDTO> userAccountDTO;
    public List<AccountCategory> accountCategory;

    public List<UserAccountDTO> getUserAccountDTO() {
        return userAccountDTO;
    }

    public void setUserAccountDTO(List<UserAccountDTO> userAccountDTO) {
        this.userAccountDTO = userAccountDTO;
    }

    public List<AccountCategory> getAccountCategory() {
        return accountCategory;
    }

    public void setAccountCategory(List<AccountCategory> accountCategory) {
        this.accountCategory = accountCategory;
    }
}
