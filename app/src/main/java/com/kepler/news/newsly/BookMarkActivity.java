package com.kepler.news.newsly;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.kepler.news.newsly.adapter.FoldingCellItemClickListener;
import com.kepler.news.newsly.adapter.FoldingCellListAdapter;

import java.util.List;

public class BookMarkActivity extends AppCompatActivity implements FoldingCellItemClickListener {


    private ListView bookmark_list;
    private SharedPreferences mPreferences;

    private int main_bg[] = {R.drawable.main_bg,R.drawable.main_bg1,R.drawable.main_bg2,R.drawable.main_bg3,R.drawable.main_bg4, R.drawable.main_bg5  };
    private List<NewsStory> alldbnews;
   // private NewsDatabase newsDatabase;
    private List<Object> productsList               = null;
    private CoordinatorLayout root;
    private Context mContext = null;
    private FoldingCellListAdapter foldingCellListAdapter;
    private BookMarkAdapter bkAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
//        mPreferences = getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE);
//        root = (CoordinatorLayout) findViewById(R.id.bookmark_root);
//        mContext = getApplicationContext();
//
//
//        newsDatabase = NewsDatabase.getDatabase(getApplicationContext());
//        int bgNumber = mPreferences.getInt("count" , 0);
//
//        Drawable backg = getResources().getDrawable(main_bg[bgNumber%main_bg.length]);
//        root.setBackground(backg);
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//
//        productsList = new ArrayList<>();
//        bookmark_list = (ListView)findViewById(R.id.bookmarks_list);
//        alldbnews = newsDatabase.feedModel().getBookMarkedNews(true);
//        for (NewsStory story: alldbnews) {
//            if(story.getBookmark()) {
//                productsList.add(story);
//                //Log.v("BOOKMARACTIVITY"       ,"alldbnews " + story.getBookmark());
//            }
//            Log.v("BOOKMARACTIVITY"       ,"alldbnews " + story.getBookmark());
//        }
//
//
//
//
//        bkAdapter = new BookMarkAdapter(this, mContext, productsList, alldbnews, true, "Book Marks");
//        //final ArrayAdapter adapter = new ArrayAdapter(this, R.layout.rearr_list_item, productsList);
//        //foldingCellListAdapter  = new FoldingCellListAdapter(this, mContext, productsList, alldbnews, true, "Book Marks");
//
//        bookmark_list.setAdapter(bkAdapter);
//        bookmark_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                // toggle clicked cell state
//
//                ((FoldingCell) view).toggle(false);
//                // register in adapter that state for selected cell is toggled
//                bkAdapter.registerToggle(pos);
//            }
//        });
//





    }


    @Override
    public void onItemClicked(View v, int position) {
        Log.v("READFULL", "" +position);
//        NewsStory story = null;
//        switch (v.getId()){
//            case R.id.read_full:
//                story = (NewsStory) productsList.get(position);
//                Log.v("READFULL", "read full clicked "+ story.getUrl() );
//                new FinestWebView.Builder(mContext)
//                        .showMenuShareVia(true)
//                        .show(story.getUrl());
//                break;
//            case R.id.source:
//                story = (NewsStory) productsList.get(position);
//                Log.v("SOURCEURL", "read full clicked "+ story.getSourceurl());
//                new FinestWebView.Builder(mContext)
//                        .showMenuShareVia(true)
//                        .show(story.getSourceurl());
//                break;
//            case R.id.description:
//            case R.id.title_back:
//                Log.v("READFULL", "description full clicked");
//                bkAdapter.registerToggle(position);
//                ((FoldingCell) v.getParent().getParent().getParent()).toggle(false);
//                break;
//            case R.id.cell_content_view1:
//                Log.v("READFULL", "cell_content_view1 full clicked");
//                bkAdapter.registerToggle(position);
//                ((FoldingCell)v.getParent()).toggle(false);
//                break;
//
//            case R.id.main_bar:
//                Log.v("READFULL", "main_bar full clicked");
//                bkAdapter.registerToggle(position);
//                ((FoldingCell)v.getParent().getParent()).toggle(false);
//                break;
//
//
//        }
    }
}
