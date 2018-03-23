package com.sjsu.lin.hw;

import android.os.Bundle;
import android.util.Log;

public class Utils {

    public static volatile int cnt;

//    public static void incrAndShowCnt(final TextView viewStatus) {
//
//        cnt ++;
//        showCnt(viewStatus);
//    }

    //    public static void showCnt(final TextView viewStatus) {
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                if (viewStatus != null) {
//                    viewStatus.setText(cnt + "");
//                }
//            }
//        }, 250);
//    }

    public static String getThreadSignature() {
        Thread t = Thread.currentThread();
        long l = t.getId();
        String name = t.getName();
        long p = t.getPriority();
        String gname = t.getThreadGroup().getName();
        return (name + ":(id)" + l + ":(priority)" + p
                + ":(group)" + gname);
    }

    public static void logThreadSignature(String tag) {
        Log.d(tag, getThreadSignature());
    }

    public static void sleepForInSecs(int secs) {
        try {
            Thread.sleep(secs * 1000);
        } catch (InterruptedException x) {
            throw new RuntimeException("interrupted", x);
        }
    }

    public static Bundle getStringAsABundle(String message) {
        Bundle b = new Bundle();
        b.putString("message", message);
        return b;
    }

    public static String getStringFromABundle(Bundle b) {
        return b.getString("message");
    }

}
