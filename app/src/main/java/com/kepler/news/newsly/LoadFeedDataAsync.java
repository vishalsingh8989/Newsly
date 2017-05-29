package com.kepler.news.newsly;

import android.os.AsyncTask;
import android.util.Log;

import com.kepler.news.newsly.helper.Common;

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
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class LoadFeedDataAsync  extends AsyncTask<Void, Void, ArrayList<NewsStory>> {



    private FoldingCellListAdapter foldingCellListAdapter   = null;
    private ArrayList<NewsStory> productsList = null;
    private static String  DESCRIPTION                       = "description";
    private static String  SOURCE                            = "source";
    private static String  SOURCENAME                       = "sourceName";
    private static String  IMAGEURL                        = "urltoimage";

    private int start =0;
    private int offset = 30;




    public LoadFeedDataAsync(FoldingCellListAdapter adapter) {
        this.foldingCellListAdapter = adapter;
        this.productsList = new ArrayList<>();

    }

    @Override
    protected ArrayList<NewsStory> doInBackground(Void... voids) {


        start = MainActivity.start;
        offset = MainActivity.offset;

        String articles = "notfound";
        String result = "";
        try {

            //http://192.168.0.4:8000/?addtime=1495955968&start=10&offset=20

            //String mUrl = "http://192.168.0.4:8000/?addtime=1495955972";
            String mUrl = "http://192.168.0.4:8000/?addtime=149595596&start="+String.valueOf(start)+"&offset="+String.valueOf(offset);


            Log.v("LOADASYNCFEED",mUrl);

            URL url = new URL(mUrl); // here is your URL path

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("addtime", "1495955972");

            Log.e("params",postDataParams.toString());



            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);



            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                conn.getInputStream()));
                StringBuffer response = new StringBuffer("");
                String inputLine = "";

                while ((inputLine = in.readLine()) != null) {

                    response.append(inputLine);
                    break;
                }
                Log.v("HYHTTP", ""+response);
                result = response.toString();
                in.close();
                JSONObject jObject = new JSONObject(result);
                JSONArray data = jObject.getJSONArray("articles");

                Log.v("HYHTTP", "articles " + data.length());

                for (int i = 0; i < data.length(); i++) {
                    NewsStory story = new NewsStory();

                    Log.v("HYHTTP", "*************************************");
                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(Common.DESCRIPTION));
                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(Common.SOURCENAME));
                    Log.v("HYHTTP", "*************************************");

                    story.setDescription(data.getJSONObject(i).getString(Common.DESCRIPTION));
                    story.setTitle(data.getJSONObject(i).getString(Common.TITLE));
                    story.setSourceName(data.getJSONObject(i).getString(Common.SOURCENAME));
                    story.setUrltoimage(data.getJSONObject(i).getString(Common.IMAGEURL));
                    story.setAuthor(data.getJSONObject(i).getString(Common.AUTHOR));


                    productsList.add(story);
                }


            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "MalformedURLException");


        } catch (IOException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "IOException");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "JSONException");



        } catch (Exception e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Exception");
        }


        return productsList;


    }




    @Override
    protected void onPostExecute(ArrayList<NewsStory> result) {
        super.onPostExecute(result);
        MainActivity.start = MainActivity.start + MainActivity.offset;

        Collections.shuffle(result);
        foldingCellListAdapter.upDateEntries(result);
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
