package com.kepler.news.newsly;

import android.os.AsyncTask;
import android.util.Log;

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

public class LoadFeedDataAsync  extends AsyncTask<Void , Void, ArrayList<HashMap<String, String>>> {


    private NewsAdapter newsAdapter                         = null;
    private ArrayList<HashMap<String, String>> productsList = null;
    private static String DESCRIPTION                       = "description";
    private static String SOURCE                            = "source";
    private static String  SOURCENAME                       = "sourceName";



    public LoadFeedDataAsync(NewsAdapter adapter) {
        this.newsAdapter = adapter;
        this.productsList = new ArrayList<>();
    }

    @Override
    protected ArrayList<HashMap<String, String>> doInBackground(Void... voids) {


        String articles = "notfound";
        String result = "";
        try {
            URL url = new URL("http://192.168.0.4:8000/");
            HttpURLConnection mHttpConnection = (HttpURLConnection) url.openConnection();
            mHttpConnection.connect();


            mHttpConnection.setRequestMethod("GET");
            int responseCode = mHttpConnection.getResponseCode();
            Log.v("HYHTTP", "\nSending 'GET' request to URL : " + url);
            Log.v("HYHTTP", "Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(mHttpConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            Log.v("HYHTTP", "response " + response.toString());
            result = response.toString();

            JSONObject jObject = new JSONObject(result);
            JSONArray data = jObject.getJSONArray("articles");

            Log.v("HYHTTP", "articles " + data.length());

            for (int i = 0; i < data.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();

                Log.v("HYHTTP", "*************************************");
                Log.v("HYHTTP", "" + data.getJSONObject(i).getString(DESCRIPTION));
                Log.v("HYHTTP", "" + data.getJSONObject(i).getString(SOURCENAME));
                Log.v("HYHTTP", "*************************************");
                map.put(DESCRIPTION, data.getJSONObject(i).getString(DESCRIPTION));
                map.put(SOURCE, data.getJSONObject(i).getString(SOURCENAME));

                productsList.add(map);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Failed to check version");


        } catch (IOException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Failed to check version");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Failed to check version");


        }



        return productsList;


    }

    @Override
    protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
        super.onPostExecute(result);

        newsAdapter.upDateEntries(result);
    }
}
