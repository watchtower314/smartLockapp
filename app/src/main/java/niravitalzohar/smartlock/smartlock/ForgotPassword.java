package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
//if user forgot his password he will get temporary password via mail and later he can change this password (at setting->info)
public class ForgotPassword extends AppCompatActivity {
    EditText userF,newPassword;
    Button cngPass;
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        userF = (EditText) findViewById(R.id.emailETF);
        newPassword = (EditText) findViewById(R.id.passwordETF);
        cngPass=(Button)findViewById(R.id.cngPass);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);





        cngPass.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String emailF = userF.getText().toString().trim().toLowerCase();
                String passwordf = newPassword.getText().toString().trim();
                changePass(emailF,passwordf);
            }

        });

    }//end onCreate

    public void changePass(String Email,String nPassword){

        String uri=AppConfig.Forgot_PASS+Email;
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
                                Intent intent = new Intent(ForgotPassword.this,
                                        Login.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "User's password was not updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ForgotPassword.this,
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
                        String errorMsg ="error - can't update user's password please try again ";
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        requestQueue4.add(stringRequest4);


    }

    @Override
    public void onBackPressed() {
    }
}//end class
