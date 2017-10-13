package com.kepler.news.newsly.updateUtils;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kepler.news.newsly.NewsStory;
import com.kepler.news.newsly.databaseHelper.News;
import com.kepler.news.newsly.databaseHelper.NewsDao;
import com.kepler.news.newsly.databaseHelper.NewsDatabase;
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
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vishaljasrotia on 8/22/17.
 */

public class LoadNews  extends AsyncTask<Void, Void, Void> {


    private static final int ZERO =0 ;
    private Context mContext;
    private String newsSource;
    private NewsDatabase database;


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
    private String  num_of_likes;
    private News news;

    public LoadNews(String newsSource, Context mContext) {

        this.newsSource = newsSource;
        this.mContext   = mContext;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        database = NewsDatabase.getDatabase(mContext);

        List<Object> newList               = new ArrayList<>();
        String result   = "";
        int count = database.feedModel().getNewsCount();
        Log.v("LOADINSETTING","COUNT : " + count);
        try {


            String baseUrl = "http://13.58.159.13/";
            String mUrl = baseUrl+ "?addtime=14955596"
                    +"&start="+String.valueOf(0)
                    +"&offset="+String.valueOf(500)
                    +"&sourceName="+newsSource.replace(" ", "%20");

            URL url1 = new URL(mUrl); // here is your URL path
            Log.v("LOADINSETTING",mUrl);
            JSONObject postDataParams = new JSONObject();
            postDataParams.put("addtime", "1495955972");
            Log.e("params",postDataParams.toString());


            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);




            OutputStream os = conn.getOutputStream();
            Log.v("HYHTTP" , "os length : " + os.toString().length());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os , "UTF-8"));

            writer.write(getPostDataString(postDataParams));
            writer.flush();
            writer.close();
            os.close();
            Log.v("HYHTTP", "after close " + writer.toString().length());
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                Log.v("HYHTTP", "BufferedReader : " +in.toString().length());

                StringBuffer response = new StringBuffer("");
                String inputLine = "";

                Log.v("HYHTTP", "response");
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    //break;
                }


                Log.v("HYHTTP", "after append "+response.length()+ " , " +in);
                result = response.toString();
                in.close();
                JSONObject jObject = new JSONObject(result);
                JSONArray data = jObject.getJSONArray("articles");
                Log.v("HYHTTP", "articles " + data.length());
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
                    num_of_likes    = data.getJSONObject(i).getString(Common.LIKES);

                    Log.v("HYHTTP", "**************************************");
                    Log.v("HYHTTP", "2source Url : " + sourceurl);
                    Log.v("HYHTTP", "2url : " + url);


                    news = new News(id , title, description, publishedat ,sourceName, sourceName, url, sourceurl, urltoimage,  author, language, country, category, Integer.parseInt(addtime), Integer.parseInt(num_of_likes), false , false);
                    Log.v("HYHTTP", "news  : " + news.toString());

                    database.feedModel().addNews(news);
                }


            }
            count = database.feedModel().getNewsCount();
            Log.v("LOADINSETTING","source : " + sourceName + " , "+  "COUNT : " + count);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "MalformedURLException");
            //isNetworkAvailable = false;


        } catch (IOException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "IOException");
            //isNetworkAvailable = false;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "JSONException");

        } catch (NetworkErrorException e)
        {
            e.printStackTrace();
            Log.v("HYHTTP", "NetworkErrorException");
            //isNetworkAvailable = false;


        } catch (Exception e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Exception");
            //isNetworkAvailable = false;
        }


        List<NewsStory> allnews = database.feedModel().getAllNews();
        for (NewsStory story: allnews) {
            Log.v("DBTEST", " URL : "  + story.getUrl() );
            Log.v("DBTEST", "SURL : "  + story.getSourceurl());
        }

        return null;

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
