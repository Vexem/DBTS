package diabetes.aclass.diabetes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
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

import java.util.List;
import java.util.Map;

import diabetes.aclass.dagger.component.DataJsonCallback;
import diabetes.aclass.model.UserEntity;
import diabetes.aclass.presenter.PresenterImpl;
import diabetes.aclass.view.IprofileView;

import static diabetes.aclass.utils.Component.API_BASE;


public class ProfileActivity extends AppCompatActivity implements IprofileView {

    PresenterImpl mainPresenter ;
    public RequestQueue mRequestQueue;
    public Context context;
    private static final String API_URL = API_BASE + "/users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.profile_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        final EditText firstnametext = findViewById(R.id.firstname);
        final EditText lastName = findViewById(R.id.lastname);
        mainPresenter = new PresenterImpl();
        mainPresenter.fetchData(API_URL, new DataJsonCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray users = response.getJSONArray("users");
                    for (int i = 0; i < users.length(); i++) {
                        JSONObject jsonObject = users.getJSONObject(i);
                        Gson gson = new Gson();
                        UserEntity u = gson.fromJson(jsonObject.toString(), UserEntity.class);
                        firstnametext.setText(u.getFirst_name());
                        lastName.setText(u.getLast_name());
                    }
                } catch (JsonIOException | JSONException e) {
                    Log.e("", e.getMessage(), e);
                }

            }
        });
        setSupportActionBar(toolbar);

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

    @Override
    public void controlProgressBar(boolean isShowProgressBar) {

    }

    @Override
    public void onSuccess(List<UserEntity> userEntityList) {
        if(userEntityList!=null && userEntityList.size()>0){

        }else{
            showToastMessage("No Data Found");
        }

    }

    @Override
    public void onFaillure(VolleyError error) {
        showToastMessage(error.getMessage());
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        showToastMessage(errorMessage);

    }

    private void showToastMessage(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

}
