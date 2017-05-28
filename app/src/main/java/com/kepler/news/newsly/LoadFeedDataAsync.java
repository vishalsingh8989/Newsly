package com.kepler.news.newsly;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.HashMap;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class LoadFeedDataAsync  extends AsyncTask<Void , Void, ArrayList<NewsStory>> {


    private NewsAdapter newsAdapter                         = null;
    private ArrayList<NewsStory> productsList = null;
    private static String  DESCRIPTION                       = "description";
    private static String  SOURCE                            = "source";
    private static String  SOURCENAME                       = "sourceName";
    private static String  IMAGEURL                        = "urltoimage";



    public LoadFeedDataAsync(NewsAdapter adapter) {
        this.newsAdapter = adapter;
        this.productsList = new ArrayList<>();
    }

    @Override
    protected ArrayList<NewsStory> doInBackground(Void... voids) {


        String articles = "notfound";
        String result = "";
        try {

            //String mUrl = "http://192.168.0.4:8000/?addtime=1495955972";
            String mUrl = "http://192.168.0.4:8000/";

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
                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(DESCRIPTION));
                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(SOURCENAME));
                    Log.v("HYHTTP", "*************************************");

                    story.setDescription(data.getJSONObject(i).getString(DESCRIPTION));
                    story.setSourceName(data.getJSONObject(i).getString(SOURCENAME));
                    story.setUrltoimage(data.getJSONObject(i).getString(IMAGEURL));


                    //map.put(DESCRIPTION, );
                    //map.put(SOURCE, );
                    productsList.add(story);
                }


            }







//            URL url = new URL("http://192.168.0.4:8000/");
//            HttpURLConnection mHttpConnection = (HttpURLConnection) url.openConnection();
//            mHttpConnection.connect();
//
//
//            mHttpConnection.setRequestMethod("GET");
//            int responseCode = mHttpConnection.getResponseCode();
//            Log.v("HYHTTP", "\nSending 'GET' request to URL : " + url);
//            Log.v("HYHTTP", "Response Code : " + responseCode);
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(mHttpConnection.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//
//            Log.v("HYHTTP", "response " + response.toString());
//            result = response.toString();
//
//            JSONObject jObject = new JSONObject(result);
//            JSONArray data = jObject.getJSONArray("articles");
//
//            Log.v("HYHTTP", "articles " + data.length());
//
//            for (int i = 0; i < data.length(); i++) {
//                HashMap<String, String> map = new HashMap<String, String>();
//
//                Log.v("HYHTTP", "*************************************");
//                Log.v("HYHTTP", "" + data.getJSONObject(i).getString(DESCRIPTION));
//                Log.v("HYHTTP", "" + data.getJSONObject(i).getString(SOURCENAME));
//                Log.v("HYHTTP", "*************************************");
//                map.put(DESCRIPTION, data.getJSONObject(i).getString(DESCRIPTION));
//                map.put(SOURCE, data.getJSONObject(i).getString(SOURCENAME));
//
//                productsList.add(map);
//            }


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

        newsAdapter.upDateEntries(result);
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
