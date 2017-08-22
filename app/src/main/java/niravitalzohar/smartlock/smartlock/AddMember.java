package niravitalzohar.smartlock.smartlock;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/* manager adding  permission to members for lock */

public class AddMember extends AppCompatActivity {
    private RadioGroup category, lockpermission;
    private RadioButton manger, member, memberpyid, oneTime, anyTime;
    private EditText _email, _phone;
    private ProgressDialog pDialog;
    private SessionManager session;
    private permission_type catgoryresult;
    private Button plus;
    private String durationResult;
    private String permissionMsg="you've been received new permissions in SmartLock app.\n" +
            "You can download the app from the app store.\n" +
            "\n" +
            "Best regards,\n" +
            "Smart Lock Team.";

    //for popup
    private PopupWindow mPopupWindow;
    private LinearLayout mRelativeLayout, days;
    private Context mContext;
    private Activity mActivity;
    private EditText start, end;
    private TextView starttv, endtv;
    private GridLayout grid1, grid2;
    private Button sunday, monday, tuesday, wed, thursday, friday, saturday, savetime,out;
    private String[] startTime;
    private String[] endTime;
    int dayflag = 0;


    //for popup2
    private LinearLayout layout1, layout2, layout3;
    private Button b1, b2, b3, save2;
    private TextView tv1, tv2, tv3;
    private String date_result, start_result, end_result;

    //for menu
    StringBuilder requestId = new StringBuilder();
    String lockid="18:fe:34:d4:c6:e8";
    public String l_status=" ";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        session = new SessionManager(getApplicationContext());

        _email = (EditText) findViewById(R.id.email);
        _phone = (EditText) findViewById(R.id.PhoneET);
        plus = (Button) findViewById(R.id.plusUser);

        startTime = new String[]{"0","0","0","0","0","0","0"};
        endTime = new String[]{"0","0","0","0","0","0","0"};

        mContext = getApplicationContext();
        mActivity = AddMember.this;
        mRelativeLayout = (LinearLayout) findViewById(R.id.activity_add_member);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);


        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        category = (RadioGroup) findViewById(R.id.categoryRG);
        int checkedRadioButtonID = category.getCheckedRadioButtonId();
        category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.manger:
                        lockpermission.setVisibility(View.VISIBLE);
                        catgoryresult = permission_type.MANGER;
                        Log.d("cat",catgoryresult.toString());
                        break;
                    case R.id.member:
                        lockpermission.setVisibility(View.VISIBLE);
                        catgoryresult = permission_type.MEMBER;
                        Log.d("cat",catgoryresult.toString());
                        break;
                    case R.id.pmemeber:
                        catgoryresult = permission_type.MEMBER_WITH_PY_ID;
                        durationResult = "always";
                        lockpermission.setVisibility(View.GONE);
                        Log.d("cat",catgoryresult.toString());
                        break;
                    default:
                        Log.v("nn", "Huh?");
                        break;
                }
            }
        });


        lockpermission = (RadioGroup) findViewById(R.id.lockperRG);
        lockpermission.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("hhhh", "nnnnnnnn");

                if (checkedId == R.id.anytime) {
                    durationResult = "always";
                    //here open pop up
                    openPopup();

                } else if (checkedId == R.id.onetime) {
                    durationResult = "once";
                    openPopup2();

                }


            }
        }); //lockpermission radio group end

        plus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.d("jjj","add");
                System.out.println(Arrays.toString(startTime));
                System.out.println(Arrays.toString(endTime));
               String email = _email.getText().toString().trim().toLowerCase();;
                String phone = _phone.getText().toString().trim();

                if (!email.isEmpty() && !phone.isEmpty()) {
                    if(durationResult=="always") {
                        addPermission(email,phone);
                    }
                    else if(durationResult=="once"){
                        addPermission2(email,phone);

                    }
                }


            }
        }); // plus listner

    }

    public void addPermission(final String email,final String phone){
        String tag_string_req = "req_register";
        Log.d("start",startTime[0]+","+startTime[1]+","+startTime[2]+","+startTime[3]+","+startTime[4]+","+startTime[5]+","+startTime[6]);
        Log.d("end",endTime[0]+","+endTime[1]+","+endTime[2]+","+endTime[3]+","+endTime[4]+","+endTime[5]+","+endTime[6]);

       pDialog.setMessage("adding member ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_PERMISSION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "Add permission Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");

                    if (status.equals("success")) {



                        Toast.makeText(getApplicationContext(), "User permission successfully added!", Toast.LENGTH_LONG).show();
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(phone, null, permissionMsg, null, null);

                        // Launch login activity
                        Intent intent = new Intent(
                                AddMember.this,
                                MngUsers.class);
                        startActivity(intent);
                        finish();
                    } else {

                        String message=jObj.getString("message");
                        String errorMsg = message+" please try again";
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GJKKK", "addmemeber Error: " + error.getMessage());
                String errorMsg ="error - can't add memeber please try again ";
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
                hideDialog();
               // Toast.makeText(getApplicationContext(),
                //error.getMessage(), Toast.LENGTH_LONG).show();
              //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", email);
                Log.d("lockid-add member",AppConfig.CURRENT_LOCKID);
                params.put("lockid", AppConfig.CURRENT_LOCKID);
               params.put("type", String.valueOf(catgoryresult.ordinal()));
                Log.d("catres",catgoryresult.toString());
                params.put("frequency", durationResult);
                params.put("start1", startTime[0]);
                params.put("start2", startTime[1]);
                params.put("start3", startTime[2]);
                params.put("start4", startTime[3]);
                params.put("start5", startTime[4]);
                params.put("start6", startTime[5]);
                params.put("start7", startTime[6]);
                params.put("end1", endTime[0]);
                params.put("end2", endTime[1]);
                params.put("end3", endTime[2]);
                params.put("end4", endTime[3]);
                params.put("end5", endTime[4]);
                params.put("end6", endTime[5]);
                params.put("end7", endTime[6]);
                params.put("token", AppConfig.TOKEN); //// TODO: 29/06/2017 chk how to send token



                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void addPermission2(final String email,final String phone){
        String tag_string_req = "req_register";

         pDialog.setMessage("adding permission ...");
         showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.ADD_PERMISSION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("bnnjjj", "Add permission Response: " + response.toString());
                  hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String status=jObj.getString("status");
                    if (status.equals("success")) {

                        Toast.makeText(getApplicationContext(), "User permission successfully added!", Toast.LENGTH_LONG).show();
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(phone, null, permissionMsg, null, null);

                        // Launch login activity
                        Intent intent = new Intent(
                                AddMember.this,
                                MngUsers.class);
                        startActivity(intent);
                        finish();
                    } else {
                        String message=jObj.getString("message");
                        String errorMsg=message+"please try again";
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
                Log.e("addMember-addpermision", "add permission Error: " + error.getMessage());
                String errorMsg ="error - can't add memeber please try again";
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
                hideDialog();
               // Toast.makeText(getApplicationContext(),
                 //       error.getMessage(), Toast.LENGTH_LONG).show();
                //  hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", email);
                params.put("lockid", AppConfig.CURRENT_LOCKID);
                Log.d("lockid-add member",AppConfig.CURRENT_LOCKID);
                params.put("frequency", durationResult);
                Log.d("type",String.valueOf(catgoryresult.ordinal()));
               params.put("type", String.valueOf(catgoryresult.ordinal()));
                params.put("date", date_result);
                Log.d("start",start_result);
                params.put("start1", start_result);
                params.put("end1", end_result);
                params.put("token", AppConfig.TOKEN); //// TODO: 29/06/2017 chk how to send the token



                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    public void openPopup(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup,null);

        mPopupWindow = new PopupWindow(
                customView,
                LinearLayout.LayoutParams.MATCH_PARENT, //width
                LinearLayout.LayoutParams.WRAP_CONTENT //height
        );

        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));


        // out=(Button)customView.findViewById(R.id.out);
        days=(LinearLayout)customView.findViewById(R.id.days) ;
        start=(EditText)customView.findViewById(R.id.start);
        end=(EditText)customView.findViewById(R.id.end1);
        starttv=(TextView)customView.findViewById(R.id.startTV);
        //end=(TextView)customView.findViewById(R.id.end1);
        grid1=(GridLayout)customView.findViewById(R.id.grid1);
        grid1.setVisibility(View.GONE);
        grid2=(GridLayout)customView.findViewById(R.id.grid2);
        grid2.setVisibility(View.GONE);
        sunday=(Button)customView.findViewById(R.id.Sunday);
        monday=(Button)customView.findViewById(R.id.Monday);
        tuesday=(Button)customView.findViewById(R.id.Tuesday);
        wed=(Button)customView.findViewById(R.id.Wednesday);
        thursday=(Button)customView.findViewById(R.id.Thursday);
        friday=(Button)customView.findViewById(R.id.Friday);
        saturday=(Button)customView.findViewById(R.id.Saturday);
        savetime=(Button)customView.findViewById(R.id.timesave) ;
        out=(Button)customView.findViewById(R.id.out);


        sunday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                grid1.setVisibility(View.VISIBLE);
                grid2.setVisibility(View.VISIBLE);

                dayflag=0;



            }
        });

        monday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                grid1.setVisibility(View.VISIBLE);
                grid2.setVisibility(View.VISIBLE);
                dayflag=1;

            }
        });

        tuesday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                grid1.setVisibility(View.VISIBLE);
                grid2.setVisibility(View.VISIBLE);
                dayflag=2;

            }
        });

        wed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                grid1.setVisibility(View.VISIBLE);
                grid2.setVisibility(View.VISIBLE);
                dayflag=3;

            }
        });

        thursday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                grid1.setVisibility(View.VISIBLE);
                grid2.setVisibility(View.VISIBLE);
                dayflag=4;

            }
        });

        friday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                grid1.setVisibility(View.VISIBLE);
                grid2.setVisibility(View.VISIBLE);
                dayflag=5;

            }
        });

        saturday.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                grid1.setVisibility(View.VISIBLE);
                grid2.setVisibility(View.VISIBLE);
                dayflag=6;

            }
        });

        savetime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                grid1.setVisibility(View.GONE);
                grid2.setVisibility(View.GONE);
                start.getText().clear();
                end.getText().clear();

            }
        });


        start.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddMember.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.d("timechk",String.format("%02d:%02d", selectedHour, selectedMinute));
                        start.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                       // start.setText(selectedHour + ":" + selectedMinute);
                        startTime[dayflag]=start.getText().toString();
                        Log.d("one",startTime[dayflag]);


                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                Log.d("two","two");
                mTimePicker.show();

            }

        });


        end.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddMember.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        //end.setText( selectedHour + ":" + selectedMinute);
                        end.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        endTime[dayflag]=end.getText().toString();
                        Log.d("one", endTime[dayflag]);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);



    }


    public void  openPopup2(){

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup2,null);

        mPopupWindow = new PopupWindow(
                customView,
                LinearLayout.LayoutParams.MATCH_PARENT, //width
                LinearLayout.LayoutParams.WRAP_CONTENT//height
        );

        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        layout1=(LinearLayout)customView.findViewById(R.id.date) ;
        layout2=(LinearLayout)customView.findViewById(R.id.start_time) ;
        layout3=(LinearLayout)customView.findViewById(R.id.end_time) ;

        b1=(Button)customView.findViewById(R.id.b_date) ;
        b2=(Button)customView.findViewById(R.id.b_start) ;
        b3=(Button)customView.findViewById(R.id.b_end) ;
        save2=(Button)customView.findViewById(R.id.save2) ;

        tv1=(TextView)customView.findViewById(R.id.date_tv) ;
        tv2=(TextView)customView.findViewById(R.id.start_tv) ;
        tv3=(TextView)customView.findViewById(R.id.end_tv) ;



        b1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentDate = Calendar.getInstance();
                int day = mcurrentDate.get(Calendar.DAY_OF_MONTH);
                int month=mcurrentDate.get(Calendar.MONTH);
                int year=mcurrentDate.get(Calendar.YEAR);

                DatePickerDialog mDatePicker;
                mDatePicker = new DatePickerDialog(AddMember.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        selectedMonth+=1;
                        //selectedDay+=1;
                        tv1.setText( selectedDay + "." + selectedMonth+"."+selectedYear);
                        date_result=tv1.getText().toString();
                        Log.d("one", date_result);
                    }
                }, year, month,day);
                mDatePicker.setTitle("Select Time");
                mDatePicker.show();

            }
        });




        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddMember.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                       // tv2.setText( selectedHour + ":" + selectedMinute);
                        tv2.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        start_result=tv2.getText().toString();
                        Log.d("one", start_result);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        b3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddMember.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                       // tv3.setText( selectedHour + ":" + selectedMinute);
                        tv3.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        end_result=tv3.getText().toString();
                        Log.d("one", end_result);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        save2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });


        mPopupWindow.showAtLocation(mRelativeLayout, Gravity.CENTER,0,0);



    } //popup2 end

    public String getAction2(final String result) {
        Log.d("result", result);
        pDialog.setMessage("waiting for checking ...");
        showDialog();

        String uri = "https://smartlockproject.herokuapp.com/api/checkLockAction/" + result+
                "?token="+AppConfig.TOKEN;
        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "chk mnger for lock response: " + response.toString());
                        //  showJSON(response);
                      //  hideDialog();
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            l_status = jsonObj.getString("status");
                            Log.d("status", l_status);
                            String action = jsonObj.getString("action");
                            if (l_status.equals("lock")) {
                                Intent intent = new Intent(AddMember.this,
                                        CloseLock.class);
                                startActivity(intent);

                            } else if (l_status.equals("unlock")) {
                                Intent intent = new Intent(AddMember.this,
                                        OpenLock.class);
                                startActivity(intent);

                            } else if((l_status.equals("timeout")) ){
                                String errorMsg ="oops something went wrong please try again-timeout error";
                                Toast.makeText(getApplicationContext(),
                                        errorMsg, Toast.LENGTH_LONG).show();
                                hideDialog();
                            }
                            else{
                                getAction2(result);
                            }


                            //  }

                        } catch (JSONException e) {
                            Log.d("catch", "ch");
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorMsg ="oops something went wrong please try again";
                        Toast.makeText(AddMember.this, errorMsg, Toast.LENGTH_LONG).show();
                        hideDialog();
                    }
                });
        Log.d("status", l_status);
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

                    //requestId=jObj.getString("requestId");


                    if(status.equals("request created")){
                        requestId.append(jObj.getString("requestId"));
                        Log.d("REQid",requestId.toString());
                        //TODO go to get action


                      //  Toast.makeText(getApplicationContext(), "checking lock Status ", Toast.LENGTH_LONG).show();

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
                Log.d("lockid",lockid);
                params.put("lockid",AppConfig.CURRENT_LOCKID);
                params.put("token",AppConfig.TOKEN);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        Log.d("REQid",requestId.toString());
        return requestId.toString();//// TODO: 08/05/2017 chk if really changing

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
                Intent intent = new Intent(AddMember.this,
                        FingerPrint.class);
                startActivity(intent);

                return true;


            case R.id.setting:
                Intent intent2 = new Intent(AddMember.this,
                        Settings.class);
                startActivity(intent2);

                return true;

            case R.id.home:
                Intent intent3 = new Intent(AddMember.this,
                        MngUsers.class);
                startActivity(intent3);

                return true;




        }

        return false;
    }


    public void chkLockStatus(){
        String uri="https://smartlockproject.herokuapp.com/api/getLock/"+AppConfig.CURRENT_LOCKID;

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
                                Intent intent = new Intent(AddMember.this,
                                        OpenLock.class);
                                startActivity(intent);
                            }

                            else {
                                Intent intent = new Intent(AddMember.this,
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
                        Toast.makeText(AddMember.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
    }
}

