package com.mlk.mlkbulescreen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.mlk.mlkbulescreen.view.Blue_Main;

/**
 * Created by iriver on 2018-02-06.
 */

public class BlueBroadcastReceiver extends BroadcastReceiver {
    final /* synthetic */ Blue_Main blueMainAct;

    public BlueBroadcastReceiver(Blue_Main blue_Main) {
        this.blueMainAct = blue_Main;
    }

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.hcea.settings.ACTION_EDIT_NOTIFICATION")) {
            String stringExtra = intent.getStringExtra("filter_edit");
            if (stringExtra.equals("close")) {
                this.blueMainAct.bluefilter_stop();
                this.blueMainAct.mBlueFilterTb.setChecked(false);
                this.blueMainAct.mScreenOnBoxLl.setVisibility(View.GONE);
            }
            if (stringExtra.equals("on")) {
                this.blueMainAct.mBlueFilterTb.setChecked(true);
                this.blueMainAct.mScreenOnBoxLl.setVisibility(View.VISIBLE);
            }
            if (stringExtra.equals("off")) {
                this.blueMainAct.mBlueFilterTb.setChecked(false);
                this.blueMainAct.mFilterPreviewTb.setChecked(false);
                this.blueMainAct.mScreenOnBoxLl.setVisibility(View.GONE);
            }
        }
    }
}