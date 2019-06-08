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
import com.example.vbill.login.LoginActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

public class LoginOptionFragment extends Fragment {
    private  static LoginOptionFragment fragment;
    private TextView phoneLogin;
    private Button wechatLoginBtn;

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
        //手机号登陆
        phoneLogin = view.findViewById(R.id.login_by_phone);
        phoneLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.login_fragment,LoginUsePhoneFragment.getInstance())
                        .commit();
            }
        });

        //微信登陆
        wechatLoginBtn = view.findViewById(R.id.login_by_wechat);
        wechatLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转到微信得到授权的页面
                getAuthFromWechat();
            }
        });
        return view;
    }
    public void getAuthFromWechat(){
        //看api在哪里注册
        if(!LoginActivity.api.isWXAppInstalled()){
            //显示未安装的信息
            return;
        }
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
        req.state = "none";
        LoginActivity.api.sendReq(req);
    }
}
