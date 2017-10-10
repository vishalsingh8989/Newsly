package com.kepler.news.newsly;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.paolorotolo.appintro.AppIntro;

import com.google.firebase.FirebaseApp;
import com.kepler.news.newsly.IntroFragments.HelloFragment;
import com.kepler.news.newsly.IntroFragments.LanguageFragment;
import com.kepler.news.newsly.IntroFragments.LoadImagesFragment;
import com.kepler.news.newsly.IntroFragments.NewsSourceFragment;
import com.kepler.news.newsly.helper.ABaseTransformer;
import com.kepler.news.newsly.helper.Common;
import com.kepler.news.newsly.helper.MyAppIntro;
import com.kepler.news.newsly.transformation.DiffTransformer;
import com.kepler.news.newsly.updateUtils.UpdateDBservice;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class IntroActivity extends AppIntro implements LoadImagesFragment.OnFragmentInteractionListener,HelloFragment.OnFragmentInteractionListener, NewsSourceFragment.OnFragmentInteractionListener , LanguageFragment.OnFragmentInteractionListener{

    int currentApiVersion = 0;
    private SharedPreferences mPreferences;

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if(currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setSystemUiVisibility(flags);
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/attic.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );

        super.onCreate(savedInstanceState);
        mPreferences = getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE);
        boolean firstLaunch = mPreferences.getBoolean(Common.FIRSTLAUNCH , true);


        FirebaseApp.initializeApp(this);
        if(!firstLaunch)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment helloFragment = new HelloFragment();


        Fragment newsSourceFragment = new NewsSourceFragment();
        Fragment loadImagesFragment = new LoadImagesFragment();


        addSlide(helloFragment);
        addSlide(newsSourceFragment);
        addSlide(loadImagesFragment);



        showSkipButton(false);
        setProgressButtonEnabled(true);


        setCustomTransformer(new DiffTransformer());
        //setFadeAnimation();
        //setIndicatorColor(R.color.colorChipBackground, R.color.n);
        setColorDoneText(Color.parseColor("#ffffff"));


        setSeparatorColor(Color.parseColor("#01111111"));
        setNextArrowColor(Color.parseColor("#F5F5F5"));


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        Calendar cal = null;
        cal = Calendar.getInstance();

        Intent intent1= new Intent(getApplicationContext(), UpdateDBservice.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent1, 0);
        AlarmManager alarm = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);


        //5 seconds
        alarm.cancel(pintent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), Common.UPDATEINTERVAL, pintent);

        //

        SharedPreferences.Editor editor = mPreferences.edit();

        editor.putBoolean(Common.FIRSTLAUNCH , false);
        editor.commit();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }




    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        Log.v("FRAGMENTInteraction",oldFragment + "  to " + newFragment);

    }
    @Override
    public void onLanguageFragmentInteraction(Uri uri) {
        Log.v("FRAGMENTInteraction", "onLanguageFragmentInteraction");

    }

    @Override
    public void onFragmentLoadImagesFragmentInteraction(Uri uri) {
        Log.v("FRAGMENTInteraction", "onFragmentLoadImagesFragmentInteraction");
    }

    @Override
    public void onNewsSourceFragmentInteraction(Uri uri) {
        Log.v("FRAGMENTInteraction", "onNewsSourceFragmentInteraction");
    }

    @Override
    public void onHelloFragmentInteraction(Uri uri, String mFrag) {
        Log.v("FRAGMENTInteraction", "onNewsSourceFragmentInteraction " + mFrag);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }
}

