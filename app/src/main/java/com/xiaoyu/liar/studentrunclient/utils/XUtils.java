package com.xiaoyu.liar.studentrunclient.utils;

import android.content.Context;
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
}
