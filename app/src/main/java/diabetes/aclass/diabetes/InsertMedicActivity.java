package diabetes.aclass.diabetes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import diabetes.aclass.dagger.component.DataJsonCallback;
import diabetes.aclass.model.MeasurementEntity;
import diabetes.aclass.model.UserEntity;
import diabetes.aclass.presenter.PostManagement;
import diabetes.aclass.presenter.PresenterImpl;

import static diabetes.aclass.utils.Component.API_BASE;

public class InsertMedicActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private int medicID =-1;
    private static final String API_URL = API_BASE + "/users";
    private static final String[] NAMES = {"Donald Duck", "Goofy", "Mickey Mouse","Scrooge McDuck"};
    private PresenterImpl mainPresenter ;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_medic_layout);

        spinner = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(InsertMedicActivity.this,
                android.R.layout.simple_spinner_item, NAMES);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        final Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                if(medicID != -1){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InsertMedicActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt("MEDIC_ID",medicID);
                    UserEntity logged_user = new UserEntity();

                    logged_user.setId(preferences.getString("ID", "ID_DEF"));
                    logged_user.setFirst_name(preferences.getString("ID", "FN_DEF"));
                    logged_user.setLast_name(preferences.getString("ID", "LN_DEF"));
                    logged_user.setEmail(preferences.getString("ID", "EMAIL_DEF"));
                    logged_user.setMedic_id(preferences.getInt("MEDIC_ID", -1));

                    saveUser(logged_user);

                    Intent myIntent = new Intent(getApplicationContext(), HomePageActivity.class);
                    startActivity(myIntent);

                }

                else{
                    Toast.makeText(InsertMedicActivity.this,"Insert a medic",Toast.LENGTH_SHORT).show();
                }

            } });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

       this.medicID = position+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
       this.medicID = -1;
    }

    public void loadMedics(){
        mainPresenter = new PresenterImpl();
        mainPresenter.fetchData(complete_url, new DataJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("measurements");
                    for (int i = 0; i < array.length(); i++) {
                        MeasurementEntity puppetME;
                        Date puppetDATE;
                        JSONObject measure = array.getJSONObject(i);
                        puppetME = new MeasurementEntity(measure.getInt("patient_id"),
                                measure.getInt("value"),
                                measure.getString("created_at"));
                        puppetDATE = new Date(puppetME.getYear(),puppetME.getMonth(),puppetME.getDay());

                        if((!puppetDATE.before(from_date)) && (!puppetDATE.after(to_date))) {
                            //                         if (to_year==puppetME.getYear()) {
                            //                              if (to_day >= puppetME.getDay() && puppetME.getDay() >= from_day) {

                            measureList.add(new MeasurementEntity(
                                    measure.getInt("patient_id"),
                                    measure.getInt("value"),
                                    measure.getString("created_at")));
                            adapter = new MeasureAdapter(HistoryPageActivity.this, measureList);
                            if(!adapterInitialized) adapterInitialized = true;
                            recyclerView.setAdapter(adapter);
                            //                              }
                            //                           }
                        }
                    }

                } catch (JsonIOException | JSONException e) {
                    Log.e("", e.getMessage(), e);
                }

            }
        });




    }

    private void saveUser(UserEntity userEntity){
        PostManagement pm = new PostManagement();

        try {
            String url = API_URL + "/saveuser";
            Gson json = new Gson();
            String postdata = json.toJson(userEntity);
            pm.saveData(url, postdata);
        } catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}