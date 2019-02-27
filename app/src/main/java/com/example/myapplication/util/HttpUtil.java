package com.example.myapplication.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 与服务器的交互工具类
 */

public class HttpUtil {
    /**
     * 处理返回的数据
     * @param address 处理的网址
     * @param listener 监听事件
     */
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL(address);
                    connection=(HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8*1000);
                    connection.setReadTimeout(8*1000);
                    InputStream in=connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    if(listener!=null){
                        //回调onFinish方法
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if(listener!=null){
                        listener.onError(e);
                    }
                }finally{
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
