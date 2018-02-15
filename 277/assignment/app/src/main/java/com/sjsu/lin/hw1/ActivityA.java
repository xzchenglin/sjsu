package com.sjsu.lin.hw1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import static com.sjsu.lin.hw1.Utils.cnt;

public class ActivityA extends AppCompatActivity {

    private TextView cntView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a);
        cntView = (TextView)findViewById(R.id.cnt_view_a);

        if(savedInstanceState != null && savedInstanceState.containsKey("cnt")) {
            Log.i("cnt", "Restoring state...");
            int si = savedInstanceState.getInt("cnt");
            cnt = si;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cntView.setText(getString(R.string.tread_cnt) + cnt);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                if (cntView != null) {
//                    cntView.setText(getString(R.string.tread_cnt + cnt));
//                }
//            }
//        }, 250);
    }

    @Override
    protected void onPause() {
        super.onPause();
        cntView.setText(getString(R.string.tread_cnt) + ++cnt);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("cnt", "Saving state...");
        outState.putInt("cnt", cnt);
        super.onSaveInstanceState(outState);
    }

    public void startDialog(View v) {
        Intent intent = new Intent(ActivityA.this, Dialog.class);
        startActivity(intent);
    }

    public void startActivityB(View v) {
        Intent intent = new Intent(ActivityA.this, ActivityB.class);
        startActivity(intent);
    }

    public void finishActivityA(View v) {
        finish();
    }
}
