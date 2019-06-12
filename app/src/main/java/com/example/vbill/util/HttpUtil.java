package com.example.vbill.util;

import android.util.Log;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpUtil {
    private static final String TAG = "HttpUtil";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    //http://47.102.197.196:1201/v1/esc/customers/itemsByCustomerId?customerId=123
    public static void sendOkHttpGetRequest(String address, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpPostRequest(String json, String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(JSON, json);
        Log.d(TAG, "sendOkHttpPostRequest:address : " + address);
        Request request = new Request.Builder().url(address).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }
}
