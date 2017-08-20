package com.kepler.news.newsly;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.github.paolorotolo.appintro.AppIntro;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.kepler.news.newsly.IntroFragments.HelloFragment;
import com.kepler.news.newsly.IntroFragments.LanguageFragment;
import com.kepler.news.newsly.IntroFragments.LoadImagesFragment;
import com.kepler.news.newsly.IntroFragments.NewsSourceFragment;
import com.kepler.news.newsly.helper.ABaseTransformer;
import com.kepler.news.newsly.helper.Common;
import com.kepler.news.newsly.helper.DiffTransformer;

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
//                .setDefaultFontPath("fonts/Altair.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );

        super.onCreate(savedInstanceState);


        mPreferences = getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE);
        boolean firstLaunch = mPreferences.getBoolean(Common.FIRSTLAUNCH , true);

        if(!firstLaunch)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment helloFragment = new HelloFragment();


        Fragment newsSourceFragment = new NewsSourceFragment();
        //Fragment languageFragment = new LanguageFragment();
        Fragment loadImagesFragment = new LoadImagesFragment();


        addSlide(helloFragment);
        addSlide(newsSourceFragment);
        //addSlide(languageFragment);
        addSlide(loadImagesFragment);



        showSkipButton(false);
        setProgressButtonEnabled(true);


        setCustomTransformer(new DiffTransformer());
        //setFadeAnimation();
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



        // Do something when the slide changes.
    }







//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

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
}

class DepthPageTransformer extends ABaseTransformer {

    private static final float MIN_SCALE = 0.75f;

    @Override
    protected void onTransform(View view, float position) {
        if (position <= 0f) {
            view.setTranslationX(0f);
            view.setScaleX(1f);
            view.setScaleY(1f);
        } else if (position <= 1f) {
            final float scaleFactor = MIN_SCALE + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setAlpha(1 - position);
            view.setPivotY(0.5f * view.getHeight());
            view.setTranslationX(view.getWidth() * -position);
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
        }
    }

    @Override
    protected boolean isPagingEnabled() {
        return true;
    }

}
