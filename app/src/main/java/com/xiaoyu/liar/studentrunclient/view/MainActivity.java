package com.xiaoyu.liar.studentrunclient.view;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.xiaoyu.liar.studentrunclient.Adapter.MainFragmentAdapter;
import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.fragment.DingcanFragment;
import com.xiaoyu.liar.studentrunclient.fragment.DingdanFragment;
import com.xiaoyu.liar.studentrunclient.service.MyWebSocketListener;
import com.zcy.acache.ACache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

public class MainActivity extends AppCompatActivity{

    private ViewPager mainViewpager;
    private TabLayout mainTablayout;
    private MainFragmentAdapter mainFragmentAdapter;
    private List<Fragment> fragments;
    public static Activity activity;
    public static WebSocket webSocket;
    private OkHttpClient okHttpClient;
    private MyWebSocketListener mWebSocketListener;
    private boolean flag = false;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("liar.xiaoyu.www.studentrun.tiaozhuan")){
                mainTablayout.getTabAt(1).select();
                Intent intent1 = new Intent();
                intent1.setAction("liar.xiaoyu.www.studentrun.huidaodingbu");
                intent1.putExtra("action","shuaxin");
                sendBroadcast(intent1);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("liar.xiaoyu.www.studentrun.tiaozhuan");
        this.registerReceiver(mBroadcastReceiver,intentFilter);
        activity = this;
    }

    private void initView() {
        mainViewpager = findViewById(R.id.main_viewpager);
        mainTablayout = findViewById(R.id.main_tablayout);
        mainTablayout.setupWithViewPager(mainViewpager);
        fragments = new ArrayList<>();
        fragments.add(new DingcanFragment());
        fragments.add(new DingdanFragment());
        mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager());
        mainFragmentAdapter.setFragments(fragments);
        mainViewpager.setAdapter(mainFragmentAdapter);
        mainViewpager.setCurrentItem(0);
        mainTablayout.getTabAt(0).setText("订餐");
        mainTablayout.getTabAt(1).setText("订单");

        //Websocket
        okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)//允许失败重试
                .readTimeout(5, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(5, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(5, TimeUnit.SECONDS)//设置连接超时时间
                .build();
        String url = "ws://studentrun.club:8080/xiaoyu/WebSocketConnection/"+ACache.get(App.app).getAsString("uuid");
        mWebSocketListener = new MyWebSocketListener();
        Request request = new Request.Builder().url(url).build();
        webSocket = okHttpClient.newWebSocket(request,mWebSocketListener);
        flag = true;
    }

    @Override
    protected void onStop() {
        flag = false;
        webSocket.close(1000,null);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        webSocket.close(1000,null);
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if(!flag){
            Request request = webSocket.request();
            webSocket = okHttpClient.newWebSocket(request,mWebSocketListener);
            Intent intent = new Intent();
            intent.setAction("liar.xiaoyu.www.studentrun.huidaodingbu");
            intent.putExtra("action","shuaxin");
            sendBroadcast(intent);
        }
        super.onResume();
    }
}
