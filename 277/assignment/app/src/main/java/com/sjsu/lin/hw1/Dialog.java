package com.sjsu.lin.hw1;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class Dialog extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
    }

    /**
     * Callback method defined by the View
     * @param v
     */
    public void finishDialog(View v) {
        Dialog.this.finish();
    }
}
