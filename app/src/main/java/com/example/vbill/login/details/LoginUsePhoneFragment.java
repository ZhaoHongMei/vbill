package com.example.vbill.login.details;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vbill.R;
import com.example.vbill.bean.User;
import com.example.vbill.home.HomeActivity;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.example.vbill.util.LoginUtil;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginUsePhoneFragment extends Fragment implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = "LoginUsePhoneFragment";
    public static final int LOGIN_SUCCEED = 1;
    public static final int LOGIN_FAILED = 2;
    public static final int SERVER_CONNECTION_FAILURE = 3;
    public static final int REGISTER_SUCCEED = 4;
    public static final int REGISTER_FAILED = 5;
    private static LoginUsePhoneFragment fragment;
    private AutoCompleteTextView usernameView;
    private TextView passwordView;
    private Button signInButton;
    private CheckBox remeberPass;
    private AutoCompleteTextView telephoneNumberView;
    private EditText verificationCodeView;
    private Button getVerificationCodeButton;
    private TextView registerText;
    private TextView loginMethodText;
    private ProgressBar progressBar;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public Gson gson;
    public OkHttpClient client;
    private FragmentActivity activity;
    private ImageView openEyeView;
    private ImageView closeEyeView;
    private String loginMethod = Constants.LOGIN_METHOD_PHONE;
    private LinearLayout phoneLoginLayout;
    private LinearLayout usernameLoginLayout;
    private boolean isTelephoneNumberExists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_phone_fragment, container, false);
        usernameView = view.findViewById(R.id.username);
        passwordView = view.findViewById(R.id.password);
        remeberPass = view.findViewById(R.id.remember_pass);
        telephoneNumberView = view.findViewById(R.id.phone_number);
        verificationCodeView = view.findViewById(R.id.verification_code);
        getVerificationCodeButton = view.findViewById(R.id.get_login_verification_code);
        registerText = view.findViewById(R.id.register_text);
        loginMethodText = view.findViewById(R.id.login_method);
        progressBar = view.findViewById(R.id.login_progress);
        signInButton = view.findViewById(R.id.sign_in_button);
        openEyeView = view.findViewById(R.id.eye_code);
        closeEyeView = view.findViewById(R.id.eye_code_close);
        phoneLoginLayout = view.findViewById(R.id.phone_login_layout);
        usernameLoginLayout = view.findViewById(R.id.username_login_layout);
        activity = getActivity();
        pref = activity.getSharedPreferences("login", activity.MODE_PRIVATE);
        editor = pref.edit();
        gson = new Gson();
        client = new OkHttpClient();

        signInButton.setOnClickListener(this);
        registerText.setOnClickListener(this);
        openEyeView.setOnClickListener(this);
        closeEyeView.setOnClickListener(this);
        loginMethodText.setOnClickListener(this);
        getVerificationCodeButton.setOnClickListener(this);
        telephoneNumberView.setOnFocusChangeListener(this);
        setStoredToViewIfRemember();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                goToSignIn();
                break;
            case R.id.register_text:
                gotoRegister();
                break;
            case R.id.eye_code:              //使密码可见
                openEyeView.setVisibility(View.GONE);
                closeEyeView.setVisibility(View.VISIBLE);
                passwordView.setInputType(131073);
                break;
            case R.id.eye_code_close:       //使密码不可见
                openEyeView.setVisibility(View.VISIBLE);
                closeEyeView.setVisibility(View.GONE);
                passwordView.setInputType(129);
                break;
            case R.id.login_method:
                if (Constants.LOGIN_METHOD_PHONE.equals(loginMethod)) {
                    loginMethodText.setText(R.string.phone_login_method);
                    loginMethod = Constants.LOGIN_METHOD_USER_NAME;
                    phoneLoginLayout.setVisibility(View.GONE);
                    usernameLoginLayout.setVisibility(View.VISIBLE);
                } else {
                    loginMethodText.setText(R.string.username_login_method);
                    loginMethod = Constants.LOGIN_METHOD_PHONE;
                    phoneLoginLayout.setVisibility(View.VISIBLE);
                    usernameLoginLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.get_login_verification_code:
                sendVerificationCode();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.phone_number:
                if (!hasFocus) {
                    String telephoneNumber = telephoneNumberView.getText().toString();
                    if (!isTelephoneNumberValid(telephoneNumber)) {
                        break;
                    }
                    try {
                        checkIfPhoneNumerExists(telephoneNumber);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
        }
    }

    private void sendVerificationCode() {
        String telephoneNumber = telephoneNumberView.getText().toString();
        try {
            checkIfPhoneNumerExists(telephoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "sendVerificationCode1: " + isTelephoneNumberExists);
        if (!isTelephoneNumberValid(telephoneNumber) || !isTelephoneNumberExists) {
            telephoneNumberView.requestFocus();
            return;
        }
        String url = Constants.USER_SERVER_PREFIX + "v1/esc/sendVerficationCode/" + telephoneNumber;
        Log.d(TAG, "sendVerificationCode: ");
        HttpUtil.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "对不起，发送验证码失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseDate = response.body().string();
                JsonObject jsonObject = gson.fromJson(responseDate, JsonObject.class);
                if (jsonObject != null && "操作成功".equals(jsonObject.get("reason")) && "0".equals(jsonObject.get("error_code"))) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "发送成功，请检查短信。", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void gotoRegister() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_fragment, RegisterFragment.getInstance())
                .commit();
    }

    private void goToSignIn() {
        if (Constants.LOGIN_METHOD_USER_NAME.equals(loginMethod)) {
            usernameView.setError(null);
            passwordView.setError(null);

            String username = usernameView.getText().toString();
            String password = passwordView.getText().toString();

            if (isUserNameValid(username) && isPasswordValid(password)) {
                progressBar.setVisibility(View.VISIBLE);
                checkIfUserExisted(username, password);
            }
        } else {
            String phoneNumber = telephoneNumberView.getText().toString();
            String verificationCode = verificationCodeView.getText().toString();
            if (isTelephoneNumberValid(phoneNumber) && isVerificationCodeValid(verificationCode)) {
                goToSignInByphoneNumber(phoneNumber, verificationCode);
            }
        }
    }

    private void goToSignInByphoneNumber(String phoneNumber, String verificationCode) {
        String url = Constants.USER_SERVER_PREFIX + "v1/esc/loginByPhone?telephoneNumber=" + phoneNumber + "&verificationCode=" + verificationCode;
        HttpUtil.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "对不起，处理失败，我们会尽快修复。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: " + responseData);
                com.example.vbill.bean.Response resp = gson.fromJson(responseData, com.example.vbill.bean.Response.class);
                if (resp == null) {
                    return;
                }
                if (resp.getData() != null) {
                    User user = gson.fromJson(gson.toJson(resp.getData()), User.class);
                    storeDataToSharedPreference(user);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    String errorMsg = resp.getErrorMsg();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            if (errorMsg == null || !"".equals(errorMsg)) {
                                verificationCodeView.setError(errorMsg);
                            } else {
                                Toast.makeText(getContext(), "登陆失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private boolean isTelephoneNumberValid(String telephoneNumber) {
        if (!telephoneNumber.matches("[0-9]{11}")) {
            telephoneNumberView.setError("手机号无效");
            return false;
        }
        return true;
    }

    private boolean isVerificationCodeValid(String verificationCode) {
        if (!verificationCode.matches("[0-9]{4}")) {
            verificationCodeView.setError("验证码不正确");
            return false;
        }
        return true;
    }

    private void checkIfPhoneNumerExists(String telephoneNumber) throws IOException {
        String url = Constants.USER_SERVER_PREFIX + "v1/esc/users/telephoneNumber/" + telephoneNumber;
        HttpUtil.sendOkHttpGetRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "checkTelephoneNumber,onResponse: " + responseData);

                JsonObject jsonObject = gson.fromJson(responseData, JsonObject.class);
                JsonElement data = jsonObject.get("data");
                if (data != null && data.isJsonObject()) {
                    isTelephoneNumberExists = data.getAsJsonObject().get("isTelephoneNumberExists").getAsBoolean();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!isTelephoneNumberExists) {
                                telephoneNumberView.setError("该手机号未注册，请先注册");
                                Toast.makeText(getContext(), "该手机号未注册，请先注册", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean isUserNameValid(String username) {
        if (TextUtils.isEmpty(username)) {
            usernameView.setError("username should not be empty!");
            return false;
        }
        return true;
    }

    private boolean isPasswordValid(String password) {
        if (TextUtils.isEmpty(password)) {
            passwordView.setError("password should not be empty!");
            return false;
        } else if (password.length() < 6) {
            passwordView.setError("password is too short!");
            return false;
        }
        return true;
    }

    private void checkIfUserExisted(String username, String password) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        String userJson = gson.toJson(map);
        Log.d(TAG, "checkIfUserExisted: usename: " + username + "password: " + password);
        String url = Constants.USER_SERVER_PREFIX + "v1/esc/login";

        HttpUtil.sendOkHttpPostRequest(userJson, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "对不起，处理失败，我们会尽快修复。", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d(TAG, "onResponse: " + responseData);
                User user = gson.fromJson(responseData, User.class);
                if (user != null) {
                    storeDataToSharedPreference(user);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "用户名或密码不正确！", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void setStoredToViewIfRemember() {
        boolean isRemember = pref.getBoolean("remember_password", false);
        if (isRemember) {
            String username = pref.getString("username", "");
            String password = pref.getString("password", "");
            usernameView.setText(username);
            passwordView.setText(password);
            remeberPass.setChecked(true);
        }
    }

    private void storeDataToSharedPreference(User user) {
        if (remeberPass.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("username", user.getName());
            editor.putString("password", user.getPassword());
        } else {
            editor.clear();
        }
        editor.putString("loginName", user.getName());
        editor.putInt("userId", user.getId());
        editor.putString("userPhotoPath", user.getPhotopath());
        editor.apply();
        Log.d(TAG, "storeDataToSharedPreference: " + pref.getString("loginName", ""));
    }

    public LoginUsePhoneFragment() {
        //constructor
    }

    public static LoginUsePhoneFragment getInstance() {
        try {
            if (fragment == null) {
                fragment = new LoginUsePhoneFragment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }

}
