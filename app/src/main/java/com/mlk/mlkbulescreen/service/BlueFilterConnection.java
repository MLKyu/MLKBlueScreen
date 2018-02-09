package com.mlk.mlkbulescreen.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.mlk.mlkbulescreen.view.Blue_Main;

/**
 * Created by iriver on 2018-02-05.
 */

public class BlueFilterConnection implements ServiceConnection {
    final /* synthetic */ Blue_Main mBlueAct;

    public BlueFilterConnection(Blue_Main blue_Main) {
        this.mBlueAct = blue_Main;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mBlueAct.mService = ((BluefilterBinder) iBinder).getService();
        this.mBlueAct.isServiceConnect = true;
    }

    public void onServiceDisconnected(ComponentName componentName) {
        this.mBlueAct.isServiceConnect = false;
    }
}
