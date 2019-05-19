package com.example.vbill.home.details;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.vbill.R;
import com.example.vbill.adapter.HomeChartSpinnerAdapter;

import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeChartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class HomeChartFragment extends Fragment {
    private static HomeChartFragment fragment;

    ImageView imageView;
    TextView textView;
    private Spinner incomePaymentSpinner;

    private OnFragmentInteractionListener mListener;

    public HomeChartFragment() {
        // Required empty public constructor
    }

    public static HomeChartFragment getInstance(){
        try{
            if(fragment == null){
                fragment = new HomeChartFragment();
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
            //get arguments
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        Log.d("HomeChartFragment", "onCreateView: ");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_chart, container, false);
        incomePaymentSpinner = view.findViewById(R.id.income_payment_spinner);
        //data : List<Map<String,Object>>
        List<Map<String,Object>> listMaps = HomeChartSpinnerAdapter.getListMaps();
        SimpleAdapter simpleAdapter = new SimpleAdapter(this.getContext(),listMaps, R.layout.chart_spinner_item, new String[]{"logo","itemName"},new int[]{R.id.spinner_image,R.id.spinner_text});
        incomePaymentSpinner.setAdapter(simpleAdapter);
        incomePaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Map selectItem  = (Map) incomePaymentSpinner.getItemAtPosition(position);
                imageView = view.findViewById(R.id.spinner_image);
                textView = view.findViewById(R.id.spinner_text);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.income));
                textView.setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
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
