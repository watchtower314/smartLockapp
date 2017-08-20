package niravitalzohar.smartlock.smartlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static niravitalzohar.smartlock.smartlock.permission_type.GUEST;
import static niravitalzohar.smartlock.smartlock.permission_type.MANGER;

public class Settings extends AppCompatActivity {
    Button mnglocks,logout,stinfo,sthelp ,stcontact,stlogs,stcontactuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mnglocks=(Button)findViewById(R.id.mngLocks);
        logout=(Button)findViewById(R.id.stlogOut);
        stinfo=(Button)findViewById(R.id.stinfo);
        sthelp=(Button)findViewById(R.id.stHelp);
        stcontact=(Button)findViewById(R.id.stcontact);
        stlogs=(Button)findViewById(R.id.stLogs);
        stcontactuser=(Button)findViewById(R.id.stcontactuser);

        mnglocks.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Launch login activity

                Intent intent = new Intent(
                        Settings.this,
                        UsersLocks.class);
                startActivity(intent);

            }

        });


        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Launch login activity
                AppConfig.TOKEN="";
                AppConfig.CURRENT_PERMISSION_TYPE=GUEST;
                AppConfig.CURRENT_LOCKID="";
                AppConfig.CURRENT_USERNAME="";

                Intent intent = new Intent(
                        Settings.this,
                        Login.class);
                startActivity(intent);

            }

        });

        sthelp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                // Launch login activity

                Intent intent = new Intent(
                        Settings.this,
                        Help.class);
                startActivity(intent);

            }

        });
        stinfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                getUser();

            }

        });

        stcontact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(
                        Settings.this,
                        ContactUs.class);
                startActivity(intent);
            }

        });

        stlogs.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AppConfig.CURRENT_PERMISSION_TYPE != permission_type.MANGER) {
                    Toast.makeText(getApplicationContext(), "you dont have permission for this action", Toast.LENGTH_LONG).show();
                } else {

                    Intent intent = new Intent(
                            Settings.this,
                            Logs.class);
                    startActivity(intent);
                }
            }

        });

        stcontactuser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                Intent intent = new Intent(
                        Settings.this,
                        ContactUsers.class);
                startActivity(intent);
            }

        });



    }

    public void getUser(){
        final String url="https://smartlockproject.herokuapp.com/api/getUser?token="+AppConfig.TOKEN;

        final StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status=jsonObj.getString("status");

                            if(status.equals("success")) {
                                 JSONObject message = jsonObj.getJSONObject("message");
                                String phone=message.getString("phone");
                                if(AppConfig.CURRENT_PERMISSION_TYPE==permission_type.MANGER) {

                                    Intent intent = new Intent(
                                            Settings.this,
                                            UserDetails.class);
                                    intent.putExtra("name", AppConfig.CURRENT_USERNAME);
                                    intent.putExtra("phone", phone);
                                    startActivity(intent);
                                }
                                else{
                                    Intent intent = new Intent(
                                            Settings.this,
                                            UserDetails.class);
                                    intent.putExtra("phone", phone);
                                    startActivity(intent);
                                }


                            }
                            else{
                                String msg=jsonObj.getString("message");
                                String passError=msg+" no valid password plese try again";
                                Toast.makeText(getApplicationContext(),
                                        passError, Toast.LENGTH_LONG).show();
                                //  hideDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Settings.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.back:
                if(AppConfig.CURRENT_PERMISSION_TYPE==MANGER) {
                    Intent intent3 = new Intent(Settings.this,
                            MngUsers.class);
                    startActivity(intent3);
                }
                else{
                    Intent intent4 = new Intent(Settings.this,
                            MemberLanding.class);
                    startActivity(intent4);
                }

                return true;


        }

        return false;
    }
}
