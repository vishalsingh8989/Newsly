package com.kepler.news.newsly.IntroFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kepler.news.newsly.R;
import com.kepler.news.newsly.adapter.CountryAdapter;
import com.kepler.news.newsly.helper.Common;
import com.kepler.news.newsly.helper.LoadImagesAdapter;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoadImagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoadImagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoadImagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LoadImagesFragment.OnFragmentInteractionListener mListener;


    private ArrayList<Boolean> languageSelected = new ArrayList<>();
    private Context mContext = null;
    private SharedPreferences mPreferences = null;
    private int[] loadImageOptionList = {Common.ALWAYS , Common.NEVER, Common.ONWIFI};

    public LoadImagesFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoadImagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoadImagesFragment newInstance(String param1, String param2) {
        LoadImagesFragment fragment = new LoadImagesFragment();
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
        View v = inflater.inflate(R.layout.fragment_load_images, container, false);

        ListView listView = (ListView)v.findViewById(R.id.loadimagelist);

        mPreferences = mContext.getSharedPreferences(Common.PREFERENCES, MODE_PRIVATE);

        int loadImages = mPreferences.getInt(Common.LOADIMAGE, 0);
        Log.v("MAINWIFISTATE" , "loadimage value : " + loadImages);

        ArrayList<String> loadImagesOption = new ArrayList<>();



        loadImagesOption.add("Always");
        loadImagesOption.add("Never");
        loadImagesOption.add("Only on WIFI");


        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        LoadImagesAdapter adapter = new LoadImagesAdapter(getActivity(),loadImagesOption );

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SharedPreferences.Editor editor = mPreferences.edit();
                Log.v("MAINWIFISTATE" , "loadimage value put : " + loadImageOptionList[i]);

                editor.putInt(Common.LOADIMAGE , loadImageOptionList[i]);
                editor.commit();
            }
        });



        listView.setItemChecked(loadImages, true);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentLoadImagesFragmentInteraction(uri);
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
        void onFragmentLoadImagesFragmentInteraction(Uri uri);
    }
}
