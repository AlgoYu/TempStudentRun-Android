package com.xiaoyu.liar.studentrunclient.view;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.utils.XUtils;
import com.zcy.acache.ACache;

public class WelComeActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner mSpSushelou;
    private EditText mEdSushehao;
    private EditText mEdShouji;
    private Button mBtBaocun;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            XUtils.ShowToast("秘钥更新成功！可以正常开始使用~");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (ACache.get(App.app).getAsObject("run") != null && (Boolean) ACache.get(App.app).getAsObject("run") == true) {
            startActivity(new Intent(WelComeActivity.this, MainActivity.class));
            finish();
        }
        initView();
        mBtBaocun.setOnClickListener(this);
    }

    private void initView() {
        mSpSushelou = findViewById(R.id.sp_sushelou);
        mEdSushehao = findViewById(R.id.ed_sushehao);
        mEdShouji = findViewById(R.id.ed_shouji);
        mBtBaocun = findViewById(R.id.bt_baocun);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("liar.xiaoyu.www.key");
        registerReceiver(mBroadcastReceiver,intentFilter);
    }

    @Override
    public void onClick(View view) {
        String phone = mEdShouji.getText().toString();
        String dorm = mEdSushehao.getText().toString();
        String ridgepole = mSpSushelou.getSelectedItem().toString();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(dorm) || TextUtils.isEmpty(ridgepole)) {
            XUtils.ShowToast("请输入您的送餐地址完整信息！");
        } else {
            if (!XUtils.isMobileNO(phone) || dorm.length() != 3) {
                XUtils.ShowToast("您填入的信息有误！");
            } else {

                View view1 = getLayoutInflater().inflate(R.layout.dialog_queren,null);
                Dialog dialog = new Dialog(WelComeActivity.this);
                dialog.setTitle("确认信息");
                dialog.setContentView(view1);
                dialog.setCancelable(false);
                TextView textView = view1.findViewById(R.id.dialog_message);
                Button queding = view1.findViewById(R.id.dialog_queding);
                Button quxiao = view1.findViewById(R.id.dialog_quxiao);
                textView.setText("请再确定您的信息，我们不提供更改!\n地址:"+ridgepole+"栋"+dorm+"寝室\n手机号:"+phone);
                queding.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ACache.get(App.app).put("ridgepole", ridgepole);
                        ACache.get(App.app).put("dorm", dorm);
                        ACache.get(App.app).put("phone", phone);

                        ACache.get(App.app).put("run", true);

                        Log.e("宿舍楼:", ACache.get(App.app).getAsString("ridgepole"));
                        Log.e("宿舍号:", ACache.get(App.app).getAsString("dorm"));
                        Log.e("手机号:", ACache.get(App.app).getAsString("phone"));

                        XUtils.ShowToast("设置成功！");

                        startActivity(new Intent(WelComeActivity.this, MainActivity.class));
                        finish();
                    }
                });
                quxiao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
//                alertDialog.setTitle("提醒:");
//                alertDialog.setNegativeButton("取消",null);
//                alertDialog.setMessage("请再确定您的信息，我们不提供更改！\n地址:"+ridgepole+"栋"+dorm+"寝室\n手机号:"+phone);
//                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ACache.get(App.app).put("ridgepole", ridgepole);
//                        ACache.get(App.app).put("dorm", dorm);
//                        ACache.get(App.app).put("phone", phone);
//
//                        ACache.get(App.app).put("run", true);
//
//                        Log.e("宿舍楼:", ACache.get(App.app).getAsString("ridgepole"));
//                        Log.e("宿舍号:", ACache.get(App.app).getAsString("dorm"));
//                        Log.e("手机号:", ACache.get(App.app).getAsString("phone"));
//
//                        XUtils.ShowToast("设置成功！");
//
//                        startActivity(new Intent(WelComeActivity.this, MainActivity.class));
//                        finish();
//                    }
//                });
//                alertDialog.create().show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }


}
