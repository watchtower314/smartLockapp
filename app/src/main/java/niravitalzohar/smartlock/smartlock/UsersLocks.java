package niravitalzohar.smartlock.smartlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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

public class UsersLocks extends AppCompatActivity {
    private ListView lockList;


    ArrayList<HashMap<String, String>> contactList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_locks);
        contactList = new ArrayList<>();
        Log.d("username logged",SQLiteHandler.CURRENT_USERNAME);

        lockList =(ListView) findViewById(R.id.locks);
        getUserLocks();

        lockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = ((TextView) view.findViewById(R.id.userlock)).getText().toString();//// TODO: 07/05/2017 do the same in user details
                Log.d("chosen lock",selected);
                SQLiteHandler.CURRENT_LOCKID=selected;
                chkUserType();
                /*Log.d("permission",SQLiteHandler.CURRENT_PERMISSION_TYPE.toString());
                if(SQLiteHandler.CURRENT_PERMISSION_TYPE==permission_type.MANGER){
                    Intent intent = new Intent(
                            UsersLocks.this,
                            MngUsers.class);
                    startActivity(intent);

                }
                else{
                    Intent intent = new Intent(
                            UsersLocks.this,
                            MainActivity.class);
                    startActivity(intent);
                }*/

               /* Log.d("chosen lock", parent.getItemAtPosition(position).toString());
               String[] parts= parent.getItemAtPosition(position).toString().split("=");
                String part1 = parts[0]; // 004
                String part2 = parts[1];
                parts=part1.split("}");
                part1=parts[0];
                Log.d("lockid",part1);*/


                //chekuserpermission if manger go to mng user else go to user info2 // for memebers
            }
        });

    }
    public void chkUserType(){
        String uri="https://smartlockproject.herokuapp.com/api/getPermission/"+SQLiteHandler.CURRENT_USERNAME+"/"+SQLiteHandler.CURRENT_LOCKID;

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

                                SQLiteHandler.CURRENT_PERMISSION_TYPE=permission_type.MANGER;
                                Log.d("permission type", SQLiteHandler.CURRENT_PERMISSION_TYPE.toString());
                                Intent intent = new Intent(
                                        UsersLocks.this,
                                        MngUsers.class);
                                startActivity(intent);
                            }
                            else if((type==permission_type.MEMBER.ordinal()))  {
                                SQLiteHandler.CURRENT_PERMISSION_TYPE=permission_type.MEMBER;
                                Intent intent = new Intent(
                                        UsersLocks.this,
                                        MemberLanding.class);
                                startActivity(intent);
                            }
                            else if((type==permission_type.MEMBER.ordinal())){
                                SQLiteHandler.CURRENT_PERMISSION_TYPE=permission_type.MEMBER_WITH_PY_ID;
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
        final String url="https://smartlockproject.herokuapp.com/api/getPermissionsByUser/"+SQLiteHandler.CURRENT_USERNAME;
        final StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                           // JSONObject message = jsonObj.getJSONObject("message");
                            JSONArray message = jsonObj.getJSONArray("message");



                            //JSONObject jsonObj = new JSONObject(response);
                          /*  JSONArray contacts=new JSONArray(response);*/

                            // JSONArray contacts = jsonObj.getJSONArray(" ");
                            for (int i = 0; i < message.length(); i++) {
                                JSONObject c = message.getJSONObject(i);

                                String id = c.getString("lockid");
                                //String type = c.getString("type");

                                // tmp hash map for single contact
                                HashMap<String, String> contact = new HashMap<>();

                                // adding each child node to HashMap key => value
                                contact.put("id", id);
                                //contact.put("name", username);
                               // contact.put("email", phone);
                                //contact.put("mobile", password);

                                // adding contact to contact list
                                contactList.add(contact);
                                ListAdapter adapter = new SimpleAdapter(
                                        UsersLocks.this, contactList,
                                        R.layout.locks_row, new String[]{"id"}, new int[]{R.id.userlock,
                                        });

                                lockList.setAdapter(adapter);

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



        //  AppController.getInstance().addToRequestQueue(getRequest, tag_string_req);
    }
}
