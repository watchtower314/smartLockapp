package niravitalzohar.smartlock.smartlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MemberChangeD extends AppCompatActivity {
    private EditText ETusername, ETphone ,newPassword ,oldPass;

    private String username=" ";
    private String phone=" ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_change_d);
        username = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        ETusername=(EditText)findViewById(R.id.m_cnguser);
        ETusername.setText(username);

        ETphone=(EditText)findViewById(R.id.m_cngphone);
        ETphone.setText(phone);

        newPassword=(EditText)findViewById(R.id.cngpasswordET);
        oldPass=(EditText)findViewById(R.id.oldpass);
    }

    public void changePass(String nPassword,String Opass){

        String uri=AppConfig.CHNG_PASS+nPassword+"/"+Opass
                +"?token="+AppConfig.TOKEN;
        final StringRequest stringRequest4 = new StringRequest(Request.Method.PUT,uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "User's  password successfully updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MemberChangeD.this,
                                        Login.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "User's password was not updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MemberChangeD.this,
                                        Login.class);
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
                        // Toast.makeText(ChangeDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        String errorMsg ="error - can't update user's password please try again ";
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                      //  AppConfig.hideDialog(pDialog);
                    }
                });

        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        requestQueue4.add(stringRequest4);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cng_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.info_cng:
                 updateUser();
                return true;
            case R.id.save_cng:
                String newPassord = newPassword.getText().toString().trim();
                String oldPassword=oldPass.getText().toString();
                changePass(newPassord,oldPassword);
                return true;

            case R.id.delete_user:

                deleteUser();
                return true;

            case R.id.back:
                finish();
                return true;


        }
        return false;
    }

    public void updateUser(){
        String userName=ETusername.getText().toString().trim().toLowerCase();
        String phone=ETphone.getText().toString().trim();

        String uri="https://smartlockproject.herokuapp.com/api/updateUser/"+AppConfig.CURRENT_USERNAME+"/"+userName
                +"/"+phone;//TODO add the prams here

        final StringRequest stringRequest = new StringRequest(Request.Method.PUT,uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "User details successfully changed!", Toast.LENGTH_LONG).show();
                                 Intent intent = new Intent(MemberChangeD.this,
                                       MemberLanding.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "User details wasn't changed!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MemberChangeD.this,
                                        MemberLanding.class);
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
                        Toast.makeText(MemberChangeD.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void deleteUser(){
        String uri3="https://smartlockproject.herokuapp.com/api/removeUser?token="+AppConfig.TOKEN;
        final StringRequest stringRequest3 = new StringRequest(Request.Method.DELETE,uri3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "User successfully removed!", Toast.LENGTH_LONG).show();
                                AppConfig.CURRENT_PERMISSION_TYPE=permission_type.GUEST;
                                Intent intent = new Intent(MemberChangeD.this,
                                        Login.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "User was not removed!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(MemberChangeD.this,
                                        MemberLanding.class);
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
                        Toast.makeText(MemberChangeD.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue3 = Volley.newRequestQueue(this);
        requestQueue3.add(stringRequest3);


    }

    @Override
    public void onBackPressed() {
    }
}
