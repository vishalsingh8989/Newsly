package com.kepler.news.newsly;

import android.content.Context;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import android.widget.ListView;



import com.ramotion.foldingcell.FoldingCell;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;


import java.util.ArrayList;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity {

    private ArrayList<NewsStory> productsList = null;
    private ListView listView                               = null;
    private NewsAdapter newsAdapter                         = null;
    private FoldingCellListAdapter foldingCellListAdapter   = null;
    private LoadFeedDataAsync  loadFeedDataAsync            = null;
    private int currentScrollState;
    public int firstVisibleItem, visibleItemCount, totalItemCount;


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
                .withMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();




        productsList            = new ArrayList<>();

        listView                = (ListView)findViewById(R.id.list1);
        foldingCellListAdapter  = new FoldingCellListAdapter(this, productsList);


        listView.setAdapter(foldingCellListAdapter);
        loadFeedDataAsync = new LoadFeedDataAsync(foldingCellListAdapter);

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

                Log.v("ONSCROLL", " " + absListView.getFirstVisiblePosition() + " , " + absListView.getChildCount() + " , "+absListView.getLastVisiblePosition());
                isScrollCompleted(absListView.getLastVisiblePosition(), currentScrollState);


            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //Log.v("ONSCROLL", "" + firstVisibleItem + " , " + visibleItemCount + " , " + totalItemCount);



            }
        });



    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void isScrollCompleted(int firstVisibleItem, int currentScrollState) {
        if ( firstVisibleItem==0&& currentScrollState==SCROLL_STATE_IDLE) {
            //Log.v("LOADASYNCFEED", "END REACHED" );
            loadFeedDataAsync = new LoadFeedDataAsync(foldingCellListAdapter);
            loadFeedDataAsync.execute();
        }
    }
}
