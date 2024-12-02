package com.example.mainactivity;

public interface Callback<T> {
    void onResponse(T result);
    void onFailure(Exception e);
}
