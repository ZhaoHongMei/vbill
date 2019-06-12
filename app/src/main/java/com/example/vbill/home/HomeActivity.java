package com.example.vbill.home;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.vbill.R;
import com.example.vbill.home.details.HomeChartFragment;
import com.example.vbill.home.details.HomeCreateFragment;
import com.example.vbill.home.details.HomeDetailFragment;
import com.example.vbill.home.details.discovery.HomeDiscoveryFragment;
import com.example.vbill.home.details.HomeLoginFragment;
import com.example.vbill.home.details.HomeMyFragment;

import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements HomeDetailFragment.OnFragmentInteractionListener,
        HomeChartFragment.OnFragmentInteractionListener,
        HomeCreateFragment.OnFragmentInteractionListener,
        HomeDiscoveryFragment.OnFragmentInteractionListener,
        HomeMyFragment.OnFragmentInteractionListener,
        HomeLoginFragment.OnFragmentInteractionListener{
    private static final String TAG = "HomeActivity";

    private Fragment homeDetailFragment;
    private Fragment homeChartFragment;
    private Fragment homeDiscoveryFragment;
    private Fragment homeMyFragment;
    private Fragment homeLoginFragment;
    private boolean login = false;
    private List<Map<String,Object>> listMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        setSpecialItemImageSize(navigation,100,100,2);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);

        //set fragments
        initFragment();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_detail:
                    replacementFragment(homeDetailFragment);
                    return true;
                case R.id.home_chart:
                    replacementFragment(homeChartFragment);
                    return true;
//                case R.id.home_create:
//                    replacementFragment(new HomeCreateFragment());
//                    return true;
                case R.id.home_discovery:
                    replacementFragment(homeDiscoveryFragment);
                    return true;
                case R.id.home_my:
                    replacementFragment(homeMyFragment);
                    return true;
            }
            return false;
        }
    };
    private void initFragment(){
//        homeDetailFragment= HomeDetailFragment.getInstance();
//        homeChartFragment = HomeChartFragment.getInstance();
//        homeDiscoveryFragment = HomeDiscoveryFragment.getInstance();
//        homeMyFragment = HomeMyFragment.getInstance();
//        homeLoginFragment = HomeLoginFragment.getInstance();
        homeDetailFragment = new HomeDetailFragment();
        homeChartFragment = new HomeChartFragment();
        homeDiscoveryFragment = new HomeDiscoveryFragment();
        homeMyFragment = new HomeMyFragment();
        homeLoginFragment = new HomeLoginFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(login){
            fragmentTransaction.replace(R.id.home_fragment_container,homeLoginFragment);
        }else{
            fragmentTransaction.replace(R.id.home_fragment_container,homeDetailFragment);
        }
        fragmentTransaction.commit();
        Log.d(TAG, "initFragment: " + "okkkkk");
    }

    public void  replacementFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.home_fragment_container,fragment);
        fragmentTransaction.commit();
    }
    //implement Fragment OnFragmentInteractionListener
    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,"fragment",Toast.LENGTH_LONG).show();
    }

    //设置按钮的位置
    public static void setSpecialItemImageSize(BottomNavigationView view,int width,int height,int index) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(index);
            ImageView imageView = item.findViewById(android.support.design.R.id.icon);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(120,120);
            params.setMargins(0,-50,0,0);
            params.gravity = Gravity.CENTER;
            imageView.setLayoutParams(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
