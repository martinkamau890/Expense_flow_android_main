package com.example.expenseflow;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expenseflow.DTO.ExpensePerAccountCategoryDTO;
import com.example.expenseflow.DTO.HomePageData;
import com.example.expenseflow.Enum.DateRangeType;

import com.example.expenseflow.api.ApiService;
import com.example.expenseflow.api.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseActivity extends AppCompatActivity {
    private TextView dayTab;
    private TextView weekTab;
    private TextView monthTab;
    private TextView totalSpendingLabel;
    private TextView totalSpendingAmount;
    private LinearLayout badgeTrend;
    private ImageView imageTrend;
    private TextView trendText;
    private TextView budgetUsageText;
    private ProgressBar budgetProgress;

    private RecyclerView expensePerCategoryList;

    private ApiService apiService;

    private List<ExpensePerAccountCategoryDTO> expenseList =
            new ArrayList<>();

    private ExpenseListAdaptor expenseListAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);

        setContentView(R.layout.expense_list);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return insets;
                }
        );

        initialize();

        apiService =
                RetrofitClient
                        .getRetrofitInstance(this)
                        .create(ApiService.class);

        initializeRecyclerView();

        loadExpenses(DateRangeType.MONTH);
        initializeActionListeners();
    }


    private void initialize() {

        dayTab = findViewById(R.id.tab_day);
        weekTab = findViewById(R.id.tab_week);
        monthTab = findViewById(R.id.tab_month);

        totalSpendingLabel =
                findViewById(R.id.tv_total_spend_label);

        totalSpendingAmount =
                findViewById(R.id.tv_total_spend_amount);

        badgeTrend =
                findViewById(R.id.badge_trend);

        imageTrend =
                findViewById(R.id.imageTrend);

        trendText =
                findViewById(R.id.tv_trend_text);

        budgetUsageText =
                findViewById(R.id.tv_budget_usage_value);

        budgetProgress =
                findViewById(R.id.progress_budget);

        expensePerCategoryList =
                findViewById(R.id.rv_expenses_per_category);
    }


    private void initializeRecyclerView() {

        expenseListAdaptor =
                new ExpenseListAdaptor(
                        expenseList,
                        ExpenseActivity.this
                );

        expensePerCategoryList.setLayoutManager(
                new LinearLayoutManager(this)
        );

        expensePerCategoryList.setAdapter(
                expenseListAdaptor
        );
    }

    public void initializeActionListeners(){

        dayTab.setOnClickListener(view ->{
            dayTab.setBackgroundResource(R.drawable.bg_pill_active);
            weekTab.setBackgroundResource(R.drawable.bg_pill_inactive);
            monthTab.setBackgroundResource(R.drawable.bg_pill_inactive);
            loadExpenses(DateRangeType.DAY);
        });
        weekTab.setOnClickListener(view -> {
            dayTab.setBackgroundResource(R.drawable.bg_pill_inactive);
            weekTab.setBackgroundResource(R.drawable.bg_pill_active);
            monthTab.setBackgroundResource(R.drawable.bg_pill_inactive);
            loadExpenses(DateRangeType.WEEK);
        });
        monthTab.setOnClickListener(view -> {
            dayTab.setBackgroundResource(R.drawable.bg_pill_inactive);
            weekTab.setBackgroundResource(R.drawable.bg_pill_inactive);
            monthTab.setBackgroundResource(R.drawable.bg_pill_active);
            loadExpenses(DateRangeType.MONTH);
        });

    }


    private void loadExpenses(DateRangeType dateRange) {

        Log.d("EXPENSE_CATEGORY_DEBUG", "Starting expense request...");

        Call<List<ExpensePerAccountCategoryDTO>> call =
                apiService.getExpensesPerCategory(
                        dateRange
                );

        call.enqueue(
                new Callback<List<ExpensePerAccountCategoryDTO>>() {

                    @Override
                    public void onResponse(
                            Call<List<ExpensePerAccountCategoryDTO>> call,
                            Response<List<ExpensePerAccountCategoryDTO>> response) {

                        Log.d("EXPENSE_CATEGORY_DEBUG", "Response code: " + response.code());

                        if (!response.isSuccessful()) {
                            Log.e("EXPENSE_CATEGORY_DEBUG", "Server returned: " + response.code());
                            return;
                        }

                        if (response.body() == null) {
                            Log.e("EXPENSE_CATEGORY_DEBUG", "Response body is NULL");

                            return;
                        }

                        List<ExpensePerAccountCategoryDTO> data = response.body();

                        Log.d("EXPENSE_CATEGORY_DEBUG", "Received " + data.size() + " expense categories");

                        for (ExpensePerAccountCategoryDTO expense : data) {

                            Log.d("EXPENSE_CATEGORY_DEBUG", "Category: " + expense.getAccountCategory() + " Amount: " + expense.getAmount());
                        }

                        expenseList.clear();
                        expenseList.addAll(data);

                        expenseListAdaptor.notifyDataSetChanged();

                    }


                    @Override
                    public void onFailure(
                            Call<List<ExpensePerAccountCategoryDTO>> call,
                            Throwable throwable) {

                        Log.e("EXPENSE_CATEGORY_DEBUG", "Request failed", throwable);

                        Toast.makeText(
                                ExpenseActivity.this,
                                "Network error: "
                                        + throwable.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }
}