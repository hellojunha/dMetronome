package com.digutsoft.metronome;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DMFPreference extends Fragment {

    View rootView;
    private SharedPreferences sharedPreferences;
    private final ArrayList<PreferenceItem> alPrefItem = new ArrayList<PreferenceItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.preference, container, false);

        final Context mContext = getActivity().getApplicationContext();
        sharedPreferences = mContext.getSharedPreferences("dMetronome", 0);

        alPrefItem.add(new PreferenceItem(R.string.pref_count, Integer.toString(sharedPreferences.getInt("count", 4))));

        if(sharedPreferences.getBoolean("flash", true)) {
            alPrefItem.add(new PreferenceItem(R.string.pref_splash, R.string.pref_splash_enabled));
        } else {
            alPrefItem.add(new PreferenceItem(R.string.pref_splash, R.string.pref_splash_disabled));
        }

        WearableListView prefListView = (WearableListView) rootView.findViewById(R.id.wlPreferenceList);
        prefListView.setAdapter(new DMCPrefListViewAdapter(mContext));
        prefListView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
                switch(Integer.parseInt(viewHolder.itemView.getTag().toString())) {
                    case 0:
                        prefChangeCount(viewHolder);
                        break;
                    case 1:
                        prefChangeFlashStatus(viewHolder);
                        break;
                }
            }

            @Override
            public void onTopEmptyRegionClick() {}
        });

        return rootView;
    }

    private void prefChangeFlashStatus(WearableListView.ViewHolder viewHolder) {
        boolean currentSetting = sharedPreferences.getBoolean("flash", true);
        sharedPreferences.edit().putBoolean("flash", !currentSetting).apply();
        TextView tvPrefSubTitle = (TextView)viewHolder.itemView.findViewById(R.id.tvPreferenceSubTitle);
        if(currentSetting) {
            tvPrefSubTitle.setText(R.string.pref_splash_disabled);
        } else {
            tvPrefSubTitle.setText(R.string.pref_splash_enabled);
        }
    }

    private void prefChangeCount(WearableListView.ViewHolder viewHolder) {
        int currentSetting = sharedPreferences.getInt("count", 4);
        currentSetting++;
        if(currentSetting > 8) {
            currentSetting = 1;
        }
        sharedPreferences.edit().putInt("count", currentSetting).apply();
        TextView tvPrefSubTitle = (TextView)viewHolder.itemView.findViewById(R.id.tvPreferenceSubTitle);
        tvPrefSubTitle.setText(Integer.toString(currentSetting));
    }

    private class PreferenceItem {
        String prefTitle;
        String prefSubTitle;

        PreferenceItem(int prefTitle, int prefSubTitle) {
            this.prefTitle = getString(prefTitle);
            this.prefSubTitle = getString(prefSubTitle);
        }

        PreferenceItem(int prefTitle, String prefSubTitle) {
            this.prefTitle = getString(prefTitle);
            this.prefSubTitle = prefSubTitle;
        }
    }

    private final class DMCPrefListViewAdapter extends WearableListView.Adapter {

        private final LayoutInflater mInflater;

        private DMCPrefListViewAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(mInflater.inflate(R.layout.preference_item, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            TextView tvTitle = (TextView) holder.itemView.findViewById(R.id.tvPreferenceTitle);
            TextView tvSubTitle = (TextView) holder.itemView.findViewById(R.id.tvPreferenceSubTitle);

            tvTitle.setText(alPrefItem.get(position).prefTitle);
            tvSubTitle.setText(alPrefItem.get(position).prefSubTitle);

            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}
