package com.martillero.romcontrol;


import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceFragment;


/*      Created by Marti.*/

public class PowerMenuFragment extends PreferenceFragment{
    HandlePreferenceFragments hpf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hpf = new HandlePreferenceFragments(getActivity(), this, "power_menu");
    }

    @Override
    public void onResume() {
        super.onResume();
        hpf.onResumeFragment();
    }

    @Override
    public void onPause() {
        super.onPause();
        hpf.onPauseFragment();
    }
}
