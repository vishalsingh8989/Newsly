package com.kepler.news.newsly;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.icu.util.ValueIterator;
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
import com.google.android.gms.ads.NativeExpressAdView;
import com.kepler.news.newsly.helper.RoundedTransformation;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class FoldingCellListAdapter extends BaseAdapter {


    private static final int ADVIEW = 0;
    private static final int FOLDINGCELLVIEW = 1;

    @Override
    public int getItemViewType(int position) {
        return position % 7 == 0  ? ADVIEW : FOLDINGCELLVIEW;
    }

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private ArrayList<NewsStory> productsList = null;
    private ArrayList<NewsStory> allNewslist = null;
    private static String DESCRIPTION        = "description";
    private static String SOURCE             = "source";
    private static String  SOURCENAME        = "sourceName";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private RoundedTransformation transformation = null;
    private MainActivity  Callback               = null;



    private int[] mycolors = {
            R.color.n,R.color.d,R.color.e,R.color.q,
            R.color.b,R.color.f,R.color.g,R.color.h,
            R.color.o,R.color.i,R.color.c,R.color.j,R.color.k,
            R.color.b, R.color.l,R.color.m,R.color.p,

    };

    public FoldingCellListAdapter(MainActivity Callback, Context context, ArrayList<NewsStory> productsList, ArrayList<NewsStory> allNewslist) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productsList = productsList;
        this.transformation = new RoundedTransformation(15, 0);
        this.Callback = Callback;
        this.allNewslist = allNewslist;
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Override
    public Object getItem(int i) {
        return productsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int type= getItemViewType(position);
        Log.v("FoldingCell", "POS : " + position + " , "+ (type == ADVIEW )+ " , "+convertView);
        FoldingCell cell =null;

        switch (type) {

            case ADVIEW:
                Log.v("FoldingCell", "POS :load ad");

                NewsStory item = (NewsStory) getItem(position);
                cell = (FoldingCell) convertView;
                final AdViewHolder adViewHolder;

                if (cell == null) {

                    adViewHolder = new AdViewHolder();
                    cell = (FoldingCell) mLayoutInflater.inflate(R.layout.native_ad_adapter, parent, false);
                    adViewHolder.adView = (NativeExpressAdView) cell.findViewById(R.id.adView);
                    // binding view parts to view holder
                    adViewHolder.adView.setVisibility(View.VISIBLE);
                    cell.setTag(adViewHolder);
                    AdRequest adRequest = new AdRequest.Builder().build();
                    adViewHolder.adView.loadAd(adRequest);

                } else {
                    adViewHolder = (AdViewHolder) cell.getTag();

                }


                //return cell;


            case FOLDINGCELLVIEW:

                item = (NewsStory) getItem(position);
                cell = (FoldingCell) convertView;
                final ViewHolder viewHolder;


                if (cell == null) {
                    viewHolder = new ViewHolder();
                    cell = (FoldingCell) mLayoutInflater.inflate(R.layout.main_cell_item, parent, false);
                    // binding view parts to view holder
                    viewHolder.description = (TextView) cell.findViewById(R.id.description);
                    viewHolder.author = (TextView) cell.findViewById(R.id.author);
                    viewHolder.title = (TextView) cell.findViewById(R.id.title);
                    viewHolder.source = (TextView) cell.findViewById(R.id.source);
                    viewHolder.sourceMini = (TextView) cell.findViewById(R.id.sourceMini);
                    viewHolder.image = (ImageView) cell.findViewById(R.id.urltoimage);
                    viewHolder.side_bar = (LinearLayout) cell.findViewById(R.id.side_bar);
                    viewHolder.side_bar1 = (LinearLayout) cell.findViewById(R.id.side_bar1);
                    viewHolder.category = (TextView) cell.findViewById(R.id.category);
                    viewHolder.readFull = (TextView) cell.findViewById(R.id.read_full);
                    viewHolder.publishedat = (TextView) cell.findViewById(R.id.publishedat);

                    cell.setTag(viewHolder);
                } else {
                    // for existing cell set valid valid state(without animation)
                    if (unfoldedIndexes.contains(position)) {
                        cell.unfold(true);
                    } else {
                        cell.fold(true);
                    }
                    viewHolder = (ViewHolder) cell.getTag();
                }

                // bind data from selected element to view through view holder
                viewHolder.description.setText(productsList.get(position).getDescription().replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));
                viewHolder.source.setText(productsList.get(position).getSourceName());
                viewHolder.sourceMini.setText(productsList.get(position).getSourceName());
                viewHolder.title.setText(productsList.get(position).getTitle().replaceAll("^\"|\"$", "").replace("&amp;", "&").replace("&quot;", "\"").replace("&#039;", "\'").replace("&rdquo;", "\"").replace("&ldquo;", "\""));
                viewHolder.author.setText("Author: " + productsList.get(position).getAuthor());
                viewHolder.category.setText(productsList.get(position).getCategory());
                viewHolder.publishedat.setText(productsList.get(position).getPublishedat());


                //String dynamicUrl =  productsList.get(position).getUrl();

                //String linkedText = String.format("<a href=\"%s\">Read Full Article</a>", dynamicUrl);


                viewHolder.readFull.setText("Read Full Article");
                //viewHolder.readFull.setMovementMethod(LinkMovementMethod.getInstance());


                viewHolder.readFull.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Callback.onItemClicked(viewHolder.readFull, position);
                    }
                });

                viewHolder.source.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Callback.onItemClicked(viewHolder.source, position);
                    }
                });

                GradientDrawable gradientDrawable = (GradientDrawable) viewHolder.side_bar.getBackground();
                //        shapeDrawable.getPaint().setColor(ContextCompat.getColor(mContext,colors[position%colors.length]));

                gradientDrawable.setColor(mContext.getResources().getColor(mycolors[position % mycolors.length]));
                viewHolder.side_bar.setBackground(gradientDrawable);
                viewHolder.side_bar1.setBackground(gradientDrawable);


                Log.v("URLPATH", " : " + productsList.get(position).getUrltoimage().trim());

                try {                //&&productsList.get(position).getUrltoimage().trim() !=null && productsList.get(position).getUrltoimage().trim() != ""){
                    Picasso.with(mContext)
                            .load(productsList.get(position).getUrltoimage())
                            .error(R.drawable.sample)
                            .into(viewHolder.image);

                } catch (Exception e) {

                }



            }
        return cell;
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

    public void upDateEntries(ArrayList<NewsStory> entries, boolean onRefresh) {

        productsList.addAll(entries);
        allNewslist.addAll((ArrayList<NewsStory>)entries.clone());

        Log.v("LOADASYNCFEED" , "size : " +productsList.size());
        this.notifyDataSetChanged();


    }

    public ArrayList<NewsStory> getProductsList() {
        return productsList;
    }

    public void refreshEntries(ArrayList<NewsStory> entries) {
        productsList.clear();
        productsList = (ArrayList<NewsStory>)entries.clone();
        Log.v("QuerySearch" , "size " + productsList.size());
        this.notifyDataSetChanged();
    }

    public ArrayList<NewsStory> AllNewsEntries() {
        return allNewslist;
    }

    private static class ViewHolder {
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

    }


    private static class AdViewHolder {
        NativeExpressAdView adView;


    }



}
