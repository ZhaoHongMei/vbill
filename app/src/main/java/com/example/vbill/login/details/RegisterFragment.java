package com.example.vbill.login.details;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vbill.R;
import com.example.vbill.bean.User;
import com.example.vbill.home.HomeActivity;
import com.example.vbill.util.Constants;
import com.example.vbill.util.HttpUtil;
import com.example.vbill.util.ImageUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RegisterFragment";
    private static RegisterFragment fragment;
    private FloatingActionButton takePhotoButton;
    private FloatingActionButton chooseFromAlbumButton;
    private AutoCompleteTextView usernameView;
    private EditText passwordView;
    private Button registerButton;
    private TextView loginView;
    private ProgressBar progressBar;
    private Gson gson;
    private FragmentActivity activity;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private Uri imageUri;
    private File photoFile = null;
    private Bitmap bitmap;
    private ImageView photoView;
    private String imagHttpPath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        photoView = view.findViewById(R.id.photo_view);
        takePhotoButton = (FloatingActionButton) view.findViewById(R.id.take_photo);
        chooseFromAlbumButton = (FloatingActionButton) view.findViewById(R.id.choose_from_album);
        usernameView = view.findViewById(R.id.register_username);
        passwordView = view.findViewById(R.id.register_password);
        registerButton = view.findViewById(R.id.register_button);
        loginView = view.findViewById(R.id.login_text);
        progressBar = view.findViewById(R.id.register_progress);
        activity = getActivity();
        gson = new Gson();
        pref = activity.getSharedPreferences("login", activity.MODE_PRIVATE);
        editor = pref.edit();
        takePhotoButton.setOnClickListener(this);
        chooseFromAlbumButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        loginView.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.CHOOSE_FROM_ALBUM);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constants.TAKE_PHOTO);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_photo:
                Log.d(TAG, "onClick: take photo");
                Toast.makeText(getActivity(), "click take photo", Toast.LENGTH_SHORT).show();
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    android.support.v4.app.ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, Constants.TAKE_PHOTO);
                } else {
                    takePhoto();
                }
                break;
            case R.id.choose_from_album:
                Log.d(TAG, "onClick: choose from album");
                if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    android.support.v4.app.ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.CHOOSE_FROM_ALBUM);
                } else {
                    chooseFromAlbum();
                }
                break;
            case R.id.register_button:
                if (photoFile != null) {
                    saveImageAndGoToRegister(photoFile);
                } else {
                    goToRegister();
                }
                break;
            case R.id.login_text:
                goToLogin();
                break;
            default:
                break;
        }
    }

    private void takePhoto() {
        Log.d(TAG, "begin takePhoto...");
        File outputImage = new File(activity.getExternalCacheDir(), "user_photo.jpg");
        if (outputImage.exists()) {
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(activity, "com.example.vbill.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, Constants.TAKE_PHOTO);
    }

    private void chooseFromAlbum() {
        Log.d(TAG, "begin choose from album...");
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, Constants.CHOOSE_FROM_ALBUM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.TAKE_PHOTO:
                Log.d(TAG, "Handling after take photo...");
                if (resultCode == activity.RESULT_OK) {
                    try {
                        bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(imageUri));
                        photoFile = new File(activity.getExternalCacheDir(), "user_photo.jpg");
                        photoView.setImageBitmap(bitmap);
                        photoView.setVisibility(View.VISIBLE);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.CHOOSE_FROM_ALBUM:
                Log.d(TAG, "Handling after choose from album...");
                if (resultCode == activity.RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent intent) {
        String imagePath = "";
        Uri uri = intent.getData();
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        photoFile = new File(imagePath);
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent intent) {
        Uri uri = intent.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
        photoFile = new File(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            bitmap = BitmapFactory.decodeFile(imagePath);
            photoView.setImageBitmap(bitmap);
            photoView.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(activity, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageAndGoToRegister(File photoFile) {
        String url = Constants.USER_SERVER_PREFIX + "v1/esc/photo";
        Log.d(TAG, "saveImageAndGoToRegister: " + url);
        ImageUtil.uploadImage(url, photoFile, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "保存失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String httpPath = response.body().string();
                imagHttpPath = httpPath;
                Log.d(TAG, "setResponseToImageHttpPath: " + imagHttpPath);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goToRegister();
                    }
                });
            }
        });
    }

    private void goToRegister() {
        usernameView.setError(null);
        passwordView.setError(null);

        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();
        String photopath = imagHttpPath;
        if (isUserNameValid(username) && isPasswordValid(password)) {
            progressBar.setVisibility(View.VISIBLE);
            saveUserToServer(username, password, photopath);
        }
    }

    private void goToLogin() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.login_fragment, LoginUsePhoneFragment.getInstance())
                .commit();
    }

    private void saveUserToServer(String username, String password, String photopath) {
        Map<String, String> map = new HashMap<String, String>();
        Log.d(TAG, "saveUserToServer: username " + username);
        map.put("name", username);
        map.put("password", password);
        map.put("photopath", photopath);
        String userJson = gson.toJson(map);
        //String url = Constants.USER_SERVER_PREFIX + "v1/esc/register";
        String url = Constants.USER_SERVER_PREFIX + "v1/esc/register";
        HttpUtil.sendOkHttpPostRequest(userJson, url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
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

    private void storeDataToSharedPreference(User user) {
        editor.putString("loginName", user.getName());
        editor.putInt("userId", user.getId());
        editor.putString("userPhotoPath", user.getPhotopath());
        editor.apply();
        Log.d(TAG, "storeDataToSharedPreference: " + pref.getString("loginName", ""));
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

    public RegisterFragment() {
        //constructor
    }

    public static RegisterFragment getInstance() {
        try {
            if (fragment == null) {
                fragment = new RegisterFragment();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragment;
    }
}
