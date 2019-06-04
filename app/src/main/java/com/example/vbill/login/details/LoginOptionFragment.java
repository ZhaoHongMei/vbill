package com.example.vbill.login.details;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
        View view  = inflater.inflate(R.layout.login_option_fragment, container, false);
        TextView phoneLogin = view.findViewById(R.id.login_by_phone);
        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_fragment,LoginUsePhoneFragment.getInstance())
                        .commit();
            }
        });
        return view;
    }
}
