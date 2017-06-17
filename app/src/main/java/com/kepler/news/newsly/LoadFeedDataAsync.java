package com.kepler.news.newsly;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.kepler.news.newsly.adapter.FoldingCellListAdapter;
import com.kepler.news.newsly.helper.Common;
import com.wang.avi.AVLoadingIndicatorView;

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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by vishaljasrotia on 28/05/17.
 */

public class LoadFeedDataAsync  extends AsyncTask<Void, Void, List<Object>> {


    private  int startFrom                                = 0;
    private LinkedHashMap<String, Integer> startMap        = null;
    private FoldingCellListAdapter foldingCellListAdapter   = null;
    private ArrayList<NewsStory> productsList               = null;
    private int    offset                                   = 30;
    private boolean onRefresh                               = false;
    private SharedPreferences pref                          = null;
    private MainActivity mainActivity                       = null;
    public static int oldsize                               = 0;
    private Context mContext                                = null;
    private String sourceName                       = "";



    public LoadFeedDataAsync( FoldingCellListAdapter adapter) {
        this.foldingCellListAdapter = adapter;
        this.productsList           = new ArrayList<>();
        this.onRefresh              = false;
        this.mainActivity           = mainActivity;

    }

    public LoadFeedDataAsync(FoldingCellListAdapter adapter, boolean onRefresh) {
        this.foldingCellListAdapter = adapter;
        this.productsList           = new ArrayList<>();
        this.onRefresh              = onRefresh;
        //this.mainActivity           = mainActivity;

    }

    public LoadFeedDataAsync(Context context, FoldingCellListAdapter adapter, boolean onRefresh, SharedPreferences pref, String sourceName , LinkedHashMap<String, Integer> startMap) {

        Log.v("LOADASYNC" ,sourceName + "OLDSIZE : " + sourceName + " , " +startFrom);
        this.foldingCellListAdapter = adapter;
        this.productsList           = new ArrayList<>();
        this.onRefresh              = onRefresh;
        this.pref                   = pref;
        //this.mainActivity           = mainActivity;
        this.oldsize                = adapter.getProductsList().size();
        this.sourceName             = sourceName;
        this.mContext               = context;
        this.startFrom               = startMap.get(sourceName);
        this.startMap               = startMap;




        //Log.v("LOADASYNC" ,sourceName + "OLDSIZE : " + sourceName + " , " +startFrom);

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                AVLoadingIndicatorView progressbar = (AVLoadingIndicatorView) mainActivity.findViewById(R.id.progress_bar);
//                progressbar.smoothToShow();
//
//            }
//        });
    }

    @Override
    protected List<Object> doInBackground(Void... voids) {

        List<Object> newList               = new ArrayList<>();
        String result   = "";
        try {

            boolean music            = pref.getBoolean(Common.MUSIC, true);
            boolean politics         = pref.getBoolean(Common.chipPolitics, true);
            boolean scienceandnature = pref.getBoolean(Common.chipScienceAndNatureSelected, true);



            //language
            boolean english   = pref.getBoolean(Common.english, true);
            boolean german    = pref.getBoolean(Common.german, true);
            boolean french    = pref.getBoolean(Common.french, true);
            boolean italian   = pref.getBoolean(Common.italian, true);


            //country
            boolean usa          = pref.getBoolean(Common.usa, true);
            boolean uk           = pref.getBoolean(Common.uk, true);
            boolean india        = pref.getBoolean(Common.india, true);
            boolean australia    = pref.getBoolean(Common.australia, true);
            boolean canada       = pref.getBoolean(Common.canada, true);
            boolean france       = pref.getBoolean(Common.france, true);
            boolean italy        = pref.getBoolean(Common.italy, true);
            boolean germany      = pref.getBoolean(Common.germany, true);



            //Log.v("LOADASYNCFEED", " start-offset" + MainActivity.start + " " +MainActivity.offset);


            //String baseUrl = "http://192.168.0.4:8000/";
//            int startFrom = startMap.get(sourceName);
           String baseUrl = "http://13.58.159.13/";
            String mUrl = baseUrl+ "?addtime=14955596"
                    +"&start="+String.valueOf(startFrom)
                    +"&offset="+String.valueOf(MainActivity.offset)
                    +"&sourceName="+sourceName.replace(" ", "%20")
                    +"&general=true"
                    +"&music="+music
                    +"&politics="+politics
                    +"&scienceandnature="+scienceandnature
                    +"&business=true"
                    +"&gaming=true"
                    +"&technology=true"
                    +"&entertainment=true"
                    +"&sport=true"
                    +"&english="+english
                    +"&german="+german
                    +"&french="+french
                    +"&italian="+italian
                    +"&hindi="+true
                    +"&usa="+usa
                    +"&uk="+uk
                    +"&india="+india
                    +"&australia="+australia
                    +"&canada="+canada
                    +"&france="+france
                    +"&italy="+italy
                    +"&germany="+germany

                    ;

                //url = &general=true&music=true&politics=false&scienceandnature=false&business=true&gaming=true&technology=true&entertainment=true&sport=true



            URL url = new URL(mUrl); // here is your URL path
            Log.v("MYURL",url + "");
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
                    NewsStory story = new NewsStory();

                    Log.v("HYHTTP", "*************************************");
                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(Common.DESCRIPTION));
                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(Common.SOURCENAME));
                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(Common.CATEGORY));
                    Log.v("HYHTTP", "*************************************");

                    story.setDescription(data.getJSONObject(i).getString(Common.DESCRIPTION));
                    story.setTitle(data.getJSONObject(i).getString(Common.TITLE));
                    story.setSourceName(data.getJSONObject(i).getString(Common.SOURCENAME));
                    story.setUrltoimage(data.getJSONObject(i).getString(Common.IMAGEURL));
                    story.setAuthor(data.getJSONObject(i).getString(Common.AUTHOR));
                    story.setCategory(data.getJSONObject(i).getString(Common.CATEGORY));
                    story.setUrl(data.getJSONObject(i).getString(Common.URL));
                    story.setSourceUrl(data.getJSONObject(i).getString(Common.SOURCEURL));
                    story.setPublishedat(data.getJSONObject(i).getString(Common.PUBLISHEDAT));
                    newList.add(story);
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

        return newList;
    }




    @Override
    protected void onPostExecute(List<Object> result) {
        super.onPostExecute(result);
        result = addNativeExpressAds(result);
        startMap.put(sourceName , foldingCellListAdapter.getProductsList().size());
        Random rn = new Random(15L);
        //Collections.shuffle(result, rn);
        foldingCellListAdapter.upDateEntries(result , onRefresh);



        //MainActivity.calledOn = foldingCellListAdapter.getProductsList().size();

        Log.v("LOADASYNC" , "size onPost : " + sourceName + " " + startMap.get(sourceName));
//        mainActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                AVLoadingIndicatorView progressbar = (AVLoadingIndicatorView) mainActivity.findViewById(R.id.progress_bar);
//                progressbar.smoothToHide();
//            }
//        });


    }


    private List<Object> addNativeExpressAds(List<Object> result){
        for(int i = oldsize ; i < result.size(); i +=1)
        {
            if(i%12==0 && i!=0) {
                NativeExpressAdView adView = new NativeExpressAdView(mContext);
                adView.setAdSize(new AdSize(300, 100));

                adView.setAdUnitId("ca-app-pub-5223778660504166/2968121932");
                adView.loadAd(new AdRequest.Builder()
                        .addTestDevice("32C278BA97F2B33C41A02691587B4F29")
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build());
                result.add(i, adView);
            }

        }

        return result;


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
