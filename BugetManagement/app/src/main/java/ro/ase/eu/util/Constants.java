package ro.ase.eu.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public interface Constants {

    String DATE_FORMAT = "dd-MM-yyyy";
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
    int ADD_EXPENSE_REQUEST_CODE = 101;
    String ADD_EXPENSE_KEY = "addExpense";

    String PROFILE_PREF_FILE_NAME = "profilePref";

    String PROFILE_FIRST_NAME_PREF = "firstNamePref";
    String PROFILE_LAST_NAME_PREF = "lastNamePref";
    String PROFILE_AGE_PREF = "agePref";
    String PROFILE_GENDER_PREF = "genderPref";

    int UPDATE_EXPENSE_REQUEST_CODE = 102;
    String REPORT_EXPENSES_KEY = "reportExpensesKey";
}
