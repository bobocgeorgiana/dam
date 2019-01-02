package ro.ase.eu.database;

public interface DatabaseConstant {

    String DATABASE_NAME = "budget.db";
    int DATABASE_VERSION = 1;

    String EXPENSE_TABLE_NAME = "Expense";

    // Sqlite tipuri de date: TEXT, REAL, INTEGER, BOOL, BLOB

    String EXPENSE_COLUMN_ID = "_id"; // id ul default din Sqlite
    String EXPENSE_COLUMN_DATE = "date";
    String EXPENSE_COLUMN_AMOUNT = "amount";
    String EXPENSE_COLUMN_CATEGORY = "category";
    String EXPENSE_COLUMN_DESCRIPTION = "description";
    String EXPENSE_COLUMN_GLOBAL_ID = "globalId";

    //atentie la scrierea scriptului de creare
    String CREATE_TABLE_EXPENSE = "CREATE TABLE " + EXPENSE_TABLE_NAME
            + " ( " + EXPENSE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            EXPENSE_COLUMN_DATE + " TEXT, " +
            EXPENSE_COLUMN_CATEGORY + " TEXT, " +
            EXPENSE_COLUMN_AMOUNT + " REAL, " +
            EXPENSE_COLUMN_DESCRIPTION + " TEXT, " +
            EXPENSE_COLUMN_GLOBAL_ID + " TEXT);";

    // CREATE TABLE EXPENSE (_id PRIMARY KEY INTEGER AUTOINCREMENT,
    //date TEXT, category TEXT, amount REAL, description TEXT);

    String DROP_TABLE_EXPENSE = "DROP TABLE IF EXISTS "
            + EXPENSE_TABLE_NAME + ";";
}
