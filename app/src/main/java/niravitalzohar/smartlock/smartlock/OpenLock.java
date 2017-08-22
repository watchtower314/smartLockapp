package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import static niravitalzohar.smartlock.smartlock.permission_type.MANGER;

public class OpenLock extends AppCompatActivity {
private ImageView close_b;
    private String cngTOstatus="close";
    public String l_status=" ";
    StringBuilder requestId = new StringBuilder();
    String lockid="18:fe:34:d4:c6:e8";
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_lock);
        close_b=(ImageView) findViewById(R.id.close_b);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        close_b.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
             // changeStatus();
                requestId.setLength(0);
                String result= lock2();
                Log.d("result-lock",result);
               // getAction(result);
            }

        });


    }


    public String lock2(){

        String tag_string_req = "req_lock";
        Log.d("CHK_STATUS","in chk status");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.LOCK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "LOCK Response: " + response.toString());
                //  hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");

                    //requestId=jObj.getString("requestId");
                    requestId.append(jObj.getString("requestId"));
                    Log.d("REQid",requestId.toString());

                    if(status.equals("request created")){
                        //TODO go to get action


                        //Toast.makeText(getApplicationContext(), "THE LOCK Status ", Toast.LENGTH_LONG).show();
                       // getAction2(requestId.toString()).equals("unhandle");
                        getAction2(requestId.toString());
                        // while (getAction2(requestId.toString()).equals("unhandle")){
                        //   getAction2(requestId.toString());
                        // }

                        //  return;
                        // Launch login activity

                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = "chkLock error";
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
                Log.e("GJKKK", "LOCK Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to unlock url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("lockid",lockid);
                params.put("username",AppConfig.CURRENT_USERNAME);
                params.put("lockid",AppConfig.CURRENT_LOCKID);
                params.put("token",AppConfig.TOKEN);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.d("REQid",requestId.toString());
        return requestId.toString();//// TODO: 08/05/2017 chk if really changing

    }

    public String getAction2(final String result) {
        Log.d("result", result);
        pDialog.setMessage("waiting for checking ...");
        showDialog();
      //  count++;

        String uri = "https://smartlockproject.herokuapp.com/api/checkLockAction/" + result+
                "?token="+AppConfig.TOKEN;
        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "chk mnger for lock response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            l_status = jsonObj.getString("status");
                            Log.d("status", l_status);
                            String action = jsonObj.getString("action");
                            if (l_status.equals("lock")) {
                                //   if(action.equals("lock")){
                                Intent intent = new Intent(OpenLock.this,
                                        CloseLock.class);
                                startActivity(intent);

                            } else if (l_status.equals("unlock")) {
                                Intent intent = new Intent(OpenLock.this,
                                        OpenLock.class);
                                startActivity(intent);

                            } else if((l_status.equals("timeout")) ){
                                String errorMsg ="oops something went wrong please try again-timeout error";
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                            else{
                                getAction2(result);
                            }


                            //  }

                        } catch (JSONException e) {
                            Log.d("catch", "ch");
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg ="oops something went wrong please try again";
                        Toast.makeText(OpenLock.this, errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });
        Log.d("status", l_status);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return l_status;
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


    public String chkLockStatus2(){

        String tag_string_req = "req_lock";
        Log.d("CHK_STATUS","in chk status");

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.CHECK_LOCK_STATUS, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "LOCK Response: " + response.toString());
                //  hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");

                    //requestId=jObj.getString("requestId");


                    if(status.equals("request created")){
                        requestId.append(jObj.getString("requestId"));
                        Log.d("REQid",requestId.toString());
                        //TODO go to get action


                      //  Toast.makeText(getApplicationContext(), "THE LOCK Status ", Toast.LENGTH_LONG).show();
                        getAction2(requestId.toString()).equals("unhandle");
                        // while (getAction2(requestId.toString()).equals("unhandle")){
                        //   getAction2(requestId.toString());
                        // }

                        //  return;
                        // Launch login activity

                    } else {
                        String message=jObj.getString("message");

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = message+" ask lock manger for help";
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
                Log.e("GJKKK", "LOCK Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to unlock url
                Map<String, String> params = new HashMap<String, String>();
                Log.d("lockid",lockid);
                params.put("username",AppConfig.CURRENT_USERNAME);
                params.put("lockid",AppConfig.CURRENT_LOCKID);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.d("REQid",requestId.toString());
        return requestId.toString();

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rest, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.lock:
                requestId.setLength(0);
                String result= chkLockStatus2();
                Log.d("result-lock",result);
                return true;

            case R.id.printf:
                Intent intent = new Intent(OpenLock.this,
                        FingerPrint.class);
                startActivity(intent);
                return true;

            case R.id.setting:
                Intent intent2 = new Intent(OpenLock.this,
                        Settings.class);
                startActivity(intent2);

                return true;
            case R.id.home:
                if(AppConfig.CURRENT_PERMISSION_TYPE==MANGER) {
                    Intent intent3 = new Intent(OpenLock.this,
                            MngUsers.class);
                    startActivity(intent3);
                }
                else{
                    Intent intent4 = new Intent(OpenLock.this,
                            MemberLanding.class);
                    startActivity(intent4);
                }

                return true;


        }

        return false;
    }

    public void chkLockStatus(){
        String uri="https://smartlockproject.herokuapp.com/api/getLock/"+AppConfig.CURRENT_LOCKID;

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");
                            JSONObject c = jsonObj.getJSONObject("message");
                            String lockStatus = c.getString("status");


                            if (lockStatus.equals("open")) {
                                Intent intent = new Intent(OpenLock.this,//// TODO: 07/05/2017 remove this
                                        OpenLock.class);
                                startActivity(intent);
                            }

                            else {
                                Intent intent = new Intent(OpenLock.this,
                                        CloseLock.class);
                                startActivity(intent);
                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OpenLock.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
    }
}
