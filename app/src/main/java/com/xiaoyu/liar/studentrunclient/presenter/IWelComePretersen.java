package com.xiaoyu.liar.studentrunclient.presenter;

import android.content.Context;
import com.xiaoyu.liar.studentrunclient.model.entity.User;

public interface IWelComePretersen {
    Boolean SaveUserData(Context context,User user);
    void FirstRun(Context context,Boolean flag);
    Boolean isFirstRun(Context context);
    User getUserData(Context context);
}