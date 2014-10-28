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
import android.app.FragmentManager;
import android.support.wearable.view.FragmentGridPagerAdapter;

public class DMCGridPagerAdapter extends FragmentGridPagerAdapter {

    public DMCGridPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int i) {
        return 2;
    }

    @Override
    public Fragment getFragment(int row, int col) {
        switch(col) {
            case 0:
                return new DMFSetTempo();
            case 1:
                return new DMFPreference();
            default:
                return null;
        }
    }
}
