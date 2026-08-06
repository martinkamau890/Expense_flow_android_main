package com.example.expenseflow;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expenseflow.DTO.PostedTransactionDTO;

import java.util.List;

public class AllTransactionAdapter extends RecyclerView.Adapter<AllTransactionAdapter.TransactionViewHolder>{

    private List<PostedTransactionDTO> postedTransactionDTOList;

    public AllTransactionAdapter(List<PostedTransactionDTO> postedTransactionDTOList) {
        this.postedTransactionDTOList = postedTransactionDTOList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card, parent, false);

        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        PostedTransactionDTO transactionDTO = postedTransactionDTOList.get(position);

        Log.d("Bound_Transaction", "Transaction Bound No: " + position);

        holder.description.setText(transactionDTO.getDescription());
        holder.transactionTypeAndDate.setText(transactionDTO.getAccount() + "." + transactionDTO.getTransactionDate());
        holder.amount.setText("Ksh"+transactionDTO.getAmount());

    }

    @Override
    public int getItemCount() {

        Log.d("Item_Count", String.valueOf(postedTransactionDTOList.size()));
        return postedTransactionDTOList.size();
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder{

    TextView description;
    TextView transactionTypeAndDate;
    TextView amount;

    public TransactionViewHolder(View view){
        super(view);

        description = view.findViewById(R.id.activity_title);
        transactionTypeAndDate = view.findViewById(R.id.activity_subtitle);
        amount = view.findViewById(R.id.activity_amount);
    }

    }
}
