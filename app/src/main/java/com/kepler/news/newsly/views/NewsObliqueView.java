package com.kepler.news.newsly.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import ak.sh.ay.oblique.Config;
import ak.sh.ay.oblique.ObliqueView;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;

/**
 * Created by vishaljasrotia on 8/26/17.
 */

public class NewsObliqueView extends ObliqueView {
    private final Paint colorPaint = new Paint(ANTI_ALIAS_FLAG);
    private Paint bitmapPaint = new Paint(ANTI_ALIAS_FLAG);
    private Bitmap bitmap = null;
    private BitmapShader bitmapShader;
    private Matrix shaderMatrix = new Matrix();
    private int baseColor = Color.TRANSPARENT;
    private float startAngle, endAngle, width, height;
    private Config config = new Config();



    public NewsObliqueView(Context context) {
        super(context);
        init(context, null);
    }

    public NewsObliqueView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NewsObliqueView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    //initialise
    private void init(Context context, AttributeSet attrs) {
        config = new Config();
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, ak.sh.ay.oblique.R.styleable.ObliqueView, 0, 0);
        if (attrs != null) {
            startAngle = a.getFloat(ak.sh.ay.oblique.R.styleable.ObliqueView_starting_slant_angle, 90f);
            endAngle = a.getFloat(ak.sh.ay.oblique.R.styleable.ObliqueView_ending_slant_angle, 90f);
            baseColor = a.getColor(ak.sh.ay.oblique.R.styleable.ObliqueView_basecolor, Color.TRANSPARENT);
            a.recycle();
            colorPaint.setStyle(Paint.Style.FILL);
            colorPaint.setColor(baseColor);
            colorPaint.setAlpha(200);
        }
    }
}
