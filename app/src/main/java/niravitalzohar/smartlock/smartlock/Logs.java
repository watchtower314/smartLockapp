package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.HashMap;

public class Logs extends AppCompatActivity {
    private ListView lv;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> logsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        logsList = new ArrayList<>();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        lv = (ListView) findViewById(R.id.list2);
        getLogs();

    }

    public void getLogs(){


        final String url=AppConfig.LOGS +"?token="+AppConfig.TOKEN;

        pDialog.setMessage("retrieving logs ...");
        AppConfig.showDialog(pDialog);

        String tag_string_req = "req_logs";
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

                                // Getting JSON Array node
                                JSONArray myLogs = jsonObj.getJSONArray("message");

                                for (int i = 0; i < myLogs.length(); i++) {
                                    JSONObject c = myLogs.getJSONObject(i);
                                    String _lockid = c.getString("lockid");
                                    String _username = c.getString("username");
                                    String _action = c.getString("action");
                                    String _time = c.getString("time");
                                    // tmp hash map for single contact
                                    HashMap<String, String> logs = new HashMap<>();
                                    Log.d("user",_username);

                                    // adding each child node to HashMap key => value
                                    logs.put("lockid", "lockid: "+_lockid);
                                    logs.put("email", "username: "+_username);
                                    logs.put("action", "action: "+_action);
                                    logs.put("time", "time: "+_time);

                                    // adding contact to contact list
                                    logsList.add(logs);
                                    ListAdapter adapter = new SimpleAdapter(
                                            Logs.this, logsList,
                                            R.layout.logs_list, new String[]{"lockid", "email", "action", "time"},
                                            new int[]{R.id.lockid, R.id.email, R.id.action, R.id.time});

                                    lv.setAdapter(adapter);
                                    AppConfig.hideDialog(pDialog);


                                }
                            }else{
                                String msg=jsonObj.getString("message");
                                String passError=msg;
                                Toast.makeText(getApplicationContext(),
                                        passError, Toast.LENGTH_LONG).show();
                                AppConfig.hideDialog(pDialog);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Logs.this,error.getMessage(),Toast.LENGTH_LONG).show();
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
                finish();
                return true;


        }

        return false;
    }
}
