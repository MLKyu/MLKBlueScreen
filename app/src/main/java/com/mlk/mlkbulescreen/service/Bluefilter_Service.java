package com.mlk.mlkbulescreen.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RemoteViews;

import com.mlk.mlkbulescreen.R;
import com.mlk.mlkbulescreen.broadcast.BlueBroadServiceReceiver;
import com.mlk.mlkbulescreen.broadcast.BlueBroadcastReceiver;
import com.mlk.mlkbulescreen.view.Blue_Main;

/**
 * Created by iriver on 2018-02-06.
 */

public class Bluefilter_Service extends Service {

    public View mView;
    private final IBinder mBinder = new BluefilterBinder(this);
    private WindowManager.LayoutParams mLayoutParams;
    private WindowManager mWindowManager;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private Notification mNotification;
    public BroadcastReceiver mBroadcastReceiver = new BlueBroadServiceReceiver(this);

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("IBinder", "IBinder");
        return this.mBinder;
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onDestroy() {
        super.onDestroy();
        this.setPreferences(0);
        if (this.mView != null) {
            unregisterReceiver(this.mBroadcastReceiver);
            ((WindowManager) getSystemService("window")).removeView(this.mView);
            this.mView = null;
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i2) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.hcea.settings.ACTION_EDIT_NOTIFICATION");
        registerReceiver(this.mBroadcastReceiver, intentFilter);
        if (this.mNotification == null) {
            this.initNoti();
        }
        if (this.mView == null) {
            this.initView();
        }

        return Service.START_NOT_STICKY;
    }

    public void initNoti() {
        this.mNotification = new Notification(2130837505
                , getString(2131099651), System.currentTimeMillis());
        this.mNotification.contentIntent = PendingIntent
                .getActivity(this, 0
                        , new Intent(this, Blue_Main.class), 0);
        PendingIntent broadcast = PendingIntent
                .getBroadcast(this
                        , 0, new Intent("com.hcea.settings.filter")
                        , 134217728);
        PendingIntent broadcast2 = PendingIntent
                .getBroadcast(this, 0
                        , new Intent("com.hcea.settings.filter2")
                        , 134217728);
        PendingIntent broadcast3 = PendingIntent
                .getBroadcast(this, 0
                        , new Intent("com.hcea.settings.filter3")
                        , 134217728);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.layout_noti);
        remoteViews.setOnClickPendingIntent(2131361806, broadcast);
        remoteViews.setOnClickPendingIntent(2131361804, broadcast2);
        remoteViews.setOnClickPendingIntent(2131361805, broadcast3);
        this.mNotification.contentView = remoteViews;
        startForeground(1, this.mNotification);
    }

    @SuppressLint("WrongConstant")
    public void initView() {
        this.mLayoutParams = new WindowManager.LayoutParams(2006, 768, -3);
        SharedPreferences sharedPreferences = getSharedPreferences("SavedData", 0);
        int i = (int) (((double) sharedPreferences.getInt("filter_percent", 40)) * 2.55d);
        int i2 = sharedPreferences.getInt("base_red", 0);
        int i3 = sharedPreferences.getInt("base_green", 0);
        int i4 = sharedPreferences.getInt("base_blue", 0);
        if (sharedPreferences.getInt("navi_filter", 0) == 1) {
            Display defaultDisplay = ((WindowManager) getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            int i5 = point.y;
            int c = getNavigationHeight();
            if (c > 0) {
                this.mLayoutParams.height = i5 + c;
                this.mLayoutParams.gravity = 48;
            }
        }
        this.mView = new View(this);
        this.mView.setBackgroundColor(Color.argb(i, i2, i3, i4));
        this.mWindowManager = (WindowManager) getSystemService("window");
        this.mWindowManager.addView(this.mView, this.mLayoutParams);
        setPreferences(1);
    }

    private int getNavigationHeight() {
        int identifier = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        return identifier > 0 ? getResources().getDimensionPixelSize(identifier) : 0;
    }

    public void setPreferences(int i) {
        this.mPreferences = getSharedPreferences("SavedData", 0);
        this.mEditor = this.mPreferences.edit();
        this.mEditor.putInt("filter_status", i);
        this.mEditor.commit();
    }

    public void setFilter(int i) {
        this.mPreferences = getSharedPreferences("SavedData", 0);
        this.mEditor = this.mPreferences.edit();
        if (i == 0) {
            this.mEditor.putInt("base_red", 30);
            this.mEditor.putInt("base_green", 18);
            this.mEditor.putInt("base_blue", 4);
        } else if (i == 1) {
            this.mEditor.putInt("base_red", 0);
            this.mEditor.putInt("base_green", 0);
            this.mEditor.putInt("base_blue", 0);
        } else if (i == 2) {
            this.mEditor.putInt("base_red", 50);
            this.mEditor.putInt("base_green", 25);
            this.mEditor.putInt("base_blue", 0);
        } else if (i == 3) {
            this.mEditor.putInt("base_red", 40);
            this.mEditor.putInt("base_green", 0);
            this.mEditor.putInt("base_blue", 0);
        }
        this.mEditor.commit();
    }

    public void setFilterReset(int i) {
        SharedPreferences sharedPreferences = getSharedPreferences("SavedData", 0);
        this.mView.setBackgroundColor(Color.argb((int) (((double) i) * 2.55d)
                , sharedPreferences.getInt("base_red", 0)
                , sharedPreferences.getInt("base_green", 0)
                , sharedPreferences.getInt("base_blue", 0)));
        this.mWindowManager.updateViewLayout(this.mView, this.mLayoutParams);
    }

    @SuppressLint("WrongConstant")
    public void updateView(int i) {
        Display defaultDisplay = ((WindowManager) getSystemService("window"))
                .getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i2 = point.x;
        if (i == 1) {
            this.mLayoutParams.width = i2 / 2;
            this.mLayoutParams.gravity = 8388613;
        } else if (i == 2) {
            this.mLayoutParams.width = -1;
            if (getSharedPreferences("SavedData", 0).getInt("navi_filter", 0) == 1
                    && this.getNavigationHeight() > 0) {
                this.mLayoutParams.gravity = 48;
            }
        }
        this.mWindowManager.updateViewLayout(this.mView, this.mLayoutParams);
    }
}