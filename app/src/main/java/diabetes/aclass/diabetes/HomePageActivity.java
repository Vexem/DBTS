package diabetes.aclass.diabetes;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import diabetes.aclass.dagger.component.DataJsonCallback;
import diabetes.aclass.model.MeasurementEntity;
import diabetes.aclass.presenter.PostManagement;
import diabetes.aclass.presenter.PresenterImpl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

import static diabetes.aclass.utils.Component.API_BASE;
import static diabetes.aclass.utils.Component.API_POST_MEASUREMENTS;
import static diabetes.aclass.utils.Component.API_POST_USER;

/**
 * A login screen that offers login via email/password.
 */
public class HomePageActivity extends AppCompatActivity {

    private static final String URL_MEASUREMENT = API_BASE +"/measurements/dailyvaluesbyid?patient_id=";

    private boolean value = false;
    public Button button;
    private TextView last_meas;
    private SharedPreferences preferences ;
    SharedPreferences.Editor editor;
    private Set<String> set ;


    List<MeasurementEntity> measureList;
    MeasureAdapter adapter ;
    Boolean adapterInitialized = false;
    RecyclerView recyclerView;
    private  String complete_url;
    private PresenterImpl mainPresenter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.homepage_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        measureList = new ArrayList<>();


        //set the date box value with today date
        EditText dateBoxText = findViewById(R.id.date_box);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dateBoxText.setText(sdf.format(new Date()));


        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        set = preferences.getStringSet("TO_SEND", null);



        last_meas =   findViewById(R.id.last_meas);
        last_meas.setText(Integer.toString(preferences.getInt("LAST_MEAS", -1)));
        editor.apply();

        if(!set.isEmpty()){
        saveOLDMeasurements();
        }


        FloatingActionButton fab = findViewById(R.id.insertvalue);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLangDialog();
            }
        });

        loadProducts();

    }

    private void loadProducts() {


        String ID = preferences.getString("ID", "DEF");
        complete_url = URL_MEASUREMENT + ID;
        mainPresenter = new PresenterImpl();
        mainPresenter.fetchData(complete_url, new DataJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("daily_measurements");
                    for (int i = 0; i < array.length(); i++) {
                        MeasurementEntity puppetME;
                        JSONObject measure = array.getJSONObject(i);

                            measureList.add(new MeasurementEntity(
                                    measure.getString("patient_id"),
                                    measure.getInt("value"),
                                    measure.getString("created_at")));
                            adapter = new MeasureAdapter(HomePageActivity.this, measureList);
                            if(!adapterInitialized) adapterInitialized = true;
                            recyclerView.setAdapter(adapter);

                    }

                } catch (JsonIOException | JSONException e) {
                    Log.e("", e.getMessage(), e);
                }
            }
        });
        Toast.makeText(this, "Daily Values Updated", Toast.LENGTH_SHORT).show();

    }






    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.insert_popup, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        edt.setInputType(InputType.TYPE_CLASS_NUMBER);

        final String ID = preferences.getString("ID", "DEF");
        Long tsLong = System.currentTimeMillis()/1000;
        final String ts = tsLong.toString();

        dialogBuilder.setTitle("Insert Value");
        dialogBuilder.setMessage("Enter value below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                MeasurementEntity measurementEntity = new MeasurementEntity();
                measurementEntity.setId(ID);
                int value =  Integer.parseInt(edt.getText().toString());
                editor.putInt("LAST_MEAS", value);
                editor.apply();
                last_meas.setText(Integer.toString(preferences.getInt("LAST_MEAS", -1)));
                measurementEntity.setValue(value);

                measurementEntity.setCreated_atDate(ts);
                saveMeasurement(measurementEntity);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void saveMeasurement(MeasurementEntity measurementEntity){
        PostManagement pm = new PostManagement();
        Gson json = new Gson();
        final String postdata = json.toJson(measurementEntity);
        String url = API_POST_MEASUREMENTS;

        pm.saveData(url, postdata, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                set.add(postdata);
                editor.putStringSet("TO_SEND", set);
                editor.apply();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });


    }

    public void saveOLDMeasurements(){

        PostManagement pm = new PostManagement();
        String url = API_POST_MEASUREMENTS;
        boolean up = true;
        Iterator<String> it = set.iterator();
        while(it.hasNext()&& up ){
            final String put = it.next();

            pm.saveData(url, put, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    set.remove(put);
                    editor.putStringSet("TO_SEND", set);
                    editor.apply();
                }
            });

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

        if (id == R.id.edit1){
            showChangeLangDialog();
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

    public void updateRC(View view){
        measureList.clear(); // clear list
        if(adapterInitialized){
            adapter.notifyDataSetChanged();}
        loadProducts();

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

