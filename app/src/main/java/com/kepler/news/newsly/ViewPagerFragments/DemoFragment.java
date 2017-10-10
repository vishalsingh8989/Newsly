package com.kepler.news.newsly.ViewPagerFragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;

/**
 * Created by vishaljasrotia on 16/06/17.
 */

public class DemoFragment extends Fragment implements FoldingCellItemClickListener {


    public static final String NEWSSOURCE = "NEWSSOURCE";
    private static final int SHARE_CALL = 10;
    private int currentScrollState;
    private String mSearchText = "";
    public int firstVisibleItem, visibleItemCount, totalItemCount;

    private static final int MODE_PRIVATE = 0;
    private List<Object> productsList               = null;
    private List<Object> allNewslist                = null;
    private ListView listView                               = null;
    private FoldingCellListAdapter foldingCellListAdapter   = null;
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
        productsList            = new ArrayList<>();

        if (!sourceName.contains(Common.BOOKMARKS)) {
            alldbnews = newsDatabase.feedModel().getSourceNews(sourceName);
        }else{
            alldbnews = newsDatabase.feedModel().getBookMarkedNews(true);
        }


        Log.v(NEWSSOURCE ,"***********************************");
        Log.v(NEWSSOURCE , sourceName + " : " + alldbnews.size());




        //productsList            = addNativeExpressAds(productsList);
//        Log.v(NEWSSOURCE       ,"ALL OBJS SIZE AF AD : " + productsList.size());
//        Log.v(NEWSSOURCE       ,"SOURCE : " +sourceName);
//        Log.v(NEWSSOURCE       ,"ALL NEWS SIZE : " + alldbnews.size());
//        Log.v(NEWSSOURCE       ,"***********************************");
        startMap                = new LinkedHashMap<>();



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
//        loadFeedDataAsync = new LoadFeedDataAsync(this , mContext, foldingCellListAdapter, true, getActivity().getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE), sourceName, startMap, newsDatabase);
//
//        //loadFeedDataAsync.execute();
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
                Log.v("SOURCEURL", "source full clicked "+ story.getSourceUrl());
                new FinestWebView.Builder(getActivity())
                        .showMenuShareVia(true)
                        .show(story.getSourceUrl());
                break;
            case R.id.description:
            case R.id.title_back:
                Log.v("READFULL", "description full clicked");
                foldingCellListAdapter.registerToggle(position);
                ((FoldingCell) v.getParent().getParent().getParent().getParent()).toggle(false);
                break;
            case R.id.text_data_layout:
                Log.v("READFULL", "text_data_layout full clicked");
                foldingCellListAdapter.registerToggle(position);
                ((FoldingCell) v.getParent().getParent().getParent()).toggle(false);
                break;
            case R.id.main_bar:
                Log.v("READFULL", "main_bar full clicked");
                foldingCellListAdapter.registerToggle(position);
                ((FoldingCell) v.getParent().getParent()).toggle(false);
                break;






        }



    }



    private void askforPermissions(final String[] PERMISSIONS, int calledby) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.v("PERMISSOINCHECK", "askforPermissions");
            ActivityCompat.requestPermissions(getActivity(),PERMISSIONS, calledby);

        }
    }

    private boolean hasPermissions(Context mContext, String[] permissions) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && mContext != null && permissions != null) {
            for (String permission : permissions) {
                if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void  onShareClicked(View v, int position, String link){

//        final String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//        };
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (!hasPermissions(getActivity(), PERMISSIONS)) {
//                Log.v("PERMISSOINCHECK", "permission missing");
//
//                askforPermissions(PERMISSIONS, SHARE_CALL);
//
//            } else {
//                Log.v("PERMISSOINCHECK", "permission already granted");
//                //invokeCamera();
//                //startActivity(new Intent(this, CameraDemoActivity.class));
//
//            }
//        }else{
//            Log.v("PERMISSOINCHECK", "sdk less tha  M");
//            //startActivity(new Intent(this, CameraDemoActivity.class));
//            //invokeCamera();
//        }




        switch (v.getId()){
            case R.id.text_data_layout:
                Log.v("PERMISSOINCHECK", "text_data_layout clicked");
                Intent i = new Intent(Intent.ACTION_SEND);

                i.setType("image/*");
                i.putExtra(Intent.EXTRA_TEXT, "Download Newsly : \nSource :" +  link);

                i.putExtra(Intent.EXTRA_STREAM, getImageUri(mContext, getBitmapFromView(v)));
                try {
                    startActivity(Intent.createChooser(i, "My Profile.."));
                } catch (android.content.ActivityNotFoundException ex) {

                    ex.printStackTrace();
                }


                break;


        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("PERMISSOINCHECK",  "onRequestPermissionsResult");

        if (requestCode == SHARE_CALL) {
            Log.v("PERMISSOINCHECK",  "onRequestPermissionsResult called by share");
        }
    }
    public Bitmap getBitmapFromView(View v) {
        //Define a bitmap with the same size as the view
        v.setDrawingCacheEnabled(true);
        Bitmap returnedBitmap = v.getDrawingCache();
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = v.getBackground();
        Paint myPaint = new Paint();
        myPaint.setStrokeWidth(3);
        Bitmap backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.main_bg4);
        backgroundImage = Bitmap.createScaledBitmap(backgroundImage, v.getWidth(), backgroundImage.getHeight()/4, false);
        backgroundImage = Bitmap.createBitmap(backgroundImage, 0,0,v.getWidth(), (int)(v.getHeight()*1.5));
        if (bgDrawable != null){
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);}
        else {
            //does not have background drawable, then draw white background on the canvas

            canvas.drawBitmap(backgroundImage, 0, 0, myPaint);
        }
        // draw the view on the canvas
        v.draw(canvas);
        //return the bitmap
        return returnedBitmap;
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
            if(i%Common.ADINTERVAL==0 && i!=0) {
                NativeExpressAdView adView = new NativeExpressAdView(mContext);
                adView.setAdSize(new AdSize(300, 100));

                if(i%Common.ADINTERVAL*2 ==0&&i!=0) {
                    adView.setAdUnitId("ca-app-pub-5223778660504166/2968121932");
                }else{
                    adView.setAdUnitId("ca-app-pub-5223778660504166/4542327317");
                }

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
        return super.onOptionsItemSelected(item);


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "title", null);
        return Uri.parse(path);
    }



}