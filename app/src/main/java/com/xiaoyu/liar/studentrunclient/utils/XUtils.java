package com.xiaoyu.liar.studentrunclient.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.xiaoyu.liar.studentrunclient.R;
import com.xiaoyu.liar.studentrunclient.view.App;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class XUtils {
    public static void ShowToast(String content){
        Toast.makeText(App.app,content,Toast.LENGTH_SHORT).show();
    }

    public static void copyDatabase(Context context){
        Log.e("数据库拷贝","开始");
        File dir = new File("data/data/"+context.getPackageName()+"/databases");
        if(!dir.exists() || !dir.isDirectory()){
            dir.mkdir();
        }
        File file = new File(dir,"myorder");
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if(!file.exists()){
            try {
                if(file.createNewFile()){
                    inputStream = context.getResources().openRawResource(R.raw.myorder);
                    outputStream = new FileOutputStream(file);
                    byte temp[] = new byte[inputStream.available()];
                    int abyte = 0;
                    while ((abyte = inputStream.read())!=-1){
                        Log.e("查看字节",abyte+"");
                        outputStream.write(abyte);
                    }
                }
            } catch (IOException e) {
                Log.e("读写数据库错误！",e.toString());
            }finally {
                if (inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(outputStream != null){
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static boolean isMobileNO(String mobileNums) {
        /**
         * 判断字符串是否符合手机号码格式
         * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
         * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
         * 电信号段: 133,149,153,170,173,177,180,181,189
         * @param str
         * @return 待检测的字符串
         */
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        return mobileNums.matches(telRegex);
    }
    /**
     * 判断手机号是否正确
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        String telRegex = "^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$";
        return phone.matches(telRegex);
    }
}
