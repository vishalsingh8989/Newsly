package com.kepler.news.newsly.updateUtils;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kepler.news.newsly.MainActivity;
import com.kepler.news.newsly.NewsStory;
import com.kepler.news.newsly.ViewPagerFragments.DemoFragment;
import com.kepler.news.newsly.databaseHelper.Feed;
import com.kepler.news.newsly.databaseHelper.News;
import com.kepler.news.newsly.databaseHelper.NewsDatabase;
import com.kepler.news.newsly.databaseHelper.NewsSourceDatabase;
import com.kepler.news.newsly.helper.Common;
import com.kepler.news.newsly.helper.LoadNews;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vishaljasrotia on 8/22/17.
 */

public class UpdateDBservice extends IntentService {
    
    private static String TAG = "UpdateDBservice";
    private NewsSourceDatabase sourcesDB;
    private List<Feed> allSources;
    



    private DemoFragment fragment = null;
    private boolean isNetworkAvailable  = true;
    private  NewsDatabase database = null;
    private String description;
    private String title;
    private String urltoimage;
    private String sourceName                       = "";
    private String author;
    private String category;
    private String url;
    private String sourceurl;
    private String publishedat;
    private String name = "";
    private String language;
    private String country;
    private String id;
    private String addtime;
    private int count = 0;

    public UpdateDBservice(){
        super("");
    }
    public UpdateDBservice(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v(TAG, "UpdateDBservice : onHandleIntent");

        List<Object> newList               = new ArrayList<>();
        String result   = "";

        database =  NewsDatabase.getDatabase(getApplicationContext());
        int count = database.feedModel().getNewsCount();
        Log.v(TAG,"COUNT : " + count);
        sourcesDB = NewsSourceDatabase.getDatabase(getApplicationContext());
        allSources = sourcesDB.feedModel().getAllFeeds();

        for (Feed feed: allSources) {

            if(!feed.subscribed)
            {
                continue;
            }
            try{


                String baseUrl = "http://13.58.159.13/";
                String mUrl = baseUrl+ "?addtime=14955596"
                        +"&start="+String.valueOf(0)
                        +"&offset="+String.valueOf(100)
                        +"&sourceName="+feed.newsSource.replace(" ", "%20");

                URL url1 = new URL(mUrl); // here is your URL path
                //Log.v(TAG,url1 + "");
                JSONObject postDataParams = new JSONObject();
                postDataParams.put("addtime", "1495955972");
                //Log.e("params",postDataParams.toString());


                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);




                OutputStream os = conn.getOutputStream();
                //Log.v(TAG , "os length : " + os.toString().length());

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os , "UTF-8"));

                writer.write(getPostDataString(postDataParams));
                writer.flush();
                writer.close();
                os.close();
                //Log.v(TAG, "after close " + writer.toString().length());
                int responseCode=conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    //Log.v(TAG, "BufferedReader : " +in.toString().length());

                    StringBuffer response = new StringBuffer("");
                    String inputLine = "";

                    //Log.v(TAG, "response");
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                        //break;
                    }


                    //Log.v(TAG, "after append "+response.length()+ " , " +in);
                    result = response.toString();
                    in.close();
                    JSONObject jObject = new JSONObject(result);
                    JSONArray data = jObject.getJSONArray("articles");
                    //Log.v(TAG, "articles " + data.length());
                    for (int i = 0; i < data.length(); i++) {
                        
                        id = data.getJSONObject(i).getString(Common.ID);

                        description     = data.getJSONObject(i).getString(Common.DESCRIPTION);
                        title           = data.getJSONObject(i).getString(Common.TITLE);
                        urltoimage      = data.getJSONObject(i).getString(Common.IMAGEURL);
                        sourceName      = data.getJSONObject(i).getString(Common.SOURCENAME);
                        author          = data.getJSONObject(i).getString(Common.AUTHOR);
                        category        = data.getJSONObject(i).getString(Common.CATEGORY);
                        url             = data.getJSONObject(i).getString(Common.URL);
                        sourceurl       = data.getJSONObject(i).getString(Common.SOURCEURL);
                        publishedat     = data.getJSONObject(i).getString(Common.PUBLISHEDAT);
                        language        = data.getJSONObject(i).getString(Common.LANGUAGE);
                        country         = data.getJSONObject(i).getString(Common.COUNTRY);
                        addtime         = data.getJSONObject(i).getString(Common.ADDTIME);
                        
                        database.feedModel().addNews(new News(id , title, description, publishedat ,sourceName,sourceName, url, urltoimage, author, language, country, category, Integer.parseInt(addtime)));
                    }


                }
                count = database.feedModel().getSourceNews(feed.newsSource).size();
                Log.v(TAG,"COUNT : " + feed.newsSource + " ,  "+ count );
            }catch (Exception e)
            {
                Log.v(TAG,"error:" + e.toString());

            }


        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {


        }
    }



    @Override
    public void onDestroy() {
        Log.v(TAG, "UpdateDBservice : onDestroy");
        super.onDestroy();
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
