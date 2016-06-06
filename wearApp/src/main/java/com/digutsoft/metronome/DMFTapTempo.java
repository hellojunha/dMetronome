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
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.CircledImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DMFTapTempo extends Fragment {

    private TextView tvTempo;
    private CircledImageView btStop;

    private Thread trBlink;

    private long s, sum;
    private int count;
    private boolean isTapTempoStarted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.taptempo, container, false);

        tvTempo = (TextView) rootView.findViewById(R.id.tvTempo);
        btStop = (CircledImageView) rootView.findViewById(R.id.btStop);

        CircledImageView btTempo = (CircledImageView) rootView.findViewById(R.id.btTempo);

        final Vibrator vibrator = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        btTempo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(100);

                if (!isTapTempoStarted) {
                    isTapTempoStarted = true;
                    tvTempo.setText("0");
                    s = System.currentTimeMillis();
                    if (trBlink != null) trBlink.interrupt();
                    btStop.setVisibility(View.VISIBLE);
                    return;
                }

                sum += System.currentTimeMillis() - s;
                count++;

                tvTempo.setText(String.format("%d", (60000 / (sum / count))));

                s = System.currentTimeMillis();
            }
        });

        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
                trBlink.start();
            }
        });

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        stop();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvTempo.setText(getResources().getText(R.string.start_tap_tempo));
    }

    protected void stop() {
        isTapTempoStarted = false;
        s = sum = count = 0;
        btStop.setVisibility(View.GONE);

        trBlink = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (tvTempo.getVisibility() == View.VISIBLE)
                                    tvTempo.setVisibility(View.INVISIBLE);
                                else if (tvTempo.getVisibility() == View.INVISIBLE)
                                    tvTempo.setVisibility(View.VISIBLE);
                            }
                        });
                    } catch (InterruptedException ignored) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTempo.setVisibility(View.VISIBLE);
                            }
                        });
                        break;
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        });
    }
}
