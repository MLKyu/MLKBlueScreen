package com.mlk.mlkbulescreen.view.listener;

import android.widget.SeekBar;
import android.widget.TextView;

import com.mlk.mlkbulescreen.R;
import com.mlk.mlkbulescreen.view.Blue_Main;

/**
 * Created by iriver on 2018-02-06.
 */

public class CustomSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {
    final /* synthetic */ Blue_Main blueMainAct;

    public CustomSeekBarChangeListener(Blue_Main blue_Main) {
        this.blueMainAct = blue_Main;
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        ((TextView) this.blueMainAct.findViewById(R.id.filter_percent)).setText(
                new StringBuilder(String.valueOf(i)).append("%").toString());
        ((SeekBar) this.blueMainAct.findViewById(R.id.filter_seeker)).setProgress(i);
        this.blueMainAct.edit.putInt("filter_percent", i);
        this.blueMainAct.edit.commit();
        this.blueMainAct.setFilterReset(i);
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
