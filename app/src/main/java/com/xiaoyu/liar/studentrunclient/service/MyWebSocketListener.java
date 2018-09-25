package com.xiaoyu.liar.studentrunclient.service;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xiaoyu.liar.studentrunclient.utils.XUtils;
import com.xiaoyu.liar.studentrunclient.view.App;
import com.xiaoyu.liar.studentrunclient.view.MainActivity;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketListener extends WebSocketListener{
    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    public WebSocket mwebSocket;
    /**
     * Invoked when a web socket has been accepted by the remote peer and may begin transmitting
     * messages.
     */
    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.e("WebSocket:","连接成功。。。");
        mwebSocket = webSocket;
    }

    /** Invoked when a text (type {@code 0x1}) message has been received. */
    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.e("WebSocket:","服务器发来消息:"+text);
//        XUtils.ShowToast(text);
        Intent intent = new Intent();
        intent.setAction("liar.xiaoyu.www.studentrun.huidaodingbu");
        intent.putExtra("action","accept");
        intent.putExtra("danhao",Integer.parseInt(text));
        App.app.sendBroadcast(intent);
    }

    /** Invoked when a binary (type {@code 0x2}) message has been received. */
    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        Log.e("WebSocket:","服务器发来字节:"+bytes);
    }

    /**
     * Invoked when the remote peer has indicated that no more incoming messages will be
     * transmitted.
     */
    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        Log.e("WebSocket:","服务器表示不再有消息传入"+code);
    }

    /**
     * Invoked when both peers have indicated that no more messages will be transmitted and the
     * connection has been successfully released. No further calls to this listener will be made.
     */
    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        Log.e("WebSocket:","双方确认不再有消息");
    }

    /**
     * Invoked when a web socket has been closed due to an error reading from or writing to the
     * network. Both outgoing and incoming messages may have been lost. No further calls to this
     * listener will be made.
     */
    @Override
    public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
        Log.e("WebSocket:","当关闭时有错误的读写的网络消息丢失");
        MainActivity.flag = false;
    }

    public void senServer(String message){
        mwebSocket.send(message);
    }
}
