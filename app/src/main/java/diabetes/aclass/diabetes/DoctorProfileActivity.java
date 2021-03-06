package diabetes.aclass.diabetes;

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
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import diabetes.aclass.dagger.component.DataJsonCallback;
import diabetes.aclass.model.MedicEntity;
import diabetes.aclass.presenter.PostManagement;
import diabetes.aclass.presenter.PresenterImpl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static diabetes.aclass.utils.Component.API_BASE;
import static diabetes.aclass.utils.Component.API_GET_MEDIC_BYID;
import static diabetes.aclass.utils.Component.API_POST_MEASUREMENTS;

public class DoctorProfileActivity extends AppCompatActivity {

    PresenterImpl mainPresenter ;
    private TextView name;
    private TextView email;
    private TextView hospital;
    public MedicEntity assigned_medic;
    private SharedPreferences preferences ;
    SharedPreferences.Editor editor;
    private Set<String> set ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_profile_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name =   findViewById(R.id.doc_name);
        email =  findViewById(R.id.doc_email);
        hospital =  findViewById(R.id.clinic_hospital);
        name.setText("serverNotResponding");
        email.setText("serverNotResponding");
        hospital.setText("serverNotResponding");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();
        set = preferences.getStringSet("TO_SEND", null);

        if(!set.isEmpty()){
            saveOLDMeasurements();
        }

        loadData();


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


    private void loadData(){
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            int MEDIC_ID = preferences.getInt("MEDIC_ID", -1);
            mainPresenter = new PresenterImpl();
            mainPresenter.fetchData(API_GET_MEDIC_BYID+MEDIC_ID, new DataJsonCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        JSONArray array = response.getJSONArray("medic");
                        assigned_medic = new MedicEntity();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject medic = array.getJSONObject(i);
                            name.setText(medic.getString("medic_name"));
                            email.setText(medic.getString("medic_mail"));
                            hospital.setText(medic.getString("medic_hospital"));
                        }

                    } catch (JsonIOException | JSONException e) {
                        Log.e("", e.getMessage(), e);
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void goToHomePageActivity(View view) {
        Intent myIntent = new Intent(this, HomePageActivity.class);
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
