package com.example.vbill.login.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vbill.R;

public class LoginUsePhoneFragment extends Fragment {
    private static LoginUsePhoneFragment fragment;

    public  LoginUsePhoneFragment(){
        //constructor
    }
    public static LoginUsePhoneFragment getInstance(){
        try{
            if(fragment == null){
                fragment = new LoginUsePhoneFragment();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.login_phone_fragment, container, false);
    }
}
