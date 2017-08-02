package com.kepler.news.newsly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kepler.news.newsly.adapter.RearrangeItemAdapter;
import com.kepler.news.newsly.databaseHelper.AppDatabase;
import com.kepler.news.newsly.databaseHelper.Feed;
import com.kepler.news.newsly.helper.DynamicListView;
import com.kepler.news.newsly.helper.MySwipeRefreshLayout;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class Rearrange extends AppCompatActivity {
    private Bitmap mFloatBitmap;

    private ImageView mImageView;
    private DragListView mDragListView = null;
    private AppDatabase database  = null;
    private List<Feed> feeds  =null;
   // private MySwipeRefreshLayout mRefreshLayout;
    private DragSortListView dragSortListView;
    private DragSortListView.DropListener onDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rearrange);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //mRefreshLayout = (MySwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        setSupportActionBar(toolbar);
        database = AppDatabase.getDatabase(getApplicationContext());
        feeds = database.feedModel().getPriorityFeeds();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        final HashMap<String, Integer> map = new HashMap<>();




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final LinkedList<String> mItemArray;
        mItemArray = new LinkedList<>();
        for (int i = 0; i < feeds.size(); i++) {
            map.put(feeds.get(i).newsSource, feeds.get(i).priority);
            if (feeds.get(i).subscribed) {
                mItemArray.add(feeds.get(i).newsSource);
            }
        }
        dragSortListView = (DragSortListView)findViewById(R.id.dragsortlist);

        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.rearr_list_item, mItemArray);
        dragSortListView.setAdapter(adapter);
        dragSortListView.setFloatViewManager(new DragSortListView.FloatViewManager() {
            @Override
            public View onCreateFloatView(int position) {
                // Guaranteed that this will not be null? I think so. Nope, got
                // a NullPointerException once...
                View v = dragSortListView.getChildAt(position + dragSortListView.getHeaderViewsCount() - dragSortListView.getFirstVisiblePosition());

                if (v == null) {
                    return null;
                }

                v.setPressed(false);

                // Create a copy of the drawing cache so that it does not get
                // recycled by the framework when the list tries to clean up memory
                //v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                v.setDrawingCacheEnabled(true);

                mFloatBitmap = Bitmap.createBitmap(v.getDrawingCache());
                v.setDrawingCacheEnabled(false);

                if (mImageView == null) {
                    mImageView = new ImageView(dragSortListView.getContext());
                }
                mImageView.setBackgroundResource(R.drawable.drag_item);
                //mImageView.setBackgroundColor(Color.parseColor("#afd32f2f"));
                mImageView.setPadding(0, 0, 0, 0);
                mImageView.setImageBitmap(mFloatBitmap);
                mImageView.setLayoutParams(new ViewGroup.LayoutParams(v.getWidth(), v.getHeight()));

                return mImageView;
            }

            @Override
            public void onDragFloatView(View floatView, Point location, Point touch) {

            }

            @Override
            public void onDestroyFloatView(View floatView) {
                ((ImageView) floatView).setImageDrawable(null);

                mFloatBitmap.recycle();
                mFloatBitmap = null;
            }
        });

        onDrop = new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                String item = (String) adapter.getItem(from);
                Log.v("DropListener" , "from " + from + "  to " + to);
                Log.v("DropListener" , "" + database.feedModel().getSingleFeed(item).newsSource + " , " + database.feedModel().getSingleFeed(item).priority);
                if (from != to) {



                    adapter.remove(item);
                    adapter.insert(item, to);


                    int movement = Math.abs(from-to);
                    int moveDirection = from-to;
                    Log.v("DropListener" , "movement "  + movement + " , " + moveDirection);

                    for(int idx = 0 ; idx < adapter.getCount() ; idx ++)
                    {
                        Feed  feed = new Feed((String) adapter.getItem(idx), true, idx);
                        database.feedModel().updateTask(feed);
                    }

                }

            }
        };
        dragSortListView.setDropListener(onDrop);


    }



    public DragSortController buildController(DragSortListView dslv) {
        // defaults are
        //   dragStartMode = onDown
        //   removeMode = flingRight
        DragSortController controller = new DragSortController(dslv);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_LONG_PRESS);
        controller.setBackgroundColor(Color.parseColor("afd32f2f"));
        return controller;
    }

}
