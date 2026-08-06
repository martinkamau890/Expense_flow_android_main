package com.example.expenseflow.Services;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.expenseflow.Converter.BigDecimalConverter;
import com.example.expenseflow.Converter.ConversionStatusConverter;
import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.DTO.JournalLineDTO;
import com.example.expenseflow.DTO.TransactionDTO;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MpesaExpenseService {

    private ParsedSms parsedSms;
    private UserAccount userAccount;
    private Context context;
    private ApiService apiService;

    public MpesaExpenseService(ParsedSms parsedSms, UserAccount userAccount, Context context, ApiService apiService) {
        this.parsedSms = parsedSms;
        this.userAccount = userAccount;
        this.apiService = apiService;
        this.context = context;
    }

    public List<JournalLineDTO> getJournalLines(){
        List<JournalLineDTO> journalLineDTO = new ArrayList<>();

        JournalLineDTO debitJournalLine = new JournalLineDTO();

        debitJournalLine.setAmount(BigDecimalConverter.toBigDecimal(parsedSms.getTransactionAmount()));
        debitJournalLine.setAccountId(userAccount.getId());
        debitJournalLine.setEntryType(EntryType.DEBIT);

        JournalLineDTO creditJournalLine = new JournalLineDTO();

        creditJournalLine.setAmount(BigDecimalConverter.toBigDecimal(parsedSms.getTransactionAmount()));
        creditJournalLine.setEntryType(EntryType.CREDIT);
        creditJournalLine.setAccountId(1L);

        journalLineDTO.add(debitJournalLine);
        journalLineDTO.add(creditJournalLine);

        return journalLineDTO;
    }

    public TransactionDTO getTransactionDTO(){

        TransactionDTO transactionDTO = new TransactionDTO();

        transactionDTO.setTransactionDate(DateTimeConverter.toLocalDateTime(parsedSms.getTransactionTime()).toString());
        transactionDTO.setTransactionType(TransactionType.EXPENSE.toString());
        transactionDTO.setTransactionEntryDTOList(getJournalLines());
        transactionDTO.setDescription("Amount sent to " + parsedSms.getCounterparty());
        transactionDTO.setRefNumber(parsedSms.getTransactionCode());
        transactionDTO.setCurrencyCode(null);

        Log.d("TRANSACTIONDTO", "This is the transaction " + transactionDTO.getRefNumber());
        return transactionDTO;
    }

    public void createTransaction(TransactionCallback transactionCallback){

        Call<TransactionResponseDTO> call = apiService.createExpense(getTransactionDTO());

        call.enqueue(new Callback<TransactionResponseDTO>() {
            @Override
            public void onResponse(Call<TransactionResponseDTO> call, Response<TransactionResponseDTO> response) {
                if (response.isSuccessful()){
                    parsedSms.setConversionStatus(ConversionStatusConverter.fromConversionStatus(ConversionStatus.PARSED));
                    TransactionResponseDTO message = response.body();
                    transactionCallback.onSuccess();
                    Log.d("TRANSACTIONDTO", message.getResponse());
                    Toast.makeText(context, message.getResponse(), Toast.LENGTH_SHORT).show();
                }else {
                    transactionCallback.onFailure(response.errorBody().toString());
                    Log.e("TRANSACTIONDTO", "HTTP Code: " + response.code());

                    try {
                        Log.e("TRANSACTIONDTO",
                                "Error Body: " + response.errorBody().string());
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
}
