package com.example.vbill.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.vbill.R;
import com.example.vbill.login.details.LoginOptionFragment;
import com.example.vbill.login.details.LoginUsePhoneFragment;
import com.example.vbill.util.Constants;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{
    public static IWXAPI api;
    private LoginOptionFragment loginOptionFragment;
    private LoginUsePhoneFragment loginPhoneFragment;

    private  void registerToWechat(){
        //注册appid
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        api.registerApp(Constants.APP_ID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        //注册appid到微信
        registerToWechat();
        initFragment();
        //go to phone number login fragment
    }

    private void initFragment(){
        loginOptionFragment = LoginOptionFragment.getInstance();
        loginPhoneFragment = LoginUsePhoneFragment.getInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_fragment,loginOptionFragment);
        fragmentTransaction.commit();
    }

    public void  replacementFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_fragment,fragment);
        fragmentTransaction.commit();
    }
}

