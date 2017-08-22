package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class UsersLocks extends AppCompatActivity {
    private ListView lockList;
    private ProgressDialog pDialog;
    private Button plusLock;

    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_locks);
        plusLock=(Button)findViewById(R.id.plusLock);


        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        contactList = new ArrayList<>();
        Log.d("username logged",AppConfig.CURRENT_USERNAME);

        lockList =(ListView) findViewById(R.id.locks);
        getUserLocks();

        lockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView) view.findViewById(R.id.userlock)).getText().toString();//// TODO: 07/05/2017 do the same in user details
                Log.d("chosen lock",selected);
                AppConfig.CURRENT_LOCKID=selected;
                chkUserType();
            }
        });

        plusLock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Launch login activity
                Intent intent = new Intent(
                        UsersLocks.this,
                        AddLock.class);
                startActivity(intent);

            }

        });


    }




    public void chkUserType(){
         String uri="https://smartlockproject.herokuapp.com/api/getPermission/"+AppConfig.CURRENT_USERNAME+"/" +AppConfig.CURRENT_LOCKID+"?token="
        +AppConfig.TOKEN;

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "permission response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject c = jsonObj.getJSONObject("message");
                            int type = c.getInt("type");
                            Log.d("type",Integer.toString(type));
                            Log.d("pppp",String .valueOf(permission_type.MEMBER.ordinal()));

                            if ((type==permission_type.MANGER.ordinal())) {

                                AppConfig.CURRENT_PERMISSION_TYPE=permission_type.MANGER;
                                Log.d("permission type", AppConfig.CURRENT_PERMISSION_TYPE.toString());
                                Intent intent = new Intent(
                                        UsersLocks.this,
                                        MngUsers.class);
                                startActivity(intent);
                            }
                            else if((type==permission_type.MEMBER.ordinal()))  {
                                AppConfig.CURRENT_PERMISSION_TYPE=permission_type.MEMBER;
                                Intent intent = new Intent(
                                        UsersLocks.this,
                                        MemberLanding.class);
                                startActivity(intent);
                            }
                            else if((type==permission_type.MEMBER.ordinal())){
                                AppConfig.CURRENT_PERMISSION_TYPE=permission_type.MEMBER_WITH_PY_ID;
                                Intent intent = new Intent(
                                        UsersLocks.this,
                                        MemberLanding.class);
                                startActivity(intent);

                            }
                            else {
                                Intent intent = new Intent(
                                        UsersLocks.this,
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
                        Toast.makeText(UsersLocks.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }

    public void getUserLocks(){
        final String url= "https://smartlockproject.herokuapp.com/api/getLocksByUser?token="+AppConfig.TOKEN;

        final StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "userslocks Response: " + response.toString());
                        //  showJSON(response);
                        //AppConfig.hideDialog(pDialog);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status=jsonObj.getString("status");

                            if(status.equals("success")) {
                                 JSONObject message = jsonObj.getJSONObject("message");

                               JSONArray userLocks=message.getJSONArray("userLocks");

                                for (int i = 0; i < userLocks.length(); i++) {
                                    JSONObject c = userLocks.getJSONObject(i);
                                    String id = c.getString("lockid");
                                    String description=c.getString("description");

                                    // tmp hash map for single contact
                                    HashMap<String, String> contact = new HashMap<>();

                                    // adding each child node to HashMap key => value
                                    contact.put("id", id);
                                    contact.put("description", description);

                                    // adding contact to contact list
                                    contactList.add(contact);
                                    ListAdapter adapter = new SimpleAdapter(
                                            UsersLocks.this, contactList,
                                            R.layout.locks_row, new String[]{"id","description"}, new int[]{R.id.userlock,R.id.lockdes
                                    });

                                    lockList.setAdapter(adapter);

                                }
                            }
                            else{
                                String msg=jsonObj.getString("message");
                                String passError=msg+" no valid token plese try again";
                                Toast.makeText(getApplicationContext(),
                                        passError, Toast.LENGTH_LONG).show();
                            //  AppConfig.hideDialog(pDialog);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String passError=" oops connection error please contact us for help";
                        Toast.makeText(UsersLocks.this,passError,Toast.LENGTH_LONG).show();
                        AppConfig.hideDialog(pDialog);

                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_screen, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.help:
                Intent intent = new Intent(UsersLocks.this,
                        Help.class);
                startActivity(intent);

                return true;

        }

        return false;
    }

    @Override
    public void onBackPressed() {
    }
}
