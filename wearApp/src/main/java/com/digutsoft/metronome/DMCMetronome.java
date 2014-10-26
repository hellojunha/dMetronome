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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DMCMetronome {

    private static final int MSG = 1;
    protected static boolean mRunning = false;

    Vibrator mVibrator;
    SharedPreferences mSharedPreferences;

    View mBackground;
    TextView mTvTempo;
    Drawable mDefaultBackground;

    int mCount = 0;
    int mPeriod;
    long mTickDuration;
    boolean isFlashEnabled;

    public DMCMetronome(Context context, Vibrator vibrator, View view) {
        mVibrator = vibrator;
        mBackground = view;
        mTvTempo = (TextView) view.findViewById(R.id.tvTempo);
        mDefaultBackground = view.getBackground();
        mSharedPreferences = context.getSharedPreferences("dMetronome", 0);
    }

    public void startTick(int ticksPerSec) {
        mRunning = true;
        mCount = 0;
        isFlashEnabled = mSharedPreferences.getBoolean("flash", true);
        mPeriod = mSharedPreferences.getInt("count", 4);
        mTvTempo.setText(Integer.toString(1));
        mTickDuration = 60000 / ticksPerSec;
        tick();
    }

    private void tick() {
        if (!mRunning) return;

        if ((mPeriod != 1) && (mCount % mPeriod == 0)) {
            mCount = 0;
            if (isFlashEnabled) {
                mBackground.setBackgroundColor(Color.parseColor("#000000"));
                mTvTempo.setTextColor(Color.parseColor("#ffffff"));
            }
            mVibrator.vibrate(200);
        } else {
            if (isFlashEnabled) {
                mBackground.setBackground(mDefaultBackground);
                mTvTempo.setTextColor(Color.parseColor("#000000"));
            }
            mVibrator.vibrate(100);
        }

        mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG), mTickDuration);
    }

    public void stopTick() {
        mRunning = false;
        mCount = 0;
        mBackground.setBackground(mDefaultBackground);
        mTvTempo.setTextColor(Color.parseColor("#000000"));
        mHandler.removeMessages(MSG);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            mCount++;
            tick();
            mTvTempo.setText(Integer.toString(mCount + 1));
        }
    };
}