package ro.ase.eu.bugetmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ro.ase.eu.database.DatabaseRepository;
import ro.ase.eu.firebase.FirebaseController;
import ro.ase.eu.util.Constants;
import ro.ase.eu.util.Expense;
import ro.ase.eu.util.ExpenseAdapter;

public class ReportActivity extends AbstractActivity {

    private ListView lvExpenses;
    private Switch switchDatabase;
    private FloatingActionButton fabChart;

    private DatabaseRepository repository;
    private FirebaseController firebaseController;

    private ArrayList<Expense> expenses = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        init();
    }

    private void init() {
        lvExpenses = findViewById(R.id.report_lv_expenses);
        switchDatabase = findViewById(R.id.report_switch_database);
        fabChart = findViewById(R.id.report_fab_chart);

        repository = new DatabaseRepository(getApplicationContext());
        firebaseController = FirebaseController.getInstance();

        selectFromLocalDatabase();

        switchDatabase.setOnCheckedChangeListener(switchEvent());
        fabChart.setOnClickListener(drawEvent());
    }

    @NonNull
    private View.OnClickListener drawEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        ChartActivity.class);
                intent.putExtra(Constants.REPORT_EXPENSES_KEY,
                        expenses);
                startActivity(intent);
            }
        };
    }

    @NonNull
    private CompoundButton.OnCheckedChangeListener switchEvent() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    selectFromLocalDatabase();
                } else {
                    expenses = (ArrayList<Expense>) firebaseController.findAll(selectEventListener());
                }
            }
        };
    }

    private void selectFromLocalDatabase() {
        repository.open();
        expenses = (ArrayList<Expense>) repository.findAllExpense();
        repository.close();
        initAdapter();
    }

    private void initAdapter() {
        ExpenseAdapter adapter = new ExpenseAdapter(getApplicationContext(), R.layout.lv_expenses_row, expenses, getLayoutInflater());
        lvExpenses.setAdapter(adapter);
    }

    private ValueEventListener selectEventListener() {
        return new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                expenses = new ArrayList<>();
                //parcurgem toate randurile din parinte, pentru a obtine obiectul de tip expense
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Expense expense = data.getValue(Expense.class);
                    if (expense != null) {
                        expenses.add(expense);
                    }
                }
                initAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("ReportActivity", "Data is not available");
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.item_report);
        item.setVisible(false);
        return true;
    }
}
