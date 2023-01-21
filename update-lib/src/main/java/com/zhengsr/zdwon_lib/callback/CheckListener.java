package com.zhengsr.zdwon_lib.callback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public abstract class CheckListener<T> implements BaseListener {
    private static final String TAG = "CheckListener";
    private Class<T> classType;
    public CheckListener() {
        try {
            Type parentType = getClass().getGenericSuperclass();
            Type type = ((ParameterizedType)parentType).getActualTypeArguments()[0];
            classType = (Class<T>) type;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Class<T> getClassType() {
        return classType;
    }


    public abstract void onCheck(T data);

}
