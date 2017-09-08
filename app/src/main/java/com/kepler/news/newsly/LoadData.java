package com.kepler.news.newsly;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.kepler.news.newsly.databaseHelper.NewsDatabase;

/**
 * Created by vishaljasrotia on 8/26/17.
 */

class LoadData  extends AsyncTask<Void, Void, Void>{

    private Context mContext;
    private NewsDatabase database;
    private String device_id;
    private long last_used;
    private int sdk_int;
    private String devicetype;
    private int appversion;

    public LoadData(Context mContext) {
        this.mContext = mContext;


    }

    @Override
    protected Void doInBackground(Void... voids) {

        database = NewsDatabase.getDatabase(mContext);

        device_id = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        last_used = System.currentTimeMillis() / 1000L;
        sdk_int = Build.VERSION.SDK_INT;
        devicetype = isTablet(mContext);
        appversion = getAppVersion();


        return null;
    }


    public static String isTablet(Context context) {


        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE ? "Tablet" : "TelePhone";
    }

    private int getAppVersion() {
        String app_version = null;
        PackageInfo pInfo = null;
        try {
            pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        app_version = pInfo.versionName;
        Log.v("VERSIONCODE", " CODE : " + pInfo.versionCode);
        return pInfo.versionCode;
    }


}
