package com.kepler.news.newsly.adapter;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by vishaljasrotia on 05/06/17.
 */

public interface RecyclerItemClickListener {


    void onItemClicked(AdapterView<?> parent, View view, int position, long id);


}
