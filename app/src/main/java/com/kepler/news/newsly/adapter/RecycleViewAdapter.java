package com.kepler.news.newsly.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.NativeExpressAdView;
import com.kepler.news.newsly.MainActivity;
import com.kepler.news.newsly.NewsStory;
import com.kepler.news.newsly.R;
import com.kepler.news.newsly.helper.RoundedTransformation;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

/**
 * Created by vishaljasrotia on 06/06/17.
 */

public class RecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();

    private List<Object> productsList = null;
    private List<Object> allNewslist = null;
    private static String DESCRIPTION        = "description";
    private static String SOURCE             = "source";
    private static String  SOURCENAME        = "sourceName";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RoundedTransformation transformation = null;
    private MainActivity Callback                = null;
    private static final int MENU_ITEM_VIEW_TYPE = 0;
    private static final int AD_VIEW_TYPE        = 1;
    public static int test = 10;
    




    private int[] mycolors = {
            R.color.n,R.color.d,R.color.e,R.color.q,
            R.color.b,R.color.f,R.color.g,R.color.h,
            R.color.o,R.color.i,R.color.c,R.color.j,R.color.k,
            R.color.b, R.color.l,R.color.m,R.color.p,
    };



    public RecycleViewAdapter(MainActivity Callback, Context context, List<Object> productsList, List<Object> allNewsList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productsList = productsList;
        this.transformation = new RoundedTransformation(15, 0);
        this.Callback = Callback;
        this.allNewslist = allNewsList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case AD_VIEW_TYPE:
                    View nativeExpressAdView = LayoutInflater.from(mContext).inflate(R.layout.native_ad_adapter, parent, false);
                    return  new NativeExpressAdViewHolder(nativeExpressAdView);
                case MENU_ITEM_VIEW_TYPE:
                    default:
                        FoldingCell newsItemLayoutView = (FoldingCell)LayoutInflater.from(mContext).inflate(R.layout.main_cell_item, parent, false);
                        return new NewsItemViewHolder(newsItemLayoutView);
            }
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);

        Log.v("onBindViewHolder",  "position :"+position+" View Type : " +viewType +  "  " + holder);

        switch(viewType){
            case AD_VIEW_TYPE:
                NativeExpressAdViewHolder nativeExpressAdViewHolder = (NativeExpressAdViewHolder)holder;
                NativeExpressAdView adView  = (NativeExpressAdView)productsList.get(position);
                ViewGroup adCardView = (ViewGroup)nativeExpressAdViewHolder.itemView;
                adCardView.removeAllViews();
                if(adView.getParent()!=null)
                {
                    ((ViewGroup)adView.getParent()).removeView(adView);

                }

                adCardView.addView(adView);
                break;
            case MENU_ITEM_VIEW_TYPE:
                default:
                    final NewsItemViewHolder newsItemViewHolder = (NewsItemViewHolder)holder;
                    NewsStory story = (NewsStory)productsList.get(position);


                    newsItemViewHolder.title.setText(story.getTitle().replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));
                    newsItemViewHolder.description.setText(story.getDescription().replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));
                    newsItemViewHolder.sourceMini.setText(" " + position);
                    newsItemViewHolder.source.setText(story.getSourceName());
                    newsItemViewHolder.publishedat.setText(story.getPublishedat());
                    newsItemViewHolder.author.setText(story.getAuthor());
                    newsItemViewHolder.category.setText(story.getCategory());



                    newsItemViewHolder.parent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Callback.onItemClicked(newsItemViewHolder.parent, position);
                        }
                    });

                    newsItemViewHolder.title.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Callback.onItemClicked(newsItemViewHolder.parent, position);
                        }
                    });

                    newsItemViewHolder.description.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Callback.onItemClicked(newsItemViewHolder.parent, position);
                        }
                    });



                    try {
                        Picasso.with(mContext)
                                .load(story.getUrltoimage())
                                .error(R.drawable.sample)
                                .into(newsItemViewHolder.image);
                    } catch (Exception e) {
                    }



        }


    }



    @Override
    public int getItemCount() {
        return productsList.size();
    }



    @Override
    public int getItemViewType(int position) {
        return (position % 8 == 0  )?AD_VIEW_TYPE:MENU_ITEM_VIEW_TYPE;
    }

    public void upDateEntries(List<Object> entries, boolean onRefresh) {
        productsList.addAll(entries);
        allNewslist.addAll(entries);
        Log.v("LOADASYNCFEED" , "size : " +productsList.size());
        this.notifyDataSetChanged();


    }


    public class NewsItemViewHolder extends RecyclerView.ViewHolder{


        FoldingCell parent;
        LinearLayout side_bar;
        LinearLayout side_bar1;
        TextView description;
        TextView title;
        TextView author;
        TextView source;
        TextView sourceMini;
        TextView category;
        TextView readFull;
        TextView publishedat;
        NativeExpressAdView adView;
        ImageView image;
        
        NewsItemViewHolder(FoldingCell cell) {
            super(cell);

            parent = (FoldingCell)cell.findViewById(R.id.folding_cell);
            description = (TextView) cell.findViewById(R.id.description);
            author = (TextView) cell.findViewById(R.id.author);
            title = (TextView) cell.findViewById(R.id.title);
            source = (TextView) cell.findViewById(R.id.source);
            sourceMini = (TextView) cell.findViewById(R.id.sourceMini);
            image = (ImageView) cell.findViewById(R.id.urltoimage);
            side_bar = (LinearLayout) cell.findViewById(R.id.side_bar);
            side_bar1 = (LinearLayout) cell.findViewById(R.id.side_bar1);
            category = (TextView) cell.findViewById(R.id.category);
            readFull = (TextView) cell.findViewById(R.id.read_full);
            publishedat = (TextView) cell.findViewById(R.id.publishedat);
            

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

    public List<Object> AllNewsEntries() {
        return allNewslist;
    }

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
