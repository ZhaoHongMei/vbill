package com.example.vbill.home.details;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbill.R;
import com.example.vbill.adapter.ParentBillRecyclerAdapter;
import com.example.vbill.bean.ParentBill;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeDetailFragment extends Fragment {
    private static final String TAG = "HomeDetailFragment";
    private static HomeDetailFragment fragment;
    private String[] listData ={"1","2","3","4"};

    private OnFragmentInteractionListener mListener;
    private List<ParentBill> listParent = new ArrayList<ParentBill>();
    private RecyclerView recyclerParent;
    private ImageButton imageButton;
    private TextView homeDetailIncome,homeDetailOutcome,homeDetailBalance;

    public HomeDetailFragment() {
        // Required empty public constructor
    }

    public static HomeDetailFragment getInstance(){
        try{
            if(fragment == null){
                fragment = new HomeDetailFragment();
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
            //要传的参数
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: start");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_detail, container, false);
        imageButton = (ImageButton)view.findViewById(R.id.createImageButton);
        recyclerParent = view.findViewById(R.id.parent_bill);
        homeDetailIncome = view.findViewById(R.id.home_detail_income);
        homeDetailOutcome = view.findViewById(R.id.home_detail_outcome);
        homeDetailBalance = view.findViewById(R.id.home_detail_balance);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.Create");
                intent.putExtra("position",String.valueOf(1));
                startActivity(intent);
            }
        });
        queryDataFromServer();
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
    @Override
    public void onResume() {
        super.onResume();
        //再次点击fragment时候，清空之前的数据，以免重复
        if(listParent!=null){
            listParent.clear();
        }
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

    private void queryDataFromServer(){
        Log.d(TAG, "queryDataFromServer: ");
        String address= Constants.SERVER_PREFIX + "v1/esc/itemsByCustomerId?customerId=123";
        HttpUtil.sendOkHttpGetRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         Gson gson=new Gson();
                         JsonObject jsonObject = gson.fromJson(responseData,JsonObject.class);
                         String statusCode =gson.fromJson(jsonObject.get("statusCode"), new TypeToken<String>() {
                         }.getType());
                         if (responseData != null && "200".equals(statusCode)) {
                             try {
                                 dealWithResData(responseData);
                                 dealWithAdapter();
                                 dealWithTextView(responseData);
                             } catch (JSONException e) {
                                 e.printStackTrace();
                             }
                         } else {
                             Toast.makeText(getActivity(), "获取清单失败", Toast.LENGTH_SHORT).show();
                         }
                     }
                });
                Log.d(TAG, "onResponse: "+ responseData);
            }

        });
    }

    public void dealWithResData(String response) throws JSONException {
        Gson gson=new Gson();
        JsonObject jsonObject = gson.fromJson(response,JsonObject.class);
        Map tempMap = gson.fromJson(jsonObject.get("data"), new TypeToken<Map>() {}.getType());
        List tempList = (List) tempMap.get("items");
        for(int i=0;i<tempList.size();i++){
            Map<String,List> parentItemMap = (Map)tempList.get(i);
            String date=String.valueOf(parentItemMap.get("date"));
            Log.d(TAG, "dealWithResData: "+parentItemMap.get("childItem"));
            ParentBill parentBill = new ParentBill(date,(List) parentItemMap.get("childItem"));
            listParent.add(parentBill);
        }
    }

    public void dealWithAdapter(){
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerParent.setLayoutManager(layoutManager2);
        recyclerParent.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        ParentBillRecyclerAdapter billByDayRecyclerAdapter = new ParentBillRecyclerAdapter(getContext(),listParent);
        recyclerParent.setAdapter(billByDayRecyclerAdapter);
    }
    public void dealWithTextView(String response){
        Gson gson=new Gson();
        JsonObject jsonObject = gson.fromJson(response,JsonObject.class);
        Map tempMap = gson.fromJson(jsonObject.get("data"), new TypeToken<Map>() {}.getType());
        String income = (String)((Map)tempMap.get("accountSummary")).get("income");
        String outcome = (String)((Map)tempMap.get("accountSummary")).get("outcome");
        String balance = String.valueOf(Double.valueOf(income) - Double.valueOf(outcome));
        homeDetailIncome.setText(income);
        homeDetailOutcome.setText(outcome);
        homeDetailBalance.setText(balance);
    }
}
