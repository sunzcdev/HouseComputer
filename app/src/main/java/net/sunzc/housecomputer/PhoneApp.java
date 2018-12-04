package net.sunzc.housecomputer;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * @author Administrator
 * @date 2018-12-04 13:36
 **/
public class PhoneApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(this, "7b5ad35aef", BuildConfig.DEBUG);
    }
}
