/*
 *
 * Copyright (c) 2014 Digutsoft.
 * http://www.digutsoft.com/
 *
 * This file is part of dMetronome.
 * Visit http://www.digutsoft.com/apps/product.php?p=metronome to know more.
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

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class DMMain extends Activity {

    DMMetronome metronome;
    TextView tvTempo;
    SeekBar sbTempo;
    NotificationCompat.Builder notificationBuilder;
    NotificationManagerCompat notificationManager;
    int mTempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.metronome_main);

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                sbTempo = (SeekBar) stub.findViewById(R.id.sbTempo);
                tvTempo = (TextView) stub.findViewById(R.id.tvTempo);
                final CircledImageView btStart = (CircledImageView) stub.findViewById(R.id.btStart);
                final CircledImageView btPlus = (CircledImageView) stub.findViewById(R.id.btPlus);
                final CircledImageView btMinus = (CircledImageView) stub.findViewById(R.id.btMinus);

                setTempo(80);

                Intent viewIntent = new Intent(DMMain.this, DMMain.class);
                PendingIntent viewPendingIntent = PendingIntent.getActivity(DMMain.this, 0, viewIntent, 0);

                notificationBuilder = new NotificationCompat.Builder(DMMain.this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentIntent(viewPendingIntent);

                notificationManager = NotificationManagerCompat.from(DMMain.this);

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                metronome = new DMMetronome(v, (LinearLayout) stub.findViewById(R.id.llBackground), tvTempo);

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
                            Toast.makeText(DMMain.this, R.string.tempo_zero, Toast.LENGTH_LONG).show();
                            return;
                        }

                        if (DMMetronome.mRunning) {
                            metronome.onStop();
                            sbTempo.setEnabled(true);
                            tvTempo.setText(Integer.toString(mTempo));
                            btStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_start));
                            btStart.setCircleColor(getResources().getColor(R.color.green));
                            btPlus.setVisibility(View.VISIBLE);
                            btMinus.setVisibility(View.VISIBLE);
                            notificationManager.cancel(1);
                        } else {
                            metronome.onStart(mTempo);
                            sbTempo.setEnabled(false);
                            btStart.setImageDrawable(getResources().getDrawable(R.drawable.ic_stop));
                            btStart.setCircleColor(getResources().getColor(R.color.red));
                            btPlus.setVisibility(View.GONE);
                            btMinus.setVisibility(View.GONE);
                            notificationBuilder.setContentText(String.format(getString(R.string.notification_running), mTempo));
                            notificationManager.notify(1, notificationBuilder.build());
                        }
                    }
                });

                btPlus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setTempo(mTempo+1);
                    }
                });

                btMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setTempo(mTempo-1);
                    }
                });
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        metronome.onStop();
    }

    private void setTempo(int tempo) {
        if(tempo < 0 || tempo > 200) return;
        tvTempo.setText(Integer.toString(tempo));
        sbTempo.setProgress(tempo);
        mTempo = tempo;
    }
}
