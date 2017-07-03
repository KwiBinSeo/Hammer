package com.example.vip.hammer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class MainActivity extends WearableActivity implements
        SensorEventListener,
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "WearMain";
    private static final String COUNT_KEY = "com.example.key.count";
    private static final String Sensor_Test="ST";
    int count =0;

    private SensorManager SM;
    private Sensor sensor1;
    private Sensor sensor2;
    private TextView mTextView;

    private GoogleApiClient mGoogleApiClient;

    private double accelX, accelY, accelZ;
    private double gyroX, gyroY, gyroZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        SM = ((SensorManager)getSystemService(SENSOR_SERVICE));
        List<Sensor> devicesSensors = SM.getSensorList(Sensor.TYPE_ALL);
        for(Sensor s : devicesSensors){
            Log.d(TAG, "name: " + s.getName());
            Log.d(TAG, "----------------------");
        }

        setAmbientEnabled();

        sensor1 = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor2 = SM.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
    }
    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);

        //mTextView.setTextColor(Color.WHITE);
        //mTextView.getPaint().setAntiAlias(false);
    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();

//        mTextView.setTextColor(Color.GREEN);
//        mTextView.getPaint().setAntiAlias(true);
    }
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    SensorEvent event;

    public void onSensorChanged(SensorEvent event) {
          if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelX = (double) event.values[0];
            accelY = (double) event.values[1];
            accelZ = (double) event.values[2];

            String msg = "X: " + accelX + "Y: " + accelY + "Z: " + accelZ;
            Log.d(TAG, msg);
            AccelCounter(accelX, accelY, accelZ, Sensor.TYPE_ACCELEROMETER);
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            gyroX = (double) event.values[0];
            gyroY = (double) event.values[1];
            gyroZ = (double) event.values[2];

            String msg = "X: " + gyroX + "Y: " + gyroY + "Z: " + gyroZ;
            Log.d(TAG, msg);

            GyroCounter(gyroX, gyroY, gyroZ, Sensor.TYPE_GYROSCOPE);
        }
    }

    private void AccelCounter(double ax, double ay, double az, int Type) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/count");

        putDataMapReq.getDataMap().putDouble("AX", ax);
        putDataMapReq.getDataMap().putDouble("AY", ay);
        putDataMapReq.getDataMap().putDouble("AZ", az);
        putDataMapReq.getDataMap().putInt("Type", Type);

        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

    private void GyroCounter(double gx, double gy, double gz, int Type)
    {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/count");

        putDataMapReq.getDataMap().putDouble("GX", gx);
        putDataMapReq.getDataMap().putDouble("GY", gy);
        putDataMapReq.getDataMap().putDouble("GZ", gz);
        putDataMapReq.getDataMap().putInt("Type", Type);


        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient, putDataReq);
    }

    //SENSOR_DELAY_UI
    // SENSOR_DELAY_NORMAL
    // SENSOR_DELAY_GAME
    // SENSOR_DELAY_FASTEST

    protected void onResume() {
        SM.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
        SM.registerListener(this, sensor2, SensorManager.SENSOR_DELAY_NORMAL);
        mGoogleApiClient.connect();
        super.onResume();

    }

    protected void onPause() {
        SM.unregisterListener(this, sensor1);
        SM.unregisterListener(this, sensor2);
        mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended(): Connection to Google API client was suspended");
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
