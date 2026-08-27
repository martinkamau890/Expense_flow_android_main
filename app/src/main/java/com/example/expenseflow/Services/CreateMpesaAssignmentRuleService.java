package com.example.expenseflow.Services;

import android.content.Context;

import com.example.expenseflow.DataBase.AppDatabase;
import com.example.expenseflow.Entity.MpesaAssignmentRule;
import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Enum.TransactionType;

public class CreateMpesaAssignmentRuleService {

    private ParsedSms parsedSms;
    private String smsType;

    private long debitAccount;

    private long creditAccount;

    private Context context;

    private TransactionType transactionType;

    private AppDatabase appDatabase = AppDatabase.getInstance(context);

    public CreateMpesaAssignmentRuleService(ParsedSms parsedSms, String smsType, TransactionType transactionType, long debitAccount, long creditAccount, Context context) {
        this.parsedSms = parsedSms;
        this.smsType = smsType;
        this.debitAccount = debitAccount;
        this.creditAccount = creditAccount;
        this.context = context;
        this.transactionType = transactionType;
    }

    public void createMpesaAssignmentRule(){
        MpesaAssignmentRule mpesaAssignmentRule = new MpesaAssignmentRule();

        mpesaAssignmentRule.setCreditAccount(creditAccount);
        mpesaAssignmentRule.setDebitAccount(debitAccount);
        mpesaAssignmentRule.setTransactionType(transactionType);
        mpesaAssignmentRule.setRecipient(parsedSms.getCounterparty());
        mpesaAssignmentRule.setSmsType(smsType);

        appDatabase.mpesaAssignmentRuleDAO().insert(mpesaAssignmentRule);
    }
}
