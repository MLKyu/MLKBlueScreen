package com.mlk.mlkbulescreen.view;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mlk.mlkbulescreen.R;
import com.mlk.mlkbulescreen.broadcast.BlueBroadcastReceiver;
import com.mlk.mlkbulescreen.service.BlueFilterConnection;
import com.mlk.mlkbulescreen.service.Bluefilter_Service;
import com.mlk.mlkbulescreen.view.listener.CustomSeekBarChangeListener;
import com.mlk.mlkbulescreen.view.listener.CustomSelectedListener;

public class Blue_Main extends AppCompatActivity implements View.OnClickListener {

    private final int PERMISSION_RESULT = 1234;

    public LinearLayout mScreenOnBoxLl;
    public ToggleButton mBlueFilterTb;
    public ToggleButton mFilterPreviewTb;
    private ToggleButton mNaviFilterTb;
    private ToggleButton mRebootstartTb;
    public SharedPreferences mPreferences;
    public SharedPreferences.Editor edit;
    public Bluefilter_Service mService;
    public boolean isServiceConnect = false;
    private ServiceConnection mConnection = new BlueFilterConnection(this);
    private BroadcastReceiver mReceiver = new BlueBroadcastReceiver(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        this.init();
        this.spinnerInit();

        ((Spinner) findViewById(R.id.color_spinner))
                .setSelection(this.mPreferences.getInt("select_color", 0));
        ((SeekBar) findViewById(R.id.filter_seeker))
                .setOnSeekBarChangeListener(new CustomSeekBarChangeListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_action, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_info:
                Builder builder = new Builder(this);
                builder.setTitle(getString(R.string.app_name));
                builder.setMessage(getString(R.string.noti_alert_message));
                builder.setPositiveButton(getString(R.string.confirm_btn), null);
                builder.show();
                return true;
            case R.id.action_anotherapp:
                startActivity(new Intent("android.intent.action.VIEW"
                        , Uri.parse("https://play.google.com/store/apps/developer?id=HC")));
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @SuppressLint("ResourceType")
    private void spinnerInit() {
//        SpinnerAdapter createFromResource = ArrayAdapter
//                .createFromResource(this
//                        , R.array.color_list, android.R.layout.simple_spinner_item);
//        createFromResource.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        ((Spinner) findViewById(R.id.color_spinner)).setAdapter(createFromResource);
        ((Spinner) findViewById(R.id.color_spinner))
                .setOnItemSelectedListener(new CustomSelectedListener(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, Bluefilter_Service.class),
                this.mConnection, BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            if (this.mService.mView != null) {
                this.mService.updateView(2);
            }
            if (this.isServiceConnect) {
                unbindService(this.mConnection);
                this.isServiceConnect = false;
            }
            if (this.mReceiver != null) {
                unregisterReceiver(this.mReceiver);
            }
        } catch (Exception e) {
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onActivityResult(int i, int i2, Intent intent) {
        if (i != 1234) {
            return;
        }
        if (!Settings.canDrawOverlays(this)) {
            this.mBlueFilterTb.setChecked(false);
            Toast.makeText(this, getString(R.string.error_text)
                    , Toast.LENGTH_SHORT).show();
        } else if (this.mBlueFilterTb.isChecked()) {
            this.edit.putInt("filter_status", 1);
            this.edit.commit();
            this.bluefilter_start();
            this.mScreenOnBoxLl.setVisibility(View.VISIBLE);
        } else {
            this.edit.putInt("filter_status", 0);
            this.edit.commit();
            this.bluefilter_stop();
            this.mFilterPreviewTb.setChecked(false);
            this.mScreenOnBoxLl.setVisibility(View.GONE);
        }
    }

    private void init() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.hcea.settings.ACTION_EDIT_NOTIFICATION");
        registerReceiver(this.mReceiver, intentFilter);
        this.mScreenOnBoxLl = (LinearLayout) findViewById(R.id.screen_on_box);
        this.mBlueFilterTb = (ToggleButton) findViewById(R.id.blue_filter_toggle);
        this.mFilterPreviewTb = (ToggleButton) findViewById(R.id.filter_preview_toggle);
        this.mNaviFilterTb = (ToggleButton) findViewById(R.id.navi_filter_toggle);
        this.mRebootstartTb = (ToggleButton) findViewById(R.id.rebootstart_toggle);
        this.mPreferences = getSharedPreferences("SavedData", 0);
        this.mBlueFilterTb.setOnClickListener(this);
        this.mFilterPreviewTb.setOnClickListener(this);
        this.mNaviFilterTb.setOnClickListener(this);
        this.mRebootstartTb.setOnClickListener(this);
        this.edit = this.mPreferences.edit();
        int filterPercent = this.mPreferences.getInt("filter_percent", 40);
        int filterStatus = this.mPreferences.getInt("filter_status", 0);
        int rebootChk = this.mPreferences.getInt("reboot_chk", 0);
        int naviFilter = this.mPreferences.getInt("navi_filter", 0);
        int selectColor = this.mPreferences.getInt("select_color", 0);
        ((TextView) findViewById(R.id.filter_percent)).setText(new StringBuilder(String.valueOf(filterPercent)).append("%").toString());
        ((SeekBar) findViewById(R.id.filter_seeker)).setProgress(filterPercent);
        ((Spinner) findViewById(R.id.color_spinner)).setSelection(selectColor);
        if (rebootChk == 1) {
            this.mNaviFilterTb.setChecked(true);
        } else if (rebootChk == 0) {
            this.mNaviFilterTb.setChecked(false);
        }
        if (naviFilter == 1) {
            this.mRebootstartTb.setChecked(true);
        } else if (naviFilter == 0) {
            this.mRebootstartTb.setChecked(false);
        }
        if (!isService("com.hcea.settings.Bluefilter_Service").booleanValue()) {
            this.mBlueFilterTb.setChecked(false);
            this.mScreenOnBoxLl.setVisibility(View.GONE);
        } else if (filterStatus == 0) {
            this.mBlueFilterTb.setChecked(false);
            this.mScreenOnBoxLl.setVisibility(View.GONE);
        } else if (filterStatus == 1) {
            this.mBlueFilterTb.setChecked(true);
            this.mScreenOnBoxLl.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("WrongConstant")
    public Boolean isService(String str) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo
                : ((ActivityManager) getSystemService("activity"))
                .getRunningServices(Integer.MAX_VALUE)) {
            if (str.equals(runningServiceInfo.service.getClassName())) {
                return Boolean.valueOf(true);
            }
        }
        return Boolean.valueOf(false);
    }

    public void permissionCheck() {
        startActivityForResult(
                new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION"
                        , Uri.parse("package:com.hcea.settings")), PERMISSION_RESULT);
    }

    public void setFilterReset(int i) {
        if (this.isServiceConnect && this.mBlueFilterTb.isChecked()) {
            this.mService.setFilterReset(i);
        }
    }

    public void bluefilter_start() {
        startService(new Intent(this, Bluefilter_Service.class));
        bindService(new Intent(this, Bluefilter_Service.class)
                , this.mConnection, BIND_AUTO_CREATE);
    }

    public void bluefilter_stop() {
        if (this.isServiceConnect) {
            unbindService(this.mConnection);
            this.isServiceConnect = false;
        }
        stopService(new Intent(this, Bluefilter_Service.class));
    }

    @SuppressLint({"NewApi"})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.blue_filter_toggle:
                if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
                    permissionCheck();
                    return;
                } else if (this.mBlueFilterTb.isChecked()) {
                    this.edit.putInt("filter_status", 1);
                    this.edit.commit();
                    bluefilter_start();
                    this.mScreenOnBoxLl.setVisibility(View.VISIBLE);
                    return;
                } else {
                    this.edit.putInt("filter_status", 0);
                    this.edit.commit();
                    bluefilter_stop();
                    this.mFilterPreviewTb.setChecked(false);
                    this.mScreenOnBoxLl.setVisibility(View.GONE);
                    return;
                }


            case R.id.filter_preview_toggle:
                if (this.mFilterPreviewTb.isChecked() && this.mBlueFilterTb.isChecked()) {
                    this.mService.updateView(1);
                    return;
                } else if (!this.mFilterPreviewTb.isChecked() && this.mBlueFilterTb.isChecked()) {
                    this.mService.updateView(2);
                    return;
                } else {
                    return;
                }
            case R.id.navi_filter_toggle:
                if (this.mRebootstartTb.isChecked()) {
                    this.edit.putInt("navi_filter", 1);
                    this.edit.commit();
                    return;
                } else if (!this.mRebootstartTb.isChecked()) {
                    this.edit.putInt("navi_filter", 0);
                    this.edit.commit();
                    return;
                } else {
                    return;
                }
            case R.id.rebootstart_toggle:
                if (this.mNaviFilterTb.isChecked()) {
                    this.edit.putInt("reboot_chk", 1);
                    this.edit.commit();
                    return;
                } else if (!this.mNaviFilterTb.isChecked()) {
                    this.edit.putInt("reboot_chk", 0);
                    this.edit.commit();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}