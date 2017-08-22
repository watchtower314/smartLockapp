package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
/* class for conacting with SmartLock team */
public class ContactUs extends AppCompatActivity {
    EditText massage;
    ImageButton send;
    TextView contactName;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        contactName=(TextView) findViewById(R.id.contactname);
        massage=(EditText)findViewById(R.id.Cmessage);
        send=(ImageButton)findViewById(R.id.Cmailsend);

        contactName.setText(AppConfig.CURRENT_USERNAME);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);



        send.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String name=contactName.getText().toString();
                String msg=massage.getText().toString();
                 sendMsg(name,msg);
            }

        });
    }

    public void sendMsg(final String name,final String msg){

        String tag_string_req = "req_msgSend";
        pDialog.setMessage("sending massage ...");
        AppConfig.showDialog(pDialog);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.SEND_MSG, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "Login Response: " + response.toString());
                //  hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");

                    if(status.equals("success")){



                        Toast.makeText(getApplicationContext(), "message successfully sent", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        String msg=jObj.getString("message");
                        String passError=msg;
                        Toast.makeText(getApplicationContext(),
                                passError, Toast.LENGTH_LONG).show();
                        AppConfig.hideDialog(pDialog);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GJKKK", "Registration Error: " + error.getMessage());
                String errorMsg ="cant send your message please try again later "+ error.getMessage();
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
                AppConfig.hideDialog(pDialog);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", name);
                params.put("message", msg);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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
                finish();
                return true;


        }

        return false;
    }

    @Override
    public void onBackPressed() {
    }
}
