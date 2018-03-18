package com.sjsu.lin.hw.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.sjsu.lin.hw.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadService extends IntentService {
    public static final String URL_EXTRA = "download_url";
    private final String TAG="ds";
    private int total, downloaded;
    private String current ="";
    private Handler handler = new Handler();
    private NotificationCompat.Builder builder;
    private NotificationManager manager;
    private int _notificationID = 1024;

    public DownloadService() {
        super("DownLoadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            initNotification();

            Bundle bundle = intent.getExtras();
            String extra = bundle.getString(URL_EXTRA);

            File dirs = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/277");
            if (!dirs.exists()) {
                dirs.mkdir();
            }

            String[] urls = extra.split("\\|");
            total = urls.length;
            String ret = "--------- Successfully downloaded ---------\n\n";

            for (String url:urls) {
                String name = url.split("/")[url.split("/").length-1];
                current = name;
                File file = new File(dirs, name);
                file.createNewFile();
                Log.d(TAG, "start：" + url + " -> " + file.getPath());
                manager.notify(_notificationID, builder.build());
                if(downloadFile(url, file)){
                    ret += name+"\n";
                }
                downloaded ++;
            }

            //finished
            builder.setProgress(0,0,false);
            builder.setContentText("Finished");
            manager.notify(_notificationID,builder.build());

            Intent sendIntent = new Intent("com.sjsu.lin.hw.downloadComplete");
            sendIntent.putExtra("downloadFile", ret);
            sendBroadcast(sendIntent);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean downloadFile(String downloadUrl, File file){
        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        InputStream inputStream = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection _downLoadCon = (HttpURLConnection) url.openConnection();
            _downLoadCon.setRequestMethod("GET");
            inputStream = _downLoadCon.getInputStream();
            int respondCode = _downLoadCon.getResponseCode();
            if (respondCode == 200) {
                handler.post(runnable);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                return true;
            } else {
                Log.d(TAG, "respondCode:" + respondCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Runnable runnable = new Runnable() {
        public void run() {
            int pec= (downloaded *100 / total);
            builder.setContentText(pec+"%" + " (downloading " + current + ")");
            builder.setProgress(100, pec, false);
            manager.notify(_notificationID,builder.build());
            handler.postDelayed(runnable, 1000);
        }
    };

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    public void initNotification(){
        builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher).setContentTitle("Download").setContentText("Downloading……");
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
