package com.kepler.news.newsly.IntroFragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.kepler.news.newsly.LoadNewSourceAsync;
import com.kepler.news.newsly.R;
import com.kepler.news.newsly.databaseHelper.NewsSourceDatabase;

public class HelloFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String TAG = "HelloFrag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public HelloFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HelloFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HelloFragment newInstance(String param1, String param2) {
        HelloFragment fragment = new HelloFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_hello, container, false);
        TextView app_name = (TextView)v.findViewById(R.id.app_name_middle);
        TextView details = (TextView)v.findViewById(R.id.details);

        Typeface tp = Typeface.createFromAsset(getActivity().getAssets(), "fonts/attic.ttf");

        app_name.setTypeface(tp);
        details.setTypeface(tp);


        return v;



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
        void onHelloFragmentInteraction(Uri uri, String mFrag);
    }
}
