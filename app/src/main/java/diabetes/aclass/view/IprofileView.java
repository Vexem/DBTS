package diabetes.aclass.view;

import com.android.volley.VolleyError;

import java.util.List;

import diabetes.aclass.model.UserEntity;

public interface IprofileView {
    void controlProgressBar(boolean isShowProgressBar);
    void onSuccess(List<UserEntity> dayWiseDataList);
    void onFaillure(VolleyError error);

    void showErrorMessage(String errorMessage);
}
