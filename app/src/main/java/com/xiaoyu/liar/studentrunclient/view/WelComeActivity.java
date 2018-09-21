package com.xiaoyu.liar.studentrunclient.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.model.entity.User;
import com.xiaoyu.liar.studentrunclient.presenter.IWelComePretersen;
import com.xiaoyu.liar.studentrunclient.presenter.IWelComePretersenImpl;

public class WelComeActivity extends AppCompatActivity implements IWelComeView, View.OnClickListener {
    private Spinner spSushelou;
    private EditText edSushehao;
    private EditText edShouji;
    private Button btBaocun;
    private IWelComePretersen iWelComePretersen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initView();
        iWelComePretersen = new IWelComePretersenImpl(this);
        if(iWelComePretersen.isFirstRun(this)!=null && iWelComePretersen.isFirstRun(this)!=null ==true){
            startActivity(new Intent(this,MainActivity.class));
        }
        btBaocun.setOnClickListener(this);
    }

    private void initView() {
        spSushelou = findViewById(R.id.sp_sushelou);
        edSushehao = findViewById(R.id.ed_sushehao);
        edShouji = findViewById(R.id.ed_shouji);
        btBaocun = findViewById(R.id.bt_baocun);
    }

    @Override
    public User getUserData() {
        User user = null;
        if(TextUtils.isEmpty(edSushehao.getText().toString()) || TextUtils.isEmpty(edShouji.getText().toString())){
            showToast("请输入完整的信息！");
        }else{
            user = new User();
            user.setRidgepole(spSushelou.getSelectedItem().toString());
            user.setConnect(edShouji.getText().toString());
            user.setDorm(edSushehao.getText().toString());
        }
        return user;
    }

    @Override
    public void cleanEdit() {
        edShouji.setText("");
        edSushehao.setText("");
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if(TextUtils.isEmpty(edSushehao.getText().toString()) || TextUtils.isEmpty(edShouji.getText().toString())){
            showToast("请输入完整的信息！");
        }else{
            User user = new User();
            user.setRidgepole(spSushelou.getSelectedItem().toString());
            user.setConnect(edShouji.getText().toString());
            user.setDorm(edSushehao.getText().toString());
            if(iWelComePretersen.SaveUserData(this,user)){
                showToast("保存成功！");
                startActivity(new Intent(this,MainActivity.class));
                iWelComePretersen.FirstRun(this,true);
            }
        }
    }
}
