package com.pbsi2.fakenewsbaby;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.GregorianCalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static String guardianUrl;
    public static String okeyText;
    public static String startDate;
    public static String endDate;
    private Toolbar mTopToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTopToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mTopToolbar);
        mTopToolbar.setLogo(R.mipmap.ic_badnews);
        /*
        Format the query dates correctly
        */

        GregorianCalendar gcalendar = new GregorianCalendar();
        String eydate = String.format("%04d", gcalendar.get(GregorianCalendar.YEAR));
        String emdate = String.format("%02d", gcalendar.get(GregorianCalendar.MONTH) + 1);
        String eddate = String.format("%02d", gcalendar.get(GregorianCalendar.DAY_OF_MONTH));
        endDate = eydate + "-" + emdate + "-" + eddate;
        gcalendar.add(GregorianCalendar.DATE, -1);
        String sydate = String.format("%04d", gcalendar.get(GregorianCalendar.YEAR));
        String smdate = String.format("%02d", gcalendar.get(GregorianCalendar.MONTH) + 1);
        String sddate = String.format("%02d", gcalendar.get(GregorianCalendar.DAY_OF_MONTH));
        startDate = sydate + "-" + smdate + "-" + sddate;
        Toast.makeText(getApplicationContext(),
                "News are from in the last 24 Hrs: " + startDate + "Today: " + endDate,
                Toast.LENGTH_LONG).show();
        Button getOn = findViewById(R.id.button);
        getOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, new LoginFragment())
                        .commit();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
