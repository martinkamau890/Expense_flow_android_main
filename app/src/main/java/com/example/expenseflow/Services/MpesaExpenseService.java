package com.example.expenseflow.Services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.expenseflow.Converter.BigDecimalConverter;
import com.example.expenseflow.Converter.ConversionStatusConverter;
import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.DTO.JournalLineDTO;
import com.example.expenseflow.DTO.TransactionDTO;
import com.example.expenseflow.Entity.JournalLine;
import com.example.expenseflow.DTO.TransactionResponseDTO;
import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Entity.UserAccount;
import com.example.expenseflow.Enum.ConversionStatus;
import com.example.expenseflow.Enum.EntryType;
import com.example.expenseflow.Enum.TransactionType;
import com.example.expenseflow.api.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MpesaExpenseService {

    private ParsedSms parsedSms;
    private UserAccount userAccount;
    private Context context;
    private ApiService apiService;

    private AppDatabase appDatabase = AppDatabase.getInstance(context);

    public MpesaExpenseService(ParsedSms parsedSms, UserAccount userAccount, Context context, ApiService apiService) {
        this.parsedSms = parsedSms;
        this.userAccount = userAccount;
        this.apiService = apiService;
        this.context = context;
    }

    public List<JournalLineDTO> getJournalLines(){
        List<JournalLineDTO> journalLine = new ArrayList<>();

        JournalLineDTO debitJournalLine = new JournalLineDTO();

        debitJournalLine.setAmount(BigDecimalConverter.toBigDecimal(parsedSms.getTransactionAmount()));
        debitJournalLine.setAccountId(userAccount.getId());
        debitJournalLine.setEntryType(EntryType.DEBIT);

        JournalLineDTO creditJournalLine = new JournalLineDTO();

        creditJournalLine.setAmount(BigDecimalConverter.toBigDecimal(parsedSms.getTransactionAmount()));
        creditJournalLine.setEntryType(EntryType.CREDIT);
        creditJournalLine.setAccountId(1L);

        journalLine.add(debitJournalLine);
        journalLine.add(creditJournalLine);


        return journalLine;
    }

    public TransactionDTO getTransactionDTO(){

        TransactionDTO transaction = new TransactionDTO();

        transaction.setTransactionDate(DateTimeConverter.toLocalDateTime(parsedSms.getTransactionTime()).toString());
        transaction.setTransactionType(TransactionType.EXPENSE.toString());
        transaction.setTransactionEntryDTOList(getJournalLines());
        transaction.setDescription("Amount sent to " + parsedSms.getCounterparty());
        transaction.setRefNumber(parsedSms.getTransactionCode());
        transaction.setCurrencyCode(null);

        Log.d("TRANSACTIONDTO", "This is the transaction " + transaction.getRefNumber());
        return transaction;
    }

    public void createTransaction(TransactionCallback transactionCallback){

        Call<TransactionResponseDTO> call = apiService.createExpense(getTransactionDTO());

        call.enqueue(new Callback<TransactionResponseDTO>() {
            @Override
            public void onResponse(Call<TransactionResponseDTO> call, Response<TransactionResponseDTO> response) {
                if (response.isSuccessful()){
                    updateParsedSms();
                    TransactionResponseDTO message = response.body();
                    transactionCallback.onSuccess();
                    Log.d("TRANSACTIONDTO", message.getResponse());
                    Toast.makeText(context, message.getResponse(), Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("TRANSACTIONDTO", "HTTP Code: " + response.code());
                    try {
                        String responseError = response.errorBody().string();
                        if (responseError.equals("Duplicate value")){
                                updateParsedSms();
                            transactionCallback.onSuccess();
                        }else {
                            transactionCallback.onFailure(response.errorBody().toString());

                            Log.e("TRANSACTIONDTO",
                                    "Error Body: " + response.errorBody().string());
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<TransactionResponseDTO> call, Throwable throwable) {
                Log.e("TRANSACTIONDTO", "Request failed", throwable);

                transactionCallback.onFailure(throwable.getMessage());
                Toast.makeText(context,
                        throwable.getMessage(),
                        Toast.LENGTH_LONG).show();}
        });
    }

    private void updateParsedSms(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(() -> {
            appDatabase.parsedSmsDAO().updateConversionStatus(ConversionStatusConverter.fromConversionStatus(ConversionStatus.PARSED), parsedSms.getId());
        });
    }


}
