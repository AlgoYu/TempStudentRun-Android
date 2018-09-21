package com.xiaoyu.liar.studentrunclient.view;

import com.xiaoyu.liar.studentrunclient.model.entity.User;

public interface IWelComeView {
    User getUserData();
    void cleanEdit();
    void showToast(String message);
}
