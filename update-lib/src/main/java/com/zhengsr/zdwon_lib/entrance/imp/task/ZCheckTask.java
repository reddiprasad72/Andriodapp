package com.zhengsr.zdwon_lib.entrance.imp.task;

import com.alibaba.fastjson.JSON;
import com.zhengsr.zdwon_lib.bean.ZTaskBean;
import com.zhengsr.zdwon_lib.callback.CheckListener;
import com.zhengsr.zdwon_lib.entrance.imp.net.ZHttpCreate;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ZCheckTask {
    private static final String TAG = "ZCheckTask";

    public ZCheckTask(final ZTaskBean info) {
        Call<String> call;
        if (info.paramsMap != null && info.paramsMap.size() > 0) {
            if (info.isGet) {
                call = ZHttpCreate.getService().getJson(info.url, info.paramsMap);
            }else{
                call = ZHttpCreate.getService().postJson(info.url,info.paramsMap);
            }
        } else {
            if (info.isGet) {
                call = ZHttpCreate.getService().getJson(info.url);
            }else{
                call = ZHttpCreate.getService().postJson(info.url);
            }
        }
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                CheckListener listener = (CheckListener) info.listener;
                try {
                    String json = response.body();

                    Class mclazz = listener.getClassType();

                    if (mclazz == null ||  mclazz == String.class){
                        listener.onCheck(json);
                    }else{
                        Object data = JSON.parseObject(json, mclazz);

                        listener.onCheck(data);
                    }


                } catch (Exception e) {
                    listener.onFail(e.getMessage());
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                info.listener.onFail(t.toString());
            }
        });

    }


}
