package bitloggers.wico;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class Search extends AppCompatActivity {
    ImageButton search,controls;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.WiCo_Purple);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        search = (ImageButton) findViewById(R.id.search_wifi);
        controls = (ImageButton) findViewById(R.id.control_btn);

    }

    public void nextAct(View v){
        switch(v.getId())
        {
            case R.id.search_wifi:
                Intent i = new Intent(this,WifiList.class);
                startActivity(i);
                break;

            case R.id.control_btn:
                Intent j = new Intent(this,Controls.class);
                startActivity(j);
                break;
        }


    }
}
