package com.cu.yunxindemo.app;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Looper;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.util.Log;

import com.cu.yunxindemo.R;
import com.cu.yunxindemo.activity.LoginActivity;
import com.cu.yunxindemo.activity.WelcomeActivity;
import com.cu.yunxindemo.util.DemoCache;
import com.cu.yunxindemo.util.Preferences;
import com.cu.yunxindemo.util.SystemUtil;
import com.cu.yunxindemo.util.T;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.SDKOptions;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.uinfo.UserInfoProvider;


/**
 * Description
 * Created by chengwanying on 2017/3/13.
 * Company BeiJing guokeyuzhou
 */

public class BaseApplication extends MultiDexApplication implements Thread.UncaughtExceptionHandler{

    /**
     * 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了
     */
    private static BaseApplication mInstance;
    /**
     * 主线程Looper
     */
    private static Looper mMainLooper;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        super.onCreate();

        mMainLooper = getMainLooper();
        mInstance = this;
        DemoCache.setContext(mInstance);
        NIMClient.init(this, loginInfo(), options());
        if (inMainProcess(mInstance)) {
            NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(
                    new Observer<StatusCode>() {
                        public void onEvent(StatusCode status) {
                            Log.i("tag", "User status changed to: " + status);
                            if (status.wontAutoLogin()) {
                                // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                                T.showLong("出错");
//                                LoginActivity.lunch(mInstance);
                            }
                        }
                    }, true);
        }




    }




    // 如果返回值为 null，则全部使用默认参数。
    private SDKOptions options() {
        SDKOptions options = new SDKOptions();

        // 如果将新消息通知提醒托管给 SDK 完成，需要添加以下配置。否则无需设置。
        StatusBarNotificationConfig config = new StatusBarNotificationConfig();
        config.notificationEntrance = WelcomeActivity.class; // 点击通知栏跳转到该Activity
        config.notificationSmallIconId = R.mipmap.ic_launcher;
        // 呼吸灯配置
        config.ledARGB = Color.GREEN;
        config.ledOnMs = 1000;
        config.ledOffMs = 1500;
        // 通知铃声的uri字符串
        config.notificationSound = "android.resource://com.netease.nim.demo/raw/msg";
        options.statusBarNotificationConfig = config;

        // 配置保存图片，文件，log 等数据的目录
        // 如果 options 中没有设置这个值，SDK 会使用下面代码示例中的位置作为 SDK 的数据目录。
        // 该目录目前包含 log, file, image, audio, video, thumb 这6个目录。
        // 如果第三方 APP 需要缓存清理功能， 清理这个目录下面个子目录的内容即可。
        String sdkPath = Environment.getExternalStorageDirectory() + "/" + getPackageName() + "/nim";
        options.sdkStorageRootPath = sdkPath;

        // 配置是否需要预下载附件缩略图，默认为 true
        options.preloadAttach = true;

        // 配置附件缩略图的尺寸大小。表示向服务器请求缩略图文件的大小
        // 该值一般应根据屏幕尺寸来确定， 默认值为 Screen.width / 2
        options.thumbnailSize = 165 / 2;

        // 用户资料提供者, 目前主要用于提供用户资料，用于新消息通知栏中显示消息来源的头像和昵称
        options.userInfoProvider = new UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String account) {
                return null;
            }

            @Override
            public int getDefaultIconResId() {
                return R.mipmap.ic_launcher;
            }

            @Override
            public Bitmap getTeamIcon(String tid) {
                return null;
            }

            @Override
            public Bitmap getAvatarForMessageNotifier(String account) {
                return null;
            }

            @Override
            public String getDisplayNameForMessageNotifier(String account, String sessionId,
                                                           SessionTypeEnum sessionType) {
                return null;
            }
        };
        return options;
    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo loginInfo() {
        // 从本地读取上次登录成功时保存的用户登录信息
        String account = Preferences.getUserAccount();
        String token = Preferences.getUserToken();

        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(token)) {
            DemoCache.setAccount(account.toLowerCase());
            return new LoginInfo(account, token);
        } else {
            return null;
        }
    }

    /*
    * 注意：除了 NIMClient.init 接口外，其他 SDK 暴露的接口都只能在 UI 进程调用。
    * 如果 APP 包含远程 service，该 APP 的 Application 的 onCreate 会多次调用。
    * 因此，如果需要在 onCreate 中调用除 init 接口外的其他接口，应先判断当前所属进程，并只有在当前是 UI 进程时才调用
    * */
    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = SystemUtil.getProcessName(context);
        return packageName.equals(processName);
    }




    public static BaseApplication getApplication() {
        return mInstance;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        android.os.Process.killProcess(android.os.Process.myPid());// 专注自杀的方法,只能自杀的方法.
        System.exit(0);
    }
}
