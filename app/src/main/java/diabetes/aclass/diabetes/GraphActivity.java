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
import android.widget.ImageButton;
import android.widget.TextView;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonIOException;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import diabetes.aclass.dagger.component.DataJsonCallback;
import diabetes.aclass.model.MeasurementEntity;
import diabetes.aclass.presenter.PresenterImpl;

import static diabetes.aclass.utils.Component.API_BASE;
import static diabetes.aclass.utils.Component.API_GET_USER_BYID;

public class GraphActivity extends AppCompatActivity {

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
    private PresenterImpl mainPresenter;
    private  String complete_url;
    private GraphView graph;
    private LineGraphSeries<DataPoint> lineGraphSeries;




    static final int DATE_DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         graph = findViewById(R.id.graph_hor);
        lineGraphSeries = new LineGraphSeries<>();
        graph.addSeries(lineGraphSeries);


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


        final Button button = (Button) findViewById(R.id.search_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               graph.removeSeries(lineGraphSeries);
             loadProducts(fromDate,toDate);
            }
        });


     /*
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3)
        });
        graph.addSeries(series);

        GraphView graph_point = findViewById(R.id.graph_point);
        LineGraphSeries<DataPoint> series_point = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3)
        });
        graph_point.addSeries(series_point);   */

    }

    private void loadProducts (Calendar from, Calendar to){

        final int to_day = toDate.get(Calendar.DAY_OF_MONTH);
        final int to_month = toDate.get(Calendar.MONTH);
        final int to_year= toDate.get(Calendar.YEAR) ;
        final int from_day= fromDate.get(Calendar.DAY_OF_MONTH);
        final int from_month= fromDate.get(Calendar.MONTH);
        final int from_year= fromDate.get(Calendar.YEAR) ;

        final Date from_date = new Date(from_year,from_month,from_day);
        final Date to_date = new Date(to_year,to_month,to_day);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String ID = preferences.getString("ID", "DEF");
        complete_url = API_BASE +"/measurements/getbyuid?patient_id=" + ID;
        mainPresenter = new PresenterImpl();
        mainPresenter.fetchData(complete_url, new DataJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int counter = 0;
                    int max = 0;
                    JSONArray array = response.getJSONArray("measurements");
                     lineGraphSeries = new LineGraphSeries<>();

                    for (int i = 0; i < array.length(); i++) {
                        MeasurementEntity puppetME;
                        Date puppetDATE;
                        JSONObject measure = array.getJSONObject(i);
                        puppetME = new MeasurementEntity(measure.getInt("patient_id"),
                                measure.getInt("value"),
                                measure.getString("created_at"));
                        puppetDATE = new Date(puppetME.getYear(),puppetME.getMonth(),puppetME.getDay());

                        if((!puppetDATE.before(from_date)) && (!puppetDATE.after(to_date))) {
                            if (puppetME.getValue()>max) max = puppetME.getValue();
                            DataPoint p = new DataPoint(counter,puppetME.getValue());
                            lineGraphSeries.appendData(p, true, 60);
                            counter++;

                        }
                    }
                    graph.addSeries(lineGraphSeries);
                    graph.getViewport().setMinX(0);
                    graph.getViewport().setMaxX(counter-1);
                    graph.getViewport().setMinY(0);
                    graph.getViewport().setMaxY(max);
                    graph.getViewport().setXAxisBoundsManual(true);
                    graph.getViewport().setYAxisBoundsManual(true);

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

    public void goToDoctorActivity(View view) {
        Intent myIntent = new Intent(this, DoctorProfileActivity.class);
        startActivity(myIntent);
    }

    public void goToHomePageActivity(View view) {
        Intent myIntent = new Intent(this, HomePageActivity.class);
        startActivity(myIntent);
    }

    public void goToInsertActivity(View view) {
        Intent myIntent = new Intent(this, InsertValueActivity.class);
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
