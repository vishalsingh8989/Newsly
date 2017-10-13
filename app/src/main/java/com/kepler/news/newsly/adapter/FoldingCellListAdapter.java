package com.kepler.news.newsly.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.icu.util.ValueIterator;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.kepler.news.newsly.MainActivity;
import com.kepler.news.newsly.NewsStory;
import com.kepler.news.newsly.R;
import com.kepler.news.newsly.ViewPagerFragments.DemoFragment;
import com.kepler.news.newsly.databaseHelper.News;
import com.kepler.news.newsly.databaseHelper.NewsDao;
import com.kepler.news.newsly.databaseHelper.NewsDatabase;
import com.kepler.news.newsly.helper.Common;
import com.kepler.news.newsly.helper.RoundedTransformation;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import info.hoang8f.widget.FButton;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.kepler.news.newsly.ViewPagerFragments.DemoFragment.NEWSSOURCE;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class FoldingCellListAdapter extends BaseAdapter {


    private static final int ADVIEW = 0;
    private static final int FOLDINGCELLVIEW = 1;
    private final boolean loadImages;
    private final NewsDao feedmodel;


    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private List<Object> productsList = null;
    private List<NewsStory> allNewslist = null;
    private static String DESCRIPTION        = "description";
    private  String sourceName;
    private static String  SOURCENAME        = "sourceName";

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RoundedTransformation transformation = null;
    private DemoFragment Callback               = null;
    private Typeface mainTypeface               = null;
    private Typeface subTypeface                = null;
    private int errorItem[] = {R.drawable.error1, R.drawable.error2, R.drawable.error3, R.drawable.error4,R.drawable.error5,R.drawable.error6, R.drawable.error7, R.drawable.error8};




    private int[] mycolors = {
            R.color.n,R.color.d,R.color.e,R.color.q,
            R.color.b,R.color.f,R.color.g,R.color.h,
            R.color.o,R.color.i,R.color.c,R.color.j,R.color.k,
            R.color.b, R.color.l,R.color.m,R.color.p,

    };
    private News status;


    public FoldingCellListAdapter(DemoFragment Callback, Context context, List<Object> productsList, List<NewsStory> allNewslist, boolean loadImages, String sourceName) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productsList = productsList;
        this.transformation = new RoundedTransformation(15, 0);
        this.Callback = Callback;
        this.allNewslist = allNewslist;
        this.loadImages = loadImages;
        this.sourceName = sourceName;

        feedmodel = NewsDatabase.getDatabase(mContext).feedModel();

        subTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        mainTypeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/RobotoCondensed-Bold.ttf");
        if(sourceName.equals(Common.BOOKMARKS))
        {
            Log.v("BOOKDEMO", "adapter : " + productsList.size());
        }

    }

    @Override
    public int getCount() {
        return allNewslist.size();
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public Object getItem(int i) {
        return allNewslist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        return FOLDINGCELLVIEW;
        //return position % Common.ADINTERVAL == 0 && position !=0 ? ADVIEW : FOLDINGCELLVIEW;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int type= getItemViewType(position);
        Log.v("FoldingCell", "POS : " + position +" , "+ sourceName +" , "+ (type == ADVIEW )+ " , "+convertView);
        FoldingCell cell =null;
        final NewsStory item;
        switch (type) {

            case ADVIEW  :
                Log.v("FoldingCell", "POS :load ad");

                //NewsStory item = (NewsStory) getItem(position);
                cell = (FoldingCell) convertView;
                final AdViewHolder adViewHolder;
                if(convertView == null) {
                    adViewHolder = new AdViewHolder();
                    cell = (FoldingCell) mLayoutInflater.inflate(R.layout.native_ad_adapter, parent, false);
                    adViewHolder.adView = (NativeExpressAdView) cell.findViewById(R.id.adView);
                    // binding view parts to view holder
                    adViewHolder.adView.setVisibility(View.VISIBLE);
                    adViewHolder.adView.loadAd(new AdRequest.Builder()
                            .addTestDevice("32C278BA97F2B33C41A02691587B4F29")
                            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                            .build());
                    cell.setTag(adViewHolder);
                }else{
                    adViewHolder = (AdViewHolder) cell.getTag();
                }

                LoadAdAsync loadAd = new LoadAdAsync(adViewHolder.adView);
                loadAd.execute();
                break;


            case FOLDINGCELLVIEW:

                item = (NewsStory) getItem(position);
                cell = (FoldingCell) convertView;
                final ViewHolder viewHolder ;

                if(convertView == null) {

                    viewHolder = new ViewHolder();
                    if(loadImages) {
                        cell = (FoldingCell) mLayoutInflater.inflate(R.layout.main_cell_item, parent, false);
                        viewHolder.image = (ImageView) cell.findViewById(R.id.urltoimage);
                    }
                    else {
                        cell = (FoldingCell) mLayoutInflater.inflate(R.layout.main_cell_item_wo_image, parent, false);
                    }
                    // binding view parts to view holder
                    viewHolder.description = (TextView) cell.findViewById(R.id.description);
                    //viewHolder.author = (TextView) cell.findViewById(R.id.author);
                    viewHolder.title = (TextView) cell.findViewById(R.id.title);
                    viewHolder.source = (FancyButton) cell.findViewById(R.id.source);
                    viewHolder.sourceMini = (TextView) cell.findViewById(R.id.sourceMini);
                    viewHolder.side_bar = (LinearLayout) cell.findViewById(R.id.side_bar);
                    viewHolder.side_bar1 = (LinearLayout) cell.findViewById(R.id.side_bar1);
                    //viewHolder.category = (TextView) cell.findViewById(R.id.category);
                    viewHolder.readFull = (FancyButton) cell.findViewById(R.id.read_full);
                    viewHolder.publishedat = (TextView) cell.findViewById(R.id.publishedat);
                    viewHolder.title_back = (TextView)cell.findViewById(R.id.title_back);
                    viewHolder.num_of_likes = (TextView)cell.findViewById(R.id.num_of_likes);
                    viewHolder.bookmark_btn = (ImageView)cell.findViewById(R.id.bookmark_btn);
                    viewHolder.text_data_layout = (LinearLayout)cell.findViewById(R.id.text_data_layout);
                    viewHolder.main_bar = (RelativeLayout)cell.findViewById(R.id.main_bar);

                    //viewHolder.like_btn = (ImageView)cell.findViewById(R.id.like_btn);

                    viewHolder.share_btn = (ImageView)cell.findViewById(R.id.share_btn);


                    viewHolder.title_back.setTypeface(mainTypeface);
                    viewHolder.title.setTypeface(mainTypeface);
                    viewHolder.description.setTypeface(subTypeface);


                    cell.setTag(viewHolder);
                }else {
                    if (unfoldedIndexes.contains(position)) {
                        cell.unfold(true);
                    } else {
                        cell.fold(true);
                    }
                    viewHolder = (ViewHolder) cell.getTag();
                }
                // bind data from selected element to view through view holder
                viewHolder.description.setText(item.getDescription().replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));
                viewHolder.source.setText(item.getSourceName());
                viewHolder.sourceMini.setText(item.getCategory());
                viewHolder.title.setText(item.getTitle().replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));
                viewHolder.title_back.setText(item.getTitle().replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));


                //viewHolder.num_of_likes.setText(item.getNum_of_likes());


                status = feedmodel.getBookMarkStatus(item.getId());
                Log.v("SOURCEURL", "URL1  : " + item.getSourceurl());
                Log.v("SOURCEURL", "URL2  : " + item.getUrltoimage());
                if(status.bookmark  == false) {
                    viewHolder.bookmark_btn.setImageResource(R.drawable.bookmark);
                }else{
                    viewHolder.bookmark_btn.setImageResource(R.drawable.bookmarked);
                }


//                Log.v("BOOKMARKLIKE", "like status  : " + status.like);
//                if(status.like  == false) {
//                    viewHolder.like_btn.setImageResource(R.drawable.like);
//                }else{
//                    viewHolder.like_btn.setImageResource(R.drawable.liked);
//                }



                viewHolder.share_btn.setOnClickListener(onShareClickListener(position, viewHolder, item.getUrl()));

                viewHolder.publishedat.setText(item.getPublishedat());

                viewHolder.readFull.setText("Read Full Article");
                viewHolder.description.setOnClickListener(onClicklistener(position));

                viewHolder.title_back.setOnClickListener(onClicklistener(position));
                viewHolder.text_data_layout.setOnClickListener(onClicklistener(position));
                viewHolder.main_bar.setOnClickListener(onClicklistener(position));
                viewHolder.readFull.setOnClickListener(onReadFullClickListener(position, viewHolder));

                viewHolder.source.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Callback.onItemClicked(viewHolder.source, position);
                    }
                });

                viewHolder.bookmark_btn.setOnClickListener(onBookmarkClickListener(item, viewHolder));

                //viewHolder.like_btn.setOnClickListener(onLikeClickListener(item, viewHolder));

                GradientDrawable gradientDrawable = (GradientDrawable) viewHolder.side_bar.getBackground();

                gradientDrawable.setColor(mContext.getResources().getColor(mycolors[position % mycolors.length]));
                viewHolder.side_bar.setBackground(gradientDrawable);
                viewHolder.side_bar1.setBackground(gradientDrawable);




                if(loadImages) {
                    try {                //&&productsList.get(position).getUrltoimage().trim() !=null && productsList.get(position).getUrltoimage().trim() != ""){
                        Picasso.with(mContext)
                                .load(item.getUrltoimage()+"")
                                .resize(250, 250)
                                .centerCrop()
                                .error(errorItem[position%errorItem.length])
                                .into(viewHolder.image);

                    } catch (Exception e) {
                        Log.v("NEWSSOURCE" ,item.getSourceName() + " : " + item.getUrltoimage()+" " +e.toString());
                        Picasso.with(mContext)
                                .load(errorItem[position%errorItem.length])
                                .resize(250, 250)
                                .centerCrop()
                                .into(viewHolder.image);
                    }
                }

                break;

            }
        return cell;
        }

    @NonNull
    private View.OnClickListener onLikeClickListener(final NewsStory item, final ViewHolder viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News likeStatus = NewsDatabase.getDatabase(mContext).feedModel().getLikeStatus(item.getId());
                Log.v("BOOKMARKLIKE", "like status  : " + likeStatus.like);
                int count = Integer.parseInt((String) viewHolder.num_of_likes.getText());
                if(likeStatus.like  == true) {
                    viewHolder.like_btn.setImageResource(R.drawable.like);
                    likeStatus.like = false;
                    count--;
                }else{
                    likeStatus.like = true;
                    viewHolder.like_btn.setImageResource(R.drawable.liked);
                    count++;
                }
                NewsDatabase.getDatabase(mContext).feedModel().updateLike(likeStatus);

                viewHolder.num_of_likes.setText(count+"");


            }
        };
    }

    @NonNull
    private View.OnClickListener onBookmarkClickListener(final NewsStory item, final ViewHolder viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                News bookMarkStatus = NewsDatabase.getDatabase(mContext).feedModel().getBookMarkStatus(item.getId());
                Log.v("BOOKMARKLIKE", "bookmark status  : " + bookMarkStatus.bookmark);
                if(bookMarkStatus.bookmark  == true) {
                    bookMarkStatus.bookmark = false;
                    viewHolder.bookmark_btn.setImageResource(R.drawable.bookmark);
                }else{
                    bookMarkStatus.bookmark = true;
                    viewHolder.bookmark_btn.setImageResource(R.drawable.bookmarked);
                }
                NewsDatabase.getDatabase(mContext).feedModel().updateBookmark(bookMarkStatus);
            }
        };
    }

    @NonNull
    private View.OnClickListener onReadFullClickListener(final int position, final ViewHolder viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callback.onItemClicked(viewHolder.readFull, position);
            }
        };
    }
    @NonNull
    private View.OnClickListener onShareClickListener(final int position, final ViewHolder viewHolder, final String link) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callback.onShareClicked(viewHolder.text_data_layout, position, link);
            }
        };
    }



    @NonNull
    private View.OnClickListener onClicklistener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Callback.onItemClicked(view, position);
            }
        };
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

    public void upDateEntries(List<Object> entries, boolean onRefresh) {

        //productsList.addAll(0,entries);

        //allNewslist.addAll(entries);
        //Callback.getFeeds();

        Log.v("LOADASYNCFEED" , "size : " +productsList.size());

        this.notifyDataSetChanged();


    }

    public List<Object> getProductsList() {
        return productsList;
    }
    public void setProductsList(List<NewsStory> list) {

    }

    public void refreshEntries(List<Object> entries) {
        productsList.clear();
        productsList = entries;
        Log.v("QuerySearch" , "size " + productsList.size());
        this.notifyDataSetChanged();
    }

//    public List<Object> AllNewsEntries() {
//        return allNewslist;
//    }

    private static class ViewHolder {
        LinearLayout side_bar;
        LinearLayout side_bar1;
        TextView description;
        TextView title;
        TextView title_back;
        TextView author;
        FancyButton source;
        TextView sourceMini;
        TextView category;
        FancyButton readFull;
        TextView publishedat;
        NativeExpressAdView adView;
        ImageView image;
        TextView num_of_likes;
        ImageView bookmark_btn;
        ImageView like_btn;
        ImageView share_btn;
        LinearLayout text_data_layout;
        RelativeLayout main_bar;


    }


    private static class AdViewHolder {
        NativeExpressAdView adView;


    }



    public  class LoadAdAsync extends AsyncTask<Void, Void ,Void>{


        NativeExpressAdView adView = null;
        AdRequest adRequest = null;

        LoadAdAsync(NativeExpressAdView ad)
        {
            this.adView = ad;
        }

        @Override
        protected Void doInBackground(Void... voids) {

           this.adRequest = new AdRequest.Builder().build();

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adView.loadAd(adRequest);
        }
    }


    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();
        Log.v("NOTIFITYTEST", "CALLED");
    }

}
