package com.kepler.news.newsly;

import android.*;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.LocaleList;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import android.widget.ListView;


import com.geniusforapp.fancydialog.FancyAlertDialog;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kepler.news.newsly.ViewPagerFragments.DemoFragment;

import com.kepler.news.newsly.databaseHelper.NewsDatabase;
import com.kepler.news.newsly.databaseHelper.NewsSourceDatabase;
import com.kepler.news.newsly.databaseHelper.NewsSource;
import com.kepler.news.newsly.adapter.FoldingCellItemClickListener;
import com.kepler.news.newsly.adapter.FoldingCellListAdapter;
import com.kepler.news.newsly.helper.Common;
import com.kepler.news.newsly.updateUtils.UpdateDBservice;
import com.kepler.news.newsly.views.CircleRefreshLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.ramotion.foldingcell.FoldingCell;
import com.thefinestartist.finestwebview.FinestWebView;




import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

public class MainActivity extends AppCompatActivity  implements FoldingCellItemClickListener, ViewPager.OnPageChangeListener, NavigationView.OnNavigationItemSelectedListener , BookMarkedFragement.OnFragmentInteractionListener{

    private List<Object> productsList               = null;
    private ListView listView                               = null;
    private FoldingCellListAdapter foldingCellListAdapter   = null;

    public static  int calledOn = 35;
    private int currentScrollState;
    private String mSearchText = "";
    public int firstVisibleItem, visibleItemCount, totalItemCount;
    private SharedPreferences mPreferences                  = null;

    public static int start =0;
    public static int offset = 30;

    int currentApiVersion = 7;

    private ViewPager viewPager;
    private SmartTabLayout viewPagerTab;
    private FragmentPagerItemAdapter fragmentPagerItemAdapter;
    private FragmentPagerItems pages;
    private DrawerLayout drawer;
    private NewsSourceDatabase database = null;
    private List<NewsSource> newsSources;
    private NavigationView navigationView;
    private int main_bg[] = {R.drawable.main_bg,R.drawable.main_bg1,R.drawable.main_bg2,R.drawable.main_bg3,R.drawable.main_bg4, R.drawable.main_bg5  };
    private FragmentPagerItem item;
    public int loadImage;
    private boolean loadImages;
    private String device_id;
    private Context mContext;
    private Object mDatabase;
    private FirebaseAnalytics mFirebaseAnalytics;


    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
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
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoCondensed-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        Calendar cal = null;
        cal = Calendar.getInstance();

        Intent intent1= new Intent(getApplicationContext(), UpdateDBservice.class);
        PendingIntent pintent = PendingIntent.getService(getApplicationContext(), 0, intent1, 0);
        AlarmManager alarm = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);


        //5 seconds
        alarm.cancel(pintent);
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), Common.UPDATEINTERVAL, pintent);

        //firebase start



        Log.v("FIREBASE" , "device_id : " + device_id + " time : " + System.currentTimeMillis() / 1000L);

        //FirebaseApp mFirebaseApp = FirebaseApp.initializeApp(getApplicationContext());

        //"https://newsly-7354b.firebaseio.com/"

        LoadData loadData = new LoadData(mContext);
        //mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = mFirebaseDatabase.getReference("message");


//        mFirebaseDatabase = FirebaseDatabase.getInstance("https://newsly-7354b.firebaseio.com/");
//
//        mFirebaseDatabase.setPersistenceEnabled(true);
//
//        DatabaseReference myRef = mFirebaseDatabase.getReference("Newsly");
//        myRef.child(device_id).child("LastUsed").setValue(System.currentTimeMillis() / 1000L);
//        //myRef.child(device_id).child("Model").setValue(model);
//        //myRef.child(device_id).child("ScreenRatio").setValue(MainActivity.dpHeight / MainActivity.dpWidth);
//        //myRef.child(device_id).child("ScreenSize").setValue(MainActivity.dpHeight + "X" + MainActivity.dpWidth);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            LocaleList locales = mContext.getResources().getConfiguration().getLocales();
//            myRef.child(device_id).child("Country").setValue(locales.toString());
//            Log.v("countrylocal1", " : " + locales.get(1));
//        } else {
//            String country = mContext.getResources().getConfiguration().locale.getDisplayCountry();
//            myRef.child(device_id).child("Country").setValue(country);
//            Log.v("countrylocal2", " : " + country);
//        }
//
//        myRef.child(device_id).child("Version").setValue(Build.VERSION.SDK_INT);
        //myRef.child(device_id).child("DeviceType").setValue(isTablet(mContext));
        //myRef.child(device_id).child("AppVersion").setValue(getAppVersion());


        //firebase end

        Log.v("lifecycle" , "onCreate");
        database = NewsSourceDatabase.getDatabase(getApplicationContext());
        newsSources = database.feedModel().getPriorityFeeds();


        setContentView(R.layout.app_drawer_layout);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    10);
            Log.e("PERMISSOINCHECK", "PERMISSION NOT GRANTED");
        } else {
            Log.e("PERMISSOINCHECK", "PERMISSION GRANTED");
        }
        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        String banner_id = "ca-app-pub-5223778660504166/7368731783";
        MobileAds.initialize(this, banner_id);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);

        mPreferences = getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        int bgNumber = mPreferences.getInt("count" , 0);

        Drawable backg = getResources().getDrawable(main_bg[bgNumber%main_bg.length]);
        drawer.setBackground(backg);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt("count", ++bgNumber);
        editor.commit();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.dummy_content, R.string.accept);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //NavigationView navigationViewRight = (NavigationView) findViewById(R.id.nav_view_right);
        //navigationViewRight.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.home);






        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        loadImage =  mPreferences.getInt(Common.LOADIMAGE, Common.ALWAYS);
        if ((mWifi.isConnected() && loadImage == Common.ONWIFI) || (loadImage == Common.ALWAYS)){
            loadImages = true;
            Log.v("MAINWIFISTATE" , "connected");
        }else{
            loadImages = false;
            Log.v("MAINWIFISTATE" , "disconnected");
        }


        pages = new FragmentPagerItems(this);

        Bundle bundle;


        int idx  = 0;
        Log.v("NEWSSOURCE", "size : "+ newsSources.size());
        for (NewsSource newsSourceObj : newsSources) {
            Log.v("NEWSSOURCE", "" + newsSourceObj.newsSource +" , "+ newsSourceObj.subscribed);
            ///boolean  checked = mPreferences.getBoolean(sourceName, false);
            if(newsSourceObj.subscribed) {
                bundle = new Bundle();
                bundle.putString(Common.SOURCENAME, newsSourceObj.newsSource);
                bundle.putBoolean(Common.LOADIMAGE , loadImages);
                item = FragmentPagerItem.of(newsSourceObj.newsSource, DemoFragment.class, bundle);
                pages.add(item);
                idx = idx + 1;
            }
        }

//        bundle.putString(Common.SOURCENAME, Common.BOOKMARKS);
//        bundle.putBoolean(Common.LOADIMAGE , loadImages);
//        item = FragmentPagerItem.of(Common.BOOKMARKS, DemoFragment.class, bundle);
//        pages.add(item);

        fragmentPagerItemAdapter = new FragmentPagerItemAdapter(getSupportFragmentManager(), pages);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setAdapter(fragmentPagerItemAdapter);

        viewPagerTab.setViewPager(viewPager);

        viewPager.addOnPageChangeListener(this);

        productsList            = new ArrayList<>();
        listView                = (ListView) findViewById(R.id.list1);


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

        //viewPager.setCurrentItem(1);





    }

    private void showNetworkNotAvailableDialog() {


        final FancyAlertDialog.Builder alert = new FancyAlertDialog.Builder(MainActivity.this)
                //.setImageRecourse(R.drawable.happy) // not working on small resolution
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





    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

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

        FragmentPagerItem page = pages.get(position);




    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.v("viewPager" , "onPageScrollStateChanged " + state );

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        Log.v("navigation", " " + menuItem.getTitle());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        int id = menuItem.getItemId();

        Log.v("onOptionsItemSelected", "test");
        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {

            SharedPreferences.Editor editor = mPreferences.edit();

            editor.putBoolean(Common.FIRSTLAUNCH, true);
            editor.commit();

            drawer.closeDrawers();
            Intent intro = new Intent(this, IntroActivity.class);
            startActivity(intro);
            return true;
        } else if (id == R.id.rate_me) {
            drawer.closeDrawers();


            Uri uri = Uri.parse("market://details?id=" + getBaseContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName())));
            }
            Log.v("tst", "test");
        } else if (id == R.id.more_apps) {


            //only for debugging

            //NewsDatabase.getDatabase(getApplicationContext()).feedModel().delete("23-8-2017");


            ////original intent
            Uri uri = Uri.parse("https://play.google.com/store/apps/developer?id=Kepler&hl=en");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName())));
            }

        } else if (id == R.id.rearrange) {
            Intent rearrange = new Intent(MainActivity.this, Rearrange.class);
            startActivity(rearrange);

        } else if (id == R.id.bookmarks) {

            Log.v("navigation", "bOOK MARKS ");

            //pages1 = new FragmentPagerItems(this);



           Intent bookmark = new Intent(getApplicationContext(), BookMarkActivity.class);
            startActivity(bookmark);




        }
        return true;
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.v("DemoFrag" , "main : " + id);
        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            if (drawer.isDrawerOpen(GravityCompat.END)) {
                drawer.closeDrawer(GravityCompat.END);
            } else {
                drawer.openDrawer(GravityCompat.END);
            }

            Intent intro = new Intent(this, IntroActivity.class);
            startActivity(intro);
            return true;
        }
        else if(id ==R.id.rate_me)
        {
            Log.v("tst" , "test");
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("PERMISSOINCHECK",  "onRequestPermissionsResult in parent");

    }


}




