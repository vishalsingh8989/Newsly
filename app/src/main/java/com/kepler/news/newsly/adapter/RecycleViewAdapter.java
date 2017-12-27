package com.kepler.news.newsly.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.bumptech.glide.Glide;
import com.kepler.news.newsly.MainActivity;
import com.kepler.news.newsly.NewsStory;
import com.kepler.news.newsly.R;
import com.kepler.news.newsly.ViewPagerFragments.DemoFragment;
import com.kepler.news.newsly.databaseHelper.News;
import com.kepler.news.newsly.databaseHelper.NewsDatabase;
import com.kepler.news.newsly.helper.RoundedTransformation;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

/**
 * Created by vishaljasrotia on 06/06/17.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    private List<Object> productsList = null;
    private List<News> newslist = null;
    private static String DESCRIPTION        = "description";
    private static String SOURCE             = "source";
    private static String  SOURCENAME        = "sourceName";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RoundedTransformation transformation = null;
    private DemoFragment Callback                = null;
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int AD_VIEW_TYPE        = 1;
    public static int test = 10;
    private NewsDatabase database = null;
    private Typeface custom_font1;
    private Typeface custom_font2;
    private String Category;
    




    private int[] mycolors = {
            R.color.n,R.color.d,R.color.e,R.color.q,
            R.color.b,R.color.f,R.color.g,R.color.h,
            R.color.o,R.color.i,R.color.c,R.color.j,R.color.k,
            R.color.b, R.color.l,R.color.m,R.color.p,
    };



    public RecycleViewAdapter(DemoFragment Callback, Context context, List<News> newslist, String category) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.transformation = new RoundedTransformation(15, 0);
        this.Callback = Callback;


        this.newslist = newslist;
        this.Category = category;
        Log.v("RecycleViewAdapter" , "COUNT : " + newslist.size());
        custom_font1 = Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        custom_font2 = Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoCondensed-Regular.ttf");




    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("RecycleViewAdapter" , "onCreateViewHolder : ");
//            switch (viewType){
//                case AD_VIEW_TYPE:
//                    //View nativeExpressAdView = LayoutInflater.from(mContext).inflate(R.layout.native_ad_adapter, parent, false);
//                    return  null;//new NativeExpressAdViewHolder(nativeExpressAdView);
//                case MENU_ITEM_VIEW_TYPE:
//                    Log.v("RecycleViewAdapter" , "MENU_ITEM_VIEW_TYPE : ");
//                    LinearLayout list_item = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.fragment_item, parent, false);
//                    return new NewsItemViewHolder(list_item);
//            }
        RelativeLayout list_item = (RelativeLayout)LayoutInflater.from(mContext).inflate(R.layout.fragment_item, parent, false);
        return new NewsItemViewHolder(list_item);

    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);

        Log.v("RecycleViewAdapter",  "position :"+position+" View Type : " +viewType +  "  " + holder);

        switch(viewType){
            case AD_VIEW_TYPE:
//                NativeExpressAdViewHolder nativeExpressAdViewHolder = (NativeExpressAdViewHolder)holder;
//                NativeExpressAdView adView  = (NativeExpressAdView)productsList.get(position);
//                ViewGroup adCardView = (ViewGroup)nativeExpressAdViewHolder.itemView;
//                adCardView.removeAllViews();
//                if(adView.getParent()!=null)
//                {
//                    ((ViewGroup)adView.getParent()).removeView(adView);
//
//                }
//
//                adCardView.addView(adView);
                break;
            case MENU_ITEM_VIEW_TYPE:

                    final NewsItemViewHolder newsItemViewHolder = (NewsItemViewHolder)holder;
                    News story = newslist.get(position);


                    long t = Long.parseLong(story.publish_date)*1000;

                    Date date = new Date(t);

                    DateFormat format = new SimpleDateFormat("dd MMM, yyyy hh:mm a");
                    format.setTimeZone(TimeZone.getTimeZone("Etc/UTC"));
                    String formatted = format.format(date);
                    System.out.println(formatted);

                    newsItemViewHolder.title.setText(story.title.replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));
                    newsItemViewHolder.summary.setText(story.summary.trim().replaceAll(" +", " ").replace("\n", "").replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));
                    newsItemViewHolder.source_name.setText(story.source_name);
                    newsItemViewHolder.author.setText(story.authors);
                    newsItemViewHolder.publish_date.setText(formatted);


//                format.setTimeZone(TimeZone.getTimeZone("Australia/Sydney"));
//                formatted = format.format(date);
//                System.out.println(formatted);
                    Log.v("NEWSDATA", "******************RecyclerView***********************");
                    Log.v("NEWSDATA", Category + " TITLE :" + story.title);
                    Log.v("NEWSDATA", Category + " SUMMARY :" + story.summary);
                    Log.v("NEWSDATA", Category + " META FAVICON :" + story.meta_favicon);
                    Log.v("NEWSDATA", Category + " PUBLISH DATE :" + formatted);
                    Log.v("NEWSDATA", Category + " TOPIMAGE :" + story.top_image);
                    Log.v("NEWSDATA", Category + " t :" + t);

                    Log.v("NEWSDATA", "*************************************************");

//                    newsItemViewHolder.sourceMini.setText(" " + position);
//                    newsItemViewHolder.source.setText(story.getSourceName());
//                    newsItemViewHolder.publishedat.setText(story.getPublishedat());
//                    //newsItemViewHolder.author.setText(story.getAuthor());
//                    //newsItemViewHolder.category.setText(story.getCategory());



//                    newsItemViewHolder.parent.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Callback.onItemClicked(newsItemViewHolder.parent, position);
//                        }
//                    });
//
//                    newsItemViewHolder.title.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            Callback.onItemClicked(newsItemViewHolder.parent, position);
//                        }
//                    });
//
//                    newsItemViewHolder.description.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//
//                            Callback.onItemClicked(newsItemViewHolder.parent, position);
//                        }
//                    });



                    try {

                        Picasso.with(mContext)
                                .load(story.top_image)
                                .error(R.drawable.sample)

                                .into(newsItemViewHolder.top_image);
                    } catch (Exception e) {
                        Log.v("GLIDELOG" , "ERROR : " + e.toString());
                    }

                try {

                    Picasso.with(mContext)
                            .load(story.meta_favicon.replace("\"",""))
                            .error(R.drawable.sample)
                            //.transform(new RoundedTransformation(5, 2))
                            .into(newsItemViewHolder.meta_favicon);
                } catch (Exception e) {
                    Log.v("GLIDELOG" , "ERROR : " + e.toString());
                }



        }


    }


    AdapterView.OnItemClickListener  mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Callback.onItemClicked(parent, view, position, id);
        }
    };

    @Override
    public int getItemCount() {
        return newslist.size();
    }



    @Override
    public int getItemViewType(int position) {
        //return (position % 8 == 0  )?AD_VIEW_TYPE:
          return      MENU_ITEM_VIEW_TYPE;
    }

    public void upDateEntries(List<News> entries, boolean onRefresh) {
        //productsList.addAll(entries);
        newslist.addAll(entries);
        Log.v("LOADASYNCFEED" , "size : " +productsList.size());
        this.notifyDataSetChanged();


    }


    public class NewsItemViewHolder extends RecyclerView.ViewHolder{


        FoldingCell parent;
        LinearLayout side_bar;
        LinearLayout side_bar1;
        TextView summary;
        TextView title;
        TextView author;
        TextView source_name;
        TextView sourceMini;
        TextView category;
        TextView readFull;
        TextView publish_date;
        //NativeExpressAdView adView;
        ImageView top_image;
        ImageView meta_favicon;
        
        NewsItemViewHolder(RelativeLayout cell) {
            super(cell);

//            parent = (FoldingCell)cell.findViewById(R.id.folding_cell);
            summary = (TextView) cell.findViewById(R.id.summary);
//            //author = (TextView) cell.findViewById(R.id.author);
            title = (TextView) cell.findViewById(R.id.title);
            top_image = (ImageView) cell.findViewById(R.id.top_image);
            meta_favicon = cell.findViewById(R.id.meta_favicon);
            author = cell.findViewById(R.id.authors);
            source_name = cell.findViewById(R.id.source_name);
            publish_date = cell.findViewById(R.id.publish_date);


            title.setTypeface(custom_font1);
            summary.setTypeface(custom_font2);
            author.setTypeface(custom_font2);
            source_name.setTypeface(custom_font2);
            publish_date.setTypeface(custom_font2);
            //source = (TextView) cell.findViewById(R.id.source);
//            sourceMini = (TextView) cell.findViewById(R.id.sourceMini);
//            image = (ImageView) cell.findViewById(R.id.urltoimage);
//            side_bar = (LinearLayout) cell.findViewById(R.id.side_bar);
//            side_bar1 = (LinearLayout) cell.findViewById(R.id.side_bar1);
//            //category = (TextView) cell.findViewById(R.id.category);
//            //readFull = (TextView) cell.findViewById(R.id.read_full);
//            publishedat = (TextView) cell.findViewById(R.id.publishedat);
            

        }
        
    }


    public class NativeExpressAdViewHolder extends  RecyclerView.ViewHolder{
        NativeExpressAdViewHolder(View view){
            super(view);
        }
    }



    public List<Object> getProductsList() {

        return productsList;
    }

    public void refreshEntries(ArrayList<NewsStory> entries) {
        productsList.clear();
        productsList = (List<Object>)entries.clone();
        Log.v("QuerySearch" , "size " + productsList.size());
        this.notifyDataSetChanged();
    }

//    public List<Object> AllNewsEntries() {
//        return allNewslist;
//    }

    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

}
