package com.speedata.control;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.control.ControlManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ControlManager mControlManager;
    private static final String TAG = "Reginer";
    private EditText mEtPkg;
    private EditText mEtApk;
    private int apnId;


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
        findViewById(R.id.btn_set_apn).setOnClickListener(this);
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
                apnId = mControlManager.createApn(createApn());
                Log.d(TAG, "apnId  is: " + apnId);
                break;
            case R.id.btn_set_apn:
                setDefaultApn(apnId);
                break;
            case R.id.btn_uninstall:
                submitPkg();
                break;
            case R.id.btn_install:
                submitPath();
                break;
        }
    }

    /**
     * 设置默认选中apn
     * @param apnId apnId
     * @return  successful  or  failed
     */
    public boolean setDefaultApn(int apnId) {
         final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
        boolean res = false;
        ContentResolver resolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put("apn_id", apnId);

        try {
            resolver.update(CURRENT_APN_URI, values, null, null);
            Cursor c = resolver.query(CURRENT_APN_URI, new String[] { "name",
                    "apn" }, "_id=" + apnId, null, null);
            if (c != null) {
                res = true;
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return res;
    }

    private void submitPkg() {
        String pkg = mEtPkg.getText().toString().trim();
        if (TextUtils.isEmpty(pkg)) {
            Toast.makeText(this, "content null", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean result = mControlManager.uninstallApp(pkg);

        Log.d(TAG, "result is:::: " + result);
    }

    private void submitPath() {
        String apk = mEtApk.getText().toString().trim();
        if (TextUtils.isEmpty(apk)) {
            Toast.makeText(this, "content null", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean result = mControlManager.installApp(apk);
        Log.d(TAG, "result is:::: " + result);
    }

    private ContentValues createApn() {
        String numeric;
        TelephonyManager telephonyManager = (TelephonyManager)
                getSystemService(Context.TELEPHONY_SERVICE);
        numeric = telephonyManager.getSimOperator();
        ContentValues values = new ContentValues();
        values.put("name", "speedata");
        values.put("apn", "myapn");
        values.put("type", "default,supl");
        values.put("numeric", numeric);
        values.put("mcc", "460");
        values.put("mnc", "01");
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
