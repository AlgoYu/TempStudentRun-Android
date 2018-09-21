package com.xiaoyu.liar.studentrunclient.model;

import android.content.Context;
import android.widget.Toast;

import com.xiaoyu.liar.studentrunclient.model.entity.User;
import com.zcy.acache.ACache;

public class IWelComeModelImpl implements IWelComeModel{
    @Override
    public Boolean SaveCacheUserData(Context context, User user) {
        Boolean aBoolean = false;
        try {
            ACache.get(context).put("UserRidgepole",user.getRidgepole());
            ACache.get(context).put("UserDorm",user.getDorm());
            ACache.get(context).put("UserConnect",user.getConnect());
            aBoolean = true;
        }catch (Exception e){
            Toast.makeText(context,e.toString(),Toast.LENGTH_SHORT).show();
        }
        return aBoolean;
    }

    @Override
    public void SaveFirstRun(Context context, Boolean flag) {
        ACache.get(context).put("FirstRun",flag);
    }

    @Override
    public Boolean isFirstRun(Context context) {
        return (Boolean) ACache.get(context).getAsObject("FirstRun");
    }

    @Override
    public User getCacheUserData(Context context) {
        User user = new User();
        user.setRidgepole(ACache.get(context).getAsString("UserRidgepole"));
        user.setDorm(ACache.get(context).getAsString("UserDorm"));
        user.setConnect(ACache.get(context).getAsString("UserConnect"));
        return user;
    }
}
