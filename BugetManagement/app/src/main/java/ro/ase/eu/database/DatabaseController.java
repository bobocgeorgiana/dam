package ro.ase.eu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseController extends SQLiteOpenHelper
        implements DatabaseConstant {

    private static DatabaseController controller;

    private DatabaseController(@Nullable Context context) {

        super(context, DATABASE_NAME,
                null,
                DATABASE_VERSION);
    }

    public static DatabaseController getInstance(Context context){
        if(controller == null){
            synchronized (DatabaseController.class){
                if(controller == null){
                    controller = new DatabaseController(context);
                }
            }
        }

        return controller;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //proiectarea baza de date
        sqLiteDatabase.execSQL(CREATE_TABLE_EXPENSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //preluare date existente
        sqLiteDatabase.execSQL(DROP_TABLE_EXPENSE);
        onCreate(sqLiteDatabase);
    }
}
