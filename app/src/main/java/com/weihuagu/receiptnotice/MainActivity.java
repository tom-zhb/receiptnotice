package com.weihuagu.receiptnotice;

import android.Manifest;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.view.MenuItem;
import android.view.Menu;
import android.content.SharedPreferences;
import android.widget.Toast;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;

import com.tao.admin.loglib.Logger;
import com.github.pedrovgs.lynx.LynxConfig;
import com.github.pedrovgs.lynx.LynxActivity;
import com.weihuagu.receiptnotice.event.SMSEvent;
import com.weihuagu.receiptnotice.permission.FcPermissions;
import com.weihuagu.receiptnotice.permission.impl.FcPermissionsCallbacks;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, FcPermissionsCallbacks {

    private static final String TAG = "MainActivity";
    public static final int MSG_RECEIVED_CODE = 1;

    private Toolbar myToolbar;
    private Button btnsetposturl;
    private FloatingActionButton btnshowlog;
    private EditText posturl;
    private SharedPreferences sp;


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        sp = getSharedPreferences("url", Context.MODE_PRIVATE);
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        btnsetposturl = (Button) findViewById(R.id.btnsetposturl);
        btnsetposturl.setOnClickListener(this);
        btnshowlog = (FloatingActionButton) findViewById(R.id.floatingshowlog);
        btnshowlog.setOnClickListener(this);
        posturl = (EditText) findViewById(R.id.posturl);
        if (getPostUrl() != null)
            posturl.setHint(getPostUrl());


        requestSMSPermission();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FcPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void requestSMSPermission() {
        FcPermissions.requestPermissions(this, "老哥给下短信权限", FcPermissions.REQ_PER_CODE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS);
    }


    @Override
    protected void onResume() {
        super.onResume();
//        boolean isAuthor = isNotificationServiceEnable();
//        if (!isAuthor) {
//            //直接跳转通知授权界面
//            //android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS是API 22才加入到Settings里，这里直接写死
//            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 是否已授权
     *
     * @return
     */
    private boolean isNotificationServiceEnable() {
        return NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnsetposturl:
                posturl.setHint(null);
                setPostUrl();
                break;
            case R.id.floatingshowlog:
                showLog();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }



    @Override
    public void onStop() {
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SMSEvent event) {
        System.out.println("发件人：" + event.getSendMobile() + "\n内容：" + event.getContent());
    }

    private void setPostUrl() {
        SharedPreferences.Editor edit = sp.edit();
        //通过editor对象写入数据
        edit.putString("posturl", posturl.getText().toString());
        //提交数据存入到xml文件中
        edit.apply();
        Toast.makeText(getApplicationContext(), "已经设置posturl为：" + posturl.getText().toString(),
                Toast.LENGTH_SHORT).show();
    }

    private String getPostUrl() {
        String posturlpath;
        posturlpath = sp.getString("posturl", "");
        if (posturlpath == null)
            return null;
        else
            return posturlpath;
    }


    private void showLog() {
        //startActivity(new Intent(this, LogActivity.class));
        openLynxActivity();
    }

    private void openLynxActivity() {
        LynxConfig lynxConfig = new LynxConfig();
        lynxConfig.setMaxNumberOfTracesToShow(4000)
                .setFilter("NLService");

        Intent lynxActivityIntent = LynxActivity.getIntent(this, lynxConfig);
        startActivity(lynxActivityIntent);
    }

    private void openSettingActivity() {
        Intent intent = new Intent(MainActivity.this, PreferenceActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                openSettingActivity();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
