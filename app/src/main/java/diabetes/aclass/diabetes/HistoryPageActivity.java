package diabetes.aclass.diabetes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import diabetes.aclass.dagger.component.DataJsonCallback;
import diabetes.aclass.model.MeasurementEntity;
import diabetes.aclass.model.UserEntity;
import diabetes.aclass.presenter.PresenterImpl;

import static diabetes.aclass.utils.Component.API_BASE;

/**
 * A login screen that offers login via email/password.
 */
public class HistoryPageActivity extends AppCompatActivity {

    private static final String URL_MEASUREMENT = API_BASE +"/measurements/getbyuid?patient_id=";


    //a list to store all the products
    List<MeasurementEntity> measureList;

    RecyclerView recyclerView;


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
    private PresenterImpl mainPresenter ;
    private  String complete_url;
    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.historypage_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getting the recyclerview from xml
        recyclerView = findViewById(R.id.recylcerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadProducts();
        measureList = new ArrayList<>();

        //set the date box value with today date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        EditText dateFromText = findViewById(R.id.date_from);
        dateFromText.setText(sdf.format(new Date()));
        EditText dateToText = findViewById(R.id.date_to);
        dateToText.setText(sdf.format(new Date()));


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

    private void loadProducts() {

        /*
         * Creating a String Request
         * The request type is GET defined by first parameter
         * The URL is defined in the second parameter
         * Then we have a Response Listener and a Error Listener
         * In response listener we will get the JSON response as a String
         * */
     //   LoginActivity lg ;
     //  UserEntity logged_user = lg.getlogged_user();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ID = preferences.getString("ID", "DEF");
        complete_url = URL_MEASUREMENT + ID;
        mainPresenter = new PresenterImpl();
        mainPresenter.fetchData(complete_url, new DataJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("measurements");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject measure = array.getJSONObject(i);
                        measureList.add(new MeasurementEntity(
                                measure.getInt("patient_id"),
                                measure.getInt("value"),
                                measure.getString("created_at")));
                    }
                    MeasureAdapter adapter = new MeasureAdapter(HistoryPageActivity.this, measureList);
                    recyclerView.setAdapter(adapter);
                } catch (JsonIOException | JSONException e) {
                    Log.e("", e.getMessage(), e);
                }

            }
        });
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

        if (id == R.id.action_logout) {
            final Intent intent = new Intent(this, LoginActivity.class);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mGoogleSignInClient.revokeAccess()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //this.startActivity(myIntent);
                            startActivity(intent);
                        }
                    });
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

