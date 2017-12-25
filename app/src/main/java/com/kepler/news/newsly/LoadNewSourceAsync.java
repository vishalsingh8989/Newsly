package com.kepler.news.newsly;

import android.accounts.NetworkErrorException;
import android.os.AsyncTask;
import android.util.Log;

import com.kepler.news.newsly.IntroFragments.NewsSourceFragment;
import com.kepler.news.newsly.adapter.CountryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vishaljasrotia on 23/07/17.
 */

public class LoadNewSourceAsync  extends AsyncTask<Object, Object, ArrayList<Object>> {


    @Override
    protected ArrayList<Object> doInBackground(Object... objects) {
        return null;
    }
}
