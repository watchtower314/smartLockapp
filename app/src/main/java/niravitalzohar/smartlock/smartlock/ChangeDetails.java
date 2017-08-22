package niravitalzohar.smartlock.smartlock;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.Calendar;
/* to this activity only user with manager permission will get ,the manager can change user's details (only email&phone) and update permission  */
public class ChangeDetails extends AppCompatActivity {
    private EditText ETusername, ETphone;
    private RadioGroup category, lockpermission;
    private RadioButton manger, member, memberpyid, oneTime, anyTime;
    private ProgressDialog pDialog;
    private permission_type catgoryresult;
    private String durationResult;

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


    //for params
    private String username=" ";
    private String phone=" ";
    private String frequency=" ";

    boolean flag=false;




//58e91fd7fafa6700044b8d611494263871536

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_details);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        startTime = new String[]{"0","0","0","0","0","0","0"};
        endTime = new String[]{"0","0","0","0","0","0","0"};
         username = getIntent().getStringExtra("name");
         phone = getIntent().getStringExtra("phone");
        frequency = getIntent().getStringExtra("freq");
        String date = getIntent().getStringExtra("date");
       if (frequency.equals("once")) {
            start_result = getIntent().getStringExtra("start");
             end_result = getIntent().getStringExtra("end");
        }
       else if(frequency.equals("always")){
           startTime = getIntent().getStringArrayExtra("start");
           endTime = getIntent().getStringArrayExtra("end");
           Log.d("CHKKKKKKKKKKKKKKKK",username+" "+phone+" "+frequency+" "+date+" "+startTime[0]+endTime[0]);

       }


           //Log.d("CHKKKKKKKKKKKKKKKK",username+" "+phone+" "+frequency+" "+date+" "+start+" "+end);
       // Log.d("CHKKKKKKKKKKKKKKKK",username+" "+phone+" "+frequency+" "+date+" ");

        ETusername=(EditText)findViewById(R.id.cnguser);
        ETusername.setText(username);

        ETphone=(EditText)findViewById(R.id.cngphone);
        ETphone.setText(phone);



        mContext = getApplicationContext();
        mActivity = ChangeDetails.this;
        mRelativeLayout = (LinearLayout) findViewById(R.id.activity_change_details);
        category = (RadioGroup) findViewById(R.id.categoryRG);
        int checkedRadioButtonID = category.getCheckedRadioButtonId();
        category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch (id) {
                    case R.id.manger:
                        lockpermission.setVisibility(View.VISIBLE);
                        catgoryresult = permission_type.MANGER;
                        break;
                    case R.id.member:
                        lockpermission.setVisibility(View.VISIBLE);
                        catgoryresult = permission_type.MEMBER;
                        break;
                    case R.id.pmemeber:
                        catgoryresult = permission_type.MEMBER_WITH_PY_ID;
                        durationResult = "always";
                        lockpermission.setVisibility(View.GONE);
                        break;
                    default:
                        Log.v("nn", "Huh?");
                        break;
                }
            }
        });

        lockpermission = (RadioGroup) findViewById(R.id.lockperRG);
        // lockpermission.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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


    }//end on create

    public void openPopup(){
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View customView = inflater.inflate(R.layout.popup,null);

        mPopupWindow = new PopupWindow(
                customView,
                LinearLayout.LayoutParams.MATCH_PARENT, //width
                LinearLayout.LayoutParams.WRAP_CONTENT//height
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
                mTimePicker = new TimePickerDialog(ChangeDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                       // start.setText(selectedHour + ":" + selectedMinute);
                        start.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
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
                mTimePicker = new TimePickerDialog(ChangeDetails.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                      //  end.setText( selectedHour + ":" + selectedMinute);
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
                mDatePicker = new DatePickerDialog(ChangeDetails.this, new DatePickerDialog.OnDateSetListener() {
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
                mTimePicker = new TimePickerDialog(ChangeDetails.this, new TimePickerDialog.OnTimeSetListener() {
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
                mTimePicker = new TimePickerDialog(ChangeDetails.this, new TimePickerDialog.OnTimeSetListener() {
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

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cng_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.info_cng:
                updateUser();
            return true;
            case R.id.save_cng:
                    if (durationResult.equals("always"))
                        updatePermission1();
                    else
                        updatePermission2();

                return true;

            case R.id.delete_user:

                  deletePermission();
                return true;

            case R.id.back:
                finish();
                return true;


        }
        return false;
    }

    public void deletePermission(){
        String uri3="https://smartlockproject.herokuapp.com/api/removePermission/"+username+"/"+AppConfig.CURRENT_LOCKID+
                "?token="+AppConfig.TOKEN;


        final StringRequest stringRequest3 = new StringRequest(Request.Method.DELETE,uri3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "User's permission successfully removed!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangeDetails.this,
                                        MngUsers.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "User's permission was not removed!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangeDetails.this,
                                        MngUsers.class);
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
                        Toast.makeText(ChangeDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue3 = Volley.newRequestQueue(this);
        requestQueue3.add(stringRequest3);

    }



    //for once permission;
    public void updatePermission2(){
        //String lockid="323djdjw32";
        pDialog.setMessage("updating permission ...");
        AppConfig.showDialog(pDialog);
        Log.d("d",durationResult);
        Log.d("C",String.valueOf(catgoryresult.ordinal()));

        String uri4="https://smartlockproject.herokuapp.com/api/updatePermission/"+username+"/"+AppConfig.CURRENT_LOCKID+"/"+
                durationResult+"/"+String.valueOf(catgoryresult.ordinal())+"/"+date_result+"/"+start_result+"/"+end_result
                +"?token="+AppConfig.TOKEN;
                

        final StringRequest stringRequest4 = new StringRequest(Request.Method.PUT,uri4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "User's  permission successfully updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangeDetails.this,
                                        MngUsers.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "User's permision was not updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangeDetails.this,
                                        MngUsers.class);
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
                       // Toast.makeText(ChangeDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                        String errorMsg ="error - can't update member permission please try again ";
                        Toast.makeText(getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                        AppConfig.hideDialog(pDialog);
                    }
                });

        RequestQueue requestQueue4 = Volley.newRequestQueue(this);
        requestQueue4.add(stringRequest4);

    }
//for always permissiom
    public void updatePermission1(){
       // String lockid="323djdjw32";

        Log.d("start",startTime[0]+","+startTime[1]+","+startTime[2]+","+startTime[3]+","+startTime[4]+","+startTime[5]+","+startTime[6]);
        Log.d("end",endTime[0]+","+endTime[1]+","+endTime[2]+","+endTime[3]+","+endTime[4]+","+endTime[5]+","+endTime[6]);
        Log.d("d",durationResult);
        Log.d("C",String.valueOf(catgoryresult.ordinal()));


        String uri2="https://smartlockproject.herokuapp.com/api/updatePermission/"+username+"/"+AppConfig.CURRENT_LOCKID+"/"+durationResult+
                "/"+String.valueOf(catgoryresult.ordinal())+"/"
               +startTime[0]+"/"+endTime[0]+"/"+startTime[1]+"/"+endTime[1]+"/"
                +startTime[2]+"/"+endTime[2]+"/"+
                startTime[3]+"/"+endTime[3]+"/"+startTime[4]+"/"+endTime[4]+"/"+startTime[5]+"/"+endTime[5]+"/"+startTime[6]+"/"+
                endTime[6]+"?token="+AppConfig.TOKEN;

        final StringRequest stringRequest2 = new StringRequest(Request.Method.PUT,uri2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "User's  permission successfully updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangeDetails.this,
                                        MngUsers.class);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "User's permision was not updated!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(ChangeDetails.this,
                                        MngUsers.class);
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
                        Toast.makeText(ChangeDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);

    }



    public void updateUser(){
        String userName=ETusername.getText().toString().trim().toLowerCase();
        String phone=ETphone.getText().toString().trim();

        String uri="https://smartlockproject.herokuapp.com/api/updateUser/"+AppConfig.CURRENT_USERNAME+"/"+userName
                +"/"+phone;//TODO add the prams here

        final StringRequest stringRequest = new StringRequest(Request.Method.PUT,uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "Register Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String status = jsonObj.getString("status");

                            if (status.equals("success")) {
                                Toast.makeText(getApplicationContext(), "User details successfully changed!", Toast.LENGTH_LONG).show();
                                flag=true;
                               // Intent intent = new Intent(ChangeDetails.this,
                                 //       MngUsers.class);
                                //startActivity(intent);
                            }

                            else {
                                Toast.makeText(getApplicationContext(), "User details wasn't changed!", Toast.LENGTH_LONG).show();
                                //Intent intent = new Intent(ChangeDetails.this,
                                  //      MngUsers.class);
                                //startActivity(intent);
                            }





                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChangeDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
    }


}//end class
