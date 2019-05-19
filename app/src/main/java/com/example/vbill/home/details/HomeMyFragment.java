package com.example.vbill.home.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vbill.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeMyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeMyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMyFragment extends Fragment {
    private static HomeMyFragment fragment;
    private OnFragmentInteractionListener mListener;
    private boolean loginFlag = false;

    public HomeMyFragment() {
        // Required empty public constructor
    }

    public static HomeMyFragment newInstance(String param1, String param2) {
        HomeMyFragment fragment = new HomeMyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeMyFragment getInstance(){
        try{
            if(fragment == null){
                fragment = new HomeMyFragment();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        fragment.setArguments(arg);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View homeMyView = inflater.inflate(R.layout.fragment_home_my, container, false);
        LinearLayout homeMylogin = homeMyView.findViewById(R.id.home_my_login);
        homeMylogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.Login");
                startActivity(intent);
            }
        });
        return homeMyView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
