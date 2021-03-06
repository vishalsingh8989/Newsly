package com.kepler.news.newsly;

import android.accounts.NetworkErrorException;
import android.os.AsyncTask;
import android.util.Log;

import com.kepler.news.newsly.IntroFragments.NewsSourceFragment;
import com.kepler.news.newsly.adapter.CountryAdapter;
import com.kepler.news.newsly.databaseHelper.NewsSource;
import com.kepler.news.newsly.databaseHelper.NewsSourceDatabase;

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

public class LoadNewSourceAsync  extends AsyncTask<Object, Object, ArrayList<NewsSource>> {

    private final NewsSourceDatabase database;
    private CountryAdapter mAdapter         = null;
    private NewsSourceFragment mFragment    = null;
    ArrayList<NewsSource> newList               = new ArrayList<>();
    private int ZERO=0;


    public LoadNewSourceAsync(NewsSourceFragment fragment, CountryAdapter adapter, NewsSourceDatabase database) {
        this.mAdapter = adapter;
        this.mFragment = fragment;
        this.database = database;
    }

    @Override
    protected ArrayList<NewsSource> doInBackground(Object... voids) {
        String result   = "";
        try {
            String mUrl = "http://13.58.159.13/newsSourceName.php";

            URL url = new URL(mUrl); // here is your URL path
            Log.v("MYURL", url + "");
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("addtime", "1495955972");
            Log.e("params", postDataParams.toString());


            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            Log.v("NEWSOURCE", "os length : " + os.toString().length());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            Log.v("NEWSOURCE", "after close " + writer.toString().length());
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                Log.v("NEWSOURCE", "BufferedReader : " + in.toString().length());

                StringBuffer response = new StringBuffer("");
                String inputLine = "";

                Log.v("NEWSOURCE", "response");
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    //break;
                }


                Log.v("NEWSOURCE", "after append " + response.length() + " , " + in);
                result = response.toString();
                in.close();
                JSONObject jObject = new JSONObject(result);
                JSONArray data = jObject.getJSONArray("newsSource");
                Log.v("NEWSOURCE", "newsSource " + data.length());

                for (int i = 0; i < data.length(); i++) {
                    List<NewsSource> temp= null;
                    try{
                        temp = database.feedModel().getFeed(data.getJSONObject(i).getString("sourceName"));
                        Log.v("NEWSOURCE", "from db " + temp +", "+temp.size());
                    }catch (Exception e) {
                        Log.v("NEWSOURCE","e "+e.toString());
                    }
                    Log.v("NEWSOURCE",data.getJSONObject(i).getString("sourceName"));
                    if(temp.size()==ZERO) {
                        newList.add(new NewsSource(data.getJSONObject(i).getString("sourceName"), false, 0));
                        database.feedModel().addTask(new NewsSource(data.getJSONObject(i).getString("sourceName"), false, 0));
                    }
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v("NEWSOURCE", "MalformedURLException");



        } catch (IOException e) {
            e.printStackTrace();
            Log.v("NEWSOURCE", "IOException");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("NEWSOURCE", "JSONException");

        } catch (NetworkErrorException e)
        {
            e.printStackTrace();
            Log.v("NEWSOURCE", "NetworkErrorException");



        } catch (Exception e) {
            e.printStackTrace();
            Log.v("NEWSOURCE", "Exception");
        }



        return newList;
    }

    @Override
    protected void onPostExecute(ArrayList<NewsSource> objects) {
        super.onPostExecute(objects);

        if (mFragment != null)
            mAdapter.upDateEntries(objects);


        //mAdapter.notifyDataSetChanged();
        if (mFragment != null){
            mFragment.setChecked(objects);
            mFragment.setProgress(false);
        }
        //Common.update(objects);
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
