package com.xiaoyu.liar.studentrunclient.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.model.entity.Order;
import com.xiaoyu.liar.studentrunclient.model.entity.RequestTemplate;
import com.xiaoyu.liar.studentrunclient.model.entity.ResponseTemplate;
import com.xiaoyu.liar.studentrunclient.utils.XUtils;
import com.xiaoyu.liar.studentrunclient.view.App;
import com.xiaoyu.liar.studentrunclient.view.MainActivity;
import com.zcy.acache.ACache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DingdanAdapter extends RecyclerView.Adapter<DingdanAdapter.DingdanViewHolder>{
    List<Order> datas = new ArrayList<>();
    LayoutInflater mLayoutInflater;
    OkHttpClient mOkHttpClient;
    Gson mGson;
    Context context;

    public DingdanAdapter(LayoutInflater layoutInflater,Context context) {
        mLayoutInflater = layoutInflater;
        this.context = context;
        mOkHttpClient = new OkHttpClient();
        mGson = new Gson();
    }

    public void setDatas(List<Order> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public List<Order> getDatas() {
        return datas;
    }

    @NonNull
    @Override
    public DingdanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DingdanViewHolder viewHolder = new DingdanViewHolder(mLayoutInflater.inflate(R.layout.hold_view,parent,false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DingdanViewHolder holder, int position) {
        final Order order = datas.get(position);
        holder.dingdanhao.setText("订单编号："+order.getId());
        holder.dingdanshijian.setText("订单时间："+order.getDatetime());
        holder.dingdanneirong.setText(order.getInfo());
        if(order.getFlag() > 0){
            holder.dingdanzhuangtai.setText("配送人员："+order.getStaff()+"\t\t\t\t电话:"+order.getPhone());
            holder.mButton.setVisibility(View.INVISIBLE);
        }else{
            holder.dingdanzhuangtai.setText("配送人员：等待小哥哥接单哦~");
            holder.mButton.setVisibility(View.VISIBLE);
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Dialog dialog = new Dialog(context);
                    View view1 = mLayoutInflater.inflate(R.layout.dialog_queren,null);
                    dialog.setTitle("确认信息");
                    dialog.setContentView(view1);
                    dialog.setCancelable(false);
                    TextView textView = view1.findViewById(R.id.dialog_message);
                    textView.setText("确定取消吗？小哥哥也许马上就来了~");
                    Button queding = view1.findViewById(R.id.dialog_queding);
                    Button quxiao = view1.findViewById(R.id.dialog_quxiao);
                    queding.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            RequestTemplate<Integer> requestTemplate = new RequestTemplate<>();
                            requestTemplate.setKey(ACache.get(MainActivity.activity).getAsString("miyao"));
                            requestTemplate.setData(order.getId());
                            String requestjson = mGson.toJson(requestTemplate);
                            Log.e("查看:",requestjson);
                            RequestBody requestBody = FormBody.create(MediaType.parse("application/json;charset=utf-8"),requestjson);
                            Request request = new Request.Builder().url("http://studentrun.club:8080/xiaoyu/Api/Order").delete(requestBody).build();
                            mOkHttpClient.newCall(request).enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    MainActivity.activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            XUtils.ShowToast("取消失败！请检查网络环境！");
                                        }
                                    });
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String temp = response.body().string();
                                    MainActivity.activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ResponseTemplate<Boolean> message = new Gson().fromJson(temp,new TypeToken<ResponseTemplate<Boolean>>(){}.getType());
                                            if(message.getData()){
                                                XUtils.ShowToast("取消成功！");
                                                Intent intent = new Intent().setAction("liar.xiaoyu.www.studentrun.tiaozhuan");
                                                MainActivity.activity.sendBroadcast(intent);
                                                dialog.dismiss();
                                            }else{
                                                dialog.dismiss();
                                                XUtils.ShowToast("已经被小哥哥接单了哦！");
                                                Intent intent = new Intent().setAction("liar.xiaoyu.www.studentrun.tiaozhuan");
                                                MainActivity.activity.sendBroadcast(intent);
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                    quxiao.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class DingdanViewHolder extends RecyclerView.ViewHolder {
        public TextView dingdanhao;
        public TextView dingdanshijian;
        public TextView dingdanneirong;
        public TextView dingdanzhuangtai;
        public Button mButton;
        public DingdanViewHolder(View itemView) {
            super(itemView);
            dingdanhao = itemView.findViewById(R.id.holder_dingdanhao);
            dingdanneirong = itemView.findViewById(R.id.holder_dingdanneirong);
            dingdanshijian = itemView.findViewById(R.id.holder_dingdanshijian);
            dingdanzhuangtai = itemView.findViewById(R.id.holder_dingdanzhuangtai);
            mButton = itemView.findViewById(R.id.holder_quxiaodingdan);
        }
    }
}
