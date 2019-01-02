package ro.ase.eu.bugetmanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class AbstractActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.item_about:
                intent = new Intent(getApplicationContext(), AboutActivity.class);
                break;
            case R.id.item_income_info:
                intent = new Intent(getApplicationContext(), IncomeInfoActivity.class);
                break;
            case R.id.item_premium:
                intent = new Intent(getApplicationContext(), PremiumActivity.class);
                break;
            case R.id.item_profile:
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                break;
            case R.id.item_home:
                intent = new Intent(getApplicationContext(), MainActivity.class);
                break;
            case R.id.item_report:
                intent = new Intent(getApplicationContext(), ReportActivity.class);
                break;
            case R.id.item_map:
                intent = new Intent(getApplicationContext(), MapsActivity.class);
                break;
            default:
                intent = null;
        }
        if (intent != null) {
            startActivity(intent);
        }

        return true;
    }

}
