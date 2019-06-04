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

import com.example.vbill.R;
import com.example.vbill.adapter.ChildBillRecyclerAdapter;
import com.example.vbill.adapter.ParentBillRecyclerAdapter;
import com.example.vbill.bean.ChildBill;
import com.example.vbill.bean.ParentBill;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeDetailFragment extends Fragment {
    private static HomeDetailFragment fragment;
    private  String[] listData= {
            "碗形猫抓板耐磨猫爪垫大号猫抓板猫咪用品",
            "可爱长条枕抱枕睡觉可拆洗",
            "韩版女装气质中长裙雪纺裙",
            "奥克斯迷你电风扇价格不统一",
            "你想要的都没有其实全部都是甜甜圈",
            "你想要的都没有其实全部都是甜甜圈",
            "你想要的都没有其实全部都是甜甜圈",
            "你想要的都没有其实全部都是甜甜圈",

    };

    private OnFragmentInteractionListener mListener;
    private List<ParentBill> listParent;
    private List<ChildBill> listChild;
    private ChildBillRecyclerAdapter adapterChild;

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

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_detail, container, false);
        ImageButton imageButton = (ImageButton)view.findViewById(R.id.createImageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("android.intent.action.Create");
                intent.putExtra("position",String.valueOf(1));
                startActivity(intent);
            }
        });
        queryBillListFromServer();
        //initRecyclerAdapter
        initBillList2();
        //parent
        RecyclerView recyclerParent = view.findViewById(R.id.parent_bill);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext());
        recyclerParent.setLayoutManager(layoutManager2);
        recyclerParent.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        ParentBillRecyclerAdapter billByDayRecyclerAdapter = new ParentBillRecyclerAdapter(getContext(),listParent);
        recyclerParent.setAdapter(billByDayRecyclerAdapter);
        return view;
    }
    public List initBillList(){
        listChild = new ArrayList<>();
        for(int i=0;i<listData.length;i++){
            ChildBill childBill = new ChildBill("工资","1512","1",R.drawable.income);
            listChild.add(childBill);
        }
        return listChild;
    }

    private List initBillList2(){
        listParent = new ArrayList<>();
        for(int i=0;i<2;i++){
            ParentBill parentBill = new ParentBill("2019年4月20日",initBillList());
            listParent.add(parentBill);
        }
        return listParent;
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
    private void queryBillListFromServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("M","4")
                        .build();
                Request getBillListRq = new Request.Builder()//""//
                        .url("http://192.168.0.103:8089/resources/accountlist")
                        .post(requestBody)
                        .build();
                try {
                    Response response = client.newCall(getBillListRq).execute();
                    String stringResonese = response.toString();
                    Log.d("response11111111", stringResonese);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
