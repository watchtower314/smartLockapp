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
    //private SQLiteHandler db;
    //private boolean flag =false;
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

    // SQLite database handler
   // db = new SQLiteHandler(getApplicationContext());

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
                //  if( checkLockManger(lockid)==false) {//if its false therefore there is no manger for this lock and i want to chek if he acctally can create manger accont
               // if( checkLockManger(lockid)==true) { //there is no manger
                 //     Log.d("flag", String.valueOf(flag));
                  //   addPermission(email,lockid);
              //     }
               /*   else {
                       //if there is manger for this lock chk if user have the right permissio
                       checkUserPermission(email,lockid);
                   }*/
                  if(AppConfig.CURRENT_PERMISSION_TYPE==MANGER) {
                      openMngAccount(email,lockid,password,phone);
                   }
                //either way add this user
                //TODO cus i dont add permission by myself it wont show the lock cus he dosent have any permission to this lock
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

    public void openMngAccount(final String Iemail, final String Ilockid, final String Ipassword, final String Iphone){
        String tag_string_req = "openMngAccount";
        //final String lockid="323djdjw32";
        //final String userid="58e91fd7fafa6700044b8d61";

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

                        // Launch login activity

                        Intent intent = new Intent(
                                SignUp.this, RegCode.class);
                        intent.putExtra("username",Iemail);
                                //Login.class);
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

        public void addPermission(final String email,final String lockid){
            String tag_string_req = "req_register";
            //final String lockid="323djdjw32";
            //final String userid="58e91fd7fafa6700044b8d61";

            //  pDialog.setMessage("Registering ...");
            // showDialog();

            StringRequest strReq = new StringRequest(Request.Method.POST,
                    AppConfig.ADD_PERMISSION, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.d("bnnjjj", "Add permission Response: " + response.toString());
                    //  hideDialog();

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String status=jObj.getString("status");
                        if(status.equals("success")){

                            Toast.makeText(getApplicationContext(), "Mager successfully got permission", Toast.LENGTH_LONG).show();

                            // Launch login activity

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
                    params.put("lockid", lockid);
                    params.put("username", email);
                    params.put("frequency", "always");
                    params.put("type", MANGER.toString());//TODO CHK
                    params.put("start1", "12:00");
                    params.put("start2", "12:00");
                    params.put("start3", "12:00");
                    params.put("start4", "12:00");
                    params.put("start5", "12:00");
                    params.put("start6", "12:00");
                    params.put("start7", "12:00");
                    params.put("end1", "12:00");
                    params.put("end2", "12:00");
                    params.put("end3", "12:00");
                    params.put("end4", "12:00");
                    params.put("end5", "12:00");
                    params.put("end6", "12:00");
                    params.put("end7", "12:00");



                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }





//check if there is no first manger to this lock
    public boolean checkLockManger(String lockid){

      //  https://smartlockproject.herokuapp.com/api/getPermissions
        String uri="https://smartlockproject.herokuapp.com/api/getLockManager/"+lockid;
        //TODO check if i didnt get any permission for this lock therefore thats the first time and return true
        //and add permission for the manger

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "chk mnger for lock response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status=jsonObj.getString("status");
                                if(status.equals("success")){
                                //// TODO: 05/05/2017 return true
                                  //  flag=true;
                                    flag=false;
                                    Log.d("flag chn sign up here ",String.valueOf(flag));

                                }



                           // int type = jsonObj.getInt("type");
                            //Log.d("type",Integer.toString(type));

                            /*if ((SQLiteHandler.CURRENT_PERMISSION_TYPE).ordinal() == type) {
                                //  Intent intent = new Intent(SignUp.this,
                                //        Login.class);
                                //startActivity(intent);
                           }
                            else{
                                pDialog.setMessage("your permission is "+type);
                                showDialog();
                            }*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUp.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        Log.d("flag chn sign up",String.valueOf(flag));


        return flag;


    }

    //check if the user really have the permission

    public void checkUserPermission(String email,final String lockid){
        String uid="58e22b39734d1d01a23a6efc";

        //String uri="https://smartlockproject.herokuapp.com/api/getPermission/"+email+"/"+SQLiteHandler.CURRENT_LOCKID;
        String uri="https://smartlockproject.herokuapp.com/api/getPermission/"+email+"/"+lockid;
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

                            if ((AppConfig.CURRENT_PERMISSION_TYPE).ordinal() == type) {
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
                        Toast.makeText(SignUp.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }



    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
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
                    if (status.equals("success")) { // TODO: 02/05/2017  ask avital how to check if there was error
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        JSONObject c = jObj.getJSONObject("message");
                        //String password = c.getString("password");
                        String uid = c.getString("userid");
                        //String username = jObj.getString("username");
                        //String phone = jObj.getString("phone");
                        //String password = jObj.getString("password");

                      //   String uid="12222222";
                       // JSONObject user = jObj.getJSONObject("user");
                        //String name = user.getString("name");
                        //String email = user.getString("email");
                        //String created_at = user
                          //      .getString("created_at");

                        // Inserting row in users table
                        //User user=new User(uid,phone,password,username);
                        //db.addUser(user);

                        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity
                        Intent intent = new Intent(
                                SignUp.this, RegCode.class);
                        intent.putExtra("username",username);
                               // Login.class);
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



/*
public class SignUp extends AppCompatActivity {

  //  private EditText _username;
    private EditText _password;
    private EditText _email;
    private EditText _phone;
    private Button signup;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

    //    _username = (EditText) findViewById(R.id.usernameET);
        _password = (EditText) findViewById(R.id.passwordET);
        _email = (EditText) findViewById(R.id.emailET);
        _phone = (EditText) findViewById(R.id.phoneEt);
        signup = (Button) findViewById(R.id.signup);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

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
             //   String username = _username.getText().toString().trim();
                String password = _password.getText().toString().trim();
                String email = _email.getText().toString().trim();
                String phone = _phone.getText().toString().trim();
                String userid="1";



                    if ( !email.isEmpty() && !password.isEmpty() && !phone.isEmpty()) {
                        User user=new User(userid,phone,password,email);
                        registerUser(user);

                   // registerUser(username, email, password,phone);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

    }*/

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */
    /*private void registerUser(final String username, final String email,
                              final String password, final  String phone) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        String uid = "12345";

        pDialog.setMessage("Registering ...");
        showDialog();

        db.addUser(username, email, uid, phone, password);
        SQLiteDatabase db1 = db.getReadableDatabase(); //for printing

        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
        Log.d("table",getTableAsString(db1,db.gettable()));
        // Launch login activity
        Intent intent = new Intent(
                SignUp.this,
                Login.class);
        startActivity(intent);
        finish();

    }*/

 /*   private void registerUser(User user){
        // Tag used to cancel the request
        String tag_string_req = "req_register";

       // pDialog.setMessage("Registering ...");
       // showDialog();










        db.addUser(user);
        SQLiteDatabase db1 = db.getReadableDatabase(); //for printing

        Toast.makeText(getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
        Log.d("table",getTableAsString(db1,db.gettable()));
        // Launch login activity
        Intent intent = new Intent(
                SignUp.this,
                Login.class);
        startActivity(intent);
        finish();


    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public String getTableAsString(SQLiteDatabase db, String tableName) {
       // Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }

}*/
