package niravitalzohar.smartlock.smartlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class RegCode extends AppCompatActivity {
    EditText num1,num2,num3,num4;
    Button submit;
    TextView sendAgain;
    private String username=" ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_code);
        num1=(EditText)findViewById(R.id.num1);
        num2=(EditText)findViewById(R.id.num2);
        num3=(EditText)findViewById(R.id.num3);
        num4=(EditText)findViewById(R.id.num4);
        submit=(Button) findViewById(R.id.submit);
        sendAgain=(TextView) findViewById(R.id.sendme);
        username = getIntent().getStringExtra("username");


        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                String one=num1.getText().toString();
                String two=num2.getText().toString();
                String three=num3.getText().toString();
                String four=num4.getText().toString();

                sendCode(one,two,three,four);


            }

        });

        sendAgain.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                sendAgain();


            }

        });



    }

    public void sendCode(final String numone,final String numtwo,final String numthree,final String numfour ){
        String tag_string_req = "sendcode";
        //final String lockid="323djdjw32";
        //final String userid="58e91fd7fafa6700044b8d61";

        //pDialog.setMessage("sending code ...");
       // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SEND_CODE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "Add permission Response: " + response.toString());
             //   hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");
                    if(status.equals("success")){

                        Toast.makeText(getApplicationContext(), "New account successfully was created  login now!", Toast.LENGTH_LONG).show();

                        // Launch login activity

                        Intent intent = new Intent(
                                RegCode.this,
                                Login.class);
                        startActivity(intent);

                    } else {

                        // Error occurred in registration. Get the error
                         String msg=jObj.getString("message");
                        String errorMsg = msg;
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
                String errorMsg = "connection error";
                Log.e("GJKKK", "sendcode Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        errorMsg , Toast.LENGTH_LONG).show();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",username);
                params.put("num1", numone);
                params.put("num2", numtwo);
                params.put("num3", numthree);
                params.put("num4", numfour);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    public void sendAgain(){
        String tag_string_req = "sendcode";
        Log.d("insendagain","in send again");

        //pDialog.setMessage("sending code ...");
        // showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SEND_CODE_AGAIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "Add permission Response: " + response.toString());
                //   hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");
                    if(status.equals("success")){

                        Toast.makeText(getApplicationContext(), "plese check your email to see new code", Toast.LENGTH_LONG).show();

                        // Launch login activity


                    } else {

                        // Error occurred in registration. Get the error
                        String msg=jObj.getString("message");
                        String errorMsg = msg+"please contact smartLock team for help";
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
                String errorMsg = "connection error";
                Log.e("GJKKK", "sendcode Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        errorMsg , Toast.LENGTH_LONG).show();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",username);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onBackPressed() {
    }
}
