package com.kepler.news.newsly;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;


import com.geniusforapp.fancydialog.FancyAlertDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import com.kepler.news.newsly.ViewPagerFragments.DemoFragment;
import com.kepler.news.newsly.adapter.FoldingCellItemClickListener;
import com.kepler.news.newsly.adapter.FoldingCellListAdapter;
import com.kepler.news.newsly.chip.Chip;
import com.kepler.news.newsly.helper.Common;
import com.kepler.news.newsly.menu.DrawerAdapter;
import com.kepler.news.newsly.menu.DrawerItem;
import com.kepler.news.newsly.menu.SimpleItem;
import com.kepler.news.newsly.menu.SpaceItem;
import com.kepler.news.newsly.views.CircleRefreshLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ramotion.foldingcell.FoldingCell;
import com.thefinestartist.finestwebview.FinestWebView;
import com.wang.avi.AVLoadingIndicatorView;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity  implements FoldingCellItemClickListener, DrawerAdapter.OnItemSelectedListener , ViewPager.OnPageChangeListener{

    private List<Object> productsList               = null;
    private List<Object> allNewslist                = null;
    private ListView listView                               = null;
    private FoldingCellListAdapter foldingCellListAdapter   = null;
    private LoadFeedDataAsync  loadFeedDataAsync            = null;
    public static  int calledOn = 35;
    private int minEntries = 30;
    private int currentScrollState;
    private String mSearchText = "";
    public int firstVisibleItem, visibleItemCount, totalItemCount;
    private CircleRefreshLayout mCircleRefreshLayout        = null;
    private SearchView mSearchView                          = null;
    private SharedPreferences mPreferences                  = null;

    private HashMap<Integer, String> mMap                                    = null;
    private ArrayList<HashMap<Integer, String>> hashMap     = null;


    public static int start =0;
    public static int offset = 30;

    int currentApiVersion = 7;


    private static final int POS_DASHBOARD = 0;
    private static final int POS_ACCOUNT = 1;
    private static final int POS_MESSAGES = 2;
    private static final int POS_CART = 3;
    private static final int POS_SOURCE = 4;
    private static final int POS_LOGOUT = 6;


    private String[] screenTitles;
    private Drawable[] screenIcons;
    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private FragmentPagerItemAdapter fragmentPagerItemAdapter;
    private FragmentPagerItems pages;


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {

        Log.v("lifecycle" , "onWindowFocusChanged");
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onResume() {

        Log.v("lifecycle" , "onResume");
        super.onResume();
        start =0;
        offset = 30;
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        // Hide both the navigation bar and the status bar.
        // SYSTEM_UI_FLAG_FULLSCREEN is only available on Android 4.1 and higher, but as
        // a general rule, you should design your app to hide the status bar whenever you
        // hide the navigation bar.
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }

        if(!isNetworkAvailable()){
            showNetworkNotAvailableDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/Altair.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
        super.onCreate(savedInstanceState);

        Log.v("lifecycle" , "onCreate");

        mMap = new HashMap();
        mMap.put(R.id.chipScienceAndNature , Common.chipScienceAndNatureSelected);
        mMap.put(R.id.chipPolitics, Common.chipPolitics);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);


        mPreferences = getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE);


        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();


        new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();


        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        int loadImage =  mPreferences.getInt(Common.LOADIMAGE, Common.ALWAYS);
        boolean loadImages;
        if ((mWifi.isConnected() && loadImage == Common.ONWIFI) || (loadImage == Common.ALWAYS)){
            loadImages = true;
            Log.v("MAINWIFISTATE" , "connected");
        }else{
            loadImages = false;
            Log.v("MAINWIFISTATE" , "disconnected");
        }


        pages = new FragmentPagerItems(this);

        LinkedHashMap<String, String> sourceNameMap = Common.createChoosenMap();
        int idx  = 0;
        for (String  sourceName : sourceNameMap.keySet()) {
            //set from
            boolean  checked = mPreferences.getBoolean(sourceName, true);
            if(checked) {
                Bundle bundle = new Bundle();
                bundle.putString(Common.SOURCENAME, sourceName);
                bundle.putBoolean(Common.LOADIMAGE , loadImages);
                pages.add(FragmentPagerItem.of(sourceName, DemoFragment.class, bundle));
                idx = idx + 1;
            }
        }

        fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);



        viewPager.setOffscreenPageLimit(idx);
        viewPager.setAdapter(fragmentPagerItemAdapter);


        viewPagerTab.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(this);


        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_DASHBOARD).setChecked(true),
                createItemFor(POS_ACCOUNT),
                createItemFor(POS_MESSAGES),
                createItemFor(POS_CART),
                createItemFor(POS_SOURCE),
                new SpaceItem(20),
                createItemFor(POS_LOGOUT)));
        adapter.setListener(this);



        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        productsList            = new ArrayList<>();
        allNewslist             = new ArrayList<>();
        //mCircleRefreshLayout    = (CircleRefreshLayout)findViewById(R.id.refresh_layout);
        listView                = (ListView) findViewById(R.id.list1);
        mSearchView             = (SearchView)findViewById(R.id.search_view);

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

        mSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);

            }
        });

//        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//                List<Object> entries = foldingCellListAdapter.AllNewsEntries();
//                foldingCellListAdapter.refreshEntries(entries);
//                mSearchText = "";
//                return false;
//            }
//        });
//
//
//        //TODO com.github.glomadrian.grav.GravView
//
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent search = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(search);
            }
        });
//
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                mSearchText = s;
//                Log.v("QuerySearch" , "onQueryTextSubmit");
//                List<Object> entries = filterNewsBasesOnSearch(foldingCellListAdapter.AllNewsEntries(), s);
//                foldingCellListAdapter.refreshEntries(entries);
//                if(entries.size()<minEntries)
//                {
//                    loadMore();
//                    entries = filterNewsBasesOnSearch(foldingCellListAdapter.AllNewsEntries(), s);
//                }
//                foldingCellListAdapter.refreshEntries(entries);
//                return true;
//            }
//
//
//
//            @Override
//            public boolean onQueryTextChange(String searchText) {
//                Log.v("QuerySearch" , "onQueryTextChange");
////                if(searchText.trim()=="") {
////                    new LoadFeedDataAsync(MainActivity.this ,foldingCellListAdapter,false, mPreferences).execute();
////                }
//                 return true;
//            }
//        });
//
//
//

    }

    private void showNetworkNotAvailableDialog() {


        final FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                .setImageRecourse(R.drawable.happy)
                .setTextTitle("SORRY")
                .setTextSubTitle("Internet is not available.")
                .setBody("Please switch on your internet connection and open app again.")
                .setNegativeColor(R.color.n)
                .setNegativeButtonText("WIFI")
                .setOnNegativeClicked(new FancyAlertDialog.OnNegativeClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        startActivity(new Intent(
                                Settings.ACTION_WIFI_SETTINGS));
                    }



                })
                .setPositiveButtonText("Mobile Network")
                .setPositiveColor(R.color.n)
                .setOnPositiveClicked(new FancyAlertDialog.OnPositiveClicked() {
                    @Override
                    public void OnClick(View view, Dialog dialog) {
                        dialog.dismiss();
                        startActivity(new Intent(
                                Settings.ACTION_DATA_ROAMING_SETTINGS));

                    }
                })
                .setAutoHide(true)
                .build();

        alert.show();

    }




    private List<Object> addNativeExpressAds(List<Object> result){
        for(int i = 0 ; i < result.size(); i +=1)
        {
            if(i%12==0 && i!=0) {
                NativeExpressAdView adView = new NativeExpressAdView(getApplicationContext());
                adView.setAdSize(new AdSize(300, 100));
                adView.setAdUnitId("ca-app-pub-5223778660504166/2968121932");
                adView.loadAd(new AdRequest.Builder().addTestDevice("32C278BA97F2B33C41A02691587B4F29").build());
                result.add(i, adView);
            }

        }

        return result;


    }


    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.transparent))
                .withTextTint(color(R.color.item_color))
                .withSelectedIconTint(color(R.color.black))
                .withSelectedTextTint(color(R.color.black_transparent));
    }


    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    private void isScrollCompleted(int firstVisibleItem, int visibleItemCount , int totalItemCount, int currentScrollState) {
        Log.v("LOADASYNCFEED", "END REACHED CHECK ,"+foldingCellListAdapter.getProductsList().size()+ " , "+calledOn  + " , " + firstVisibleItem+ " , "+ visibleItemCount +" , " +totalItemCount);
        if ((!mSearchText.trim().equals("")&&foldingCellListAdapter.getProductsList().size() == firstVisibleItem+visibleItemCount)
                ||(calledOn == firstVisibleItem+visibleItemCount  && currentScrollState==SCROLL_STATE_IDLE)) {
            Log.v("LOADASYNCFEED", "END REACHED" );
            loadMore();
        }
    }

    private void loadMore() {
//        loadFeedDataAsync = new LoadFeedDataAsync(MainActivity.this, foldingCellListAdapter, true, mPreferences);
//        loadFeedDataAsync.execute();
//        calledOn=foldingCellListAdapter.getProductsList().size();
//        Log.v("LOADASYNCFEED", "END REACHED : " + calledOn );
    }


    private List<Object> filterNewsBasesOnSearch(List<Object> entries, String searchText) {


        NewsStory story = null;
        List<Object> filteredNews = new ArrayList<>();

        for(int i = 0 ; i < entries.size(); i =i+1 ){

            if(i%12 != 0 &&  i != 0) {
                Log.v("FILTER" , " F " +i);
                story = (NewsStory)entries.get(i);
                if(story.getDescription().toLowerCase().contains(searchText.toLowerCase())
                        || story.getTitle().toLowerCase().contains(searchText.toLowerCase())
                        || story.getAuthor().toLowerCase().contains(searchText.toLowerCase())
                        || story.getSourceName().toLowerCase().contains(searchText.toLowerCase()) ) {
                    filteredNews.add(story);
                }

            }

        }

        filteredNews = addNativeExpressAds(filteredNews);

        Log.v("QuerySearch" , "filtersize " + filteredNews.size() + " , " + entries.size());
        LoadFeedDataAsync.oldsize = entries.size();
        return filteredNews;
    }

    @Override
    public void onItemClicked(View v, int position) {
        Log.v("READFULL", "" +position);
        NewsStory story = null;
        switch (v.getId()){
            case R.id.read_full:
                story = (NewsStory) productsList.get(position);
                Log.v("READFULL", "read full clicked "+ story.getUrl() );
                new FinestWebView.Builder(this).show(story.getUrl());
                break;
            case R.id.source:
                story = (NewsStory) productsList.get(position);
                Log.v("READFULL", "read full clicked "+ story.getSourceUrl());
                new FinestWebView.Builder(this).show("http://"+story.getSourceUrl());
                break;
            case R.id.description:
                Log.v("READFULL", "description full clicked");
                foldingCellListAdapter.registerToggle(position);
                ((FoldingCell) v.getParent().getParent()).toggle(false);
                break;


        }


    }

    @Override
    public void onItemSelected(int position) {
        Log.v("onItemSelected", "onItemSelected clicked " +position );
        switch (position){
            case 1:
                Intent category = new Intent(this, Category.class);
                startActivity(category);
                break;
            case 2:
                break;

        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.v("lifecycle", "onPause");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.v("lifecycle", "low memory");

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.v("viewPager" , "onPageScrolled " + position  + " , " + positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        Log.v("viewPager" , "onPageSelected " + position );

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.v("viewPager" , "onPageScrollStateChanged " + state );

    }
}
