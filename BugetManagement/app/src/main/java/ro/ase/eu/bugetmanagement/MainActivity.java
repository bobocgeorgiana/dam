package ro.ase.eu.bugetmanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ro.ase.eu.database.DatabaseRepository;
import ro.ase.eu.firebase.FirebaseController;
import ro.ase.eu.util.Constants;
import ro.ase.eu.util.Expense;
import ro.ase.eu.util.ExpenseAdapter;

public class MainActivity extends AbstractActivity {

    private TextView tvMessage;
    private FloatingActionButton fabAddExpense;
    private ListView lvExpenses;

    private static final String SELECTED_LABEL_KEY = "selectedLabelKey";

    private int selectedTextColorIndex = -1;

    List<Expense> expenses = new ArrayList<>();
    private int selectedPosition;

    private DatabaseRepository repository;
    private FirebaseController firebaseController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();

        if (savedInstanceState != null) {
            Log.i("MainActivity", "selected color value " + selectedTextColorIndex);
            tvMessage.setText(savedInstanceState.getString(SELECTED_LABEL_KEY));
        }
    }

    private void initComponents() {
        tvMessage = findViewById(R.id.tv_message);
        fabAddExpense = findViewById(R.id.main_fab_add_expense);
        lvExpenses = findViewById(R.id.main_lv_expenses);

        repository = new DatabaseRepository(getApplicationContext());
        firebaseController = FirebaseController.getInstance();

        repository.open();
        expenses = repository.findAllExpense();
        repository.close();

//        ArrayAdapter<Expense> adapter = new ArrayAdapter<>(getApplication(),
//                android.R.layout.simple_list_item_1, expenses);
        ExpenseAdapter adapter = new ExpenseAdapter(getApplicationContext(),
                R.layout.lv_expenses_row, expenses, getLayoutInflater());

        lvExpenses.setAdapter(adapter);
        lvExpenses.setOnItemClickListener(updateEvent());
        lvExpenses.setOnItemLongClickListener(deleteEvent());

        fabAddExpense.setOnClickListener(addExpenseEvent());
    }

    @NonNull
    private AdapterView.OnItemLongClickListener deleteEvent() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView,
                                           View view,
                                           final int position,
                                           long l) {
                AlertDialog.Builder builder = new AlertDialog
                        .Builder(MainActivity.this);

                builder.setTitle(R.string.main_alert_title)
                        .setMessage(getString(R.string.main_alert_message,
                                expenses.get(position).getDate() != null ?
                                        Constants.simpleDateFormat
                                                .format(expenses.get(position)
                                                        .getDate())
                                        : getString(R.string.main_alert_no_date)))
                        .setPositiveButton(getString(R.string.main_alert_positive_button),
                                positiveEvent(position))
                        .setNegativeButton(getString(R.string.main_alert_negative_button),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(MainActivity.this,
                                                getString(R.string.main_alert_delete_cancel),
                                                Toast.LENGTH_LONG).show();
                                    }
                                });
                //create- creaza instanta AlertDialog, show -metoda din AlertDialog, afiseaza mesajul
                builder.create().show();
                return true;
            }
        };
    }

    @NonNull
    private DialogInterface.OnClickListener positiveEvent(final int position) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface,
                                int i) {
                repository.open();
                int result = repository.deleteExpense(expenses.get(position));
                repository.close();
                if (result == 1) {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.main_alert_delete_success),
                            Toast.LENGTH_LONG).show();
                    firebaseController.remove(expenses.get(position));
                    expenses.remove(position);
                    notifyAdapter();
                } else {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.main_alert_delete_failed),
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @NonNull
    private AdapterView.OnItemClickListener updateEvent() {
        return new AdapterView
                .OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView,
                                    View view,
                                    int position,
                                    long l) {

                Intent intent = new Intent(getApplicationContext(), NewExpenseActivity.class);
                intent.putExtra(Constants.ADD_EXPENSE_KEY, expenses.get(position));
                selectedPosition = position;
                startActivityForResult(intent,
                        Constants.UPDATE_EXPENSE_REQUEST_CODE);

            }
        };
    }

    private View.OnClickListener addExpenseEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),
                        NewExpenseActivity.class);
                //startActivity(intent);
                startActivityForResult(intent,
                        Constants.ADD_EXPENSE_REQUEST_CODE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {

            Expense expense = data.getParcelableExtra(Constants.ADD_EXPENSE_KEY);
            if (expense != null) {

                if (requestCode == Constants.ADD_EXPENSE_REQUEST_CODE) {
                    insertExpense(expense);
                } else if (requestCode == Constants.UPDATE_EXPENSE_REQUEST_CODE) {
                    updateExpense(expense);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.main_result_activity_error),
                    Toast.LENGTH_LONG).show();
        }

    }

    private void updateExpense(Expense expense) {
        expenses.get(selectedPosition).setDate(expense.getDate());
        expenses.get(selectedPosition).setAmount(expense.getAmount());
        expenses.get(selectedPosition).setCategory(expense.getCategory());
        expenses.get(selectedPosition).setDescription(expense.getDescription());

        repository.open();
        String globalId = exportToFirebase(expenses.get(selectedPosition));
        if (globalId != null) {
            expenses.get(selectedPosition).setGlobalId(globalId);
        }
        int result = repository.updateExpense(expenses.get(selectedPosition));

        if (result == 1) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.main_update_success),
                    Toast.LENGTH_LONG).show();
            notifyAdapter();
        } else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.main_update_failed),
                    Toast.LENGTH_LONG).show();
        }
        repository.close();
    }

    private void insertExpense(Expense expense) {
        Toast.makeText(getApplicationContext(),
                expense.toString(),
                Toast.LENGTH_LONG).show();

        repository.open();
        Long id = repository.insertExpense(expense);
        if (id != -1) {
            expense.setId(id);
            String globalId = exportToFirebase(expense);
            if (globalId != null) {
                expense.setGlobalId(globalId);
                repository.updateExpense(expense);
            }
            expenses.add(expense);
            notifyAdapter();
        }
        repository.close();
    }

    private String exportToFirebase(Expense expense) {
        return firebaseController.upsertExpense(expense);
    }

    private void notifyAdapter() {
        ExpenseAdapter adapter = (ExpenseAdapter) lvExpenses.getAdapter();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.item_home);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        tvMessage.setText(item.getTitle());
        super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SELECTED_LABEL_KEY, tvMessage.getText().toString());
        Toast.makeText(getApplicationContext(), R.string.main_save_instance_success_message, Toast.LENGTH_SHORT).show();
    }
}
