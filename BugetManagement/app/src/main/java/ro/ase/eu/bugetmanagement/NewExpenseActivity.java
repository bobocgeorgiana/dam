package ro.ase.eu.bugetmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

import ro.ase.eu.util.Constants;
import ro.ase.eu.util.Expense;

public class NewExpenseActivity extends AppCompatActivity {

    TextInputEditText tieDate;
    TextInputEditText tieAmount;
    TextInputEditText tieDescription;
    Spinner spnCategories;
    Button btnSave;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_expense);

        intent = getIntent();
        initComponents();
    }

    private void initComponents() {
        tieDate = findViewById(R.id.new_expense_tid_date);
        tieAmount = findViewById(R.id.new_expense_tid_amount);
        tieDescription = findViewById(R.id.new_expense_tid_description);
        spnCategories = findViewById(R.id.new_expense_spn_categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.new_expense_categories_values, R.layout.support_simple_spinner_dropdown_item);
        spnCategories.setAdapter(adapter);
        btnSave = findViewById(R.id.new_expense_btn_save);
        btnSave.setOnClickListener(saveEvent());

        if (intent.hasExtra(Constants.ADD_EXPENSE_KEY)) {
            Expense expense = intent.getParcelableExtra(
                    Constants.ADD_EXPENSE_KEY);
            if (expense != null) {
                tieDate.setText(expense.getDate() != null ?
                        Constants.simpleDateFormat
                                .format(expense.getDate()) : null);
                tieAmount.setText(expense.getAmount() != null ?
                        expense.getAmount().toString() : null);
                tieDescription.setText(expense.getDescription());
                selectCategory(expense.getCategory());
            }

        }
    }

    private void selectCategory(String selectedCategory) {
        Adapter adapter = spnCategories.getAdapter();
        if (selectedCategory == null || adapter == null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).toString().equals(selectedCategory)) {
                    spnCategories.setSelection(i);
                    break;
                }
            }
        }
    }

    private View.OnClickListener saveEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    Date date = convertDateFromString(tieDate.getText().toString());
                    String category = spnCategories.getSelectedItem().toString();
                    Double amount = Double.parseDouble(tieAmount.getText().toString());
                    String description = tieDescription.getText().toString();

                    Expense expense = new Expense(date, category, amount, description);
                    //Toast.makeText(getApplicationContext(), expense.toString(), Toast.LENGTH_LONG).show();

                    intent.putExtra(Constants.ADD_EXPENSE_KEY, expense);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        };
    }

    private boolean isValid() {

        if (tieDate.getText() == null || tieDate.getText().toString().trim().isEmpty() || convertDateFromString(tieDate.getText().toString()) == null) {
            Toast.makeText(getApplicationContext(), R.string.new_expense_date_valid_error, Toast.LENGTH_LONG).show();
            return false;
        } else if (tieAmount.getText() == null || tieAmount.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.new_expense_amount_valid_error, Toast.LENGTH_LONG).show();
            return false;
        } else if (tieDescription.getText() == null || tieDescription.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.new_expense_description_valid_error, Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private Date convertDateFromString(String value) {

        Date result = null;
        try {
            result = Constants.simpleDateFormat.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
