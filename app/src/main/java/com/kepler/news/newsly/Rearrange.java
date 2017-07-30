package com.kepler.news.newsly;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kepler.news.newsly.databaseHelper.AppDatabase;
import com.kepler.news.newsly.databaseHelper.Feed;
import com.woxthebox.draglistview.DragItemAdapter;
import com.woxthebox.draglistview.DragListView;

import java.util.List;

public class Rearrange extends AppCompatActivity {

    private DragListView mDragListView = null;
    private AppDatabase database  = null;
    private List<Feed> feeds  =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rearrange);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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


        mDragListView = (DragListView)findViewById(R.id.dragsortlist);
//        DragItemAdapter<   >
//
//        ItemAdapter listAdapter = new ItemAdapter(mItemArray, R.layout.list_item, R.id.image, false);
//        mDragListView.setAdapter(listAdapter);
//        mDragListView.setCanDragHorizontally(false)
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
