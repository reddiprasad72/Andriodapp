package com.zhengsr.zdwon_lib.bean;

import android.content.Context;

import com.zhengsr.zdwon_lib.callback.BaseListener;

import java.util.LinkedHashMap;
import java.util.Map;


public class ZTaskBean {

    public Context context;

    public String url;


    public int threadCount = 1;

    public int reFreshTime = 1000;


    public String filePath;


    public String fileName;
    public String fileNameAdded;


    public Map<String,String> paramsMap = new LinkedHashMap<>();

    public boolean allowBackDownload = false;


    public boolean useBreakPoint;


    public long fileLength = -1;


    public BaseListener listener;


    public boolean isGet = true;

    @Override
    public String toString() {
        return "ZTaskBean{" +
                "context=" + context +
                ", url='" + url + '\'' +
                ", threadCount=" + threadCount +
                ", reFreshTime=" + reFreshTime +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", fileNameAdded='" + fileNameAdded + '\'' +
                ", paramsMap=" + paramsMap +
                ", allowBackDownload=" + allowBackDownload +
                ", useBreakPoint=" + useBreakPoint +
                ", fileLength=" + fileLength +
                ", listener=" + listener +
                ", isGet=" + isGet +
                '}';
    }
}
