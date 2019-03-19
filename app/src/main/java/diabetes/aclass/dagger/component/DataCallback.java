package diabetes.aclass.dagger.component;

import org.json.JSONObject;

import diabetes.aclass.model.UserEntity;

public interface DataCallback {
    void onSuccess(JSONObject response);
}
