package com.mlk.mlkbulescreen.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.mlk.mlkbulescreen.service.Bluefilter_Service;

/**
 * Created by iriver on 2018-02-06.
 */

public class BlueBroadServiceReceiver extends BroadcastReceiver {
    final /* synthetic */ Bluefilter_Service mService;

    public BlueBroadServiceReceiver(Bluefilter_Service bluefilter_Service) {
        this.mService = bluefilter_Service;
    }

    @SuppressLint("WrongConstant")
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.hcea.settings.ACTION_EDIT_NOTIFICATION")) {
            String stringExtra = intent.getStringExtra("filter_edit");
            if (stringExtra.equals("close")) {
                this.mService.setPreferences(0);
                if (this.mService.mView != null) {
                    this.mService.onDestroy();
                } else if (this.mService.mView == null) {
                    this.mService.unregisterReceiver(this.mService.mBroadcastReceiver);
                    this.mService.stopSelf();
                }
                context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            }
            if (stringExtra.equals("on")) {
                if (this.mService.mView == null) {
                    this.mService.initView();
                }
                this.mService.setPreferences(1);
                context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            }
            if (stringExtra.equals("off")) {
                if (this.mService.mView != null) {
                    ((WindowManager) this.mService.getSystemService("window"))
                            .removeView(this.mService.mView);
                    this.mService.mView = null;
                }
                this.mService.setPreferences(0);
                context.sendBroadcast(new Intent("android.intent.action.CLOSE_SYSTEM_DIALOGS"));
            }
        }
    }
}
