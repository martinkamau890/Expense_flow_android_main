package com.example.expenseflow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expenseflow.Converter.BigDecimalConverter;
import com.example.expenseflow.DTO.ExpensePerAccountCategoryDTO;

import java.util.List;

public class ExpenseListAdaptor extends RecyclerView.Adapter<ExpenseListAdaptor.ExpenseAdaptor>{


    private final List<ExpensePerAccountCategoryDTO> expensePerAccountCategoryDTOList;
    private final Context context;

    public ExpenseListAdaptor(List<ExpensePerAccountCategoryDTO> expensePerAccountCategoryDTOList, Context context) {
        this.expensePerAccountCategoryDTOList = expensePerAccountCategoryDTOList;
        this.context = context;

    }

    @NonNull
    @Override
    public ExpenseAdaptor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_per_category_card, parent, false);
        return new ExpenseAdaptor(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdaptor holder, int position) {

        ExpensePerAccountCategoryDTO expense = expensePerAccountCategoryDTOList.get(position);

        Log.d("EXPENSE_LIST_BIND", "Account category " + expense.getAccountCategory());

        holder.expenseCategory.setText(expense.getAccountCategory());
        holder.amount.setText(BigDecimalConverter.fromBigDecimal(expense.getAmount()));

    }

    @Override
    public int getItemCount() {
        return expensePerAccountCategoryDTOList.size();
    }

    public static class ExpenseAdaptor extends RecyclerView.ViewHolder{

        private TextView expenseCategory;
        private TextView expenseCategoryPercentage;
        private TextView amount;
        private TextView percentageChange;

        private ProgressBar progressBar;
        public ExpenseAdaptor(@NonNull View itemView) {

            super(itemView);

            expenseCategory = itemView.findViewById(R.id.tv_expense_category);
            expenseCategoryPercentage = itemView.findViewById(R.id.tv_category_percent);
            amount = itemView.findViewById(R.id.tv_amount);
            percentageChange = itemView.findViewById(R.id.tv_parcentage_change);
            progressBar = itemView.findViewById(R.id.progress_bar);

        }
    }
}
