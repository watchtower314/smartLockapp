package niravitalzohar.smartlock.smartlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static niravitalzohar.smartlock.smartlock.permission_type.MANGER;
import static niravitalzohar.smartlock.smartlock.permission_type.MEMBER;


public class UserDetails extends AppCompatActivity {

    private TextView TVusername,TVuserphone ,userpermission;
    private String frequency=" ";
    private String date=" ";
    private String start=" ";
    private String end=" ";
    private ImageView change_bt;
    private String name=" ";
    private String phone=" ";
    private String[] startTime;
    private String[] endTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        String details = getIntent().getStringExtra("userD");
         Log.d("kk",details);

        startTime = new String[]{"0","0","0","0","0","0","0"};
        endTime = new String[]{"0","0","0","0","0","0","0"};

        //TODO change this logic
        String[] parts = details.split(",\\s*");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 034556

        Log.d("kk",part1);
        Log.d("kk",part2);

        parts=part1.split("=");
        String []parts2=part2.split("=");
        part1 = parts[0];
        part2 = parts[1];
        String part3=parts2[0];
        String part4=parts2[1];

        Log.d("kk",part1);
        Log.d("kk",part2);
        Log.d("kk",part3);
        Log.d("kk",part4);

        name=part4;
        phone=part2;
// till here

        TVusername=(TextView) findViewById(R.id.useremail);
        TVusername.setText(part4);

        TVuserphone=(TextView) findViewById(R.id.userphone);
        TVuserphone.setText(part2);

        userpermission=(TextView)findViewById(R.id.userpermission);
        change_bt=(ImageView)findViewById(R.id.change_bt);

        getuserPermission(part4);

        change_bt.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                if (SQLiteHandler.CURRENT_PERMISSION_TYPE!=MANGER){
                    Intent intent = new Intent(getBaseContext(), MemberChangeD.class);
                    intent.putExtra("name", name);
                    intent.putExtra("phone", phone);
                    startActivity(intent);

                }
                if (frequency.equals("once")) {

                    Intent intent = new Intent(getBaseContext(), ChangeDetails.class);
                    intent.putExtra("name", name);
                    intent.putExtra("phone", phone);
                    intent.putExtra("freq", frequency);
                    intent.putExtra("date", date);
                    intent.putExtra("start", start);
                    intent.putExtra("end", end);
                    startActivity(intent);
                }
                else if(frequency.equals("always")){
                    date="null";
                    Intent intent = new Intent(getBaseContext(), ChangeDetails.class);
                    intent.putExtra("name", name);
                    intent.putExtra("phone", phone);
                    intent.putExtra("freq", frequency);
                    intent.putExtra("date", date);
                    intent.putExtra("start", startTime);
                    intent.putExtra("end", endTime);
                    startActivity(intent);

                }
            }

           });



    }

    public void getuserPermission(String username){
       // String lockid="323djdjw32";

        String uri="https://smartlockproject.herokuapp.com/api/getPermission/"+username+"/"+SQLiteHandler.CURRENT_LOCKID;

        final StringRequest stringRequest = new StringRequest(uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE", "permission Response: " + response.toString());
                        //  showJSON(response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            JSONObject message = jsonObj.getJSONObject("message");
                            frequency=message.getString("frequency");
                           // String type=jsonObj.getString("type"); //tell avital to chk it cus its missing
                            if (frequency.equals("once")) {
                                Log.d("bbb","immm hereeeeeeeeee");
                                  date=message.getString("date");
                                JSONObject c = message.getJSONObject("hours");
                                  start = c.getString("start");
                                  end = c.getString("end");
                                Log.d("startttttttttt", start);

                                userpermission.setText("Frequency:  "+frequency+"\n"+"Date: "+date+"\n"+"From: "+start+"\n"+"To: "+end);

                            }
                            else if(frequency.equals("always")){
                                JSONObject duration=message.getJSONObject("duration");
                                JSONObject Saturday=duration.getJSONObject("Saturday");
                                startTime[6]=Saturday.getString("start");
                                endTime[6]=Saturday.getString("end");
                                JSONObject Friday=duration.getJSONObject("Friday");
                                startTime[5]=Friday.getString("start");
                                endTime[5]=Friday.getString("end");
                                JSONObject Thursday=duration.getJSONObject("Thursday");
                                startTime[4]=Thursday.getString("start");
                                endTime[4]=Thursday.getString("end");
                                JSONObject Wednesday=duration.getJSONObject("Wednesday");
                                startTime[3]=Wednesday.getString("start");
                                endTime[3]=Wednesday.getString("end");
                                JSONObject Tuesday=duration.getJSONObject("Tuesday");
                                startTime[2]=Tuesday.getString("start");
                                endTime[2]=Tuesday.getString("end");
                                JSONObject Monday=duration.getJSONObject("Monday");
                                startTime[1]=Monday.getString("start");
                                endTime[1]=Monday.getString("end");
                                JSONObject Sunday=duration.getJSONObject("Sunday");
                                startTime[0]=Monday.getString("start");
                                endTime[0]=Monday.getString("end");
                                userpermission.setText("Frequency:  "+frequency+"\n"+
                                "Sunday - "+"from "+startTime[0]+" to "+endTime[0]+"\n"+
                                 "Monday -"+"from "+startTime[1]+" to "+endTime[1]+"\n"+
                                 "Tuesday - "+"from "+startTime[2]+" to "+endTime[2]+"\n"+
                                 "Wednesday - "+"from "+startTime[3]+" to "+endTime[3]+"\n"+
                                 "Thursday - "+"from "+startTime[4]+" to "+endTime[4]+"\n"+
                                  "Friday - "+"from "+startTime[5]+" to "+endTime[5]+"\n"+
                                   "Saturday - "+"from "+startTime[6]+" to "+endTime[6]
                                );

                            }
                            else{
                                Toast.makeText(getApplicationContext(), "cant show permission right now", Toast.LENGTH_LONG).show();

                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }//end on response
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UserDetails.this,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
