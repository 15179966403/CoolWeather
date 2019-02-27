package com.example.myapplication.util;

/**
 * 处理服务器返回的结果,onFinish处理正常返回的结果，onError处理错误
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
