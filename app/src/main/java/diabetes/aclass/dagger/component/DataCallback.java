package diabetes.aclass.dagger.component;

import org.json.JSONObject;

public interface DataCallback {
    void onSuccess(JSONObject response);
}
