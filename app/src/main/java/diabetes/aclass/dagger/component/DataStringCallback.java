package diabetes.aclass.dagger.component;

import java.util.Map;

public interface DataStringCallback {
    Map<String, String> onPostSucces(Map<String, String> response);

}
