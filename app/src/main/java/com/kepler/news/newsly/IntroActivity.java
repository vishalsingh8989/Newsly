package com.kepler.news.newsly;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.kepler.news.newsly.IntroFragments.CountryFragment;
import com.kepler.news.newsly.IntroFragments.HelloFragment;
import com.kepler.news.newsly.IntroFragments.LanguageFragment;
import com.kepler.news.newsly.IntroFragments.LoadImagesFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class IntroActivity extends AppIntro implements LoadImagesFragment.OnFragmentInteractionListener,HelloFragment.OnFragmentInteractionListener, CountryFragment.OnFragmentInteractionListener , LanguageFragment.OnFragmentInteractionListener{

    int currentApiVersion = 0;

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

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Altair.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        super.onCreate(savedInstanceState);


        Fragment helloFragment = new HelloFragment();
        Fragment countryFragment = new CountryFragment();
        //Fragment languageFragment = new LanguageFragment();
        Fragment loadImagesFragment = new LoadImagesFragment();


        addSlide(helloFragment);
        addSlide(countryFragment);
        //addSlide(languageFragment);
        addSlide(loadImagesFragment);


        showSkipButton(false);
        setProgressButtonEnabled(true);

        setFadeAnimation();
        setIndicatorColor(R.color.colorChipBackground, R.color.n);
        setColorDoneText(Color.parseColor("#111111"));


        setSeparatorColor(Color.parseColor("#00ffffff"));
        setNextArrowColor(Color.parseColor("#aa1133"));


        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
//        setVibrate(true);
//        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }




    @Override
    public void onHelloFragmentInteraction(Uri uri) {
        Log.v("FRAGMENT", "onHelloFragmentInteraction");
    }

    @Override
    public void onCountryFragmentInteraction(Uri uri) {
        Log.v("FRAGMENT", "onCountryFragmentInteraction");

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onLanguageFragmentInteraction(Uri uri) {
        Log.v("FRAGMENT", "onLanguageFragmentInteraction");

    }

    @Override
    public void onFragmentLoadImagesFragmentInteraction(Uri uri) {

    }
}
