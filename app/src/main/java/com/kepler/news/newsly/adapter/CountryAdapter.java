package com.kepler.news.newsly.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kepler.news.newsly.R;
import com.kepler.news.newsly.databaseHelper.NewsSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vishaljasrotia on 11/06/17.
 */

public class CountryAdapter extends BaseAdapter {


    private Context mContext = null;
    private LayoutInflater mLayoutInflater = null;
    private List<NewsSource> countryList = null;
    private ArrayList<Boolean> countrySelected = null;
    private int[] background = {R.drawable.sample, R.mipmap.usa_flag, R.mipmap.uk_flag, R.mipmap.india_flag,
                                R.mipmap.aus_flag, R.mipmap.canada_flag, R.mipmap.france_flag, R.mipmap.italy_flag
    };

    public CountryAdapter(Context context, List<NewsSource> countryList) {


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
        RelativeLayout cell =null;

        cell = (RelativeLayout) convertView;
        final CountryHolder viewHolder ;

        if(convertView == null) {
            viewHolder = new CountryHolder();
            cell = (RelativeLayout) mLayoutInflater.inflate(R.layout.country_item, parent, false);
            // binding view parts to view holder
            viewHolder.txtTitle = (TextView) cell.findViewById(R.id.txtTitle);


            cell.setTag(viewHolder);
        }else {
            viewHolder = (CountryHolder) cell.getTag();
        }

        viewHolder.txtTitle.setText((String) countryList.get(position).newsSource);


        return cell;
    }

    static class CountryHolder
    {

        TextView txtTitle;

    }
    public void upDateEntries(List<NewsSource> entries) {

        countryList.addAll(entries);

        this.notifyDataSetChanged();


    }

}