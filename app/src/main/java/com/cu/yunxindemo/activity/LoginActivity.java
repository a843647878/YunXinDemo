package com.cu.yunxindemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.util.L;
import com.cu.yunxindemo.util.MD5;
import com.cu.yunxindemo.util.Preferences;
import com.cu.yunxindemo.util.T;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

/**
 * Description
 * Created by chengwanying on 2017/3/13.
 * Company BeiJing guokeyuzhou
 */

public class LoginActivity extends Activity{
    EditText account;
    EditText password;
    Button login;



    public static void lunch(Context context){
        if (context == null) return;
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        initView(this);
    }

    public void initView(Activity context){
        login = (Button) context .findViewById(R.id.login);
        account = (EditText) context .findViewById(R.id.account);
        password = (EditText) context .findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(this);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void doLogin() {
        if (TextUtils.isEmpty(account.getText().toString())){
            T.showLong("账号不能是空的");
            return;
        }
        if (TextUtils.isEmpty(password.getText().toString())){
            T.showLong("密码不能是空的");
            return;
        }
        LoginInfo info = new LoginInfo(account.getText().toString(),tokenFromPassword(password.getText().toString())); // config...


        RequestCallback<LoginInfo> callback =
                new RequestCallback<LoginInfo>() {
                    @Override
                    public void onSuccess(LoginInfo param) {
                        //登录成功
                        T.showShort("登录成功："+param.getAccount());
                        Preferences.saveUserAccount(param.getAccount());
                        Preferences.saveUserToken(param.getToken());
                        FriendsList.launch(LoginActivity.this);
                    }

                    @Override
                    public void onFailed(int code) {
                        //失败
                        T.showShort("登录失败：code="+code);
                    }

                    @Override
                    public void onException(Throwable exception) {
                        //异常
                    }
                    // 可以在此保存LoginInfo到本地，下次启动APP做自动登录用

                };
        NIMClient.getService(AuthService.class).login(info)
                .setCallback(callback);
    }
}
