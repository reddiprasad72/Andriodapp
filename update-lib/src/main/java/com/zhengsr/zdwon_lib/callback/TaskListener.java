package com.zhengsr.zdwon_lib.callback;

import com.zhengsr.zdwon_lib.bean.ZBean;


public interface TaskListener extends BaseListener{
    void onSuccess(String filePath,String md5Msg);
    void onDownloading(ZBean bean);

}
