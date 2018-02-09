package com.mlk.mlkbulescreen.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.mlk.mlkbulescreen.service.Bluefilter_Service;

/**
 * Created by iriver on 2018-02-06.
 */

public class buttonBroadcastReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent intent2;

        if (intent.getAction().equals("com.hcea.settings.filter")) {
            intent2 = new Intent("com.hcea.settings.ACTION_EDIT_NOTIFICATION");
            intent2.putExtra("filter_edit", "close");
            context.sendBroadcast(intent2);
        }

        if (intent.getAction().equals("com.hcea.settings.filter2")) {
            intent2 = new Intent("com.hcea.settings.ACTION_EDIT_NOTIFICATION");
            intent2.putExtra("filter_edit", "on");
            context.sendBroadcast(intent2);
        }

        if (intent.getAction().equals("com.hcea.settings.filter3")) {
            intent2 = new Intent("com.hcea.settings.ACTION_EDIT_NOTIFICATION");
            intent2.putExtra("filter_edit", "off");
            context.sendBroadcast(intent2);
        }

        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("SavedData", 0);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            int i = sharedPreferences.getInt("reboot_chk", 0);
            if (i == 1) {
                edit.putInt("filter_status", 1);
                edit.commit();
                context.startService(new Intent(context, Bluefilter_Service.class));
            } else if (i == 0) {
                edit.putInt("filter_status", 0);
                edit.commit();
            }
        }
    }
}