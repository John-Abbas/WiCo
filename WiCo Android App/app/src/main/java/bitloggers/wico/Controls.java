package bitloggers.wico;

import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class Controls extends AppCompatActivity {
    TcpClient mTcpClient;
    ImageButton switch1,switch2,switch3,switch4;
    static boolean switch1_state = false,switch2_state = false,switch3_state = false,switch4_state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);
        switch1 = (ImageButton) findViewById(R.id.app1_sw);
        switch2 = (ImageButton) findViewById(R.id.app2_sw);
        switch3 = (ImageButton) findViewById(R.id.app3_sw);
        switch4 = (ImageButton) findViewById(R.id.app4_sw);
        new ConnectTask().execute("");
    }

    protected void onRestart() {
        new ConnectTask().execute("");
        super.onRestart();
    }

    protected void onStop() {
        if(mTcpClient != null)
            mTcpClient.stopClient();
        super.onStop();
    }
    public void controlRelays(View v) {

        switch (v.getId())
        {
            case R.id.app1_sw:
                //sends the message to the server
                if(switch1_state == true) {
                    switch1.setImageResource(R.mipmap.switch_off);
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("OFF1");
                    }
                    switch1_state = false;
                }
                else {
                    switch1.setImageResource(R.mipmap.switch_on);
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("ON1");
                    }
                    switch1_state = true;
                }
                break;
            case R.id.app2_sw:
                if(switch2_state == true) {
                    switch2.setImageResource(R.mipmap.switch_off);
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("OFF2");
                    }
                    switch2_state = false;
                }
                else {
                    switch2.setImageResource(R.mipmap.switch_on);
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("ON2");
                    }
                    switch2_state = true;
                }
                break;
            case R.id.app3_sw:
                if(switch3_state == true) {
                    switch3.setImageResource(R.mipmap.switch_off);
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("OFF3");
                    }
                    switch3_state = false;
                }
                else {
                    switch3.setImageResource(R.mipmap.switch_on);
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("ON3");
                    }
                    switch3_state = true;
                }
                break;
            case R.id.app4_sw:
                if(switch4_state == true) {
                    switch4.setImageResource(R.mipmap.switch_off);
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("OFF4");
                    }
                    switch4_state = false;
                }
                else {
                    switch4.setImageResource(R.mipmap.switch_on);
                    if (mTcpClient != null) {
                        mTcpClient.sendMessage("ON4");
                    }
                    switch4_state = true;
                }
                break;
        }
    }


    public class ConnectTask extends AsyncTask<String, String, TcpClient> {
        @Override
        protected TcpClient doInBackground(String... message) {

            //we create a TCPClient object
            mTcpClient = new TcpClient(new TcpClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            });
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            //response received from server
            Log.d("test", "response " + values[0]);
            //process server response here....

        }
    }
}
