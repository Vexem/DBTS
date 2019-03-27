package diabetes.aclass.presenter;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;

import java.util.HashMap;
import java.util.Map;

import diabetes.aclass.base.NetworkController;
import diabetes.aclass.dagger.component.DataJsonCallback;
import diabetes.aclass.dagger.component.DataStringCallback;

public class PresenterImpl {


    public void fetchData(final String url, final DataJsonCallback callback){
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

    public void fetchArray(final String url, final DataJsonCallback callback){
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JsonArray response){
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



    public void saveData(final String url, final DataStringCallback callback){
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("", response.toString());
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                callback.onPostSucces(params);
                return params;
            }
        };
        NetworkController.getInstance().addToRequestQueue(request);
    }


    private void showLog(String message){
        Log.d("skm",message==null?"":message);
    }


}