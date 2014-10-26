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
