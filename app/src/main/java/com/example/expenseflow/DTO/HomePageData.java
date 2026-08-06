package com.example.expenseflow.DTO;

public class HomePageData {
    private PageResponse<PostedTransactionDTO> postedTransaction;
    private TransactionSummaryDTO transactionSummary;

    public PageResponse<PostedTransactionDTO> getPostedTransaction() {
        return postedTransaction;
    }

    public void setPostedTransaction(PageResponse<PostedTransactionDTO> postedTransaction) {
        this.postedTransaction = postedTransaction;
    }

    public TransactionSummaryDTO getTransactionSummary() {
        return transactionSummary;
    }

    public void setTransactionSummary(TransactionSummaryDTO transactionSummary) {
        this.transactionSummary = transactionSummary;
    }
}
