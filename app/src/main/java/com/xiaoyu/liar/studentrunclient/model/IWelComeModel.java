package com.xiaoyu.liar.studentrunclient.model;

import android.content.Context;

import com.xiaoyu.liar.studentrunclient.model.entity.User;

public interface IWelComeModel {
    Boolean SaveCacheUserData(Context context, User user);
    void SaveFirstRun(Context context,Boolean flag);
    Boolean isFirstRun(Context context);
    User getCacheUserData(Context context);
}