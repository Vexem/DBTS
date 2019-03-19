package diabetes.aclass.presenter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.URL;

import diabetes.aclass.base.NetworkController;
import diabetes.aclass.dagger.component.DataCallback;

public class ProfilePresenterImpl {


    public void fetchData(final String url, final DataCallback callback){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("", response.toString());
                callback.onSuccess(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });
        NetworkController.getInstance().addToRequestQueue(request);
    }


    private void showLog(String message){
        Log.d("skm",message==null?"":message);
    }


}