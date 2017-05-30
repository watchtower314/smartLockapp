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

public class MngUsers extends AppCompatActivity {

    private ListView lv;
    private Button plus;
    private String l_status=" ";
    StringBuilder requestId = new StringBuilder();
   // StringBuilder l_status = new StringBuilder();
    String lockid="18:fe:34:d4:c6:e8";
    String userid="58e91fd7fafa6700044b8d61";
    private ProgressDialog pDialog;




    ArrayList<HashMap<String, String>> contactList;
//    RequestQueue queue = Volley.newRequestQueue(this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_users);
        contactList = new ArrayList<>();

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        lv = (ListView) findViewById(R.id.list);
        plus=(Button)findViewById(R.id.plusUser2);

        getData();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                 Intent i = new Intent(MngUsers.this, UserDetails.class);
                 i.putExtra("userD", parent.getItemAtPosition(position).toString());
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
    public void getData2(){

    }

    public void getData(){
        // URL to get contacts JSON
        //final String url = "http://api.androidhive.info/contacts";
       // final String url="https://smartlockproject.herokuapp.com/api/getUsers";
        final String url="https://smartlockproject.herokuapp.com/api/getUsersByLock/"+SQLiteHandler.CURRENT_LOCKID;
        String tag_string_req = "req_register";
        final StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            // Getting JSON Array node
                            JSONArray contacts = jsonObj.getJSONArray("message");

                            //JSONObject jsonObj = new JSONObject(response);
                          /*  JSONArray contacts=new JSONArray(response);*/

                            // JSONArray contacts = jsonObj.getJSONArray(" ");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                String id = c.getString("_id");
                                String username = c.getString("username");
                                String phone = c.getString("phone");
                                String password = c.getString("password");
                                // tmp hash map for single contact
                                HashMap<String, String> contact = new HashMap<>();

                                // adding each child node to HashMap key => value
                                contact.put("id", id);
                                contact.put("name", username);
                                contact.put("email", phone);
                                //contact.put("mobile", password);

                                // adding contact to contact list
                                contactList.add(contact);
                                ListAdapter adapter = new SimpleAdapter(
                                        MngUsers.this, contactList,
                                        R.layout.list_view_layout, new String[]{"name", "email"}, new int[]{R.id.name,
                                        R.id.email});

                                lv.setAdapter(adapter);





/*
                        // Getting JSON Array node
                        JSONArray contacts = jsonObj.getJSONArray("contacts");

                        // looping through All Contacts
                        for (int i = 0; i < contacts.length(); i++) {
                            JSONObject c = contacts.getJSONObject(i);

                            String id = c.getString("id");
                            String name = c.getString("name");
                            String email = c.getString("email");
                            String address = c.getString("address");
                            String gender = c.getString("gender");

                            // Phone node is JSON Object
                            JSONObject phone = c.getJSONObject("phone");
                            String mobile = phone.getString("mobile");
                            String home = phone.getString("home");
                            String office = phone.getString("office");

                            // tmp hash map for single contact
                            HashMap<String, String> contact = new HashMap<>();

                            // adding each child node to HashMap key => value
                            contact.put("id", id);
                            contact.put("name", name);
                            contact.put("email", email);
                            contact.put("mobile", mobile);

                            // adding contact to contact list
                            contactList.add(contact);
                            ListAdapter adapter = new SimpleAdapter(
                                    MainActivity.this, contactList,
                                    R.layout.list_view_layout, new String[]{"name", "email",
                                    "mobile"}, new int[]{R.id.name,
                                    R.id.email, R.id.mobile});

                            lv.setAdapter(adapter);*/

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MngUsers.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



        //  AppController.getInstance().addToRequestQueue(getRequest, tag_string_req);
    }



    public void go(View view){

     /*   Intent intent = new Intent(
                MainActivity.this,
                Login.class);
        startActivity(intent);*/

        Intent intent = new Intent(
                MngUsers.this,
                AddMember.class);
        startActivity(intent);


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
                requestId.setLength(0);
              String result= chkLockStatus2();
                Log.d("result-lock",result);
                return true;


            case R.id.printf:
                Intent intent = new Intent(MngUsers.this,
                        FingerPrint.class);
                startActivity(intent);

                return true;


        }

        return false;
    }

    public void getAction(String result){
        Log.d("result",result);
        String uri="https://smartlockproject.herokuapp.com/api/checkLockAction/"+result;
        do{
            final StringRequest stringRequest = new StringRequest(uri,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE", "chk mnger for lock response: " + response.toString());
                            //  showJSON(response);
                            try {
                                JSONObject jsonObj = new JSONObject(response);
                                l_status=jsonObj.getString("status");
                                String action=jsonObj.getString("action");
                                if(l_status.equals("lock")) {
                                    //   if(action.equals("lock")){
                                    Intent intent = new Intent(MngUsers.this,
                                            CloseLock.class);
                                    startActivity(intent);

                                }
                                else if(l_status.equals("unlock")){
                                    Intent intent = new Intent(MngUsers.this,
                                            OpenLock.class);
                                    startActivity(intent);

                                }
                                //  }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }//end on response
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MngUsers.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

        }
        while(l_status.equals("unhandle"));
    }



    public String getAction2(final String result){
        Log.d("result",result);
        pDialog.setMessage("waiting for checking ...");
        showDialog();


        String uri="https://smartlockproject.herokuapp.com/api/checkLockAction/"+result;
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
                                //   if(action.equals("lock")){
                                Intent intent = new Intent(MngUsers.this,
                                        CloseLock.class);
                                startActivity(intent);

                            } else if (l_status.equals("unlock")) {
                                Intent intent = new Intent(MngUsers.this,
                                        OpenLock.class);
                                startActivity(intent);

                            }
                            else {
                                Log.d("here","hereee");
                                getAction2(result);
                            }


                            //  }

                        } catch (JSONException e) {
                            Log.d("catch","ch");
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MngUsers.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        Log.d("status",l_status);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        return l_status;

      /*  while(l_status.equals("unhandle")) {
            final StringRequest stringRequest2 = new StringRequest(uri,
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
                                    //   if(action.equals("lock")){
                                    Intent intent = new Intent(MngUsers.this,
                                            CloseLock.class);
                                    startActivity(intent);

                                } else if (l_status.equals("unlock")) {
                                    Intent intent = new Intent(MngUsers.this,
                                            OpenLock.class);
                                    startActivity(intent);

                                }
                                //  }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }//end on response
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MngUsers.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

            RequestQueue requestQueue2 = Volley.newRequestQueue(this);
            requestQueue2.add(stringRequest);


        }//end while*/
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

                    //requestId=jObj.getString("requestId");


                    if(status.equals("request created")){
                        requestId.append(jObj.getString("requestId"));
                        Log.d("REQid",requestId.toString());
                        //TODO go to get action


                     //   Toast.makeText(getApplicationContext(), "THE LOCK Status ", Toast.LENGTH_LONG).show();
                        getAction2(requestId.toString()).equals("unhandle");
                        // while (getAction2(requestId.toString()).equals("unhandle")){
                        //   getAction2(requestId.toString());
                        // }

                        //  return;
                        // Launch login activity

                    } else {
                        String message=jObj.getString("message");

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg = message+" ask lock manger for help";
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

                // params.put("lockId", SQLiteHandler.CURRENT_LOCKID);
                Log.d("lockid",lockid);
                params.put("username",SQLiteHandler.CURRENT_USERNAME);
                params.put("lockid",SQLiteHandler.CURRENT_LOCKID);
               // params.put("userId",userid);
              //  params.put("lockId",lockid);

                // params.put("username", SQLiteHandler.CURRENT_USERNAME);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.d("REQid",requestId.toString());
        return requestId.toString();//// TODO: 08/05/2017 chk if really changing

    }




    public void chkLockStatus(){
        String uri="https://smartlockproject.herokuapp.com/api/getLock/"+SQLiteHandler.CURRENT_LOCKID;

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");
                            JSONObject c = jsonObj.getJSONObject("message");
                            String lockStatus = c.getString("status");


                            if (lockStatus.equals("open")) {
                                Intent intent = new Intent(MngUsers.this,
                                        OpenLock.class);
                                startActivity(intent);
                            }

                            else {
                                Intent intent = new Intent(MngUsers.this,
                                        CloseLock.class);
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
                        Toast.makeText(MngUsers.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void getStatus(MenuItem item){
        //https://smartlockproject.herokuapp.com/api/getLock/:lockid
        String lock_id="323djdjw32";
        /*String uri = String.format("https://smartlockproject.herokuapp.com/api/getUser?param1=%1$s",
                uid);*/
        String uri="https://smartlockproject.herokuapp.com/api/getLock/"+lock_id;

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            if (status.equals("open")) {
                                Intent intent = new Intent(MngUsers.this,
                                        OpenLock.class);
                                startActivity(intent);
                            }

                            else {
                                Intent intent = new Intent(MngUsers.this,
                                        CloseLock.class);
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
                        Toast.makeText(MngUsers.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
  /*  private List<User> userList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_users);
      recyclerView = (RecyclerView) findViewById(R.id.users_recycler_view);

        mAdapter = new UserAdapter(userList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareUserData();
    }
    private void prepareUserData() {
        User user = new User(123456, "Josh Toker", "0546377534", "3333333", "josh@gmail.com");
        userList.add(user);

        user = new User(123456, "Anna piterovski", "0546377534", "4444444", "Anna@gmail.com");
        userList.add(user);
    }
    }
}*/