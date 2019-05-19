package com.example.vbill.login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.vbill.R;
import com.example.vbill.login.details.LoginOptionFragment;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    private LoginOptionFragment loginOptionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }
        initFragment();
        //go to phone number login fragment
        TextView phoneLogin = findViewById(R.id.login_by_phone);
//        phoneLogin.setOnClickListener(new );
    }

    private void initFragment(){
        loginOptionFragment = LoginOptionFragment.getInstance();
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

