package com.example.vbill.home.details;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.vbill.R;
import com.example.vbill.util.Constants;


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
    private TextView loginText;

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

        homeMylogin.setOnClickListener(this);
        userPhoto.setOnClickListener(this);

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
            String defaultUserPhoto = Constants.USER_SERVER_PREFIX + "v1/esc/images/defaultUserPhoto.png";
            String photoPath = pref.getString("userPhotoPath", defaultUserPhoto);

            Glide.with(getContext()).load(photoPath).into(userPhoto);
            loginText.setText(R.string.log_off);
            loginText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editor.putString("loginName", "");
                    editor.remove("userId");
                    editor.remove("userPhotoPath");
                    editor.apply();
                    refreshInternal();
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
