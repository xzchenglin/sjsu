package com.sjsu.lin.hw;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.sjsu.lin.hw.handler.WorkerThread;

import org.apache.commons.lang3.StringUtils;

public class ActivityCurrency extends Activity {

    static TextView textResult;
    Spinner spinner;
    static String[] cs;
    static String[] rs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currency_activity);

        spinner = (Spinner) findViewById(R.id.spinner1);
        textResult = ((TextView)findViewById(R.id.tv3));

        cs = getResources().getStringArray(R.array.currency);
        rs = getResources().getStringArray(R.array.currencyRate);

        ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, cs);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner .setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] arr = getResources().getStringArray(R.array.currency);
                Log.d("aaa", arr[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void go(View v) throws Exception {
        Intent intent = new Intent(ActivityCurrency.this, DialogCurrency.class);
        startActivity(intent);

        new Thread(new WorkerThread(this)).start();
    }

    public void sendBcr() throws Exception{

        Thread.sleep(300);

        EditText et = findViewById(R.id.editText1);

        if(et == null || StringUtils.isBlank(et.getText())){
            return;
        }

        Double in = Double.parseDouble(et.getText() + "");

        Intent broadcastIntent = new Intent("com.sjsu.lin.hw.intent.input");
        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        broadcastIntent.putExtra("amount", in);
        broadcastIntent.putExtra("target", spinner.getSelectedItemPosition());
        this.sendBroadcast(broadcastIntent);
    }

    public static class OutReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Double amount = intent.getDoubleExtra("amount", 0.0);
            int target = intent.getIntExtra("target", 0);
            String tc = cs[target];
            textResult.setText("Converted US $" + amount + " to " + tc + " " + amount*Double.parseDouble(rs[target]));
        }
    }
}
