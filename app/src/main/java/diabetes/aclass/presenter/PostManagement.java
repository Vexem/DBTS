package diabetes.aclass.presenter;

import android.util.Log;


import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.support.constraint.Constraints.TAG;

public class PostManagement {
    public void saveData(String url, String postdata){
        OkHttpClient client = new OkHttpClient();

        MediaType MEDIA_TYPE = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(MEDIA_TYPE, postdata);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.w("failure Response", "failure Response");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String mMessage = response.body().string();
                Log.e(TAG, mMessage);
            }
        });
    }
}
