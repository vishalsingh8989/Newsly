package com.kepler.news.newsly.helper;

import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroBase;
import com.github.paolorotolo.appintro.CustomFontCache;
import com.kepler.news.newsly.R;

/**
 * Created by vishaljasrotia on 8/22/17.
 */

public class MyAppIntro extends AppIntroBase {




    @Override
    protected int getLayoutId() {
        return R.layout.appintro;
    }

    /**
     * Override viewpager bar color
     *
     * @param color your color resource
     */
    public void setBarColor(@ColorInt final int color) {
        LinearLayout bottomBar = (LinearLayout) findViewById(com.github.paolorotolo.appintro.R.id.bottom);
        bottomBar.setBackgroundColor(color);
    }

    /**
     * Override next button arrow color
     *
     * @param color your color
     */
    public void setNextArrowColor(@ColorInt final int color) {
        ImageButton nextButton = (ImageButton) findViewById(com.github.paolorotolo.appintro.R.id.next);
        nextButton.setColorFilter(color);
    }

    /**
     * Override separator color
     *
     * @param color your color resource
     */
    public void setSeparatorColor(@ColorInt final int color) {
        TextView separator = (TextView) findViewById(com.github.paolorotolo.appintro.R.id.bottom_separator);
        separator.setBackgroundColor(color);
    }

    /**
     * Override skip text
     *
     * @param text your text
     */
    public void setSkipText(@Nullable final CharSequence text) {
        TextView skipText = (TextView) findViewById(com.github.paolorotolo.appintro.R.id.skip);
        skipText.setText(text);
    }

    /**
     * Override skip text typeface
     *
     * @param typeURL URL of font file located in Assets folder
     */
    public void setSkipTextTypeface(@Nullable final String typeURL) {
        TextView skipText = (TextView) findViewById(com.github.paolorotolo.appintro.R.id.skip);
        if (CustomFontCache.get(typeURL, this) != null) {
            skipText.setTypeface(CustomFontCache.get(typeURL, this));
        }
    }

    /**
     * Override done text
     *
     * @param text your text
     */
    public void setDoneText(@Nullable final CharSequence text) {
        TextView doneText = (TextView) findViewById(com.github.paolorotolo.appintro.R.id.done);
        doneText.setText(text);
    }

    /**
     * Override done text typeface
     *
     * @param typeURL your text
     */
    public void setDoneTextTypeface(@Nullable final String typeURL) {
        TextView doneText = (TextView) findViewById(com.github.paolorotolo.appintro.R.id.done);
        if (CustomFontCache.get(typeURL, this) != null) {
            doneText.setTypeface(CustomFontCache.get(typeURL, this));
        }
    }

    /**
     * Override done button text color
     *
     * @param colorDoneText your color resource
     */
    public void setColorDoneText(@ColorInt final int colorDoneText) {
        TextView doneText = (TextView) findViewById(com.github.paolorotolo.appintro.R.id.done);
        doneText.setTextColor(colorDoneText);
    }

    /**
     * Override skip button color
     *
     * @param colorSkipButton your color resource
     */
    public void setColorSkipButton(@ColorInt final int colorSkipButton) {
        TextView skip = (TextView) findViewById(com.github.paolorotolo.appintro.R.id.skip);
        skip.setTextColor(colorSkipButton);
    }

    /**
     * Override Next button
     *
     * @param imageNextButton your drawable resource
     */
    public void setImageNextButton(final Drawable imageNextButton) {
        final ImageView nextButton = (ImageView) findViewById(com.github.paolorotolo.appintro.R.id.next);
        nextButton.setImageDrawable(imageNextButton);
    }

    /**
     * Shows or hides Done button, replaced with setProgressButtonEnabled
     *
     * @deprecated use {@link #setProgressButtonEnabled(boolean)} instead.
     */
    @Deprecated
    public void showDoneButton(boolean showDone) {
        setProgressButtonEnabled(showDone);
    }
}
