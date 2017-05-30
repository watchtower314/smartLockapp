package niravitalzohar.smartlock.smartlock;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class MemberChangeD extends AppCompatActivity {
    private EditText ETusername, ETphone;

    private String username=" ";
    private String phone=" ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_change_d);
        username = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        ETusername=(EditText)findViewById(R.id.m_cnguser);
        ETusername.setText(username);

        ETphone=(EditText)findViewById(R.id.m_cngphone);
        ETphone.setText(phone);
    }
}
