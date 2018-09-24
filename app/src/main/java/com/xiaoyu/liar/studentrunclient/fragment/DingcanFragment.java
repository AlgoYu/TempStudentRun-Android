package com.xiaoyu.liar.studentrunclient.fragment;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.model.entity.Order;
import com.xiaoyu.liar.studentrunclient.model.entity.RequestTemplate;
import com.xiaoyu.liar.studentrunclient.model.entity.ResponseTemplate;
import com.xiaoyu.liar.studentrunclient.utils.XUtils;
import com.xiaoyu.liar.studentrunclient.view.App;
import com.zcy.acache.ACache;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DingcanFragment extends Fragment implements View.OnClickListener {

    private OkHttpClient mOkHttpClient;
    private EditText mEdInfo;
    private Button mBtRun;
    private Gson mGson;
    private Dialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dingcan, container, false);
        initView(view);
        mBtRun.setOnClickListener(this);
        return view;
    }

    private void initView(View view) {
        mEdInfo = view.findViewById(R.id.ed_info);
        mBtRun = view.findViewById(R.id.bt_run);
        mOkHttpClient = new OkHttpClient();
        mGson = new Gson();
        mDialog = new Dialog(getActivity());
        mDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_load,null));
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View view) {
        final String info = mEdInfo.getText().toString();
        if(TextUtils.isEmpty(info)){
            XUtils.ShowToast("请输入您需要点的东西哦！");
        }else{
            mDialog.show();
            mEdInfo.setText("");
            Order order = new Order();
            final Integer ridgepole = Integer.parseInt(ACache.get(App.app).getAsString("ridgepole"));
            final Integer dorm = Integer.parseInt(ACache.get(App.app).getAsString("dorm"));
            final String phone = ACache.get(App.app).getAsString("phone");

            Log.e("订单提交前查看缓存数据",ridgepole+"");
            Log.e("订单提交前查看缓存数据",dorm+"");
            Log.e("订单提交前查看缓存数据",phone+"");

            order.setUuid(ACache.get(App.app).getAsString("uuid"));
            order.setRidgepole(ridgepole);
            order.setContact(phone);
            order.setDorm(dorm);
            order.setInfo(info);

            final RequestTemplate<Order> requestTemplate = new RequestTemplate<>();
            requestTemplate.setKey(ACache.get(App.app).getAsString("miyao"));
            requestTemplate.setData(order);
            String requestjson = mGson.toJson(requestTemplate);

            Log.e("查看提交订单的json数据",requestjson);
            RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"),requestjson);
            Request request = new Request.Builder().url("http://studentrun.club:8080/xiaoyu/Api/Order").post(requestBody).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e("提交订单返回数据:",e.toString());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            XUtils.ShowToast("提交错误！请检查网络环境！");
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String temp = response.body().string();
                    Log.e("提交订单返回数据:",temp);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            ResponseTemplate<Integer> message = new Gson().fromJson(temp,new TypeToken<ResponseTemplate<Integer>>(){}.getType());
                            if(message.getSucces()){
                                Intent intent = new Intent().setAction("liar.xiaoyu.www.studentrun.tiaozhuan");
                                getActivity().sendBroadcast(intent);
                                XUtils.ShowToast("提交成功！");
                                mDialog.dismiss();
                            }else {
                                XUtils.ShowToast("提交失败！");
                            }
                        }
                    });
                }
            });
        }
    }
}
