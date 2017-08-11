package com.kepler.news.newsly.IntroFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArraySet;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.kepler.news.newsly.LoadNewSourceAsync;
import com.kepler.news.newsly.R;
import com.kepler.news.newsly.adapter.CountryAdapter;
import com.kepler.news.newsly.databaseHelper.AppDatabase;
import com.kepler.news.newsly.databaseHelper.Feed;
import com.kepler.news.newsly.helper.Common;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;
import static com.thefinestartist.utils.content.ContextUtil.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsSourceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsSourceFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView countryListView = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;



    private  ArrayList<String> countrySelected = new ArrayList<>();
    private Context mContext = null;
    private String[] countries = {"Global" , Common.usa,Common.uk, Common.india, Common.australia, Common.canada, Common.french, Common.italy, Common.germany};
    private SharedPreferences mPreferences                  = null;
    private SharedPreferences.Editor editor                 = null;
    private LoadNewSourceAsync loadNewSourceAsync          = null;
    private AppDatabase database = null;

    ArrayList<String> mSources = new ArrayList<>();
    private List<Feed> feeds;
    private List<Feed> allfeeds;
    private String TAG = "";
    private int priority = 0;

    public NewsSourceFragment() {
        // Required empty public constructor
    }
    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CountryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsSourceFragment newInstance(String param1, String param2) {
        NewsSourceFragment fragment = new NewsSourceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mContext = getContext();
        mSources.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_country, container, false);

        countryListView = (ListView) v.findViewById(R.id.countrylist);

        //String[] countryList = new String[] {"Global","United States of America","United Kingdom","India", "Australia"};

        database = AppDatabase.getDatabase(getActivity().getApplicationContext());
        feeds = database.feedModel().getAllFeeds();
//        final ArrayList<Object> countryList = new ArrayList<>();
//
//        countryList.clear();


//        LinkedHashMap<String, String> choosen = Common.createChoosenMap();
//        for (Map.Entry<String, String> mapEntry : choosen.entrySet()) {
//            //myMap.put(mapEntry.getKey(), mapEntry.getValue());
//            countryList.add(mapEntry.getKey());
//            countries.add(mapEntry.getKey());
//
//        }

            mPreferences = mContext.getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE);
//
//        countrySelected.clear();
//        for (int index = 0;index <countries.size() ; index++) {
//            boolean  checked = mPreferences.getBoolean(countries.get(index), false);
//            countrySelected.add(checked);
//        }




        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,countryList);

        CountryAdapter adapter = new CountryAdapter(getActivity(),feeds);

        countryListView.setAdapter(adapter);

        countryListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);

        loadNewSourceAsync = new LoadNewSourceAsync(this, adapter, database);

        loadNewSourceAsync.execute();


        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("multichoice" , i + ", " + feeds.get(i).newsSource);
                boolean subscribed = !database.feedModel().getFeed(feeds.get(i).newsSource).get(0).subscribed;
                countryListView.setItemChecked(i, subscribed);

                int priority = 0;
                if(subscribed) {
                    priority = database.feedModel().getMaxPriority() + 1;
                }
                database.feedModel().updateTask(new Feed(feeds.get(i).newsSource, subscribed, priority));



            }
        });

        countryListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {

            }
        });

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onNewsSourceFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.v("FRAGMENT" , "Country onDetach");
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onNewsSourceFragmentInteraction(Uri uri);
    }


    public void setChecked(ArrayList<Feed> objects)
    {




//        for(int index = 0 ; index < objects.size();index++) {
//            if(mPreferences.getBoolean((String) objects.get(index), false))
//            {
//                countryListView.setItemChecked(index, true);
//                mSources.add((String) objects.get(index));
//            }
//
//        }
//        String strBecons = new Gson().toJson(mSources);
//
//        mPreferences.edit().putString("BECON_LIST", strBecons).apply();
//
//        Log.v("GsonTest", "" + strBecons);
//
//
//
        allfeeds = database.feedModel().getAllFeeds();
        int idx = 0;
        for (Feed obj: allfeeds) {

            //Feed feed = Feed.builder().setNewsSource((String)obj).setSubscribed(true).setPriority(idx).build();
            if(obj.subscribed)
            {
                Log.v("RoomAdd", obj.newsSource + " ,  " + obj.priority);
                countryListView.setItemChecked(idx ,  true);
            }
            idx++;



        }
//
//        List<Feed> allFeeds = database.feedModel().getAllFeeds();
//
//        Log.v("RoomAdd", "alltask : " + allFeeds.size());

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.v("NEWSOURCE","onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("NEWSOURCE","onStart");
    }
}
