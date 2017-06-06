package com.kepler.news.newsly;

import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import android.widget.ListView;


import com.kepler.news.newsly.chip.Chip;
import com.kepler.news.newsly.chip.OnChipClickListener;
import com.kepler.news.newsly.chip.OnSelectClickListener;
import com.kepler.news.newsly.helper.Common;
import com.kepler.news.newsly.views.CircleRefreshLayout;
import com.ramotion.foldingcell.FoldingCell;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;



import java.util.ArrayList;
import java.util.HashMap;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity  implements FoldingCellItemClickListener{

    private ArrayList<NewsStory> productsList               = null;
    private ArrayList<NewsStory> allNewslist                = null;
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
    private SharedPreferences mPreferences                  = null;
    private SharedPreferences.Editor editor                 = null;
    private Chip chipScienceAndNature                       = null;
    private boolean chipScienceAndNatureSelected            = true;

    private Chip chipPolitics                               = null;
    private boolean chipPoliticsSelected                    = true;


    private HashMap<Integer, String> mMap                                    = null;
    private ArrayList<HashMap<Integer, String>> hashMap     = null;


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




        mMap = new HashMap();
        mMap.put(R.id.chipScienceAndNature , Common.chipScienceAndNatureSelected);
        mMap.put(R.id.chipPolitics, Common.chipPolitics);




        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPreferences = getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE);



        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();



        chipScienceAndNatureSelected = mPreferences.getBoolean(Common.chipScienceAndNatureSelected ,  true);
        chipPoliticsSelected         = mPreferences.getBoolean(Common.chipPolitics ,  true);


        Log.v("CHIP" ,"mPreferences Science Nature " + chipScienceAndNatureSelected);
        Log.v("CHIP" ,"mPreferences Politics  " + chipPoliticsSelected);
        chipScienceAndNature    = (Chip)findViewById(R.id.chipScienceAndNature);
        chipPolitics            = (Chip)findViewById(R.id.chipPolitics);

        chipScienceAndNature.setClicked(chipScienceAndNatureSelected);
        chipPolitics.setClicked(chipPoliticsSelected);



        productsList            = new ArrayList<>();
        allNewslist             = new ArrayList<>();
        //mCircleRefreshLayout    = (CircleRefreshLayout)findViewById(R.id.refresh_layout);
        listView                = (ListView) findViewById(R.id.list1);
        mSearchView             = (SearchView)findViewById(R.id.search_view);
        foldingCellListAdapter  = new FoldingCellListAdapter(MainActivity.this,this, productsList, allNewslist);


        listView.setAdapter(foldingCellListAdapter);
        loadFeedDataAsync = new LoadFeedDataAsync(MainActivity.this, foldingCellListAdapter, true, mPreferences);

        loadFeedDataAsync.execute();


        chipScienceAndNature.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(View v, boolean selected) {
                chipScienceAndNatureSelected = !chipScienceAndNatureSelected;
                Log.v("CHIP" ,"onChipClick " + selected);
                editor = mPreferences.edit();
                editor.putBoolean(Common.chipScienceAndNatureSelected, selected);
                editor.commit();



            }
        });

        chipScienceAndNature.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onSelectClick(View v, boolean selected) {
                chipScienceAndNatureSelected = !chipScienceAndNatureSelected;
                Log.v("CHIP" ,"onChipClick " + selected);
                editor = mPreferences.edit();
                editor.putBoolean(Common.chipScienceAndNatureSelected, selected);
                editor.commit();
            }
        });




        chipPolitics.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(View v, boolean selected) {
                chipPoliticsSelected = !chipPoliticsSelected;
                Log.v("CHIP" ,"onChipClick " + selected);
                editor = mPreferences.edit();
                editor.putBoolean(Common.chipPolitics, selected);
                editor.commit();


            }
        });

        chipPolitics.setOnSelectClickListener(new OnSelectClickListener() {
            @Override
            public void onSelectClick(View v, boolean selected) {
                chipPoliticsSelected = !chipPoliticsSelected;
                Log.v("CHIP" ,"onChipClick " + selected);
                editor = mPreferences.edit();
                editor.putBoolean(Common.chipPolitics, selected);
                editor.commit();
            }
        });






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
                    new LoadFeedDataAsync(MainActivity.this ,foldingCellListAdapter,false, mPreferences).execute();
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
        Log.v("LOADASYNCFEED", "END REACHED CHECK ,"+foldingCellListAdapter.getProductsList().size()+","+mSearchText + ","  +calledOn  + " , " + firstVisibleItem+ " , "+ visibleItemCount +" , " +totalItemCount);
        if ((!mSearchText.trim().equals("")&&foldingCellListAdapter.getProductsList().size() == firstVisibleItem+visibleItemCount)
                ||(calledOn == firstVisibleItem+visibleItemCount && currentScrollState==SCROLL_STATE_IDLE)) {
            Log.v("LOADASYNCFEED", "END REACHED" );
            loadMore();
        }
    }

    private void loadMore() {
        loadFeedDataAsync = new LoadFeedDataAsync(MainActivity.this, foldingCellListAdapter, true, mPreferences);
        loadFeedDataAsync.execute();
        calledOn=calledOn+offset;
        Log.v("LOADASYNCFEED", "END REACHED" );
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

    @Override
    public void onItemClicked(View v, int position) {
        Log.v("READFULL", "" +position);
        switch (v.getId()){
            case R.id.read_full:
                Log.v("READFULL", "read full clicked" );

                break;
            case R.id.source:
                Log.v("READFULL", "source clicked" );
                break;
        }


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
