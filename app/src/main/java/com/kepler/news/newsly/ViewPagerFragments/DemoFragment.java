package com.kepler.news.newsly.ViewPagerFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.kepler.news.newsly.LoadFeedDataAsync;
import com.kepler.news.newsly.NewsStory;
import com.kepler.news.newsly.R;
import com.kepler.news.newsly.adapter.FoldingCellItemClickListener;
import com.kepler.news.newsly.adapter.FoldingCellListAdapter;
import com.kepler.news.newsly.databaseHelper.News;
import com.kepler.news.newsly.databaseHelper.NewsSource;
import com.kepler.news.newsly.databaseHelper.NewsSourceDatabase;
import com.kepler.news.newsly.databaseHelper.NewsDatabase;
import com.kepler.news.newsly.helper.BounceListener;
import com.kepler.news.newsly.helper.BounceScroller;
import com.kepler.news.newsly.helper.Common;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ramotion.foldingcell.FoldingCell;
import com.thefinestartist.finestwebview.FinestWebView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * Created by vishaljasrotia on 16/06/17.
 */

public class DemoFragment extends Fragment implements FoldingCellItemClickListener {


    public static final String NEWSSOURCE = "NEWSSOURCE";
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
    private static Activity parent = null;
    private int calledOn = 0;

    public  static AVLoadingIndicatorView avLoadingIndicatorView = null;

    private boolean paused = false;
    private Animation animFadein;
    private BounceScroller scroller;
    private NewsSourceDatabase newsSourceDatabase;
    private List<NewsSource> newsSourcelist;
    private NewsDatabase newsDatabase;
    private List<News> newsList;
    private Context mContext;
    private NewsDatabase database;
    private List<NewsStory> alldbnews;
    private Bundle mArgs;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v("fragmentLifecycle", "onCreateView " + sourceName);

        mArgs                   = getArguments();
        productsList            = new ArrayList<>();
        allNewslist             = new ArrayList<>();
        mContext                = getActivity().getApplicationContext();
        newsSourceDatabase      = NewsSourceDatabase.getDatabase(mContext);
        newsSourcelist          = newsSourceDatabase.feedModel().getAllFeeds();
        newsDatabase            = NewsDatabase.getDatabase(mContext);
        sourceName              = mArgs.getString(Common.SOURCENAME);
        alldbnews               = newsDatabase.feedModel().getSourceNews(sourceName);

        Log.v(NEWSSOURCE       ,"***********************************");
        Log.v(NEWSSOURCE       ,"productlist " + productsList);


        productsList            = new ArrayList<>();
        
        for (NewsStory story: alldbnews) {
            if(story.getSourceName().contains("New York Post")) {
                Log.v(NEWSSOURCE, "before sourceName : " + sourceName + " , " + story.getUrltoimage() + "");
            }
            if(story.getSourceName().equals(sourceName))
            productsList.add(story);

        }
        //Log.v(NEWSSOURCE       ,"ALL OBJS SIZE BF AD : " + productsList.size());
        productsList            = addNativeExpressAds(productsList);
        //Log.v(NEWSSOURCE       ,"ALL OBJS SIZE AF AD : " + productsList.size());
        //Log.v(NEWSSOURCE       ,"SOURCE : " +sourceName);
        //Log.v(NEWSSOURCE       ,"ALL NEWS SIZE : " + alldbnews.size());
        //Log.v(NEWSSOURCE       ,"***********************************");
        startMap                = new LinkedHashMap<>();


        for (NewsStory story: alldbnews) {
            if (story.getSourceName().contains("New York Post")) {
                Log.v(NEWSSOURCE, "after sourceName : " + sourceName + " , " + story.getUrltoimage() + "");
            }
        }
        for(NewsSource newsSourceObj : newsSourcelist) {
            startMap.put(newsSourceObj.newsSource, 0);
        }

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.demo_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.v("fragmentLifecycle", "onViewCreated" + sourceName);
        mArgs = getArguments();
        int position = FragmentPagerItem.getPosition(mArgs);



        sourceName = mArgs.getString(Common.SOURCENAME);
        loadImages = mArgs.getBoolean(Common.LOADIMAGE);

        Log.v("onViewCreated", position + " : " + sourceName);

        listView                = (ListView)view.findViewById(R.id.list1);
        foldingCellListAdapter  = new FoldingCellListAdapter(this, mContext, productsList, alldbnews, loadImages, sourceName);




        listView.setAdapter(foldingCellListAdapter);
        loadFeedDataAsync = new LoadFeedDataAsync(this , mContext, foldingCellListAdapter, true, getActivity().getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE), sourceName, startMap, newsDatabase);

        //loadFeedDataAsync.execute();
        startMap.put(sourceName, calledOn);




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
                Log.v("SOURCEURL", "read full clicked "+ story.getSourceUrl());
                new FinestWebView.Builder(getActivity())
                        .showMenuShareVia(true)
                        .show(story.getSourceUrl());
                break;
            case R.id.description:
            case R.id.title_back:
                Log.v("READFULL", "description full clicked");
                foldingCellListAdapter.registerToggle(position);
                ((FoldingCell) v.getParent().getParent().getParent()).toggle(false);
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
                ||(foldingCellListAdapter.getProductsList().size() == firstVisibleItem+visibleItemCount  && currentScrollState==SCROLL_STATE_IDLE) && calledOn != foldingCellListAdapter.getProductsList().size()) {
            Log.v("LOADASYNCFEED", "END REACHED1" );
            loadMore();

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.v("fragmentLifecycle", "onResume " + sourceName);
        if(productsList.size() == 0 && paused == true) {
            loadMore();
        }
        paused= false;
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.v("fragmentLifecycle", "oonStart " + sourceName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.v("fragmentLifecycle", "onAttach(Context context) " + sourceName);
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.v("fragmentLifecycle", "onPause " + sourceName);
        paused= true;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.v("fragmentLifecycle", "onAttach(Activity activity) " + sourceName);
    }

    private void loadMore() {
        Log.v("LOADASYNCFEED", "END REACHED2" );

    }





    private BounceListener bl = new BounceListener() {
        @Override
        public void onState(boolean header, BounceScroller.State state) {
            if (header) {
                if (state == BounceScroller.State.STATE_SHOW) {
                    Log.v("BounceListener","STATE_SHOW");
                } else if (state == BounceScroller.State.STATE_OVER) {
                    Log.v("BounceListener","STATE_OVER");
                } else if (state == BounceScroller.State.STATE_FIT_EXTRAS) {
                    scroller.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scroller.resetState();
                        }
                    }, 1000);
                }
            }
        }

        @Override
        public void onOffset(boolean header, int offset) {

        }
    };


    private List<Object> addNativeExpressAds(List<Object> result){
        for(int i = 0 ; i < result.size(); i +=1)
        {
            if(i%12==0 && i!=0) {
                NativeExpressAdView adView = new NativeExpressAdView(mContext);
                adView.setAdSize(new AdSize(300, 100));

                adView.setAdUnitId("ca-app-pub-5223778660504166/2968121932");
                adView.loadAd(new AdRequest.Builder()
                        .addTestDevice("32C278BA97F2B33C41A02691587B4F29")
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build());
                result.add(i, adView);
            }

        }

        return result;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_menu, menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Log.v("DemoFrag" , "item selected :" + item.getItemId());


        loadFeedDataAsync.execute();
        return super.onOptionsItemSelected(item);


    }

    public void getFeeds() {
        alldbnews = newsDatabase.feedModel().getAllNews();



    }
}