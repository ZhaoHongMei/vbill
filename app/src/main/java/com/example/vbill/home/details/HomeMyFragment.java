package com.example.vbill.home.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.vbill.R;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeMyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeMyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMyFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeMyFragment";
    private static HomeMyFragment fragment;
    private OnFragmentInteractionListener mListener;
    private boolean loginFlag = false;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private LinearLayout homeMylogin;
    private ImageView userPhoto;
    private TextView loginText,billDay,billCount;
    private Button logOutLayout;
    private LinearLayout changeUserLayout;
    private LinearLayout generalLogoutLayout;
    private String customerId;
    private SharedPreferences loginPref;

    public HomeMyFragment() {
        // Required empty public constructor
    }

    public static HomeMyFragment newInstance(String param1, String param2) {
        HomeMyFragment fragment = new HomeMyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeMyFragment getInstance() {
        try {
            if (fragment == null) {
                fragment = new HomeMyFragment();
            }
        } catch (Exception e) {
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
        Log.d(TAG, "onCreateView: ");
        pref = getContext().getSharedPreferences("login", getContext().MODE_PRIVATE);
        editor = pref.edit();
        // Inflate the layout for this fragment
        View homeMyView = inflater.inflate(R.layout.fragment_home_my, container, false);
        homeMylogin = homeMyView.findViewById(R.id.home_my_login);
        userPhoto = homeMyView.findViewById(R.id.user_photo);
        loginText = homeMyView.findViewById(R.id.login_text);
        logOutLayout = homeMyView.findViewById(R.id.me_log_out);
        changeUserLayout = homeMyView.findViewById(R.id.me_change_user);
        generalLogoutLayout = homeMyView.findViewById(R.id.general_logout_layout);
        billDay = homeMyView.findViewById(R.id.bill_day);
        billCount = homeMyView.findViewById(R.id.bill_count);
        homeMylogin.setOnClickListener(this);
        userPhoto.setOnClickListener(this);
        logOutLayout.setOnClickListener(this);
        changeUserLayout.setOnClickListener(this);
        loginPref = getActivity().getSharedPreferences("login",getActivity().MODE_PRIVATE);
        customerId = String.valueOf(loginPref.getInt("userId", -1));

        return homeMyView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onResume() {
        super.onResume();
        refreshInternal();
    }

    private void refreshInternal() {
        if (pref == null) {
            pref = getContext().getSharedPreferences("login", getContext().MODE_PRIVATE);
        }
        String loginName = pref.getString("loginName", "");
        Log.d(TAG, "refreshInternal: " + loginName);
        if ("".equals(loginName)) {
            generalLogoutLayout.setVisibility(View.GONE);
            userPhoto.setImageResource(R.drawable.login);
            loginText.setText(R.string.not_login);
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.Login");
                    startActivity(intent);
                }
            });
            userPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("android.intent.action.Login");
                    startActivity(intent);
                }
            });
        } else {
            generalLogoutLayout.setVisibility(View.VISIBLE);
            String defaultUserPhoto = Constants.USER_SERVER_PREFIX + "v1/esc/images/defaultUserPhoto.png";
            String photoPath = pref.getString("userPhotoPath", defaultUserPhoto);
            getBillDayAndCount();
            //Glide.with(getContext()).load(photoPath).into(userPhoto);
            Glide.with(getContext()).load(photoPath).asBitmap().centerCrop().into(new BitmapImageViewTarget(userPhoto) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    userPhoto.setImageDrawable(circularBitmapDrawable);
                }
            });
            loginText.setText(pref.getString("loginName", ""));
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            userPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogImage();
                }
            });
        }
    }

    private void showDialogImage() {
        String defaultUserPhoto = Constants.USER_SERVER_PREFIX + "v1/esc/images/defaultUserPhoto.png";
        String photoPath = pref.getString("userPhotoPath", defaultUserPhoto);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View imgEntryView = inflater.inflate(R.layout.dialog_image, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        ImageView dialogImageView = imgEntryView.findViewById(R.id.dialog_image);
        Glide.with(getContext()).load(photoPath).into(dialogImageView);
        dialogImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.setView(imgEntryView);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_log_out:
                editor.putString("loginName", "");
                editor.remove("userId");
                editor.remove("userPhotoPath");
                editor.apply();
                refreshInternal();
                break;
            case R.id.me_change_user:
                Intent intent = new Intent("android.intent.action.Login");
                startActivity(intent);
                break;
            default:
                break;
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

    public void getBillDayAndCount(){
        String address = Constants.SERVER_PREFIX + "v1/esc/" + customerId + "/totalCounts";
        HttpUtil.sendOkHttpGetRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
                        String statusCode = gson.fromJson(jsonObject.get("statusCode"), String.class);
                        Log.d(TAG, "onResponse: " + responseData);
                        if (responseData != null && "200".equals(statusCode)) {
                            JsonObject dataJson = gson.fromJson(jsonObject.get("data"), JsonObject.class);
                            billDay.setText((String.valueOf(dataJson.get("totalDay")))+"天");
                            billCount.setText(String.valueOf(dataJson.get("totalAccountsCount"))+"笔");
                        } else {
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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
