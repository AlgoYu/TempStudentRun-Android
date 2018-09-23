package com.xiaoyu.liar.studentrunclient.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoyu.liar.studentrunclient.Adapter.DingdanAdapter;
import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.model.entity.Order;
import com.xiaoyu.liar.studentrunclient.model.entity.ResponseTemplate;
import com.xiaoyu.liar.studentrunclient.utils.XUtils;
import com.xiaoyu.liar.studentrunclient.view.App;
import com.zcy.acache.ACache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DingdanFragment extends Fragment {

    private RecyclerView mListDingdan;
    private static DingdanAdapter mDingdanAdapter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("action")!=null && intent.getStringExtra("action").equals("shuaxin")){
                notifiDingdan();
            }else if(intent.getStringExtra("action")!=null && intent.getStringExtra("action").equals("accept")){
                int danhao = intent.getIntExtra("danhao", -1);
                for(Order order:mDingdanAdapter.getDatas()){
                    if(order.getId()==danhao){
                        XUtils.ShowToast("订单有新状态哦！");
                        notifiDingdan();
                        break;
                    }
                }
            }
        }
    };
    private OkHttpClient mOkHttpClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dingdan, container, false);
        initView(view);
        mListDingdan.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        return view;
    }

    private void initView(View view) {
        mListDingdan = view.findViewById(R.id.list_dingdan);
        mDingdanAdapter = new DingdanAdapter(getLayoutInflater());
        mListDingdan.setAdapter(mDingdanAdapter);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("liar.xiaoyu.www.studentrun.huidaodingbu");
        getActivity().registerReceiver(mBroadcastReceiver,intentFilter);
        mOkHttpClient = new OkHttpClient();
        notifiDingdan();
    }

    public void notifiDingdan(){
        String url = "http://192.168.0.105:8080/Api/OrderByUUID?key=" + ACache.get(App.app).getAsString("miyao") + "&uuid="+ ACache.get(App.app).getAsString("uuid");
        Log.e("URL",url);
        Request request = new Request.Builder().url(url).get().build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("网络请求错误:", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("查看返回的数据", temp);
                ResponseTemplate<List<Order>> message = new Gson().fromJson(temp, new TypeToken<ResponseTemplate<List<Order>>>() {
                }.getType());
                final List<Order> data = message.getData();
                if (message.getSucces()) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDingdanAdapter.setDatas(data);
                            mListDingdan.smoothScrollToPosition(0);
                        }
                    });
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            XUtils.ShowToast("数据请求失败！");
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }
}
