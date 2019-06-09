package com.example.vbill.home.details;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.vbill.R;
import com.example.vbill.adapter.DiscoveryMenuAdapter;
import com.example.vbill.bean.DiscoveryMenu;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeDiscoveryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeDiscoveryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeDiscoveryFragment extends Fragment {
    private static HomeDiscoveryFragment fragment;
    Context context;
    private RecyclerView recyclerView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeDiscoveryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeChartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeDiscoveryFragment newInstance(String param1, String param2) {
        HomeDiscoveryFragment fragment = new HomeDiscoveryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static HomeDiscoveryFragment getInstance(){
        try{
            if(fragment == null){
                fragment = new HomeDiscoveryFragment();
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
//        setContentView(R.layout.fragment_home_discovery);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_discovery, container, false);
        recyclerView = view.findViewById(R.id.home_discovery_menu);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        DiscoveryMenuAdapter discoveryMenuAdapter = new DiscoveryMenuAdapter(initDescoveryMenu());
        recyclerView.setAdapter(discoveryMenuAdapter);

        return view;
    }

    private List<DiscoveryMenu> initDescoveryMenu() {
        DiscoveryMenu discoveryMenu1 = new DiscoveryMenu(R.drawable.remenhuodong, "时光手账");
        DiscoveryMenu discoveryMenu2 = new DiscoveryMenu(R.drawable.remenhuodong, "美食");
        DiscoveryMenu discoveryMenu3 = new DiscoveryMenu(R.drawable.remenhuodong, "娱乐");
        DiscoveryMenu discoveryMenu4 = new DiscoveryMenu(R.drawable.remenhuodong, "旅游");
        DiscoveryMenu discoveryMenu5 = new DiscoveryMenu(R.drawable.remenhuodong, "热门活动");
        DiscoveryMenu discoveryMenu6 = new DiscoveryMenu(R.drawable.shouzhangben, "限时特惠");
        DiscoveryMenu discoveryMenu7 = new DiscoveryMenu(R.drawable.shouzhangben, "投资");
        DiscoveryMenu discoveryMenu8 = new DiscoveryMenu(R.drawable.shouzhangben, "出行");
        DiscoveryMenu discoveryMenu9 = new DiscoveryMenu(R.drawable.shouzhangben, "购物");
        DiscoveryMenu discoveryMenu10 = new DiscoveryMenu(R.drawable.shouzhangben, "全部");

        List<DiscoveryMenu> discoveryMenus = new ArrayList<>();
        discoveryMenus.add(discoveryMenu1);
        discoveryMenus.add(discoveryMenu2);
        discoveryMenus.add(discoveryMenu3);
        discoveryMenus.add(discoveryMenu4);
        discoveryMenus.add(discoveryMenu5);
        discoveryMenus.add(discoveryMenu6);
        discoveryMenus.add(discoveryMenu7);
        discoveryMenus.add(discoveryMenu8);
        discoveryMenus.add(discoveryMenu9);
        discoveryMenus.add(discoveryMenu10);
        return discoveryMenus;
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
