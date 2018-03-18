package com.sjsu.lin.hw.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.sjsu.lin.hw.ActivityAccelerometer;

import java.util.Random;

public class AccelerometerTask extends AsyncTask<String, Integer, Integer> implements OnCancelListener {

    ActivityAccelerometer activity;
    Context ctx;
    ProgressDialog pd = null;
    float f1;
    float f2;
    float f3;

    public AccelerometerTask(ActivityAccelerometer inr, Context inCtx) {
        activity = inr;
        ctx = inCtx;
    }

    protected void onPreExecute() {

        pd = new ProgressDialog(ctx);
        pd.setTitle("");
        pd.setMessage("In Progress...");
        pd.setCancelable(true);
        pd.setOnCancelListener(this);
        pd.setIndeterminate(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMax(activity.cnt);
        pd.show();
    }

    protected void onProgressUpdate(Integer... progress) {
        pd.setProgress(progress[0]+1);
        activity.valueUpdate(f1, f2, f3, progress[0]+1);
    }

    protected void onPostExecute(Integer result) {
        activity.statusUpdate("Done.");
        pd.cancel();
    }

    protected Integer doInBackground(String... strings) {

        for(int i=0; i<activity.cnt; i++) {
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Random r = new Random();
            f1 = r.nextFloat() * 100;
            f2 = r.nextFloat() * 100;
            f3 = r.nextFloat() * 100;

            publishProgress(i);
        }

        return 1;
    }

    public void onCancel(DialogInterface d) {
        if(this.getStatus() != Status.FINISHED) {
            activity.statusUpdate("Stopped.");
        }
        this.cancel(true);
    }
}
