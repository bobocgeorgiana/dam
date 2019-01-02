package ro.ase.eu.bugetmanagement;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import ro.ase.eu.util.Constants;

public class ProfileActivity extends AbstractActivity {

    private TextInputEditText tieFirstName;
    private TextInputEditText tieLastName;
    private TextInputEditText tieAge;
    private RadioGroup rgGender;
    private Button btnSave;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    private void init() {
        tieFirstName = findViewById(R.id.profile_tid_first_name);
        tieLastName = findViewById(R.id.profile_tid_last_name);
        tieAge = findViewById(R.id.profile_tid_age);
        rgGender = findViewById(R.id.profile_rg_gender);
        btnSave = findViewById(R.id.profile_btn_save);

        sharedPreferences = getSharedPreferences(Constants.PROFILE_PREF_FILE_NAME,
                MODE_PRIVATE);

        btnSave.setOnClickListener(saveEvent());

        restoreSharedPref();
    }

    private View.OnClickListener saveEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = tieFirstName.getText() != null
                        ? tieFirstName.getText().toString() : null;
                String lastName = tieLastName.getText() != null ?
                        tieLastName.getText().toString() : null;
                Integer age = tieAge.getText() != null ?
                        Integer.parseInt(tieAge.getText().toString())
                        : null;
                Integer genderCheckedButtonId = rgGender.getCheckedRadioButtonId();

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(Constants.PROFILE_FIRST_NAME_PREF, firstName);
                editor.putString(Constants.PROFILE_LAST_NAME_PREF, lastName);
                editor.putInt(Constants.PROFILE_AGE_PREF, age);
                editor.putInt(Constants.PROFILE_GENDER_PREF, genderCheckedButtonId);

                boolean result = editor.commit();

                if (result) {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.profile_shared_edit_result),
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            R.string.profile_shared_editor_result_error,
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private void restoreSharedPref() {
        String firstName = sharedPreferences.getString(Constants.PROFILE_FIRST_NAME_PREF,
                null);
        String lastName = sharedPreferences.getString(Constants.PROFILE_LAST_NAME_PREF,
                null);
        Integer age = sharedPreferences.getInt(Constants.PROFILE_AGE_PREF, 0);
        Integer checkRadioButtonGenderId = sharedPreferences
                .getInt(Constants.PROFILE_GENDER_PREF,
                        R.id.profile_rb_gender_male);

        tieFirstName.setText(firstName);
        tieLastName.setText(lastName);
        tieAge.setText(age > 0 ? age.toString() : null);
        rgGender.check(checkRadioButtonGenderId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.item_profile);
        item.setVisible(false);
        return true;
    }
}
