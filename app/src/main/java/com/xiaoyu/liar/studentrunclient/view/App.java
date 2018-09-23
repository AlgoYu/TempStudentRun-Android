package com.xiaoyu.liar.studentrunclient.view;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoyu.liar.studentrunclient.model.entity.ResponseTemplate;
import com.xiaoyu.liar.studentrunclient.sqlite.MySQLite;
import com.zcy.acache.ACache;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class App extends Application{
    public static App app;
//    public static MySQLite mySQLite;
//    public static Intent intent;
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
//        intent = new Intent("WebSocketService");
//        intent.setPackage(getPackageName());
//        startService(intent);


        if(ACache.get(this).getAsObject("save") == null || (Boolean) ACache.get(this).getAsObject("save") == false){
//            XUtils.copyDatabase(this);
            String uuid = UUID.randomUUID().toString();
            ACache.get(this).put("uuid",uuid);
            ACache.get(this).put("save",true);
        }

//        mySQLite = new MySQLite(this);


        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url("http://192.168.0.105:8080/Api/Miyao").get().build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("网络请求错误:",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String temp = response.body().string();
                Log.e("查看返回的数据",temp);
                ResponseTemplate<String> message = new Gson().fromJson(temp,new TypeToken<ResponseTemplate<String>>(){}.getType());
                ACache.get(app).put("miyao",message.getData());
                Log.e("秘钥是否缓存!",ACache.get(app).getAsString("miyao"));
            }
        });
    }
}
