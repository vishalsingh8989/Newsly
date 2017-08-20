package com.kepler.news.newsly.helper;


import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.kepler.news.newsly.R;


/**
 * Created by vishaljasrotia on 8/20/17.
 */

public class DiffTransformer implements ViewPager.PageTransformer {
    private int mPageNumber = -2;

    @Override
    public void transformPage(View page, float position) {

        RelativeLayout rl = (RelativeLayout) page;

        //Log.v("DiffTransformer",page + " , " + position);

        mPageNumber = 2; //or 2 . same thing . 0 is diferent;
        for(int i = 0; i< rl.getChildCount(); i++) {
            if(rl.getChildAt(i) instanceof ImageView) {
                //Log.v("DiffTransformer", page + " , ImageView");
                mPageNumber = 0;
                break;
            }else if(rl.getChildAt(i) instanceof ListView)
            {
                ListView lt = (ListView) rl.getChildAt(i);
                if(lt.getId()==R.id.countrylist){
                    mPageNumber = 1;
                    break;
                }
            }
        }




        int pageWidth = page.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);


        }else if (position <= 1) {

            if(mPageNumber == 0) {

                if (position != 0.0) {
                    rl.findViewById(R.id.world).setTranslationX(position * 0.5f * pageWidth);

                    rl.findViewById(R.id.app_name_middle).setTranslationX(position * 1.8f * pageWidth);
                    rl.findViewById(R.id.details).setTranslationX(position * 1.5f * pageWidth);
                }
            } else if (mPageNumber == 1){
                Log.v("DiffTransformer", position + "  , " + pageWidth + " , " + (-position*(pageWidth/2)) + " , " + rl.findViewById(R.id.title).getTranslationY());

                rl.findViewById(R.id.title).setTranslationX(-position*(pageWidth/2));
                ListView lt = (ListView) rl.findViewById(R.id.countrylist);
                lt.setTranslationY(-position*(position/4));

            }else {

                rl.findViewById(R.id.title).setTranslationX(-position*(pageWidth/2));
                rl.findViewById(R.id.loadimagelist).setTranslationX(-position*(pageWidth/4));
            }
                //mDim.setTranslationX((float) (-(1 - position) * pageWidth));
                //mDimLabel.setTranslationX((float) (-(1 - position) * pageWidth))
                //mCheck.setTranslationX((float) (-(1 - position) * 1.5 * pageWidth));
                //mDoneButton.setTranslationX((float) (-(1 - position) * 1.7 * pageWidth));
                // The 0.5, 1.5, 1.7 values you see here are what makes the view move in a different speed.
                // The bigger the number, the faster the view will translate.
                // The result float is preceded by a minus because the views travel in the opposite direction of the movement.






        }else{
            // This page is way off-screen to the right.
            page.setAlpha(0);

        }
    }


}
