package com.sjsu.lin.hw;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sjsu.lin.hw.task.AccelerometerTask;

import org.apache.commons.lang3.StringUtils;

public class ActivityAccelerometer extends Activity {

    public int cnt = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
    }

    public void go(View v) throws InterruptedException {

        EditText et = findViewById(R.id.et_cnt);

        if(et == null || StringUtils.isBlank(et.getText())){
            return;
        }

        cnt = Integer.parseInt(et.getText() + "");

        AccelerometerTask task = new AccelerometerTask(this, this);
        task.execute();
    }

    public void valueUpdate(float f1, float f2, float f3, int cnt){
        ((TextView) findViewById(R.id.tv_x)).setText("X : " + f1);
        ((TextView) findViewById(R.id.tv_y)).setText("Y : " + f2);
        ((TextView) findViewById(R.id.tv_z)).setText("Z : " + f3);

        TextView tv = findViewById(R.id.tv3);
        String s = "Count: " + cnt + "\n" + "X : " + f1 + ", Y : " + f2 + " Z : " + f3;
        tv.setText(s + "\n" + tv.getText());
    }

    public void statusUpdate(String s){
        TextView tv = findViewById(R.id.tv3);
        tv.setText(s + "\n" + tv.getText());
    }
}
