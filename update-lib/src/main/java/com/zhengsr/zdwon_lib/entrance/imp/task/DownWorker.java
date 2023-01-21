package com.zhengsr.zdwon_lib.entrance.imp.task;

import android.annotation.SuppressLint;

import com.zhengsr.zdwon_lib.bean.ZBean;
import com.zhengsr.zdwon_lib.bean.ZTaskBean;
import com.zhengsr.zdwon_lib.entrance.imp.net.ZHttpCreate;
import com.zhengsr.zdwon_lib.utils.ZCommontUitls;

import io.reactivex.internal.observers.BlockingBaseObserver;
import okhttp3.ResponseBody;


abstract class DownWorker {
    private static final String TAG = "DownWorker";

    protected ZTaskBean mTaskBean;
    protected ZBean mBean;
    public DownWorker(ZTaskBean bean) {
        mTaskBean = bean;
        mBean = new ZBean();

    }

    @SuppressLint("CheckResult")
    public void checkMemory(final ZTaskBean bean){

        if (bean.fileLength != -1){
           if (!isCanDown(bean.filePath,bean.fileLength)){
               bean.listener.onFail(bean.filePath+"error : No buffer space here");
           }else{
               handleData(bean);
           }
        }else{
            ZHttpCreate.getService().getFileLength(bean.url)
                    .compose(ZCommontUitls.<ResponseBody>rxScheduers())
                    .subscribeWith(new BlockingBaseObserver<ResponseBody>() {
                        @Override
                        public void onNext(ResponseBody responseBody) {
                            long contentLength = responseBody.contentLength();

                            if (!isCanDown(bean.filePath,contentLength)){
                                bean.listener.onFail(bean.filePath+"error : No buffer space here");
                            }else{
                                bean.fileLength = contentLength;
                                handleData(bean);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            bean.listener.onFail(e.getMessage());
                        }
                    });
        }
    }

    private boolean isCanDown(String filePath,long fileLength){
        long deviceSize = ZCommontUitls.getAvailDiskSize(filePath);
        if (fileLength > deviceSize){
            return false;
        }
        return true;
    }

    public abstract void handleData(ZTaskBean bean);


}
