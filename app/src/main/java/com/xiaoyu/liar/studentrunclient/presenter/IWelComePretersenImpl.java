package com.xiaoyu.liar.studentrunclient.presenter;

import android.content.Context;
import com.xiaoyu.liar.studentrunclient.model.IWelComeModel;
import com.xiaoyu.liar.studentrunclient.model.IWelComeModelImpl;
import com.xiaoyu.liar.studentrunclient.model.entity.User;
import com.xiaoyu.liar.studentrunclient.view.IWelComeView;

public class IWelComePretersenImpl implements IWelComePretersen{
    IWelComeView iWelComeView;
    IWelComeModel iWelComeModel = new IWelComeModelImpl();

    public IWelComePretersenImpl(IWelComeView iWelComeView) {
        this.iWelComeView = iWelComeView;
    }

    @Override
    public Boolean SaveUserData(Context context,User user) {
        return iWelComeModel.SaveCacheUserData(context,user);
    }

    @Override
    public void FirstRun(Context context, Boolean flag) {
        iWelComeModel.SaveFirstRun(context,flag);
    }

    @Override
    public Boolean isFirstRun(Context context) {
        return iWelComeModel.isFirstRun(context);
    }

    @Override
    public User getUserData(Context context) {
        return iWelComeModel.getCacheUserData(context);
    }

}