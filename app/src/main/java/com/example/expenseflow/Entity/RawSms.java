package com.example.expenseflow.Entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.expenseflow.Enum.ConversionStatus;

@Entity(indices = {
        @Index(value = "providerSmsId", unique = true)
})
public class RawSms {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @NonNull
    private long providerSmsId;

    @NonNull
    private String transactionCode;

    private String sender;

    private String body;

    private long smsDate;

    private String conversionStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConversionStatus() {
        return conversionStatus;
    }

    public void setConversionStatus(String conversionStatus) {
        this.conversionStatus = conversionStatus;
    }

    public long getProviderSmsId() {
        return providerSmsId;
    }

    public void setProviderSmsId(long providerSmsId) {
        this.providerSmsId = providerSmsId;
    }

    @NonNull
    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(@NonNull String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getSmsDate() {
        return smsDate;
    }

    public void setSmsDate(long smsDate) {
        this.smsDate = smsDate;
    }

}
