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

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

public class DMAMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final GridViewPager gridViewPager = (GridViewPager)findViewById(R.id.gvpMain);
        gridViewPager.setAdapter(new DMCGridPagerAdapter(getFragmentManager()));
    }
}
