package com.example.vip.hammer;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class activity_MPAndroid extends AppCompatActivity  implements
        SensorEventListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        DataApi.DataListener {

    LineChart lineChart;
    LineData data_accX;
    public float number;
    public static int count = 0, index = 0;
    public static int check = 0;
    public float max = 40, min = -40;

    public static ArrayList<Entry> entries_accX = new ArrayList<>();
    public static ArrayList<Entry> entries_accY = new ArrayList<>();
    public static ArrayList<Entry> entries_accZ = new ArrayList<>();
    public static ArrayList<String> labels = new ArrayList<>();
    public String flag;

    // 가속도 센서 데이터를 저장하는 배열
    public static float[] accX = new float[20];
    public static float[] accY = new float[20];
    public static float[] accZ = new float[20];
    public static int[] _flag = new int[20];

    // 시계 부분 추가
    GoogleApiClient mGoogleClient;
    private static final String TAG = "Phone";
    private float number_accX, number_accY, number_accZ;
    private SensorManager SM;
    private Sensor sensor1;

    // 데이터 갯수 세는 변수
    private static int input_count = 0;
    private static int inputed_count = 0;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__mpandroid);

        lineChart = (LineChart) findViewById(R.id.chart);

        lineChart.getAxisLeft().setAxisMinValue(min);
        lineChart.getAxisLeft().setAxisMaxValue(max);
        lineChart.getAxisRight().setAxisMinValue(min);
        lineChart.getAxisRight().setAxisMaxValue(max);

        YAxis yAxis_Left = lineChart.getAxisLeft();
        yAxis_Left.setLabelCount(5,true);
        YAxis yAxis_Right = lineChart.getAxisRight();
        yAxis_Right.setLabelCount(5,true);

        SM = ((SensorManager) getSystemService(SENSOR_SERVICE));
        List<Sensor> devicesSensors = SM.getSensorList(Sensor.TYPE_ALL);

        for(Sensor s : devicesSensors){
            Log.d(TAG, "name: " + s.getName());
            Log.d(TAG, "----------------------");
        }

        sensor1 = SM.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGoogleClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();

        // 텍스트뷰 생성
        textView = (TextView) findViewById(R.id.textView);
    }

    public void DrawGrap() {

        entries_accX.clear();
        entries_accY.clear();
        entries_accZ.clear();
        labels.clear();

        for(int i = 0; i < index; i++)
        {
            entries_accX.add(new Entry(accX[i], i));
            entries_accY.add(new Entry(accY[i], i));
            entries_accZ.add(new Entry(accZ[i], i));
            flag = Integer.toString(_flag[i]);
            labels.add(flag);
        }

        LineDataSet dataset_accX = new LineDataSet(entries_accX, "acc X Data");
        dataset_accX.setColor(Color.RED);
        LineDataSet dataset_accY = new LineDataSet(entries_accY, "acc Y Data");
        dataset_accY.setColor(Color.BLUE);
        LineDataSet dataset_accZ = new LineDataSet(entries_accZ, "acc Z Data");
        dataset_accY.setColor(Color.GREEN);

        ArrayList<LineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataset_accX);
        dataSets.add(dataset_accY);
        dataSets.add(dataset_accZ);

        data_accX = new LineData(labels, dataSets);

        lineChart.setData(data_accX);

        lineChart.animateY(0);
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

    private void updateAccelerometer2(double Ax, double Ay, double Az ) {

    }

    private void updateGyro(double Gx, double Gy, double Gz) {

        Switch G_Switch= (Switch)findViewById(R.id.Gswitch);

        Gx = Double.parseDouble(String.format("%.2f",Gx));
        Gy = Double.parseDouble(String.format("%.2f",Gy));
        Gz = Double.parseDouble(String.format("%.2f",Gz));

        String Gyro_Msg = "Gyro " + "X : " + Gx + " Y : " + Gy + " Gz : " + Gz;

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

    private void updateAccelerometer(double Ax, double Ay, double Az ) {
        Switch A_Switch = (Switch) findViewById(R.id.Aswitch);

        Ax = Double.parseDouble(String.format("%.2f",Ax));
        Ay = Double.parseDouble(String.format("%.2f",Ay));
        Az = Double.parseDouble(String.format("%.2f",Az));

        if(A_Switch.isChecked()) {

            // DB 삽입 부분
            input_count++; // 데이터 입력받은 순간 count 값 증가

            insert(Ax, Ay, Az); //Database에 추가
            inputed_count++;
            textView.setText(inputed_count + " / " + input_count); // 카운트 부분 추가
            // DB 삽입 부분 끝

            number_accX = (float) Ax;
            number_accY = (float) Ay;
            number_accZ = (float) Az;
            check++;
            if (check >= 19) {
                for (int i = 0; i < 19; i++) {
                    accX[i] = accX[i + 1];
                    accY[i] = accY[i + 1];
                    accZ[i] = accZ[i + 1];
                    _flag[i] = _flag[i + 1];
                }
                accX[19] = number_accX;
                accY[19] = number_accY;
                accZ[19] = number_accZ;
                _flag[19] = count;
                DrawGrap();
            } else {
                accX[index] = number_accX;
                accY[index] = number_accY;
                accZ[index] = number_accZ;
                _flag[index] = count;
                Log.d("NUMBER", Float.toString(number));

                DrawGrap();
                index++;
            }
            count++;
        }
        String Acc_Msg = "Acc " + "X : " + Ax + " Y : " + Ay + " Gz : " + Az;
    }
    //데이터 삽입 시작
    public void insert(double Ax, double Az, double Ay){
        insertToDatabase(Ax, Az, Ay);
    }

    private void insertToDatabase(double _Ax, double _Ay, double _Az) {
        String Ax = Double.toString(_Ax);
        String Ay = Double.toString(_Ay);
        String Az = Double.toString(_Az);
        class InsertData extends AsyncTask<String, String, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(activity_MPAndroid.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), "데이터가 정상적으로 입력되었습니다.", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                try{
                    String Ax = params[0];
                    String Ay = params[1];
                    String Az = params[2];

                    String link = "http://220.69.209.170/vip/android/hammer/insert_data.php";

                    String data  = URLEncoder.encode("Ax", "UTF-8") + "=" + URLEncoder.encode(Ax, "UTF-8");
                    data += "&" + URLEncoder.encode("Ay", "UTF-8") + "=" + URLEncoder.encode(Ay, "UTF-8");
                    data += "&" + URLEncoder.encode("Az", "UTF-8") + "=" + URLEncoder.encode(Az, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( data );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();

                    // Read Server Response
                    while(reader.readLine() != null)
                    {
                        sb.append(reader.readLine());
                        break;
                    }
                    return sb.toString();
                }
                catch(Exception e){
                    return new String("Exception: " + e.getMessage());
                }

            }
        }
        InsertData task = new InsertData();
        task.execute(Ax, Ay, Az);
    }
    // 데이터 삽입 끝
}
