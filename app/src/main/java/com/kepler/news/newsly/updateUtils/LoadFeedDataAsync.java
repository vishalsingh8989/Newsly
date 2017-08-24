package com.kepler.news.newsly.updateUtils;

/**
 * Created by vishaljasrotia on 8/24/17.
 */


    import android.accounts.NetworkErrorException;
    import android.content.Context;
    import android.content.SharedPreferences;
    import android.os.AsyncTask;
    import android.util.Log;

    import com.google.android.gms.ads.AdRequest;
    import com.google.android.gms.ads.AdSize;
    import com.google.android.gms.ads.NativeExpressAdView;
    import com.kepler.news.newsly.MainActivity;
    import com.kepler.news.newsly.NewsStory;
    import com.kepler.news.newsly.R;
    import com.kepler.news.newsly.ViewPagerFragments.DemoFragment;
    import com.kepler.news.newsly.adapter.FoldingCellListAdapter;
    import com.kepler.news.newsly.databaseHelper.News;
    import com.kepler.news.newsly.databaseHelper.NewsDatabase;
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
    private String num_of_likes;


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

    public LoadFeedDataAsync(DemoFragment fragment , Context context, FoldingCellListAdapter adapter, boolean onRefresh, SharedPreferences pref, String sourceName , LinkedHashMap<String, Integer> startMap, NewsDatabase database) {

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
        this.fragment               = fragment;
        this.database               = database;

        isNetworkAvailable = true;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        try{
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AVLoadingIndicatorView progressbar = (AVLoadingIndicatorView) fragment.getActivity().findViewById(R.id.progress_bar);
                    progressbar.smoothToShow();

                }
            });
        }catch (Exception e)
        {
        }
    }

    @Override
    protected List<Object> doInBackground(Void... voids) {

        List<Object> newList               = new ArrayList<>();
        String result   = "";
        int count = database.feedModel().getNewsCount();
        Log.v("NEWSSOURCEDATABASE","COUNT : " + count);
        try {


            String baseUrl = "http://13.58.159.13/";
            String mUrl = baseUrl+ "?addtime=14955596"
                    +"&start="+String.valueOf(startFrom)
                    +"&offset="+String.valueOf(MainActivity.offset)
                    +"&sourceName="+sourceName.replace(" ", "%20");

            URL url1 = new URL(mUrl); // here is your URL path
            Log.v("MYURL",url1 + "");
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
                    NewsStory story = new NewsStory();

//                    Log.v("HYHTTP", "*************************************");
//                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(Common.DESCRIPTION));
//                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(Common.SOURCENAME));
//                    Log.v("HYHTTP", "" + data.getJSONObject(i).getString(Common.CATEGORY));
//                    Log.v("HYHTTP", "*************************************");


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



                    story.setId(id);

                    story.setDescription(description);
                    story.setTitle(title);
                    story.setSourceName(sourceName);
                    story.setUrltoimage(urltoimage);
                    story.setAuthor(author);
                    story.setCategory(category);
                    story.setUrl(url);
                    story.setSourceUrl(sourceurl);
                    story.setPublishedat(publishedat);
                    story.setLanguage(language);
                    story.setCountry(country);
                    story.setNum_of_likes(num_of_likes);

                    newList.add(story);

                    database.feedModel().addNews(new News(id , title, description, publishedat ,sourceName,sourceName, url, urltoimage, author, language, country, category, Integer.parseInt(addtime), Integer.parseInt(num_of_likes), false, false));
                }


            }
            count = database.feedModel().getNewsCount();
            Log.v("NEWSSOURCEDATABASE","COUNT : " + count);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "MalformedURLException");
            isNetworkAvailable = false;


        } catch (IOException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "IOException");
            isNetworkAvailable = false;

        } catch (JSONException e) {
            e.printStackTrace();
            Log.v("HYHTTP", "JSONException");

        } catch (NetworkErrorException e)
        {
            e.printStackTrace();
            Log.v("HYHTTP", "NetworkErrorException");
            isNetworkAvailable = false;


        } catch (Exception e) {
            e.printStackTrace();
            Log.v("HYHTTP", "Exception");
            isNetworkAvailable = false;
        }

        return newList;
    }




    @Override
    protected void onPostExecute(List<Object> result) {
        super.onPostExecute(result);
        result = addNativeExpressAds(result);
        //startMap.put(sourceName , foldingCellListAdapter.getProductsList().size());
        Random rn = new Random(15L);
        //Collections.shuffle(result, rn);

        //foldingCellListAdapter.upDateEntries(result , onRefresh);


        //DemoFragment.avLoadingIndicatorView.smoothToHide();
        //MainActivity.calledOn = foldingCellListAdapter.getProductsList().size();

        Log.v("LOADASYNC" , "size onPost : " + sourceName + " " + startMap.get(sourceName));

        try {
            fragment.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AVLoadingIndicatorView progressbar = (AVLoadingIndicatorView) fragment.getActivity().findViewById(R.id.progress_bar);
                    progressbar.smoothToHide();


                }
            });
        }catch (Exception e)
        {

        }


//        if(!isNetworkAvailable ) {
//            fragment.showNetworkNotAvailableDialog();
//        }

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
