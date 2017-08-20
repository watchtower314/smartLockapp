package niravitalzohar.smartlock.smartlock;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Member;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static niravitalzohar.smartlock.smartlock.permission_type.MANGER;
import static niravitalzohar.smartlock.smartlock.permission_type.MEMBER;


public class Login extends AppCompatActivity {
    private static final String TAG = SignUp.class.getSimpleName();
    private Button signin;
    private EditText user;
    private EditText password;
    private ProgressDialog pDialog;
    private SessionManager session;
   // private SQLiteHandler db;
    private TextView new_member,new_mng,forgot;
    private String lockid;


    //for popup
   /* private PopupWindow mPopupWindow;
    private LinearLayout mLinearLayout;
    private Context mContext;
    private Activity mActivity;
    private TextView lockidTv,phone_mng;
    private EditText lockidET, phone_mngET;
    private Button v_button;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

     //   mContext = getApplicationContext();
      //  mActivity = Login.this;
       // mLinearLayout=(LinearLayout)findViewById(R.id.activity_login);

        user = (EditText) findViewById(R.id.emailET);
        password = (EditText) findViewById(R.id.passwordET);
        new_member = (TextView) findViewById(R.id.newmember);
        new_mng=(TextView) findViewById(R.id.newMngr);
        forgot=(TextView)findViewById(R.id.forgot);
        Button signin = (Button) findViewById(R.id.signin);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
       // db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // Login button Click Event
        signin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String Iemail = user.getText().toString().trim();
                String Ipassword = password.getText().toString().trim();

                // Check for empty data in the form
                if (!Iemail.isEmpty() && !Ipassword.isEmpty()) {
                    // login user
                  //  check(Iemail,Ipassword);
                    loginFunc(Iemail,Ipassword);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        new_member.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
              /*  SQLiteHandler.CURRENT_PERMISSION_TYPE= MEMBER;
                Intent intent = new Intent(getBaseContext(), mng_code.class);
               // intent.putExtra("PermissionType","member");
                startActivity(intent);*/
                Intent intent = new Intent(getBaseContext(), SignUp.class);
                // intent.putExtra("PermissionType","member");
                startActivity(intent);
            }

        });

        new_mng.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Log.d("jhh","hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
      //          openPopupMng();
                AppConfig.CURRENT_PERMISSION_TYPE=MANGER;
                Intent intent = new Intent(getBaseContext(), mng_code.class);
                //intent.putExtra("PermissionType","manager");
                startActivity(intent);
            }

        });






    }//end on create

    public void loginFunc(final String Iemail, final String Ipassword){
        String tag_string_req = "req_login";
        pDialog.setMessage("verifing login details ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "Login Response: " + response.toString());
                //  hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");

                    if(status.equals("success")){
                         JSONObject c = jObj.getJSONObject("message");
                        String token=c.getString("token");
                        Log.d("token",token);


                        Toast.makeText(getApplicationContext(), "User logged in successfully", Toast.LENGTH_LONG).show();
                        AppConfig.CURRENT_USERNAME=Iemail;
                        AppConfig.TOKEN=token;
                        Intent intent = new Intent(Login.this,
                                UsersLocks.class);
                        startActivity(intent);

                        // Launch login activity

                    } else {
                        String msg=jObj.getString("message");
                        String passError=msg;
                        if(msg.equals("Need to verify mail first")){
                            Intent intent = new Intent(Login.this,
                                    RegCode.class);
                            intent.putExtra("username",Iemail);
                            startActivity(intent);

                        }
                        Toast.makeText(getApplicationContext(),
                                passError, Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GJKKK", "Registration Error: " + error.getMessage());
                String errorMsg ="login failed please try again";
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
                  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", Iemail);
                params.put("password", Ipassword);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_screen, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.help:
                Intent intent = new Intent(Login.this,
                        Help.class);
                startActivity(intent);

                return true;

        }

        return false;
    }


    }//end class
/*    private static final String TAG = SignUp.class.getSimpleName();
    private Button signin;
    private EditText user;
    private EditText password;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    private TextView new_member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText) findViewById(R.id.emailET);
        password = (EditText) findViewById(R.id.passwordET);
        new_member=(TextView)findViewById(R.id.newmember);
        Button signin=(Button)findViewById(R.id.signin);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



// Login button Click Event
        signin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String Iuser = user.getText().toString().trim();
                String Ipassword = password.getText().toString().trim();

                // Check for empty data in the form
                if (!Iuser.isEmpty() && !Ipassword.isEmpty()) {
                    // login user
                    String userid="58e91fd7fafa6700044b8d61";
                    checkLogin(Iuser, Ipassword,userid);
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        new_member.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String ip="12";
                



            }

            });



    }

    private void checkLogin(final String Iemail, final String Ipassword , final String userid) {

        String uri = String.format("https://smartlockproject.herokuapp.com/api/getUser?param1=%1$s",userid);
       */ /*StringRequest myReq = new StringRequest(Method.GET,
                uri,
                createMyReqSuccessListener(),
                createMyReqErrorListener());*/
        // Tag used to cancel the request
        /*        String tag_string_req = "req_login";

               pDialog.setMessage("Logging in ...");
                showDialog();

                StringRequest strReq = new StringRequest(Method.GET,
                        uri, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response.toString());
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                           //boolean error = jObj.getBoolean("error");

                            // Check for error node in json
                            if (response.toString()!="error") {
                                // user successfully logged in
                                // Create login session
                                //session.setLogin(true);

                                // Now store the user in SQLite
                                String uid = jObj.getString("_id");
                                String username = jObj.getString("username");
                                String phone = jObj.getString("phone");
                                String password = jObj.getString("password");

                                //
                                //JSONObject user = jObj.getJSONObject("user");
                                //String name = user.getString("name");
                                //String email = user.getString("email");
                                //String created_at = user
                                //        .getString("created_at");

                                // Inserting row in users table
                               // db.addUser(name, email, uid, created_at);*/



 /*                               // Launch main activity
                                if (Ipassword.equals(password)) {
                                    Intent intent = new Intent(Login.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                // Error in login. Get the error message
                                String errorMsg = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                error.getMessage(), Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        // Posting parameters to login url
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userid", userid);
                       // params.put("password", password);

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


}//end class



 /*
        Log.d("im","herero");
        pDialog.setMessage("Logging in ...");
        showDialog();
        String uid = "1234";
        String phone = "05234455637";
        String resultpass=db.getSinlgeEntry(email);
        if (resultpass.equals(password)){
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Log.d("jjj","no good");
        }



    }

        private void showDialog() {
            if (!pDialog.isShowing())
                pDialog.show();
        }

        private void hideDialog() {
            if (pDialog.isShowing())
                pDialog.dismiss();
        }



*/