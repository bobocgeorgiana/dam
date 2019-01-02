package ro.ase.eu.bugetmanagement;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AboutActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.item_about);
        item.setVisible(false);
        return  true;
    }
}
