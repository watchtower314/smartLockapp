package niravitalzohar.smartlock.smartlock;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zohar on 06/04/2017.
 */
enum permission_type { MANGER, MEMBER_WITH_PY_ID, MEMBER,GUEST }


public class AppConfig extends AppCompatActivity {

    //StringBuilder requestId = new StringBuilder();
    public static permission_type CURRENT_PERMISSION_TYPE;
    public static String CURRENT_LOCKID;
    public static String CURRENT_USERNAME;
    public static String TOKEN;

    // Server user login url
    public static String URL_LOGIN = "https://smartlockproject.herokuapp.com/api/getUser";

    public static String LOGIN_URL="https://smartlockproject.herokuapp.com/api/login";

    // Server user register url
    public static String URL_REGISTER = "https://smartlockproject.herokuapp.com/api/addUser";

    public static String ADD_PERMISSION=" https://smartlockproject.herokuapp.com/api/addPermission";

    public static String LOCK="https://smartlockproject.herokuapp.com/api/requestLockAction/lock";

    public static String UNLOCK="https://smartlockproject.herokuapp.com/api/requestLockAction/unlock";

    public static String ADD_FINGER_PRINT="https://smartlockproject.herokuapp.com/api/requestLockAction/addFingerprint";

    public static String REMOVE_FINGER_PRINT="https://smartlockproject.herokuapp.com/api/requestLockAction/delFingerprint";

    public static String CHECK_LOCK_STATUS="https://smartlockproject.herokuapp.com/api/requestLockAction/checkStatus";

    public static String GET_ACTION="https://smartlockproject.herokuapp.com/api/checkLockAction/";

   public static String CHECK_LOCK_ACTION="https://smartlockproject.herokuapp.com/api/checkLockAction/";

    public static String CHNG_PASS="https://smartlockproject.herokuapp.com/api/changePassword/";

    public static String Forgot_PASS="https://smartlockproject.herokuapp.com/api/forgotPassword/";

    public static String OPEN_MNG_ACCOUNT="https://smartlockproject.herokuapp.com/api/openManagerAccount";

    public static String SEND_MSG="https://smartlockproject.herokuapp.com/api/contactUs";

    public static String LOGS="https://smartlockproject.herokuapp.com/api/getUserLogs";

    public static String SEND_EMAIL="https://smartlockproject.herokuapp.com/api/sendMemberMessage";

    public static String SEND_CODE="https://smartlockproject.herokuapp.com/api/validationCode";

    public static String SEND_CODE_AGAIN="https://smartlockproject.herokuapp.com/api/sendValidCode";

    public static void showDialog(ProgressDialog pDialog) {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public static void hideDialog(ProgressDialog pDialog) {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }










    //"https://smartlockproject.herokuapp.com/api/getPermission"
}