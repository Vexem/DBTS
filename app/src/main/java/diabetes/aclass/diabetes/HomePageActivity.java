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
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import diabetes.aclass.model.MeasurementEntity;
import diabetes.aclass.presenter.PostManagement;

import static diabetes.aclass.utils.Component.API_POST_MEASUREMENTS;
import static diabetes.aclass.utils.Component.API_POST_USER;

/**
 * A login screen that offers login via email/password.
 */
public class HomePageActivity extends AppCompatActivity {
    private boolean value = false;
    public Button button;
    private TextView last_meas;

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

        last_meas =   findViewById(R.id.last_meas);


        FloatingActionButton fab = findViewById(R.id.insertvalue);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLangDialog();
            }
        });

    }


    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.insert_popup, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        edt.setInputType(InputType.TYPE_CLASS_NUMBER);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
                last_meas.setText(edt.getText().toString());
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

        try {
            String url = API_POST_MEASUREMENTS;
            Gson json = new Gson();
            String postdata = json.toJson(measurementEntity);
            pm.saveData(url, postdata);
            Toast.makeText(this, "Value Inserted", Toast.LENGTH_SHORT).show();

        } catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
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

