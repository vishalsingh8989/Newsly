package com.kepler.news.newsly.helper;

/**
 * Created by vishaljasrotia on 21/07/17.
 */

import com.kepler.news.newsly.helper.BounceScroller.State;


public interface BounceListener {

    public void onState(boolean header, State state);

    public void onOffset(boolean header, int offset);

}