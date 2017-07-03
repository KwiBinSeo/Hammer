package com.example.vip.hammer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by vip on 2017-05-22.
 */

public class LineChart extends View {

    Paint mPaint;

    ArrayList<Integer> X_Adata = new ArrayList<>();
    ArrayList<Integer> X_Gdata = new ArrayList<>();

    int XA_x = 10;
    int XG_x = 10;

    ArrayList<XYZ> Y_Adata = new ArrayList<>();
    ArrayList<XYZ> Y_Gdata = new ArrayList<>();
    Boolean AcCheck = Boolean.FALSE;
    Boolean GyCheck = Boolean.FALSE;

    public LineChart(Context context) {
        super(context);

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(3);
        mPaint.setAntiAlias(true);
    }

    public void Grapdraw(double ax, double ay, double az,  boolean A_check) {


        XYZ xyztemp = new XYZ();
        xyztemp.setData(ax, ay, az);


        if ((Y_Adata.size() > 100)) {
            X_Adata.remove(0);
            Y_Adata.remove(0);

            for (int i = 0; i < X_Adata.size(); i++)
                X_Adata.set(i, X_Adata.get(i) - 10);

            X_Adata.add(1010);
            Y_Adata.add(xyztemp);


        }
        else {
            Y_Adata.add(xyztemp);
            XA_x = XA_x + 10;
            X_Adata.add(XA_x);
        }

        AcCheck = A_check;

        Log.d("post", "ax " + ax + "ay " + ay + "az " + az + " A_C:" + A_check);
        postInvalidate();       // onDraw()

    }

    public void Grapdraw2(double gx,double gy, double gz, boolean G_check) {

        XYZ xyztemp2 = new XYZ();
        xyztemp2.setData(gx, gy, gz);

        if((Y_Gdata.size() > 100) ) {
            X_Gdata.remove(0);
            Y_Gdata.remove(0);

            for (int i = 0; i < X_Gdata.size(); i++)
                X_Gdata.set(i, X_Gdata.get(i) - 10);

            X_Gdata.add(1010);
            Y_Gdata.add(xyztemp2);

        }
        else
        {
            Y_Gdata.add(xyztemp2);
            XG_x=XG_x+10;
            X_Gdata.add(XG_x);
        }

        GyCheck = G_check;

        postInvalidate();       // onDraw() ȣ��

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);

        mPaint.setColor(Color.BLACK);
        canvas.drawLine(100, 100, 100, 1000, mPaint);
        canvas.drawLine(100, 500, 1000, 500, mPaint);

        int cnt = 50;
        mPaint.setTextSize(40);

        for (int i = 0; i < 800; i += 100, cnt -= 10)
            canvas.drawText("" + cnt, 100, i, mPaint);

        if (AcCheck) {
            for (int i = 1; i < Y_Adata.size(); i++) {

                mPaint.setColor(Color.BLUE);
                canvas.drawLine(X_Adata.get(i - 1), (int)(500 - Y_Adata.get(i - 1).Datax * 10), X_Adata.get(i),(int) (500 - Y_Adata.get(i).Datax * 10), mPaint);

                mPaint.setColor(Color.RED);
                canvas.drawLine(X_Adata.get(i - 1), (int)(500 - Y_Adata.get(i - 1).Datay * 10), X_Adata.get(i), (int)(500 - Y_Adata.get(i).Datay * 10), mPaint);

                mPaint.setColor(Color.GREEN);
                canvas.drawLine(X_Adata.get(i - 1), (int)(500 - Y_Adata.get(i - 1).Dataz * 10), X_Adata.get(i), (int)(500 - Y_Adata.get(i).Dataz * 10), mPaint);
            }
        }

        if (GyCheck) {
            for (int i = 1; i < Y_Gdata.size(); i++) {
                mPaint.setColor(Color.BLUE);
                canvas.drawLine(X_Gdata.get(i - 1), (int)(500 - Y_Gdata.get(i - 1).Datax * 10), X_Gdata.get(i),(int) (500 - Y_Gdata.get(i).Datax * 10), mPaint);

                mPaint.setColor(Color.RED);
                canvas.drawLine(X_Gdata.get(i - 1), (int)(500 - Y_Gdata.get(i - 1).Datay * 10), X_Gdata.get(i), (int)(500 - Y_Gdata.get(i).Datay * 10), mPaint);

                mPaint.setColor(Color.GREEN);
                canvas.drawLine(X_Gdata.get(i - 1), (int)(500 - Y_Gdata.get(i - 1).Dataz * 10), X_Gdata.get(i), (int)(500 - Y_Gdata.get(i).Dataz * 10), mPaint);
            }

        }
    }
}
