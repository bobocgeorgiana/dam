package ro.ase.eu.bugetmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONException;

import ro.ase.eu.network.HttpManager;
import ro.ase.eu.network.Premium;
import ro.ase.eu.network.PremiumParser;

public class PremiumActivity extends AbstractActivity {

    // private static final String URL = "https://api.myjson.com/bins/184n06";
    private static final String URL = "https://api.myjson.com/bins/34flu";
    private Premium premium;
    private Button btnBronze;
    private Button btnSilver;
    private Button btnGold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        @SuppressLint("StaticFieldLeak") HttpManager manager = new HttpManager() {
            @Override
            protected void onPostExecute(String s) {
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                try {
                    //premium = PremiumParser.fromJson(s);
                    premium = PremiumParser.fromXml("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                            "<Premium>\n" +
                            "   <item type=\"Bronze\">\n" +
                            "      <category>Food</category>\n" +
                            "      <product>\n" +
                            "         <name>Pizza Diavola</name>\n" +
                            "         <price>13.5</price>\n" +
                            "      </product>\n" +
                            "   </item>\n" +
                            "   <item type=\"Silver\">\n" +
                            "      <category>Food</category>\n" +
                            "      <products>\n" +
                            "         <product>\n" +
                            "            <name>Pizza Diavola</name>\n" +
                            "            <price>13.5</price>\n" +
                            "         </product>\n" +
                            "         <product>\n" +
                            "            <name>Piept de pui la gratar</name>\n" +
                            "            <price>30</price>\n" +
                            "         </product>\n" +
                            "      </products>\n" +
                            "   </item>\n" +
                            "   <item type=\"Gold\">\n" +
                            "      <category>Food</category>\n" +
                            "      <products>\n" +
                            "         <product>\n" +
                            "            <name>Pizza Diavola</name>\n" +
                            "            <price>13.5</price>\n" +
                            "         </product>\n" +
                            "         <product>\n" +
                            "            <name>Piept de pui la gratar</name>\n" +
                            "            <price>30</price>\n" +
                            "         </product>\n" +
                            "      </products>\n" +
                            "   </item>\n" +
                            "   <item type=\"Gold\">\n" +
                            "      <category>Food1</category>\n" +
                            "      <products>\n" +
                            "         <product>\n" +
                            "            <name>Pizza Diavola2</name>\n" +
                            "            <price>13.5</price>\n" +
                            "         </product>\n" +
                            "         <product>\n" +
                            "            <name>Piept de pui la gratar2</name>\n" +
                            "            <price>30</price>\n" +
                            "         </product>\n" +
                            "      </products>\n" +
                            "   </item>\n" +
                            "</Premium>\n");

                    PremiumParser.fromJson(s);
                    Toast.makeText(getApplicationContext(),
                            premium.toString(), Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.premium_premium_parser_error),
                            Toast.LENGTH_LONG).show();
                }
            }
        };

        manager.execute(URL);
        init();
    }

    private void init(){
        btnBronze = findViewById(R.id.premium_btn_bronze);
        btnSilver= findViewById(R.id.premium_btn_silver);
        btnGold = findViewById(R.id.premium_btn_gold);

        btnBronze.setOnClickListener(bronzeEvent());
        btnSilver.setOnClickListener(silverEvent());
        btnGold.setOnClickListener(goldEvent());
    }

    @NonNull
    private View.OnClickListener goldEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(premium!=null && premium.getGold()!=null){
                    Toast.makeText(getApplicationContext(),
                            premium.getGold().toString(),
                            Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.premium_no_content),
                            Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @NonNull
    private View.OnClickListener silverEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(premium!=null && premium.getSilver()!=null){
                    Toast.makeText(getApplicationContext(),
                            premium.getSilver().toString(),
                            Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.premium_no_content),
                            Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @NonNull
    private View.OnClickListener bronzeEvent() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(premium!=null && premium.getBronze()!=null){
                    Toast.makeText(getApplicationContext(),
                            premium.getBronze().toString(),
                            Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.premium_no_content),
                            Toast.LENGTH_LONG).show();

                }
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.item_premium);
        item.setVisible(false);
        return true;
    }
}
