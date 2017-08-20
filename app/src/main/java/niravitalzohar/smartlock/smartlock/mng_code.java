package niravitalzohar.smartlock.smartlock;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class mng_code extends AppCompatActivity {
    private TextView lockidTv;
    private EditText lockidET;
    private Button v_button;
    private String permissionType;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mng_code);

        lockidTv=(TextView)findViewById(R.id.lockidTV) ;
        lockidET=(EditText)findViewById(R.id.lockidET) ;
        v_button=(Button)findViewById(R.id.v_button);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        v_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String lockid=lockidET.getText().toString().trim();
                Log.d("kkk",lockid);
                Intent intent = new Intent(getBaseContext(), SignUp.class);
                intent.putExtra("lockid",lockid);
                startActivity(intent);

            }
        });


    }


}
