package com.kepler.news.newsly.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kepler.news.newsly.NewsStory;
import com.kepler.news.newsly.R;
import com.ramotion.foldingcell.FoldingCell;

import java.util.ArrayList;

/**
 * Created by vishaljasrotia on 11/06/17.
 */

public class CountryAdapter extends BaseAdapter {


    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private ArrayList<String> countryList = null;

    public CountryAdapter(Context context, ArrayList<String> countryList) {


        this.mContext = context;
        this.countryList = countryList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return countryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout cell =null;

        cell = (LinearLayout) convertView;
        final CountryHolder viewHolder ;

        if(convertView == null) {
            viewHolder = new CountryHolder();
            cell = (LinearLayout) mLayoutInflater.inflate(R.layout.country_item, parent, false);

            // binding view parts to view holder
            viewHolder.txtTitle = (TextView) cell.findViewById(R.id.txtTitle);
            viewHolder.txtTitle.setTextColor(Color.parseColor("#ffffff"));

            cell.setTag(viewHolder);
        }else {
            viewHolder = (CountryHolder) cell.getTag();
        }

        viewHolder.txtTitle.setText(countryList.get(position));


        return cell;
    }

    static class CountryHolder
    {

        TextView txtTitle;
    }
}