package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static niravitalzohar.smartlock.smartlock.permission_type.MANGER;


public class SignUp extends AppCompatActivity {
    private static final String TAG = SignUp.class.getSimpleName();
    private EditText _password;
    private EditText _email;
    private EditText _phone;
    private Button signup;
    private ProgressDialog pDialog;
    private SessionManager session;
    private boolean flag =true; // there is manger
    private TextView already;

    private String lockid=" ";


    @Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);
        if (AppConfig.CURRENT_PERMISSION_TYPE==MANGER) {
            lockid = getIntent().getStringExtra("lockid");
            Log.d("kk", lockid);
        }


    //    _username = (EditText) findViewById(R.id.usernameET);
    _password = (EditText) findViewById(R.id.passwordET);
    _email = (EditText) findViewById(R.id.emailET);
    _phone = (EditText) findViewById(R.id.phoneEt);
    signup = (Button) findViewById(R.id.signup);
        already=(TextView)findViewById(R.id.alredy);

    // Progress dialog
    pDialog = new ProgressDialog(this);
    pDialog.setCancelable(false);

    // Session manager
    session = new SessionManager(getApplicationContext());
    // Check if user is already logged in or not
    if (session.isLoggedIn()) {
        // User is already logged in. Take him to main activity
        Intent intent = new Intent(SignUp.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Register Button Click event
    signup.setOnClickListener(new View.OnClickListener() {

        public void onClick(View view) {
            String password = _password.getText().toString().trim();
            String email = _email.getText().toString().trim();
            String phone = _phone.getText().toString().trim();

            if (!phone.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                  if(AppConfig.CURRENT_PERMISSION_TYPE==MANGER) {
                      openMngAccount(email,lockid,password,phone);
                   }
                  else {
                      registerUser(email, phone, password);
                  }
            } else {
                Toast.makeText(getApplicationContext(),
                        "Please enter your details!", Toast.LENGTH_LONG)
                        .show();
            }
        }
    });

        already.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Launch login activity
                Intent intent = new Intent(
                        SignUp.this,
                        Login.class);
                startActivity(intent);

            }

        });

  } //end onCreate
 //this func creats new user account and give him automatically manger permmision
    public void openMngAccount(final String Iemail, final String Ilockid, final String Ipassword, final String Iphone){
        String tag_string_req = "openMngAccount";

          pDialog.setMessage("opening account ...");
         showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.OPEN_MNG_ACCOUNT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "Add permission Response: " + response.toString());
                 hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");
                    if(status.equals("success")){

                        Toast.makeText(getApplicationContext(), "New account successfully was created y login now!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(
                                SignUp.this, RegCode.class);
                        intent.putExtra("username",Iemail);
                        startActivity(intent);

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = status;
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GJKKK", "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",Iemail);
                params.put("lockid", Ilockid);
                params.put("password", Ipassword);
                params.put("phone", Iphone);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }


    /**
     * creates account with out giving him permission (cus he already have permission given him by the lock manger)
     * */
    private void registerUser(final String username, final String phone,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");
                    if (status.equals("success")) {

                        JSONObject c = jObj.getJSONObject("message");
                        String uid = c.getString("userid");
                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch RegCode activity
                        Intent intent = new Intent(
                                SignUp.this, RegCode.class);
                        intent.putExtra("username",username);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                         String errorMsg = status;
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("phone", phone);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rest, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.help:
                Intent intent = new Intent(SignUp.this,
                        Help.class);
                startActivity(intent);

                return true;

        }

        return false;
    }


}//end class
