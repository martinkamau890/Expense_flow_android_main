package com.example.expenseflow.DTO;

public class ExtractedMessageDTO {

    long id;
    String transactionCode;
    String sender;
    String message;
    long date;

    public ExtractedMessageDTO(long id,String sender, String message, long date, String transactionCode) {
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.transactionCode = transactionCode;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String transactionCode) {
        this.transactionCode = transactionCode;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
