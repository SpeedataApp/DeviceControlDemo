package com.speedata.control;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.control.ControlManager;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ControlManager mControlManager;
    private static final String TAG = "Reginer";
    private EditText mEtPkg;
    private EditText mEtApk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mControlManager = ControlManager.getDeviceControl(this);
        Button mBtnReboot = (Button) findViewById(R.id.btn_reboot);
        Button mBtnShutdown = (Button) findViewById(R.id.btn_shutdown);
        Button mBtnRecovery = (Button) findViewById(R.id.btn_recovery);
        mBtnReboot.setOnClickListener(this);
        mBtnShutdown.setOnClickListener(this);
        mBtnRecovery.setOnClickListener(this);
        Button mBtnSetTime = (Button) findViewById(R.id.btn_set_time);
        mBtnSetTime.setOnClickListener(this);
        Button mBtnCreateApn = (Button) findViewById(R.id.btn_create_apn);
        mBtnCreateApn.setOnClickListener(this);
        Button mBtnUninstall = (Button) findViewById(R.id.btn_uninstall);
        mBtnUninstall.setOnClickListener(this);
        Button mBtnInstall = (Button) findViewById(R.id.btn_install);
        mBtnInstall.setOnClickListener(this);
        mEtPkg = (EditText) findViewById(R.id.et_pkg);
        mEtApk = (EditText) findViewById(R.id.et_apk);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reboot:
                mControlManager.rebootDevice();
                break;
            case R.id.btn_shutdown:
                mControlManager.shutdownDevice();
                break;
            case R.id.btn_recovery:
                mControlManager.recoveryDevice();
                break;
            case R.id.btn_set_time:
                mControlManager.setSystemTime(1497369600);
                break;
            case R.id.btn_create_apn:
                int apnId;
                apnId = mControlManager.createApn(createApn());
                Log.d(TAG, "apnId  is: " + apnId);
                break;
            case R.id.btn_uninstall:
                submitPkg();
                break;
            case R.id.btn_install:
                submitPath();
                break;
        }
    }


    private void submitPkg() {
        String pkg = mEtPkg.getText().toString().trim();
        if (TextUtils.isEmpty(pkg)) {
            Toast.makeText(this, "content null", Toast.LENGTH_SHORT).show();
            return;
        }
        mControlManager.uninstallApp(pkg);
    }

    private void submitPath() {
        String apk = mEtApk.getText().toString().trim();
        if (TextUtils.isEmpty(apk)) {
            Toast.makeText(this, "content null", Toast.LENGTH_SHORT).show();
            return;
        }
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        mControlManager.installApp(Uri.fromFile(new File(path + File.separator + apk + ".apk")));
    }

    private ContentValues createApn() {
        String NUMERIC;
        TelephonyManager telephonyManager = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        NUMERIC = telephonyManager.getSimOperator();
        String[] EM_APN = {"Speedata", "spd", "460", "02", "default,supl"};
        ContentValues values = new ContentValues();
        values.put("name", EM_APN[0]);
        values.put("apn", EM_APN[1]);
        values.put("type", EM_APN[4]);
        values.put("numeric", NUMERIC);
        values.put("mcc", "460");
        values.put("mnc", "00");
        values.put("proxy", "");
        values.put("port", "");
        values.put("mmsproxy", "");
        values.put("mmsport", "");
        values.put("user", "");
        values.put("server", "");
        values.put("password", "");
        values.put("mmsc", "");
        return values;
    }
}
