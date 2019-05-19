package com.example.vbill.login.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vbill.R;

public class LoginOptionFragment extends Fragment {
    private  static LoginOptionFragment fragment;

    public LoginOptionFragment(){
        //constructor
    }
    public static LoginOptionFragment getInstance(){
        try{
            if(fragment == null){
                fragment = new LoginOptionFragment();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_option_fragment, container, false);
    }
}
