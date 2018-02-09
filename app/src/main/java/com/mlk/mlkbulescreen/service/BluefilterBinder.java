package com.mlk.mlkbulescreen.service;

import android.os.Binder;

/**
 * Created by iriver on 2018-02-05.
 */

public class BluefilterBinder extends Binder {
    final /* synthetic */ Bluefilter_Service mService;

    public BluefilterBinder(Bluefilter_Service bluefilter_Service) {
        this.mService = bluefilter_Service;
    }

    Bluefilter_Service getService() {
        return this.mService;
    }
}