package ro.ase.eu.bugetmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro.ase.eu.util.ChartView;
import ro.ase.eu.util.Constants;
import ro.ase.eu.util.Expense;

public class ChartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chart);
        Intent intent = getIntent();

        List<Expense> expenses = intent != null
                && intent.hasExtra(Constants
                .REPORT_EXPENSES_KEY) ?
                intent.<Expense>getParcelableArrayListExtra(Constants
                        .REPORT_EXPENSES_KEY) : null;

        setContentView(new ChartView(getApplicationContext(),
                createSource(expenses)));
    }

    private Map<String, Double> createSource(List<Expense> expenses) {
        if (expenses == null ||
                expenses.size() == 0) {
            return null;
        }

        Map<String, Double> result = new HashMap<>();

        for (Expense expense : expenses) {
            if (result.containsKey(expense.getCategory())) {
                result.put(expense.getCategory(),
                        result.get(expense.getCategory())
                                + expense.getAmount());
            } else {
                result.put(expense.getCategory(),
                        expense.getAmount());
            }
        }

        return result;
    }
}
