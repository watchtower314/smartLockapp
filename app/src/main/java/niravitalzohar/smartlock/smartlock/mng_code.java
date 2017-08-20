package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class mng_code extends AppCompatActivity {
    private TextView lockidTv;
    private EditText lockidET;
    private Button v_button;
    private String permissionType;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_code);

        lockidTv=(TextView)findViewById(R.id.lockidTV) ;
        lockidET=(EditText)findViewById(R.id.lockidET) ;
        v_button=(Button)findViewById(R.id.v_button);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        // permissionType = getIntent().getStringExtra("PermissionType");
        //Log.d("kk",permissionType);

        v_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String lockid=lockidET.getText().toString().trim();
               // SQLiteHandler.CURRENT_LOCKID=lockid;

                Log.d("kkk",lockid);

                Intent intent = new Intent(getBaseContext(), SignUp.class);
                intent.putExtra("lockid",lockid);
                startActivity(intent);

              //  getlock(lockid);



            }
        });


    }

    public void getlock(final String lockid){
        pDialog.setMessage("verifing login details ...");
        AppConfig.showDialog(pDialog);

        String uri="https://smartlockproject.herokuapp.com/api/getLock/"+lockid;

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            Log.d("hjj",status);

                            if (status!="error") {
                              /*  Intent intent = new Intent(mng_code.this,
                                        SignUp.class);
                                startActivity(intent);*/
                                Intent intent = new Intent(getBaseContext(), SignUp.class);
                                intent.putExtra("lockid",lockid);
                                startActivity(intent);
                            }
                            else{
                                Intent intent = new Intent(mng_code.this,
                                        MainActivity.class);
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
                      //  Toast.makeText(mng_code.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        String errorMsg ="cant find the lock please contact us by email";
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        AppConfig.hideDialog(pDialog);
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
}
