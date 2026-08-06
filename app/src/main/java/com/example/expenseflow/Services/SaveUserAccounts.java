package com.example.expenseflow.Services;

import android.content.Context;
import android.widget.Toast;

import com.example.expenseflow.DTO.UserAccountBundle;
import com.example.expenseflow.DTO.UserAccountDTO;
import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.AccountCategory;
import com.example.expenseflow.Entity.UserAccount;
import com.example.expenseflow.api.ApiService;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveUserAccounts {
    public void saveUserAccountBundle(ApiService apiService, Context context){
        Call<UserAccountBundle> call = apiService.loadUserAccounts();

        call.enqueue(new Callback<UserAccountBundle>() {
            @Override
            public void onResponse(Call<UserAccountBundle> call, Response<UserAccountBundle> response) {
                if (response.isSuccessful() || response.body() != null){

                    ExecutorService executorService = Executors.newSingleThreadExecutor();

                    executorService.execute(() -> {
                        UserAccountBundle userAccountBundle = response.body();
                        saveAccountCategories(userAccountBundle.getAccountCategory(), context);
                        saveUserAccounts(userAccountBundle.getUserAccountDTO(), context);

                        executorService.shutdown();
                    });
                }

            }

            @Override
            public void onFailure(Call<UserAccountBundle> call, Throwable throwable) {
                Toast.makeText(context, "Error loading your account", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void saveUserAccounts(List<UserAccountDTO> userAccounts, Context context){

        AppDatabase database = AppDatabase.getInstance(context);

        for (UserAccountDTO userAccount: userAccounts){

            UserAccount account = new UserAccount();

            account.setId(userAccount.getId());
            account.setName(userAccount.getName());
            account.setAccountCategory(userAccount.getAccountCategory());
            account.setAccountType(userAccount.getAccountType());
            account.setCurrencyCode(userAccount.getCurrencyCode());

            database.userAccountDAO().insert(account);
        }

    }

    public void saveAccountCategories(List<AccountCategory> accountCategories, Context context){
        AppDatabase database = AppDatabase.getInstance(context);
        for (AccountCategory accountCategory: accountCategories){
            database.accountCategoryDAO().insert(accountCategory);
        }
    }
}
