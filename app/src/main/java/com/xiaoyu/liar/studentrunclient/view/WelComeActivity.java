package com.xiaoyu.liar.studentrunclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.utils.XUtils;
import com.zcy.acache.ACache;

public class WelComeActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner mSpSushelou;
    private EditText mEdSushehao;
    private EditText mEdShouji;
    private Button mBtBaocun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if(ACache.get(App.app).getAsObject("run")!=null && (Boolean) ACache.get(App.app).getAsObject("run")==true){
            startActivity(new Intent(WelComeActivity.this,MainActivity.class));
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
    }

    @Override
    public void onClick(View view) {
        String phone = mEdShouji.getText().toString();
        String dorm = mEdSushehao.getText().toString();
        String ridgepole = mSpSushelou.getSelectedItem().toString();
        if(TextUtils.isEmpty(phone) || TextUtils.isEmpty(dorm) || TextUtils.isEmpty(ridgepole)){
            XUtils.ShowToast("请输入您的送餐地址完整信息！");
        }else{
            if(phone.length() != 11 || dorm.length() != 3){
                XUtils.ShowToast("您填入的信息有误！");
            }else{
                ACache.get(App.app).put("ridgepole",ridgepole);
                ACache.get(App.app).put("dorm",dorm);
                ACache.get(App.app).put("phone",phone);

                ACache.get(App.app).put("run",true);

                Log.e("宿舍楼:",ACache.get(App.app).getAsString("ridgepole"));
                Log.e("宿舍号:",ACache.get(App.app).getAsString("dorm"));
                Log.e("手机号:",ACache.get(App.app).getAsString("phone"));

                XUtils.ShowToast("设置成功！");

                startActivity(new Intent(WelComeActivity.this,MainActivity.class));
                finish();
            }
        }
    }
}
