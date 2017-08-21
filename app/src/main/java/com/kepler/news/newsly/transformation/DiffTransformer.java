package com.kepler.news.newsly.transformation;


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
        mPageNumber = -2; //or 2 . same thing . 0 is diferent;
        for(int i = 0; i< rl.getChildCount(); i++) {
            if(rl.getChildAt(i) instanceof ImageView && rl.getChildAt(i).getId() == R.id.world) {
                mPageNumber = 0;
                break;
            }else if(rl.getChildAt(i) instanceof ListView && rl.getChildAt(i).getId() == R.id.countrylist) {
                mPageNumber = 1;
                break;
            }else if(rl.getChildAt(i) instanceof ListView && rl.getChildAt(i).getId() == R.id.photocamera) {
                mPageNumber = 2;
            }
        }
        int pageWidth = page.getWidth();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            page.setAlpha(0);
        }else if (position <= 1) {
            if(mPageNumber == 0) {

                rl.findViewById(R.id.world).setTranslationX(position*(pageWidth*1.3f));
                rl.findViewById(R.id.app_name_middle).setTranslationX(-position*(pageWidth/4));
                rl.findViewById(R.id.details).setTranslationX(position*(pageWidth*1.3f));

            } else if (mPageNumber == 1){
                Log.v("DiffTransformer", mPageNumber +", "+position + "  , " + pageWidth + " , " + (-position*(pageWidth/2)) + " , " + rl.findViewById(R.id.title).getTranslationY());
                rl.findViewById(R.id.newspaper).setTranslationX(position*(pageWidth*1.4f));
                rl.findViewById(R.id.title).setTranslationX(-position*(pageWidth/4));
                rl.findViewById(R.id.countrylist).setTranslationY(position*(position*1.4f));;

            }else {
                rl.findViewById(R.id.photocamera).setTranslationX(position*(pageWidth*1.4f));
                rl.findViewById(R.id.title).setTranslationX(-position*(pageWidth/4));
                rl.findViewById(R.id.loadimagelist).setTranslationX(position*(pageWidth*1.4f));
            }
        }else{
            // This page is way off-screen to the right.
            page.setAlpha(0);
        }
    }
}
