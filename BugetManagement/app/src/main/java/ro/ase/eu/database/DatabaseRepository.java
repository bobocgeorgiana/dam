package ro.ase.eu.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.ase.eu.util.Constants;
import ro.ase.eu.util.Expense;

public class DatabaseRepository implements DatabaseConstant {

    private SQLiteDatabase database;
    private DatabaseController controller;

    public DatabaseRepository(Context context) {
        controller = DatabaseController.getInstance(context);
    }

    public void open() {
        try {
            database = controller.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            database.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
    }

    public long insertExpense(Expense expense) {
        if (expense == null) {
            return -1;
        }

        return database.insert(EXPENSE_TABLE_NAME,
                null,
                createContentValuesFromExpense(expense)); // returneaza id-ul inregistrarii sau -1 daca apare eroare
    }

    public int updateExpense(Expense expense) {
        if (expense == null || expense.getId() == null) {
            return -1;
        }

        return database.update(EXPENSE_TABLE_NAME,
                createContentValuesFromExpense(expense),
                EXPENSE_COLUMN_ID + "=?",
                new String[]{expense.getId().toString()});
    }

    public int deleteExpense(Expense expense) {
        if (expense == null || expense.getId() == null) {
            return -1;
        }

        return database.delete(EXPENSE_TABLE_NAME,
                EXPENSE_COLUMN_ID + "=?",
                new String[]{expense.getId().toString()});
    }

    private ContentValues createContentValuesFromExpense(Expense expense) {
        if (expense == null) {
            return null;
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(EXPENSE_COLUMN_GLOBAL_ID, expense.getGlobalId());
        contentValues.put(EXPENSE_COLUMN_DATE,
                expense.getDate() != null ?
                        Constants.simpleDateFormat
                                .format(expense.getDate())
                        : null);
        contentValues.put(EXPENSE_COLUMN_CATEGORY,
                expense.getCategory());
        contentValues.put(EXPENSE_COLUMN_AMOUNT,
                expense.getAmount());
        contentValues.put(EXPENSE_COLUMN_DESCRIPTION,
                expense.getDescription());

        return contentValues;
    }

    public List<Expense> findAllExpense() {

        List<Expense> results = new ArrayList<>();
        //select * from Expense;
        //orice select intoarce un cursor. mare grija la interpretarea variabilelor.
        Cursor cursor = database.query(EXPENSE_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {

            Long id = cursor.getLong(cursor.getColumnIndex(EXPENSE_COLUMN_ID));
            String globalId = cursor.getString(cursor.getColumnIndex(EXPENSE_COLUMN_GLOBAL_ID));
            Date date = null;

            try {
                date = Constants.simpleDateFormat
                        .parse(cursor.getString(cursor
                                .getColumnIndex(EXPENSE_COLUMN_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String category = cursor.getString(cursor
                    .getColumnIndex(EXPENSE_COLUMN_CATEGORY));
            Double amount = cursor.getDouble(cursor
                    .getColumnIndex(EXPENSE_COLUMN_AMOUNT));
            String description = cursor
                    .getString(cursor
                            .getColumnIndex(EXPENSE_COLUMN_DESCRIPTION));

            results.add(new Expense(globalId, id, date, category, amount, description));
        }

        //nu uitati sa inchideti conexiunea
        cursor.close();

        return results;
    }
}
