package niravitalzohar.smartlock.smartlock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    private LinearLayout allLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allLayout = (LinearLayout) findViewById(R.id.rel_activity_main);

        allLayout.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ktk","here");
                startActivity(new Intent(MainActivity.this, Login.class));

            }
        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_screen, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.help:
                Intent intent = new Intent(MainActivity.this,
                        Help.class);
                startActivity(intent);

                return true;

        }

        return false;
    }


}
