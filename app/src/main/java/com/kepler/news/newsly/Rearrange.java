package com.kepler.news.newsly;

import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kepler.news.newsly.adapter.RearrangeItemAdapter;
import com.kepler.news.newsly.databaseHelper.AppDatabase;
import com.kepler.news.newsly.databaseHelper.Feed;
import com.kepler.news.newsly.helper.DynamicListView;
import com.kepler.news.newsly.helper.MySwipeRefreshLayout;
import com.mobeta.android.dslv.DragSortListView;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;
import com.woxthebox.draglistview.swipe.ListSwipeHelper;
import com.woxthebox.draglistview.swipe.ListSwipeItem;

import java.util.ArrayList;
import java.util.List;

public class Rearrange extends AppCompatActivity {

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
        feeds = database.feedModel().getAllFeeds();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final ArrayList<String> mItemArray;
        mItemArray = new ArrayList<>();
        for (int i = 0; i < feeds.size(); i++) {
            mItemArray.add(feeds.get(i).newsSource );
        }
        dragSortListView = (DragSortListView)findViewById(R.id.dragsortlist);

        final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.rearr_list_item, mItemArray);
        dragSortListView.setAdapter(adapter);


        onDrop = new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                Log.v("DropListener" , "from " + from + "  to " + to);
                if (from != to) {
                    String item = (String) adapter.getItem(from);
                    adapter.remove(item);
                    adapter.insert(item, to);
                }

            }
        };
        dragSortListView.setDropListener(onDrop);
        //dragSortListView.setOnD

//        mDragListView = (DragListView)findViewById(R.id.dragsortlist);
//        mDragListView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        RearrangeItemAdapter RearrangeItemAdapter = new RearrangeItemAdapter(mItemArray, R.layout.rearr_list_item, R.id.image, true);
//        mDragListView.setAdapter(RearrangeItemAdapter, true);
//        mDragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
//
//
//            @Override
//            public void onItemDragging(int itemPosition, float x, float y) {
//                super.onItemDragging(itemPosition, x, y);
//                Log.v("onItemDragging", "itemPosition : " + itemPosition + " , " + x + " , " + y);
//
//            }
//
//            @Override
//            public void onItemDragStarted(int position) {
//                mRefreshLayout.setEnabled(false);
//                Toast.makeText(mDragListView.getContext(), "Start - position: " + position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onItemDragEnded(int fromPosition, int toPosition) {
//                mRefreshLayout.setEnabled(true);
//                if (fromPosition != toPosition) {
//                    Toast.makeText(mDragListView.getContext(), "End - position: " + toPosition, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//
//
//
//
//
//        mRefreshLayout.setScrollingView(mDragListView.getRecyclerView());
//        mRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getApplicationContext(), R.color.b));
//        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mRefreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mRefreshLayout.setRefreshing(false);
//                    }
//                }, 2000);
//            }
//        });
//
//        mDragListView.setSwipeListener(new ListSwipeHelper.OnSwipeListenerAdapter() {
//            @Override
//            public void onItemSwipeStarted(ListSwipeItem item) {
//                mRefreshLayout.setEnabled(false);
//            }
//
//            @Override
//            public void onItemSwipeEnded(ListSwipeItem item, ListSwipeItem.SwipeDirection swipedDirection) {
//                mRefreshLayout.setEnabled(true);
//
//                // Swipe to delete on left
//                if (swipedDirection == ListSwipeItem.SwipeDirection.LEFT) {
//                    Pair<Long, String> adapterItem = (Pair<Long, String>) item.getTag();
//                    int pos = mDragListView.getAdapter().getPositionForItem(adapterItem);
//                    mDragListView.getAdapter().removeItem(pos);
//                }
//            }
//        });


    }

    private void rearrange(int from, int to) {
        onDrop.drop(from, to);
    }


    public class RearrangeAdapter extends BaseAdapter{

        private List<Feed> feeds = null;
        private Context mComtext = null;
        private LayoutInflater mLayoutInflator = null;

        public RearrangeAdapter(List<Feed> feeds, Context mContext)
        {
            this.feeds = feeds;
            this.mComtext = mContext;
            mLayoutInflator = LayoutInflater.from(mContext);
        }

        public RearrangeAdapter(Object o, int rearrange_list_item, int image, boolean b) {
        }


        @Override
        public int getCount() {
            return feeds.size();
        }

        @Override
        public Object getItem(int i) {
            return feeds.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = mLayoutInflator.inflate(R.layout.rearrange_list_item,null );
            TextView textView = (TextView)view.findViewById(R.id.text1);
            textView.setText(feeds.get(i).newsSource);


            return view;
        }
    }

}
