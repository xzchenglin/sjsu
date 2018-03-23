package com.sjsu.lin.hw;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class DialogCurrency extends Activity {
    static TextView textResult;
    static String[] cs;
    static Double amount;
    static int target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        textResult = ((TextView)findViewById(R.id.btn_in_text));
        cs = getResources().getStringArray(R.array.currency);
    }

    public void finishDialog(View v) {
        Intent broadcastIntent = new Intent("com.sjsu.lin.hw.intent.output");
        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        broadcastIntent.putExtra("amount", amount);
        broadcastIntent.putExtra("target", target);
        this.sendBroadcast(broadcastIntent);

        DialogCurrency.this.finish();
    }

    public static class InputReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            amount = intent.getDoubleExtra("amount", 0.0);
            target = intent.getIntExtra("target", 0);
            String tc = cs[target];
            textResult.setText("Convert US $" + amount + " to " + tc);
        }
    }
}
