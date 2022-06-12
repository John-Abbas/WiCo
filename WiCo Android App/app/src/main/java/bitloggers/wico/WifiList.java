package bitloggers.wico;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.text.TextUtils.indexOf;
import static android.widget.CheckBox.*;

public class WifiList extends AppCompatActivity {

    TextView mainText;
    WifiManager mainWifi;
    WifiReceiver receiverWifi;
    List<ScanResult> wifiList;
    StringBuilder sb = new StringBuilder();
    ListView wifiListView;
    ArrayList<String> lst;
    ArrayAdapter<String> wifiArrAdptr;
    AlertDialog dialog;
    EditText wifi_name, wifi_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_list);
        mainText = (TextView) findViewById(R.id.WifiNum);
        mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (mainWifi.isWifiEnabled() == false)
        {
            // If wifi disabled then enable it
            Toast.makeText(getApplicationContext(), "WiFi is disabled..making it enabled",
                    Toast.LENGTH_LONG).show();
            mainWifi.setWifiEnabled(true);
        }
        receiverWifi = new WifiReceiver();
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        mainText.setText("Starting Scan...");
        wifiListView = (ListView) findViewById(R.id.wifiList);
        lst = new ArrayList<String>();
        wifiArrAdptr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lst);
        wifiListView.setAdapter(wifiArrAdptr);


        wifiListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String wifi_ssid = (String) wifiListView.getItemAtPosition(position);
                AlertDialog.Builder mAlert = new AlertDialog.Builder(WifiList.this);
                View mView = getLayoutInflater().inflate(R.layout.connect_wifi,null);
                wifi_name = (EditText) mView.findViewById(R.id.device_name);
                wifi_pass = (EditText) mView.findViewById(R.id.device_pass);
                CheckBox sHPassChBox = (CheckBox)mView.findViewById(R.id.show_pass);
                sHPassChBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
                {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                    {
                        if ( isChecked ) {
                            wifi_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                        else{
                            wifi_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }

                    }
                });
                wifi_name.setText(wifi_ssid);
                wifi_name.setEnabled(false);
                mAlert.setView(mView);
                dialog = mAlert.create();
                dialog.show();
            }
        });
    }

    protected void onPause() {
        unregisterReceiver(receiverWifi);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    public void dialogProcess(View view) {
        switch(view.getId())
        {
            case R.id.cancel:
                dialog.dismiss();
                break;

            case R.id.device_connect:
                WifiConfiguration wifiConfig = new WifiConfiguration();
                wifiConfig.SSID = String.format("\"%s\"", wifi_name.getText());
                wifiConfig.preSharedKey = String.format("\"%s\"", wifi_pass.getText());

                int netId = mainWifi.addNetwork(wifiConfig);
                mainWifi.disconnect();
                mainWifi.enableNetwork(netId, true);
                mainWifi.reconnect();
                dialog.dismiss();
                break;
        }
    }

    // Broadcast receiver class called its receive method
    // when number of wifi connections changed

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
            List<String> ssidList = new ArrayList<>();
            lst.clear();
            sb = new StringBuilder();
            wifiList = mainWifi.getScanResults();
            List<WifiConfiguration> configuredList = mainWifi.getConfiguredNetworks();
            for(WifiConfiguration config : configuredList) {
                ssidList.add(config.SSID.substring(config.SSID.indexOf("\"")+1,config.SSID.length()-1));
            }
            sb.append("\n\t\tNumber Of Wifi connections : " + wifiList.size() + "\n\n");
            for (int i = 0; i < wifiList.size(); i++) {
                String wifidata = wifiList.get(i).toString();
                if(!ssidList.contains(wifidata.substring(indexOf(wifidata, ":")+2, indexOf(wifidata, ","))))
                    lst.add(wifidata.substring(indexOf(wifidata, ":")+2, indexOf(wifidata, ",")));
            }
            mainText.setText(sb);
            wifiArrAdptr.notifyDataSetChanged();

        }

    }
}
