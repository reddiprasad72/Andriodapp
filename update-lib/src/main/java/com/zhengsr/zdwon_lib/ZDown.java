package com.zhengsr.zdwon_lib;

import android.content.Context;

import com.zhengsr.zdwon_lib.callback.BaseListener;
import com.zhengsr.zdwon_lib.entrance.RequestManager;
import com.zhengsr.zdwon_lib.entrance.CheckRequest;
import com.zhengsr.zdwon_lib.entrance.imp.db.ZDBManager;

public class ZDown {

    public static CheckRequest checkWith(Context context){
        return CheckRequest.get(context);
    }

    public static RequestManager with(Context context){
        ZDBManager.getInstance().config(context.getApplicationContext());
        return RequestManager.getInstance().with(context);
    }


    public static void pause(){
        if (RequestManager.getInstance().mTask != null) {
            RequestManager.getInstance().mTask.pause();
        }
    }


    public static void start(){
        if (RequestManager.getInstance().mTask != null) {
            RequestManager.getInstance().mTask.start();
        }
    }


    public static void stopTask(){
        if (RequestManager.getInstance().mTask != null) {
            RequestManager.getInstance().mTask.pause();
            RequestManager.getInstance().mTask = null;
        }
    }


    public static void stopTaskAndDeleteCache() {
        if (RequestManager.getInstance().mTask != null) {
            RequestManager.getInstance().mTask.pause();
            RequestManager.getInstance().mTask.deleteCache();
            RequestManager.getInstance().mTask = null;
        }

    }


    public static boolean isTaskExists(){
        return RequestManager.getInstance().mTask != null;
    }


    public static boolean isRunning(){
        if (RequestManager.getInstance().mTask != null) {
            return RequestManager.getInstance().mTask.isRunning();
        }
        return false;
    }


    public static void updateListener(BaseListener listener){
        if (RequestManager.getInstance().mTask != null) {
            RequestManager.getInstance().mTask.updateListener(listener);
        }
    }


    public static void deleteCacheAndStart(){
        stopTaskAndDeleteCache();
        start();
    }
}
