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

import java.util.HashMap;
import java.util.Map;

import diabetes.aclass.dagger.component.DataJsonCallback;
import diabetes.aclass.model.UserEntity;
import diabetes.aclass.presenter.PostManagement;
import diabetes.aclass.presenter.PresenterImpl;

import static diabetes.aclass.utils.Component.API_BASE;
import static diabetes.aclass.utils.Component.API_GET_MEDICS;
import static diabetes.aclass.utils.Component.API_POST_USER;

public class InsertMedicActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private int spinner_index = -1;
    private static  String[] NAMES ;
    private static  String[] NAMES2 = {"a","b","c"} ;
    private PresenterImpl mainPresenter ;
    private Map<String, Integer> map= new HashMap<>();
    private int medic_id;
    private ArrayAdapter<String> adapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_medic_layout);
        mainPresenter = new PresenterImpl();
        spinner = (Spinner)findViewById(R.id.spinner1);

        loadMedics();

      final Button button = (Button) findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){

                if(spinner_index != -1){

                    String res = NAMES[spinner_index];
                    medic_id = map.get(res);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(InsertMedicActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();

                    editor.putInt("MEDIC_ID", medic_id);
                    editor.apply();
                    UserEntity logged_user = new UserEntity();

                    logged_user.setId(preferences.getString("ID", "ID_DEF"));
                    logged_user.setFirst_name(preferences.getString("NAME", "FN_DEF"));
                    logged_user.setLast_name(preferences.getString("SURNAME", "LN_DEF"));
                    logged_user.setEmail(preferences.getString("MAIL", "EMAIL_DEF"));
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

       this.spinner_index = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
       this.spinner_index = -1;
    }

    public void loadMedics(){

        mainPresenter.fetchData(API_GET_MEDICS, new DataJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("medics");
                    NAMES = new String[array.length()];
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject medic = array.getJSONObject(i);
                        NAMES[i] = medic.getString("medic_name");
                        map.put( medic.getString("medic_name"),medic.getInt("medic_id"));

                    }
                    adapter = new ArrayAdapter<String>(InsertMedicActivity.this,
                            android.R.layout.simple_spinner_item, NAMES);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(InsertMedicActivity.this);

                } catch (JsonIOException | JSONException e) {
                    Log.e("", e.getMessage(), e);
                }


            }
         });

    }

    private void saveUser(UserEntity userEntity){
        PostManagement pm = new PostManagement();

        try {
            String url = API_POST_USER;
            Gson json = new Gson();
            String postdata = json.toJson(userEntity);
            pm.saveData(url, postdata);
        } catch(Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}