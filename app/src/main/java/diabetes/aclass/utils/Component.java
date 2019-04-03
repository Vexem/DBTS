package diabetes.aclass.utils;

public class Component {
//    public static final String API_BASE = "http://192.168.1.73/api/v1/";
//    public static final String API_BASE = "http://192.168.43.70/";
        public static final String API_BASE = "http://192.168.1.7/";
    public static final String API_GET_USERS = API_BASE + "users";
    public static final String API_GET_USER_BYID = API_BASE + "users/getuserbyid?user_id=";
    public static final String API_POST_USER = API_BASE + "users/saveuser";
    public static final String API_GET_MEDICS = API_BASE + "medics";
    public static final String API_GET_MEDIC_BYID = API_BASE + "medics/getbyid?medic_id=";
    public static final String API_GET_MEDICINES = API_BASE + "medicines";
    public static final String API_GET_MEASUREMENTS = API_BASE + "measurements";
    public static final String API_POST_MEASUREMENTS = API_BASE + "measurements/save";
    public static final String API_GET_MEASUREMENTS_BYID = API_BASE + "measurements/getbyuid?patient_id=";

    public static final String GOOGLE_AUTH = "http://dbts.com/auth/google_oauth2/";
}
