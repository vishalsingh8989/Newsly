package com.kepler.news.newsly.updateUtils;

import android.accounts.NetworkErrorException;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kepler.news.newsly.databaseHelper.News;
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
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vishaljasrotia on 8/22/17.
 */

public class UpdateDBservice extends IntentService {
    
    private static String TAG = "UpdateDBservice";
    //private NewsDatabase database;


    public static String ID                                 = "id" ;
    public static String TITLE                              = "title";
    public static String SUMMARY                            = "summary";
    public static String LANGUAGE                           = "language" ;
    public static String COUNTRY                            = "country";
    public static String CATEGORY                           = "category";
    public static String TAGS                               = "tags";
    public static String ARTICLE_URL                        = "artilce_url" ;
    public static String SOURCE_URL                         = "source_url";
    public static String SOURCE_NAME                        = "source_name";
    public static String TOP_IMAGE                          = "top_image";
    public static String PUBLISH_DATE                       = "publish_date";
    public static String AUTHORS                            = "authors";
    public static String META_FAVICON                       = "meta_favicon";
    public static String TRENDING                           = "trending";

    private News news;
    public UpdateDBservice(){
        super("");
    }
    public UpdateDBservice(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.v(TAG, "UpdateDBservice : onHandleIntent");


        updateDB();


    }

    public void updateDB() {
//        database = NewsDatabase.getDatabase(getApplicationContext());
//
//        //List<Object> newList               = new ArrayList<>();
//        String result   = "";
//        int count = database.feedModel().getNewsCount();
//        Log.v("LOADINSETTING","COUNT : " + count);
//        try {
//
//
//            String baseUrl = "http://13.58.159.13/";
//            String mUrl = baseUrl;
//
//            URL url1 = new URL(mUrl); // here is your URL path
//            Log.v("LOADINSETTING",mUrl);
//            JSONObject postDataParams = new JSONObject();
//            postDataParams.put("addtime", "1495955972");
//            Log.e("params",postDataParams.toString());
//
//
//            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
//            conn.setReadTimeout(15000 /* milliseconds */);
//            conn.setConnectTimeout(15000 /* milliseconds */);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//
//
//
//
//            OutputStream os = conn.getOutputStream();
//            Log.v("HYHTTP" , "os length : " + os.toString().length());
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os , "UTF-8"));
//
//            writer.write(getPostDataString(postDataParams));
//            writer.flush();
//            writer.close();
//            os.close();
//            Log.v("HYHTTP", "after close " + writer.toString().length());
//            int responseCode=conn.getResponseCode();
//            if (responseCode == HttpsURLConnection.HTTP_OK) {
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                Log.v("HYHTTP", "BufferedReader : " +in.toString().length());
//
//                StringBuffer response = new StringBuffer("");
//                String inputLine = "";
//
//                Log.v("HYHTTP", "response");
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                    //break;
//                }
//
//
//                Log.v("HYHTTP", "after append "+response.length()+ " , " +in);
//                result = response.toString();
//                in.close();
//                JSONObject jObject = new JSONObject(result);
//                JSONArray data = jObject.getJSONArray("articles");
//                Log.v("HYHTTP", "articles " + data.length());
//                for (int i = 0; i < data.length(); i++) {
//                    news= new News();
//
//                    /*
//                    public static String id ;
//                    public static String title;
//                    public static String summary;
//                    public static String language;
//                    public static String country;
//                    public static String category;
//                    public static String tags;
//                    public static String article_url;
//                    public static String source_url;
//                    public static String source_name;
//                    public static String top_image;
//                    public static String publish_date;
//                    public static String authors;
//                    public static String meta_favicon;
//                    public static String trending;
//                    */
//
//                    news.id         = data.getJSONObject(i).getString(Common.ID);
//
//                    news.title      = data.getJSONObject(i).getString(Common.TITLE);
//                    news.summary    = data.getJSONObject(i).getString(Common.SUMMARY);
//                    news.language   = data.getJSONObject(i).getString(Common.LANGUAGE);
//
//                    news.country    = data.getJSONObject(i).getString(Common.COUNTRY);
//                    news.category   = data.getJSONObject(i).getString(Common.CATEGORY);
//                    news.tags       = data.getJSONObject(i).getString(Common.TAGS);
//                    news.article_url        = data.getJSONObject(i).getString(Common.ARTICLE_URL);
//                    news.source_url         = data.getJSONObject(i).getString(Common.SOURCE_URL);
//                    news.source_name        = data.getJSONObject(i).getString(Common.SOURCE_NAME);
//                    news.top_image          = data.getJSONObject(i).getString(Common.TOP_IMAGE);
//                    news.publish_date       = data.getJSONObject(i).getString(Common.PUBLISH_DATE);
//                    news.authors         = data.getJSONObject(i).getString(Common.AUTHORS);
//                    news. meta_favicon   = data.getJSONObject(i).getString(Common.META_FAVICON);
//                    news.trending        = data.getJSONObject(i).getString(Common.TRENDING);
//
//                    Log.v("HYHTTP", "**************************************");
//                    Log.v("HYHTTP", "Loadnews source Url : " + news.title);
//                    Log.v("HYHTTP", "Loadnews url : " + news.source_name);
//                    database.feedModel().addNews(news);
//                }
//
//
//            }
//            count = database.feedModel().getNewsCount();
//            //Log.v("LOADINSETTING","source : " + sourceName + " , "+  "COUNT : " + count);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            Log.v("HYHTTP", "MalformedURLException");
//            //isNetworkAvailable = false;
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.v("HYHTTP", "IOException");
//            //isNetworkAvailable = false;
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.v("HYHTTP", "JSONException");
//
//        } catch (NetworkErrorException e)
//        {
//            e.printStackTrace();
//            Log.v("HYHTTP", "NetworkErrorException");
//            //isNetworkAvailable = false;
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.v("HYHTTP", "Exception");
//            //isNetworkAvailable = false;
//        }
//


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
