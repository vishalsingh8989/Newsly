package com.kepler.news.newsly.IntroFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.github.paolorotolo.appintro.AppIntroFragment;
import com.kepler.news.newsly.R;
import com.kepler.news.newsly.adapter.CountryAdapter;
import com.kepler.news.newsly.helper.Common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    private OnFragmentInteractionListener mListener;



    private  ArrayList<Boolean> countrySelected = new ArrayList<>();
    private Context mContext = null;
    private String[] countries = {"Global" , Common.usa,Common.uk, Common.india, Common.australia, Common.canada, Common.french, Common.italy, Common.germany};
    private SharedPreferences mPreferences                  = null;
    private SharedPreferences.Editor editor                 = null;


    public NewsSourceFragment() {
        // Required empty public constructor
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_country, container, false);

        final ListView countryListView = (ListView) v.findViewById(R.id.countrylist);

        //String[] countryList = new String[] {"Global","United States of America","United Kingdom","India", "Australia"};


        ArrayList<String> countryList = new ArrayList<>();
        final ArrayList<String> countries = new ArrayList<>();

        countryList.clear();
        countries.clear();


        countryList.add("Global");
        countries.add("Global");


        LinkedHashMap<String, String> choosen = Common.createChoosenMap();
        for (Map.Entry<String, String> mapEntry : choosen.entrySet()) {
            //myMap.put(mapEntry.getKey(), mapEntry.getValue());
            countryList.add(mapEntry.getKey());
            countries.add(mapEntry.getKey());

        }
//        countryList.add("USA");
//        countryList.add("UK");
//        countryList.add("India");
//        countryList.add("Australia");
//        countryList.add("Canada");
//        countryList.add("France");
//        countryList.add("Italy");
//        countryList.add("Germany");

        mPreferences = mContext.getSharedPreferences(Common.PREFERENCES , MODE_PRIVATE);

        countrySelected.clear();
        for (int index = 0;index <countries.size() ; index++) {
            boolean  checked = mPreferences.getBoolean(countries.get(index), false);
            countrySelected.add(checked);
        }




        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,countryList);

        CountryAdapter adapter = new CountryAdapter(getActivity(),countryList );

        countryListView.setAdapter(adapter);
        countryListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);



        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.v("multichoice" , i + ", " + view);
                countryListView.setItemChecked(i, !countrySelected.get(i));
                countrySelected.set(i, !countrySelected.get(i));
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(countries.get(i), countrySelected.get(i));
                editor.commit();

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



        Log.v("COUNTRYFRAG" , "" + countrySelected.size() + " " + countries.size());
        SharedPreferences.Editor editor = mPreferences.edit();
        for(int index = 0 ; index < countrySelected.size();index++) {
            countryListView.setItemChecked(index, countrySelected.get(index));
            editor.putBoolean(countries.get(index), countrySelected.get(index));
        }
        editor.commit();
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
}