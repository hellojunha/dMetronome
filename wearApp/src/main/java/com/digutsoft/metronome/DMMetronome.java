/*
 *
 * Copyright (c) 2014 Digutsoft.
 * http://www.digutsoft.com/
 *
 * This file is part of dMetronome and it was originally from android-metronome.
 * Visit http://www.digutsoft.com/apps/product.php?id=metronome to know more about dMetronome,
 * and Google android-metronome to know more about original code.
 *
 * dMetronome is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * It is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 */

package com.digutsoft.metronome;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DMMetronome {

    static boolean mRunning = false;
    Vibrator mVibrator;
    LinearLayout llBackground;
    TextView tvTempo;
    Drawable mDefaultBackground;
    int mCount = 0;
    int mPeriod = 4;
    long mTickDuration;

    public DMMetronome(Vibrator vibrator, LinearLayout llBackground, TextView tvTempo) {
        mVibrator = vibrator;
        this.llBackground = llBackground;
        this.tvTempo = tvTempo;
        mDefaultBackground = llBackground.getBackground();
    }

    public void onStart(int ticksPerSec) {
        mRunning = true;
        mCount = 0;
        tvTempo.setText(Integer.toString(1));
        mTickDuration = 60000 / ticksPerSec;
        run();
    }

    private void run() {
        if (!mRunning) return;

        if ((mPeriod != 1) && (mCount % mPeriod == 0)) {
            mCount = 0;
            llBackground.setBackgroundColor(Color.parseColor("#000000"));
            tvTempo.setTextColor(Color.parseColor("#ffffff"));
            mVibrator.vibrate(100);
        } else {
            llBackground.setBackground(mDefaultBackground);
            tvTempo.setTextColor(Color.parseColor("#000000"));
            mVibrator.vibrate(50);
        }

        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mTickDuration);
    }

    public void onStop() {
        mRunning = false;
        mCount = 0;
        llBackground.setBackground(mDefaultBackground);
        tvTempo.setTextColor(Color.parseColor("#000000"));
        mHandler.removeMessages(MSG);
    }

    private static final int MSG = 1;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            mCount++;
            run();
            tvTempo.setText(Integer.toString(mCount + 1));
        }
    };
}