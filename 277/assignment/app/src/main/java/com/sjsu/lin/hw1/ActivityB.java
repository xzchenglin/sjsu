package com.sjsu.lin.hw1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.sjsu.lin.hw1.Utils.cnt;

public class ActivityB extends Activity {

    private TextView cntView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        cntView = (TextView)findViewById(R.id.cnt_view_b);
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
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                if (cntView != null) {
//                }
//            }
//        }, 250);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void startDialog(View v) {
        Intent intent = new Intent(ActivityB.this, Dialog.class);
        startActivity(intent);
    }

    public void startActivityA(View v) {
        Intent intent = new Intent(ActivityB.this, ActivityA.class);
        startActivity(intent);
    }

    public void finishActivityB(View v) {
        finish();
    }
}
