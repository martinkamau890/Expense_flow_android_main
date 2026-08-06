package com.example.expenseflow.Services;

import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Entity.RawSms;

public interface SmsParser {

    ParsedSms parseSms(RawSms rawSms);
}
