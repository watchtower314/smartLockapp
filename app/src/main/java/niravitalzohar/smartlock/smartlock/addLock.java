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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddLock extends AppCompatActivity {
    EditText lockid;
    Button addLock;
    private ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lock);
        lockid=(EditText)findViewById(R.id.lockIdET);
        addLock=(Button)findViewById(R.id.addLock);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        addLock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String lID = lockid.getText().toString().trim();;
                addLock(lID);
            }
        }); // plus listner

    }
 //this will add  manger permission to the given lockid
    public void addLock(final String Id){
        String tag_string_req = "req_addLock";
        pDialog.setMessage("adding lock ...");
        AppConfig.showDialog(pDialog);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_PERMISSION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "Login Response: " + response.toString());
                //  hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");

                    if(status.equals("success")){
                        Toast.makeText(getApplicationContext(), "lock successfully added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(
                                AddLock.this,
                                UsersLocks.class);
                        startActivity(intent);
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
                Log.e("GJKKK", "addlock Error: " + error.getMessage());
                String errorMsg ="cant add lock "+ error.getMessage();
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
                AppConfig.hideDialog(pDialog);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", AppConfig.CURRENT_USERNAME);
                params.put("lockid", Id);
                params.put("token",AppConfig.TOKEN);

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

