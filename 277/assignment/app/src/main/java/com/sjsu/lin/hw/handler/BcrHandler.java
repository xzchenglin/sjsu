package com.sjsu.lin.hw.handler;

import android.os.Handler;
import android.os.Message;

import com.sjsu.lin.hw.ActivityCurrency;

public class BcrHandler extends Handler {
    private ActivityCurrency parentActivity = null;

    public BcrHandler(ActivityCurrency inParentActivity) {
        parentActivity = inParentActivity;
    }

    @Override
    public void handleMessage(Message msg) {
        try {
            parentActivity.sendBcr();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
