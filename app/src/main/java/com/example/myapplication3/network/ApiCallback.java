package com.example.myapplication3.network;

public interface ApiCallback {
    void onSuccess(String response);
    void onError(String error);
}
