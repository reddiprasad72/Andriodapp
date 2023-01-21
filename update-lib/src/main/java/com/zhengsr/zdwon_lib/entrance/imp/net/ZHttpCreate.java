package com.zhengsr.zdwon_lib.entrance.imp.net;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ZHttpCreate {

    private static final int TIME_OUT = 20;


    public static ZHttpServer getService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .client(getOkhttpClient())
                .build();
        return retrofit.create(ZHttpServer.class);
    }

    public static OkHttpClient getOkhttpClient(){
        return OkHttpHolder.BUILDER;
    }


    private static class OkHttpHolder{
         static OkHttpClient BUILDER = new OkHttpClient.Builder()
                 .readTimeout(TIME_OUT, TimeUnit.SECONDS)
                 .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                 .writeTimeout(TIME_OUT+TIME_OUT,TimeUnit.SECONDS)
                 .addInterceptor(new Interceptor() {
                     @Override
                     public Response intercept(Chain chain) throws IOException {
                         Request original = chain.request();

                         Request request = original.newBuilder()
                                 .header("Accept-Encoding", "identity")
                                 .build();

                         return chain.proceed(request);

                     }
                 })
                 .build();
    }
}
