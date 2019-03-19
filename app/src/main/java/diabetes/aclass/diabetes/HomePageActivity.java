package diabetes.aclass.diabetes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.PendingIntent.getActivity;

/**
 * A login screen that offers login via email/password.
 */
public class HomePageActivity extends AppCompatActivity {
    private boolean value = false;
    public Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set the date box value with today date
        EditText dateBoxText = findViewById(R.id.date_box);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateBoxText.setText(sdf.format(new Date()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_profile) {
            Intent myIntent = new Intent(this, ProfileActivity.class);
            //this.startActivity(myIntent);
            startActivity(myIntent);
            return true;
        }

        if (id == R.id.action_help) {
            Intent myIntent = new Intent(this, HelpPageActivity.class);
            //this.startActivity(myIntent);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToSensorActivity(View view) {
        Intent myIntent = new Intent(this, Sensor_Activity.class);
        startActivity(myIntent);
    }

    public void goToInsertActivity(View view) {
        Intent myIntent = new Intent(this, InsertValueActivity.class);
        startActivity(myIntent);
    }

    public void goToBTSincActivity(View view) {
        Intent myIntent = new Intent(this, BT_Activity.class);
        startActivity(myIntent);
    }


    public void goToDoctorActivity(View view) {
        Intent myIntent = new Intent(this, DoctorProfileActivity.class);
        startActivity(myIntent);
    }

    public void goToGraphActivity(View view) {
        Intent myIntent = new Intent(this, GraphActivity.class);
        startActivity(myIntent);
    }

    public void goToHistoryActivity(View view) {
        Intent myIntent = new Intent(this, HistoryPageActivity.class);
        startActivity(myIntent);
    }
}

