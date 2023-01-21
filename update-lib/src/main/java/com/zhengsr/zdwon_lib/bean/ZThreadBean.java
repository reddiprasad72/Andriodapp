package com.zhengsr.zdwon_lib.bean;




public class ZThreadBean {

    public int threadId;

    public String url;

    public String name;

    public long startPos;

    public long endPos;

    public long threadLength = 0;


    @Override
    public String toString() {
        return "ZThreadBean{" +
                "threadId=" + threadId +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                ", threadLength=" + threadLength +
                '}';
    }
}
