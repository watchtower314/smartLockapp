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
    private SQLiteHandler db;
    private TextView new_member,new_mng;
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
        Button signin = (Button) findViewById(R.id.signin);


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
                String Iemail = user.getText().toString().trim();
                String Ipassword = password.getText().toString().trim();

                // Check for empty data in the form
                if (!Iemail.isEmpty() && !Ipassword.isEmpty()) {
                    // login user
                    check(Iemail,Ipassword);
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
                SQLiteHandler.CURRENT_PERMISSION_TYPE=MANGER;
                Intent intent = new Intent(getBaseContext(), mng_code.class);
                //intent.putExtra("PermissionType","manager");
                startActivity(intent);
            }

        });




    }//end on create
    /*
   public void  openPopupMng(){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.mng_pop,null);

        mPopupWindow = new PopupWindow(
                customView,
                LinearLayout.LayoutParams.MATCH_PARENT, //width
                LinearLayout.LayoutParams.WRAP_CONTENT//height
        );

        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

     //   mLinearLayout=(LinearLayout)customView.findViewById(R.id.mng_popLayout) ;
     //   phone_mng=(TextView)findViewById(R.id.phone_mng);
      // phone_mngET=(EditText)findViewById(R.id.phone_mngET);
        lockidTv=(TextView)customView.findViewById(R.id.lockidTV) ;
        lockidET=(EditText) customView.findViewById(R.id.lockidET) ;
      v_button=(Button)customView.findViewById(R.id.v_button);


       v_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String lockid=lockidET.getText().toString().trim();
                Log.d("kkk",lockid);


                Intent intent = new Intent(Login.this,
                       SignUp.class);
                startActivity(intent);

                //get phone number and lock id and check if match , pass to sign up activity flag of manger
               // mPopupWindow.dismiss();

            }
        });





       mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER,0,0);



    } //popup2 end
    */

    public void check(final String Iemail, final String Ipassword){
      //  String uid="58e91fd7fafa6700044b8d61";
        /*String uri = String.format("https://smartlockproject.herokuapp.com/api/getUser?param1=%1$s",
                uid);*/
        pDialog.setMessage("verifing login details ...");
        showDialog();
        String uri="https://smartlockproject.herokuapp.com/api/getUser/"+Iemail;

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "login chk Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject c = jsonObj.getJSONObject("message");
                            String password = c.getString("password");
                            Log.d("login password",password);


                            if (Ipassword.equals(password)) {
                                SQLiteHandler.CURRENT_USERNAME=Iemail;
                                //chkUserType(Iemail);
                                Intent intent = new Intent(Login.this,
                                            UsersLocks.class);
                               // intent.putExtra("username", Iemail);
                                startActivity(intent);

                            }
                            else {
                                String passError="no valid password plese try again";
                                Toast.makeText(getApplicationContext(),
                                        passError, Toast.LENGTH_LONG).show();
                                hideDialog();


                            }





                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                        String errorMsg ="login failed please try again";
                      //  Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void chkUserType(String email){
        String uri="https://smartlockproject.herokuapp.com/api/getPermission/"+email+"/"+SQLiteHandler.CURRENT_LOCKID;

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "permission response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            int type = jsonObj.getInt("type");
                            Log.d("type",Integer.toString(type));

                            if ((SQLiteHandler.CURRENT_PERMISSION_TYPE).ordinal() == type) {
                                //  Intent intent = new Intent(SignUp.this,
                                //        Login.class);
                                //startActivity(intent);
                            }
                            else{
                                pDialog.setMessage("your permission is "+type);
                                showDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



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