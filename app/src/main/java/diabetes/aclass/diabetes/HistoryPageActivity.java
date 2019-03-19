package diabetes.aclass.diabetes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class HistoryPageActivity extends AppCompatActivity {
    private boolean value = false;
    public Button button;
    private TextView dateFromTv;
    private ImageButton fromPickDate;
    private TextView dateToTv;
    private ImageButton toPickDate;
    private Calendar toDate;
    private Calendar fromDate;
    private TextView activeDateDisplay;
    private Calendar activeDate;

    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historypage_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //set the date box value with today date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        EditText dateFromText = findViewById(R.id.date_from);
        dateFromText.setText(sdf.format(new Date()));
        EditText dateToText = findViewById(R.id.date_to);
        dateToText.setText(sdf.format(new Date()));

        //insert example rows
        TableLayout tl = (TableLayout) findViewById(R.id.history_table);

        for(int i = 1; i < 10; i++){

            int color = getResources().getColor(R.color.table_row_odd);
            if((i%2)==0){
                color = getResources().getColor(R.color.table_row_even);
            }

            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            tr.setBackgroundColor(color);

            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(1,1,1,1);

            TextView tv = new TextView(this);
            tv.setLayoutParams(layoutParams);
            tv.setText("0"+i+"/01/2018");
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.black));
            tr.addView(tv);

            tv = new TextView(this);
            tv.setLayoutParams(layoutParams);
            tv.setText("4"+i);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.black));
            tr.addView(tv);

            tv = new TextView(this);
            tv.setLayoutParams(layoutParams);
            tv.setText("20"+i+" (!)");
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.black));
            tr.addView(tv);

            tv = new TextView(this);
            tv.setLayoutParams(layoutParams);
            tv.setText("7"+i);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(getResources().getColor(R.color.black));
            tr.addView(tv);

            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }

        //date picker FROM
        dateFromTv = (TextView) findViewById(R.id.date_from);
        fromPickDate = (ImageButton) findViewById(R.id.fromPickDate);
        fromDate = Calendar.getInstance();

        fromPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(dateFromTv, fromDate);
            }
        });

        //date picker TO
        dateToTv = (TextView) findViewById(R.id.date_to);
        toPickDate = (ImageButton) findViewById(R.id.toPickDate);
        toDate = Calendar.getInstance();

        toPickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDateDialog(dateToTv, toDate);
            }
        });

        updateDisplay(dateFromTv, toDate);
        updateDisplay(dateToTv, toDate);
    }

    private void updateDisplay(TextView dateDisplay, Calendar date) {
        dateDisplay.setText(
                new StringBuilder()
                        .append(date.get(Calendar.DAY_OF_MONTH)).append("/")
                        .append(date.get(Calendar.MONTH) + 1).append("/")
                        .append(date.get(Calendar.YEAR)));

    }

    public void showDateDialog(TextView dateDisplay, Calendar date) {
        activeDateDisplay = dateDisplay;
        activeDate = date;
        showDialog(DATE_DIALOG_ID);
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            activeDate.set(Calendar.YEAR, year);
            activeDate.set(Calendar.MONTH, monthOfYear);
            activeDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDisplay(activeDateDisplay, activeDate);
            unregisterDateDisplay();
        }
    };

    private void unregisterDateDisplay() {
        activeDateDisplay = null;
        activeDate = null;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, dateSetListener, activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        super.onPrepareDialog(id, dialog);
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(activeDate.get(Calendar.YEAR), activeDate.get(Calendar.MONTH), activeDate.get(Calendar.DAY_OF_MONTH));
                break;
        }
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

    public void goToInsertActivity(View view) {
        Intent myIntent = new Intent(this, InsertValueActivity.class);
        startActivity(myIntent);
    }

    public void goToHomePageActivity(View view) {
        Intent myIntent = new Intent(this, HomePageActivity.class);
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


}

