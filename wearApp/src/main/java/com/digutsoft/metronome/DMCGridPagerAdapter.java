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
