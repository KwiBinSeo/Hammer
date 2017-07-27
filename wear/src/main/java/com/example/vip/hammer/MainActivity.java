package com.example.vip.hammer;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends WearableActivity {

    private Button workBtn, upBtn, downBtn;
    private String up, down, work;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setAmbientEnabled();

        workBtn = (Button) findViewById(R.id.WorkBtn);
        upBtn = (Button) findViewById(R.id.UpBtn);
        downBtn = (Button) findViewById(R.id.DownBtn);

        up = "up";
        down = "down";
        work = "work";

        // 걷기 버튼
        workBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("state",work); // workBtn 의 text를 intent 전달
                startActivity(intent);

                finish(); // 현재 스택 종료
            }
        });

        // 계단 올라가기 버튼
        upBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("state",up); // upBtn 의 text를 intent 전달
                startActivity(intent);

                finish(); // 현재 스택 종료
            }
        });

        // 계단 내려가기 버튼
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                intent.putExtra("state",down); // downBtn 의 text를 intent 전달
                startActivity(intent);

                finish(); // 현재 스택 종료
            }
        });
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);
        updateDisplay();
    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        updateDisplay();
    }

    @Override
    public void onExitAmbient() {
        updateDisplay();
        super.onExitAmbient();
    }

    private void updateDisplay() {

    }
}
