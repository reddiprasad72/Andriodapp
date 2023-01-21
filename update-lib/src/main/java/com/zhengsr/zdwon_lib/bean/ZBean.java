package com.zhengsr.zdwon_lib.bean;


public class ZBean {

    public long totalLength;

    public long curLength;

    public String speed;

    public float progress;

    @Override
    public String toString() {
        return "ZBean{" +
                "totalLength=" + totalLength +
                ", curLength=" + curLength +
                ", speed='" + speed + '\'' +
                '}';
    }
}
