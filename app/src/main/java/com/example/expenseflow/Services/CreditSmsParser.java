package com.example.expenseflow.Services;

import com.example.expenseflow.Converter.ConversionStatusConverter;
import com.example.expenseflow.Converter.DateTimeConverter;
import com.example.expenseflow.Converter.SmsTypeConverter;
import com.example.expenseflow.Entity.ParsedSms;
import com.example.expenseflow.Entity.RawSms;
import com.example.expenseflow.Enum.ConversionStatus;
import com.example.expenseflow.Enum.SmsType;

import java.time.LocalDateTime;

public class CreditSmsParser implements SmsParser {
    @Override
    public ParsedSms parseSms(RawSms rawSms) {

        ParsedSms parseSms = new ParsedSms();
        Extractor extractor = new Extractor();

        parseSms.setRawSmsId(rawSms.getId());
        parseSms.setSmsType(SmsTypeConverter.fromSmsType(SmsType.CREDIT_SMS));
        parseSms.setTransactionCode(extractor.getTransactionCode(rawSms));
        parseSms.setTransactionTime(extractor.getTransactionTime(rawSms));
        parseSms.setTransactionAmount(extractor.getTransactionAmount(rawSms));
        parseSms.setTransactionFee(extractor.getTransactionFee(rawSms));
        parseSms.setCounterparty(extractor.getCounterParty(rawSms));
        parseSms.setConversionStatus(ConversionStatusConverter.fromConversionStatus(ConversionStatus.PENDING));
        parseSms.setCreatedAt(DateTimeConverter.fromLocalDateTime(LocalDateTime.now()));
        parseSms.setParsedAt(null);
        return parseSms;
    }

}
