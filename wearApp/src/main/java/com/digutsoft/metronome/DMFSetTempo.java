/*
 *
 * Copyright (c) 2014 Digutsoft.
 * http://www.digutsoft.com/
 *
 * This file is part of dMetronome.
 * Visit http://www.digutsoft.com/apps/product.php?id=metronome to know more.
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

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.CircledImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class DMFSetTempo extends Fragment {

    View rootView;
    DMCMetronome metronome;
    TextView tvTempo;
    SeekBar sbTempo;
    NotificationCompat.Builder notificationBuilder;
    NotificationManagerCompat notificationManager;
    int mTempo;
    Context mContext;
    PowerManager.WakeLock wakeLock;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.settempo, container, false);

        sbTempo = (SeekBar) rootView.findViewById(R.id.sbTempo);
        tvTempo = (TextView) rootView.findViewById(R.id.tvTempo);
        final CircledImageView btStart = (CircledImageView) rootView.findViewById(R.id.btStart);
        final CircledImageView btPlus = (CircledImageView) rootView.findViewById(R.id.btPlus);
        final CircledImageView btMinus = (CircledImageView) rootView.findViewById(R.id.btMinus);

        mContext = getActivity().getApplicationContext();

        setTempo(80);

        Intent viewIntent = new Intent(getActivity(), DMAMain.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(getActivity(), 0, viewIntent, 0);

        notificationBuilder = new NotificationCompat.Builder(getActivity())
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentIntent(viewPendingIntent);

        notificationManager = NotificationManagerCompat.from(getActivity());

        Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        metronome = new DMCMetronome(getActivity(), vibrator, rootView.findViewById(R.id.bilBackground));

        PowerManager powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getString(R.string.app_name));

        sbTempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                setTempo(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTempo == 0) {
                    Toast.makeText(getActivity(), R.string.tempo_zero, Toast.LENGTH_LONG).show();
                    return;
                }

                if (DMCMetronome.mRunning) {
                    metronome.stopTick();

                    sbTempo.setEnabled(true);
                    tvTempo.setText(Integer.toString(mTempo));
                    btStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
                    btStart.setCircleColor(getResources().getColor(R.color.green));
                    btPlus.setVisibility(View.VISIBLE);
                    btMinus.setVisibility(View.VISIBLE);

                    wakeLock.release();

                    notificationManager.cancel(1);
                } else {
                    metronome.startTick(mTempo);

                    sbTempo.setEnabled(false);
                    btStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop));
                    btStart.setCircleColor(getResources().getColor(R.color.red));
                    btPlus.setVisibility(View.GONE);
                    btMinus.setVisibility(View.GONE);

                    wakeLock.acquire();

                    notificationBuilder.setContentText(String.format(getString(R.string.notification_running), mTempo));
                    notificationManager.notify(1, notificationBuilder.build());
                }
            }
        });

        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTempo(mTempo + 1);
            }
        });

        btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTempo(mTempo - 1);
            }
        });

        return rootView;
    }

    public void onDestroy() {
        super.onDestroy();
        metronome.stopTick();
        if(wakeLock.isHeld()) wakeLock.release();
        notificationManager.cancel(1);
    }

    private void setTempo(int tempo) {
        if (tempo < 0 || tempo > 200) return;
        tvTempo.setText(Integer.toString(tempo));
        sbTempo.setProgress(tempo);
        mTempo = tempo;
    }
}
