package com.kepler.news.newsly;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class NewsAdapter extends BaseAdapter {


    private ArrayList<HashMap<String, String>> productsList = null;
    private static String DESCRIPTION = "description";
    private static String SOURCE = "source";
    private static String  SOURCENAME = "sourceName";
    private Context mContext;
    private LayoutInflater mLayoutInflater;


    public NewsAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        productsList = new ArrayList<>();
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
        RelativeLayout itemView;
        if (convertView == null) {
            itemView = (RelativeLayout) mLayoutInflater.inflate(
                    R.layout.list_item, parent, false);

        } else {
            itemView = (RelativeLayout) convertView;
        }


        TextView descriptionText = (TextView)
                itemView.findViewById(R.id.description);
        TextView source = (TextView)
                itemView.findViewById(R.id.source);


        descriptionText.setText(productsList.get(position).get(DESCRIPTION));
        source.setText(productsList.get(position).get(SOURCENAME));
        return itemView;

    }

    public void upDateEntries(ArrayList<HashMap<String, String>> entries) {
        productsList = entries;
        this.notifyDataSetChanged();
    }



}
