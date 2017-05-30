package niravitalzohar.smartlock.smartlock;

/**
 * Created by zohar on 06/04/2017.
 */

public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "https://smartlockproject.herokuapp.com/api/getUser";

    // Server user register url
    public static String URL_REGISTER = "https://smartlockproject.herokuapp.com/api/addUser";

    public static String ADD_PERMISSION=" https://smartlockproject.herokuapp.com/api/addPermission";

    public static String LOCK="https://smartlockproject.herokuapp.com/api/requestLockAction/lock";

    public static String UNLOCK="https://smartlockproject.herokuapp.com/api/requestLockAction/unlock";

    public static String ADD_FINGER_PRINT="https://smartlockproject.herokuapp.com/api/requestLockAction/addFingerprint";

    public static String REMOVE_FINGER_PRINT="https://smartlockproject.herokuapp.com/api/requestLockAction/deFingerprint";

    public static String CHECK_LOCK_STATUS="https://smartlockproject.herokuapp.com/api/requestLockAction/checkStatus";

    public static String GET_ACTION="https://smartlockproject.herokuapp.com/api/checkLockAction/";








    //"https://smartlockproject.herokuapp.com/api/getPermission"
}