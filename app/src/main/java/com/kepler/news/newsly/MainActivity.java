package com.kepler.news.newsly;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity {

    private ArrayList<HashMap<String, String>> productsList = null;
    private ListView listView = null;
    private static String DESCRIPTION = "description";
    private static String SOURCE = "source";
    private static String  SOURCENAME = "sourceName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        productsList = new ArrayList<HashMap<String, String>>();
        listView = (ListView)findViewById(R.id.list1);

        ArrayList<HashMap<String, String>> productsList;
        new loadASync().execute();





    }


    public  class loadASync extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {
            checkVersion();
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            MainActivity.this, productsList,
                            R.layout.list_item, new String[] { DESCRIPTION,
                            SOURCENAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });





        }
    }

    private void setListAdapter(ListAdapter adapter) {

        listView.setAdapter(adapter);



    }

    private void checkVersion() {

        String articles = "notfound";
        String result = "";
        try {
            URL url = new URL("http://192.168.0.4:8000/");
            HttpURLConnection mHttpConnection = (HttpURLConnection) url.openConnection();
            mHttpConnection.connect();
            mHttpConnection.setRequestMethod("GET");
            int responseCode = mHttpConnection.getResponseCode();
            Log.v("HYHTTP","\nSending 'GET' request to URL : " + url);
            Log.v("HYHTTP","Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(mHttpConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();


            Log.v("HYHTTP","response " + response.toString());
            result = response.toString();

            JSONObject jObject = new JSONObject(result);
            JSONArray data = jObject.getJSONArray("articles");

            Log.v("HYHTTP","articles " + data.length());

            for(int i = 0 ; i < data.length() ; i ++)
            {
                HashMap<String, String> map = new HashMap<String, String>();

                Log.v("HYHTTP", "*************************************");
                Log.v("HYHTTP" , "" + data.getJSONObject(i).getString(DESCRIPTION));
                Log.v("HYHTTP" , "" + data.getJSONObject(i).getString(SOURCENAME));
                Log.v("HYHTTP", "*************************************");
                map.put(DESCRIPTION, data.getJSONObject(i).getString(DESCRIPTION));
                map.put(SOURCE, data.getJSONObject(i).getString(SOURCENAME));

                productsList.add(map);
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Failed to check version" );


        } catch (IOException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Failed to check version" );

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Failed to check version");


        }

    }





}
