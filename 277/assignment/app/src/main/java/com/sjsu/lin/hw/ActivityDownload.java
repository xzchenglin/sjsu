package com.sjsu.lin.hw;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sjsu.lin.hw.service.DownloadService;

import org.apache.commons.lang3.StringUtils;

public class ActivityDownload extends Activity {
    EditText et1, et2, et3, et4, et5;
    static TextView tv;
    private DownloadReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        et1 = findViewById(R.id.et1);
        et2 = findViewById(R.id.et2);
        et3 = findViewById(R.id.et3);
        et4 = findViewById(R.id.et4);
        et5 = findViewById(R.id.et5);

        et1.setText("https://www.cisco.com/c/dam/en_us/about/annual-report/2016-annual-report-full.pdf");
        et2.setText("https://www.cisco.com/web/about/ac79/docs/innov/IoE_Economy.pdf");
        et3.setText("https://www.cisco.com/web/strategy/docs/gov/everything-for-cities.pdf");
        et4.setText("http://www.verizonenterprise.com/resources/reports/rp_DBIR_2017_Report_execsummary_en_xg.pdf");
        et5.setText("http://www.verizonenterprise.com/resources/reports/rp_DBIR_2017_Report_en_xg.pdf");

        tv = findViewById(R.id.tv3);

        mReceiver = new DownloadReceiver();
        registerReceiver(mReceiver, new IntentFilter("com.sjsu.lin.hw.downloadComplete"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    public void download(View v) throws InterruptedException {
        String req = "";
        if(StringUtils.isNotBlank(et1.getText())) {
            req += et1.getText() + "|";
        }
        if(StringUtils.isNotBlank(et2.getText())) {
            req += et2.getText() + "|";
        }
        if(StringUtils.isNotBlank(et3.getText())) {
            req += et3.getText() + "|";
        }
        if(StringUtils.isNotBlank(et4.getText())) {
            req += et4.getText() + "|";
        }
        if(StringUtils.isNotBlank(et5.getText())) {
            req += et1.getText();
        }

        Intent intent = new Intent(getApplicationContext(), DownloadService.class);
        intent.putExtra(DownloadService.URL_EXTRA, req);
        startService(intent);
    }

    public static class DownloadReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String ex = intent.getStringExtra("downloadFile");
            tv.setText(ex);
        }
    }

}
