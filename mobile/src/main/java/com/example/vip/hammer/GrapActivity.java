package com.example.vip.hammer;

/**
 * Created by vip on 2017-05-22.
 */
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;


public class GrapActivity extends Activity implements
        SensorEventListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener {
    private SensorManager SM;
    private Sensor sensor1;

    private static final String TAG = "Phone";
    private static final String COUNT_KEY = "com.example.key.count";
    private static final String Sensor_Test = "ST";
    private int count = 0;
    private String temp;
    GoogleApiClient mGoogleClient;

    private LineChart LC;
    private FrameLayout FL;

    private TextView textView1, textView2;

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grap);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        FL = (FrameLayout) findViewById(R.id.FL);

        SM=((SensorManager)getSystemService(SENSOR_SERVICE));
        List<Sensor> devicesSensors = SM.getSensorList(Sensor.TYPE_ALL);
        for(Sensor s : devicesSensors){
            Log.d(TAG, "name: " + s.getName());
            Log.d(TAG, "----------------------");
        }

        sensor1 = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Button btnCallMain = (Button) findViewById(R.id.CallMain);
        btnCallMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Onclick", "CallMainActivity");
                finish();
            }

        });
        mGoogleClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        //dbhelp = new DbHelper(context);

        LC = new LineChart(this);
        FL.addView(LC);
        textView1 = (TextView) findViewById((R.id.textView1));
        textView2 = (TextView) findViewById((R.id.textView2));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Google API Client was connected");
        Wearable.DataApi.addListener(mGoogleClient, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SM.unregisterListener(this, sensor1);
        Wearable.DataApi.removeListener(mGoogleClient, this);

        mGoogleClient.disconnect();
    }

    @Override
    protected void onStop() {
        if (null != mGoogleClient && mGoogleClient.isConnected()) {
            mGoogleClient.disconnect();
        }
        super.onStop();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Wearable.DataApi.removeListener(mGoogleClient, this);
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/count") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();

                    if(Sensor.TYPE_ACCELEROMETER== dataMap.getInt("Type"))
                        updateAccelerometer(dataMap.getDouble("AX"),dataMap.getDouble("AY"), dataMap.getDouble("AZ"));

                    if(Sensor.TYPE_GYROSCOPE== dataMap.getInt("Type")) {
                        // Log.i("Gyro test", "test "+dataMap.getInt("GX")+dataMap.getInt("GY")+dataMap.getInt("GZ")+dataMap.getInt("Type"));
                        updateGyro(dataMap.getDouble("GX"), dataMap.getDouble("GY"), dataMap.getDouble("GZ"));
                    }

                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }


    private void updateAccelerometer(double Ax, double Ay, double Az ) {
        Switch A_Switch= (Switch)findViewById(R.id.ASwitch);

        Ax = Double.parseDouble(String.format("%.2f",Ax));
        Ay = Double.parseDouble(String.format("%.2f",Ay));
        Az = Double.parseDouble(String.format("%.2f",Az));

        String Acc_Msg = "Acc " + "X : " + Ax + " Y : " + Ay + " Gz : " + Az;

        textView1.setText(Acc_Msg);

        LC.Grapdraw(Ax, Ay, Az, A_Switch.isChecked());

    }
    private void updateAccelerometer2(double Ax, double Ay, double Az ) {

    }

    private void updateGyro(double Gx, double Gy, double Gz) {

        Switch G_Switch= (Switch)findViewById(R.id.GSwitch);

        Gx = Double.parseDouble(String.format("%.2f",Gx));
        Gy = Double.parseDouble(String.format("%.2f",Gy));
        Gz = Double.parseDouble(String.format("%.2f",Gz));

        String Gyro_Msg = "Gyro " + "X : " + Gx + " Y : " + Gy + " Gz : " + Gz;
        textView2.setText(Gyro_Msg);

        LC.Grapdraw2(Gx, Gy, Gz, G_Switch.isChecked());

    }

    public void onResume()
    {
        super.onResume();
        SM.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            double accelX = (double) event.values[0];
            double accelY = (double) event.values[1];
            double accelZ = (double) event.values[2];

            String msg = "X: " + accelX + "Y: " + accelY + "Z: " + accelZ;
            Log.d(TAG, msg);

            updateAccelerometer2(accelX, accelY, accelZ);

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
