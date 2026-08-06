package com.example.expenseflow.Services;

import com.example.expenseflow.Entity.RawSms;
import com.example.expenseflow.Enum.SmsType;

public class SmsClassifier {

    public SmsType classify(RawSms rawSms){

        String body = rawSms.getBody().toLowerCase();

        if (body == null || body.isEmpty()){
            return SmsType.FULIZA_SMS;
        }

        if (body.contains(" sent to ")){
            return SmsType.CREDIT_SMS;
        }

        if (body.contains(" received ")) {
            return SmsType.DEBIT_SMS;
        }

        if (body.contains(" paid to ")) {
            return SmsType.CREDIT_SMS;
        }

        if (body.contains(" Withdraw ")) {
            return SmsType.WITHDRAW;
        }

        return SmsType.FULIZA_SMS;
    }
}
