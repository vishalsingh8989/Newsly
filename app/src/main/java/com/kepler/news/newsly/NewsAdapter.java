package com.kepler.news.newsly;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.HashMap;

import ak.sh.ay.oblique.ObliqueView;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class NewsAdapter extends BaseAdapter {


    private ArrayList<NewsStory> productsList = null;
    private static String DESCRIPTION        = "description";
    private static String SOURCE             = "source";
    private static String  SOURCENAME        = "sourceName";
    private Context mContext;
    private LayoutInflater mLayoutInflater;



    public NewsAdapter(Context context, ArrayList<NewsStory> productsList) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.productsList = productsList;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        if (convertView == null) {
            itemView = (LinearLayout) mLayoutInflater.inflate(R.layout.list_item, parent, false);

        } else {
            itemView = (LinearLayout) convertView;
        }


        TextView descriptionText = (TextView) itemView.findViewById(R.id.description);
        TextView source = (TextView) itemView.findViewById(R.id.source);
        ObliqueView imageView = (ObliqueView)itemView.findViewById(R.id.obliqueView);


        descriptionText.setText(productsList.get(position).getDescription());
        source.setText(productsList.get(position).getSourceName());
        Picasso.with(mContext).load(productsList.get(position).getUrltoimage()).into(imageView);

        return itemView;

    }

    public void upDateEntries(ArrayList<NewsStory> entries) {
        productsList = entries;
        this.notifyDataSetChanged();
    }



}
