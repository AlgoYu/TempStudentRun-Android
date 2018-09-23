package com.xiaoyu.liar.studentrunclient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class WebSocketService extends Service{
    OkHttpClient mOkHttpClient;
    MyWebSocketListener mWebSocketListener;
    public static WebSocket webSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e("WebSocketService","创建");
        mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//允许失败重试
                .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(5, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        String url = "ws://192.168.0.105:8080/WebSocketConnection/client";
        mWebSocketListener = new MyWebSocketListener();
        Request request = new Request.Builder().url(url).build();
        webSocket = mOkHttpClient.newWebSocket(request, mWebSocketListener);
//        client.dispatcher().executorService().shutdown();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("WebSocketService","启动");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("WebSocketService","销毁");
        mWebSocketListener.mwebSocket.close(1000,null);
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        Log.e("WebSocketService","停止");
        return super.stopService(name);
    }
}
