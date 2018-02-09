package com.mlk.mlkbulescreen.view.listener;

import android.view.View;
import android.widget.AdapterView;

import com.mlk.mlkbulescreen.view.Blue_Main;

/**
 * Created by iriver on 2018-02-06.
 */

public class CustomSelectedListener implements AdapterView.OnItemSelectedListener {
    final /* synthetic */ Blue_Main blueMainAct;

    public CustomSelectedListener(Blue_Main blue_Main) {
        this.blueMainAct = blue_Main;
    }

    public void onItemSelected(AdapterView adapterView, View view, int i, long j) {
        int i2 = this.blueMainAct.mPreferences.getInt("filter_percent", 40);
        this.blueMainAct.edit.putInt("select_color", i);
        this.blueMainAct.edit.commit();
        this.blueMainAct.mService.setFilter(i);
        if (this.blueMainAct.mService.mView != null) {
            this.blueMainAct.mService.setFilterReset(i2);
        }
    }

    public void onNothingSelected(AdapterView adapterView) {
    }
}
