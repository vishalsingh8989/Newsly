package com.kepler.news.newsly;

import android.content.Context;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import android.widget.ListView;



import com.kepler.news.newsly.helper.CallbackAdapter;
import com.kepler.news.newsly.views.CircleRefreshLayout;
import com.ramotion.foldingcell.FoldingCell;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;



import java.util.ArrayList;
import java.util.Arrays;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity{

    private ArrayList<NewsStory> productsList = null;
    private ArrayList<NewsStory> allNewslist = null;
    private ListView listView                               = null;
    private FoldingCellListAdapter foldingCellListAdapter   = null;
    private LoadFeedDataAsync  loadFeedDataAsync            = null;
    private int calledOn = 30;
    private int minEntries = 30;
    private int currentScrollState;
    private String mSearchText = "";
    public int firstVisibleItem, visibleItemCount, totalItemCount;
    private CircleRefreshLayout mCircleRefreshLayout        = null;
    private SearchView mSearchView                          = null;



    public static int start =0;
    public static int offset = 30;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoCondensed-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();




        productsList            = new ArrayList<>();
        allNewslist             = new ArrayList<>();
        //mCircleRefreshLayout    = (CircleRefreshLayout)findViewById(R.id.refresh_layout);
        listView                = (ListView) findViewById(R.id.list1);
        mSearchView             = (SearchView)findViewById(R.id.search_view);
        foldingCellListAdapter  = new FoldingCellListAdapter(MainActivity.this,this, productsList, allNewslist);


        listView.setAdapter(foldingCellListAdapter);
        loadFeedDataAsync = new LoadFeedDataAsync(MainActivity.this, foldingCellListAdapter, true);

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
                isScrollCompleted(firstVisibleItem, visibleItemCount , totalItemCount, currentScrollState);


            }

            @Override
            public void onScroll(AbsListView absListView, int f, int v, int t) {

                //Log.v("ONSCROLL", "" + firstVisibleItem + " , " + visibleItemCount + " , " + totalItemCount);
                firstVisibleItem = f;
                visibleItemCount = v;
                totalItemCount   = t;


            }
        });


        mSearchView.setIconified(true);
        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                ArrayList<NewsStory> entries = foldingCellListAdapter.AllNewsEntries();
                foldingCellListAdapter.refreshEntries(entries);
                mSearchText = "";
                return false;
            }
        });
        mSearchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.v("QuerySearch" , "onFocusChange " + b);

                if(!b) {
                   // ArrayList<NewsStory> entries = foldingCellListAdapter.AllNewsEntries();
                   // foldingCellListAdapter.refreshEntries(entries);
                    //new LoadFeedDataAsync(MainActivity.this ,foldingCellListAdapter).execute();
                }
            }
        });

        //TODO com.github.glomadrian.grav.GravView

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v("QuerySearch" , "onClick");
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mSearchText = s;
                Log.v("QuerySearch" , "onQueryTextSubmit");
                ArrayList<NewsStory> entries = filterNewsBasesOnSearch(foldingCellListAdapter.AllNewsEntries(), s);
                foldingCellListAdapter.refreshEntries(entries);
                if(entries.size()<minEntries)
                {
                    loadMore();
                    entries = filterNewsBasesOnSearch(foldingCellListAdapter.AllNewsEntries(), s);
                }
                foldingCellListAdapter.refreshEntries(entries);
                return true;
            }



            @Override
            public boolean onQueryTextChange(String searchText) {
                Log.v("QuerySearch" , "onQueryTextChange");
                if(searchText.trim()=="") {
                    new LoadFeedDataAsync(MainActivity.this ,foldingCellListAdapter).execute();
                }
                return true;
            }
        });


//        mCircleRefreshLayout.setOnRefreshListener(new CircleRefreshLayout.OnCircleRefreshListener() {
//            @Override
//            public void completeRefresh() {
//                Log.v("REFRESHLAYOUT" , "completeRefresh");
//            }
//
//            @Override
//            public void refreshing() {
//                Log.v("REFRESHLAYOUT" , "refreshing1");
//                if(productsList!=null && productsList.size()!=0) {
//                    loadFeedDataAsync = new LoadFeedDataAsync(MainActivity.this,foldingCellListAdapter, true);
//                    loadFeedDataAsync.execute();
//                    Log.v("REFRESHLAYOUT" , "refreshing2");
//
//                }
//
//            }
//        });



    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void isScrollCompleted(int firstVisibleItem, int visibleItemCount , int totalItemCount, int currentScrollState) {
        Log.v("LOADASYNCFEED", "END REACHED CHECK ,"+foldingCellListAdapter.getProductsList().size()+","+mSearchText + ","  +calledOn  + " , " + firstVisibleItem+ " , "+ visibleItemCount );
        if ((!mSearchText.trim().equals("")&&foldingCellListAdapter.getProductsList().size() == firstVisibleItem+visibleItemCount)
                ||(calledOn == firstVisibleItem+visibleItemCount && currentScrollState==SCROLL_STATE_IDLE))
        {
            Log.v("LOADASYNCFEED", "END REACHED" );
            loadMore();
        }
    }

    private void loadMore() {
        loadFeedDataAsync = new LoadFeedDataAsync(MainActivity.this, foldingCellListAdapter, true);
        loadFeedDataAsync.execute();
        calledOn=calledOn+offset;
    }


    private ArrayList<NewsStory> filterNewsBasesOnSearch(ArrayList<NewsStory> entries, String searchText) {



        ArrayList<NewsStory> filteredNews = new ArrayList<>();
        for(NewsStory story: entries) {

            if(story.getDescription().toLowerCase().contains(searchText)
                    || story.getTitle().toLowerCase().contains(searchText)
                    || story.getAuthor().toLowerCase().contains(searchText)
                    || story.getSourceName().toLowerCase().contains(searchText) ) {
                filteredNews.add(story);
            }

        }
        Log.v("QuerySearch" , "filtersize " + filteredNews.size() + " , " + entries.size());
        return filteredNews;
    }
//    @Override
//    public void onRefreshComplete() {
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        mCircleRefreshLayout.finishRefreshing();
//        Log.v("REFRESHLAYOUT" , "onRefreshComplete");
//
//
//    }
}
