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
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

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
        Log.d(TAG, "queryBillListFromServer: ");
        String address= Constants.SERVER_PREFIX + "v1/esc/customers/itemsByCustomerId?customerId=123";
        HttpUtil.sendOkHttpGetRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: "+ responseData);
            }
        });
    }

    public void dealWithResData(String response){
        Gson gson=new Gson();
        JsonObject jsonObject = gson.fromJson(response,JsonObject.class);
        List tempList = new ArrayList();
        tempList =gson.fromJson(jsonObject.get("item"),new TypeToken<List>() {
        }.getType());
        for(int i =0;i<tempList.size();i++){
            Map itmeMap = (Map) tempList.get(i);
//            if(itmeMap.)
        }
    }
}
