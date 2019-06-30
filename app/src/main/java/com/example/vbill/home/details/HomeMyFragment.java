package com.example.vbill.home.details;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.vbill.R;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.example.vbill.util.PlatformUtil;
import com.example.vbill.util.ShareToolUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
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
    private boolean canClockFlag;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private LinearLayout homeMylogin;
    private ImageView userPhoto, goClockImage;
    private TextView loginText, billDay, billCount, signDay, clockTxt;
    private Button logOutLayout;
    private LinearLayout changeUserLayout, goClock;
    private LinearLayout generalLogoutLayout;
    private String customerId;
    private SharedPreferences loginPref;
    private LinearLayout shareLayout;

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
        shareLayout = homeMyView.findViewById(R.id.share_layout);
        signDay = homeMyView.findViewById(R.id.sign_day);
        goClock = homeMyView.findViewById(R.id.go_clock);
        goClockImage = homeMyView.findViewById(R.id.go_clock_image);
        clockTxt = homeMyView.findViewById(R.id.clock_txt);

        homeMylogin.setOnClickListener(this);
        userPhoto.setOnClickListener(this);
        logOutLayout.setOnClickListener(this);
        changeUserLayout.setOnClickListener(this);
        shareLayout.setOnClickListener(this);
        goClock.setOnClickListener(this);
        loginPref = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
        customerId = String.valueOf(loginPref.getInt("userId", -1));
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.CHOOSE_FROM_ALBUM);
        }

        clearBillRecordInfo();

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
        loginPref = getActivity().getSharedPreferences("login", getActivity().MODE_PRIVATE);
        customerId = String.valueOf(loginPref.getInt("userId", -1));
        refreshInternal();
        getBillRecordInfo();
        if (!canClockFlag) {
            clockTxt.setText("已打卡");
            goClockImage.setImageDrawable(getResources().getDrawable(R.drawable.haveclock));
        } else {
            clockTxt.setText("去打卡");
            goClockImage.setImageDrawable(getResources().getDrawable(R.drawable.goclcok));
        }
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
            getBillRecordInfo();
            editor.putInt("ClockYear", 0);
            editor.putInt("ClockDayOfYear", 0);
            editor.apply();
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
                clearBillRecordInfo();
                break;
            case R.id.me_change_user:
                clearBillRecordInfo();
                Intent intent = new Intent("android.intent.action.Login");
                startActivity(intent);
                break;
            case R.id.share_layout:
                Log.d(TAG, "onClick: share_layout");
                //Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.phone1);
                //File file = ShareToolUtil.saveSharePic(getContext(), thumb);
                //Log.d(TAG, "onClick: file " + file);
                shareImageDefault(copyToFolder());
                break;
            case R.id.go_clock:
                if (canClockFlag) {
                    goToClock();
                } else {
                    Toast.makeText(getContext(), "您今天已经打卡了！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private File copyToFolder() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.CHOOSE_FROM_ALBUM);
        } else {
            return ShareToolUtil.saveSharePic(getActivity(), BitmapFactory.decodeResource(getResources(), R.drawable.qr_code_title));
        }
        return null;
    }

    protected void shareImageDefault(File file) {

        Log.d(TAG, "shareImage: file " + file);
        if (file != null && file.exists()) {
            Intent intent = new Intent(Intent.ACTION_SEND); // 启动分享发送的属性
            intent.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(getActivity(), "com.example.vbill.fileprovider", file));// 分享的内容
            intent.setType("image/*");// 分享发送的数据类型
            Intent chooser = Intent.createChooser(intent, "Share image");
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                Log.d(TAG, "shareImage: ");
                startActivity(chooser);
            }
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

    public void getBillRecordInfo() {
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
                            signDay.setText((String.valueOf(dataJson.get("clockDay"))) + "天");
                            billDay.setText((String.valueOf(dataJson.get("totalDay"))) + "天");
                            billCount.setText(String.valueOf(dataJson.get("totalAccountsCount")) + "笔");
                            Boolean ifClockedToday = gson.fromJson(dataJson.get("ifClockedToday"), Boolean.class);
                            canClockFlag = ifClockedToday ? false : true;
                            if (ifClockedToday) {
                                clockTxt.setText("已打卡");
                                goClockImage.setImageDrawable(getResources().getDrawable(R.drawable.haveclock));
                            } else {
                                clockTxt.setText("去打卡");
                                goClockImage.setImageDrawable(getResources().getDrawable(R.drawable.goclcok));
                            }
                        } else {
                            Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void goToClock() {
        String address = Constants.SERVER_PREFIX + "v1/esc/" + customerId + "/clock";
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
                            String msgJson = gson.fromJson(dataJson.get("msg"), String.class);
                            Log.d(TAG, "run: msgJson" + msgJson);
                            signDay.setText((String.valueOf(dataJson.get("clockDay"))) + "天");
                            clockTxt.setText("已打卡");
                            goClockImage.setImageDrawable(getResources().getDrawable(R.drawable.haveclock));

                        } else {
                            Toast.makeText(getActivity(), "打卡数据处理失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    public void clearBillRecordInfo() {
        signDay.setText("0天");
        billDay.setText("0天");
        billCount.setText("0笔");
    }

    public boolean canClock() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        int storedYear = pref.getInt("ClockYear", 0);
        int storedDay = pref.getInt("ClockDayOfYear", 0);
        Log.d(TAG, "canClock: " + "storedYear" + storedYear + ";storedDay" + storedDay + ";year+day" + year + day);
        if (storedYear == year && storedDay == day) {
            return false;
        }
        return true;
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
