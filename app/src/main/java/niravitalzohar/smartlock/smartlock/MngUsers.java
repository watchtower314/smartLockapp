package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
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
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;
 /*main page for user with mangar permission , a list of lock's users will be shown.manager can view user's details be clicking
  * on user's name from the list or he can add new permission for user by perssing the plus button
   * */
public class MngUsers extends AppCompatActivity {

    private ListView lv;
    private Button plus;
    private String l_status=" ";
    StringBuilder requestId = new StringBuilder();
    private TextView Activemng;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_users);
        Activemng=(TextView)findViewById(R.id.active_mngName);
        Activemng.setText(AppConfig.CURRENT_USERNAME);
        contactList = new ArrayList<>();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        lv = (ListView) findViewById(R.id.list);
        plus=(Button)findViewById(R.id.plusUser2);

        getData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedName = ((TextView) view.findViewById(R.id.name)).getText().toString();
                String selectedPhone = ((TextView) view.findViewById(R.id.phone)).getText().toString();
                 Intent i = new Intent(MngUsers.this, UserDetails.class);
                i.putExtra("name", selectedName);
                i.putExtra("phone", selectedPhone);
                // i.putExtra("userD", parent.getItemAtPosition(position).toString());
                  startActivity(i);

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(MngUsers.this,
                        AddMember.class);
                startActivity(intent);
            }

        });

    }

//retrieving lock's users -users with permission for this lock
    public void getData(){
        // URL to get contacts JSON
        pDialog.setMessage("retrieving data ...");
        showDialog();
        final String url="https://smartlockproject.herokuapp.com/api/getUsersByLock/"+AppConfig.CURRENT_LOCKID
                +"?token="+AppConfig.TOKEN;

        String tag_string_req = "req_register";
        final StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        AppConfig.hideDialog(pDialog);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status=jsonObj.getString("status");

                            if(status.equals("success")) {

                                // Getting JSON Array node
                                JSONArray contacts = jsonObj.getJSONArray("message");

                                for (int i = 0; i < contacts.length(); i++) {
                                    JSONObject c = contacts.getJSONObject(i);
                                    String id = c.getString("_id");
                                    String username = c.getString("username");
                                    String phone = c.getString("phone");

                                    HashMap<String, String> contact = new HashMap<>();

                                    // adding each child node to HashMap key => value
                                    contact.put("id", id);
                                    contact.put("name", username);
                                    contact.put("phone", phone);
                                    //contact.put("mobile", password);

                                    // adding contact to contact list
                                    contactList.add(contact);
                                    ListAdapter adapter = new SimpleAdapter(
                                            MngUsers.this, contactList,
                                            R.layout.list_view_layout, new String[]{"name", "phone"}, new int[]{R.id.name,
                                            R.id.phone});

                                    lv.setAdapter(adapter);


                                }
                            } else{
                                String msg=jsonObj.getString("message");
                                String passError=msg+" no valid token plese try again";
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
                        String passError=" connection error";
                        Toast.makeText(MngUsers.this,passError,Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

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
                requestId.setLength(0);//to initilize the request id for new request
              String result= chkLockStatus2();
                Log.d("result-lock",result);
                return true;


            case R.id.printf:
                Intent intent = new Intent(MngUsers.this,
                        FingerPrint.class);
                startActivity(intent);
                return true;

            case R.id.setting:
                Intent intent2 = new Intent(MngUsers.this,
                        Settings.class);
                startActivity(intent2);

                return true;

            case R.id.home:
                Intent intent3 = new Intent(MngUsers.this,
                        MngUsers.class);
                startActivity(intent3);

                return true;



        }

        return false;
    }


    public String getAction2(final String result){
        Log.d("result",result);
        pDialog.setMessage("waiting for checking ...");
        showDialog();
       // count++;


        String uri="https://smartlockproject.herokuapp.com/api/checkLockAction/"+result+
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
                            Log.d("status",l_status);
                            String action = jsonObj.getString("action");
                            if (l_status.equals("lock")) {
                                Intent intent = new Intent(MngUsers.this,
                                        CloseLock.class);
                                startActivity(intent);

                            } else if (l_status.equals("unlock")) {
                                Intent intent = new Intent(MngUsers.this,
                                        OpenLock.class);
                                startActivity(intent);

                            }
                            else if((l_status.equals("timeout")) ){
                                String errorMsg ="oops someting went wrong please try again-timeout error";
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                            else if((l_status.equals("error")) ){
                                String errorMsg=jsonObj.getString("message");
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                            else{
                                getAction2(result);
                            }


                        } catch (JSONException e) {
                            Log.d("catch","ch");
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                      //  Toast.makeText(MngUsers.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        String errorMsg ="oops someting went wrong please try again";
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });
        Log.d("status",l_status);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return l_status;

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

                    if(status.equals("request created")){
                        requestId.append(jObj.getString("requestId"));
                        Log.d("REQid",requestId.toString());
                        getAction2(requestId.toString()).equals("unhandle");
                    } else {
                        String message=jObj.getString("message");

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = message+" ask lock manager for help";
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
                params.put("lockid",AppConfig.CURRENT_LOCKID);
                params.put("token",AppConfig.TOKEN);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.d("REQid",requestId.toString());
        return requestId.toString();

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
    }

}
