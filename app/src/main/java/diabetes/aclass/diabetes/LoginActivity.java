package diabetes.aclass.diabetes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.HashSet;
import java.util.Set;

import diabetes.aclass.model.UserEntity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private static final String TAG = "AndroidClarified";
    private GoogleSignInClient mGoogleSignInClient;
    public static final String GOOGLE_ACCOUNT = "google_account";
    private int RC_SIGN_IN = 0;
    public UserEntity logged_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> set = new HashSet<String>();
        editor.putStringSet("TO_SEND", set);
        editor.apply();


        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);

        } else {
            setContentView(R.layout.activity_login);
            SignInButton googleSignInButton = findViewById(R.id.sign_in_button);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

            googleSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            });
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            // The Task returned from this call is always completed, no need to attach
            // a listener.

            try {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                handleSignInResult(account);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            }
        }
    }


    private void handleSignInResult(final GoogleSignInAccount account) {
        Intent myIntent = new Intent(getApplicationContext(), InsertMedicActivity.class);
        loadData(account);
        startActivity(myIntent);
    }

    private void loadData(final GoogleSignInAccount account){

        logged_user = new UserEntity();
        logged_user.setId(account.getId());
        logged_user.setFirst_name(account.getGivenName());
        logged_user.setLast_name(account.getFamilyName());
        logged_user.setEmail(account.getEmail());
        Log.d("account", account.toString());
        store_logged_user(logged_user);

    }


    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, HomePageActivity.class);
        intent.putExtra(LoginActivity.GOOGLE_ACCOUNT, googleSignInAccount);
        startActivity(intent);
        finish();
    }
    
    public void store_logged_user(UserEntity logged_user){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ID",logged_user.getId());
        editor.putString("MAIL",logged_user.getEmail());
        editor.putString("NAME",logged_user.getFirst_name());
        editor.putString("SURNAME",logged_user.getLast_name());

        editor.apply();
    }

}