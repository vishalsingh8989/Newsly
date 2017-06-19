package com.kepler.news.newsly.ViewPagerFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.kepler.news.newsly.LoadFeedDataAsync;
import com.kepler.news.newsly.MainActivity;
import com.kepler.news.newsly.NewsStory;
import com.kepler.news.newsly.R;
import com.kepler.news.newsly.adapter.FoldingCellItemClickListener;
import com.kepler.news.newsly.adapter.FoldingCellListAdapter;
import com.kepler.news.newsly.helper.Common;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ramotion.foldingcell.FoldingCell;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * Created by vishaljasrotia on 16/06/17.
 */

public class DemoFragment extends Fragment implements FoldingCellItemClickListener {



    private int currentScrollState;
    private String mSearchText = "";
    public int firstVisibleItem, visibleItemCount, totalItemCount;

    private static final int MODE_PRIVATE = 0;
    private List<Object> productsList               = null;
    private List<Object> allNewslist                = null;
    private ListView listView                               = null;
    private FoldingCellListAdapter foldingCellListAdapter   = null;
    private LoadFeedDataAsync  loadFeedDataAsync            = null;
    private String sourceName                               = "";
    private LinkedHashMap<String, Integer> startMap         = null;
    private boolean loadImages = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        productsList            = new ArrayList<>();
        allNewslist             = new ArrayList<>();
        startMap                = Common.createStartMap();
        return inflater.inflate(R.layout.demo_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle mArgs = getArguments();
        int position = FragmentPagerItem.getPosition(mArgs);

        sourceName = mArgs.getString(Common.SOURCENAME);
        loadImages = mArgs.getBoolean(Common.LOADIMAGE);

        //LinkedHashMap<String, String> map = Common.createChoosenMap();
        //sourceName = map.get(sourceName);

        Log.v("onViewCreated", position + " : " + sourceName);

        listView                = (ListView)view.findViewById(R.id.list1);
        foldingCellListAdapter  = new FoldingCellListAdapter(this, getContext(), productsList, allNewslist, loadImages);


        listView.setAdapter(foldingCellListAdapter);
        loadFeedDataAsync = new LoadFeedDataAsync(getActivity().getApplicationContext(), foldingCellListAdapter, true, getActivity().getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE), sourceName, startMap);

        loadFeedDataAsync.execute();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                // toggle clicked cell state

                ((FoldingCell) view).toggle(false);
                // register in adapter that state for selected cell is toggled
                foldingCellListAdapter.registerToggle(pos);
            }
        });



        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

                currentScrollState = scrollState;

                Log.v("ONSCROLL", " " + firstVisibleItem + " , " + visibleItemCount + " , "+totalItemCount);
                isScrollCompleted(firstVisibleItem, visibleItemCount , productsList.size(), currentScrollState);


            }

            @Override
            public void onScroll(AbsListView absListView, int f, int v, int t) {


                firstVisibleItem = f;
                visibleItemCount = v;
                totalItemCount   = productsList.size();
                Log.v("ONSCROLL", "" + firstVisibleItem + " , " + visibleItemCount + " , " + totalItemCount);


            }
        });





    }

    @Override
    public void onItemClicked(View v, int position) {
        Log.v("READFULL", "" +position);
        NewsStory story = null;
        switch (v.getId()){
            case R.id.read_full:
                story = (NewsStory) productsList.get(position);
                Log.v("READFULL", "read full clicked "+ story.getUrl() );
                new FinestWebView.Builder(getActivity())
                        .showMenuShareVia(true)
                        .show(story.getUrl());
                break;
            case R.id.source:
                story = (NewsStory) productsList.get(position);
                Log.v("READFULL", "read full clicked "+ story.getSourceUrl());
                new FinestWebView.Builder(getActivity())
                        .showMenuShareVia(true)
                        .show("http://"+story.getSourceUrl());
                break;
            case R.id.description:
                Log.v("READFULL", "description full clicked");
                foldingCellListAdapter.registerToggle(position);
                ((FoldingCell) v.getParent().getParent()).toggle(false);
                break;


        }


    }


    private void isScrollCompleted(int firstVisibleItem, int visibleItemCount , int totalItemCount, int currentScrollState) {
        for(String sr :  startMap.keySet())
        {
            Log.v("LOADASYNCFEED" , sourceName +" isScrollCompleted  " + sr);
        }

        Log.v("LOADASYNCFEED",  sourceName + " END REACHED CHECK ,"+foldingCellListAdapter.getProductsList().size()+ " , "+startMap.get(sourceName)  + " , " + firstVisibleItem+ " , "+ visibleItemCount +" , " +totalItemCount);
        if ((!mSearchText.trim().equals("")&&foldingCellListAdapter.getProductsList().size() == firstVisibleItem+visibleItemCount)
                ||(foldingCellListAdapter.getProductsList().size() == firstVisibleItem+visibleItemCount  && currentScrollState==SCROLL_STATE_IDLE)) {
            Log.v("LOADASYNCFEED", "END REACHED" );
            loadMore();
        }
    }

    private void loadMore() {
        loadFeedDataAsync = new LoadFeedDataAsync(getActivity().getApplicationContext(), foldingCellListAdapter, true, getActivity().getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE), sourceName, startMap);
        loadFeedDataAsync.execute();
        //calledOn=foldingCellListAdapter.getProductsList().size();
        //Log.v("LOADASYNCFEED", "END REACHED : " + calledOn );
    }

}