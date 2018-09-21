package com.xiaoyu.liar.studentrunclient.view;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.xiaoyu.liar.studentrunclient.Adapter.MainFragmentAdapter;
import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.fragment.DingcanFragment;
import com.xiaoyu.liar.studentrunclient.fragment.DingdanFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    private ViewPager mainViewpager;
    private TabLayout mainTablayout;
    private MainFragmentAdapter mainFragmentAdapter;
    private List<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mainTablayout.addOnTabSelectedListener(this);
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
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Toast.makeText(this,"哈哈！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
