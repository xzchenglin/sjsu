package com.sjsu.lin.hw.handler;

import com.sjsu.lin.hw.ActivityCurrency;

public class WorkerThread implements Runnable {
    private ActivityCurrency parentActivity = null;

    public WorkerThread(ActivityCurrency parentActivity) {
        this.parentActivity = parentActivity;
    }

    public void run() {
        try {
            parentActivity.sendBcr();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
