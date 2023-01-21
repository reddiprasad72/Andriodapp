package com.zhengsr.zdwon_lib.entrance;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.zhengsr.zdwon_lib.bean.ZTaskBean;
import com.zhengsr.zdwon_lib.widget.InvisiabelFragment;

import java.io.File;


public class CheckParams {
    private static final String TAG = "CheckParams";
    public CheckParams() {
    }

    public ZTaskBean check(ZTaskBean info,InvisiabelFragment.LifecyleListener lifecyleListener){

        if (TextUtils.isEmpty(info.url)){
            throw new RuntimeException("url can not be null");
        }


        if (TextUtils.isEmpty(info.filePath)){
            boolean isLake = lacksPermission(info.context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (Build.VERSION.SDK_INT >= 23 && isLake){
                throw new RuntimeException("you need reuqest WRITE_EXTERNAL_STORAGE");
            }else {
                info.filePath = info.context.getExternalFilesDir(null).getAbsolutePath() //Environment.getExternalStorageDirectory().getAbsolutePath()
                        + File.separator + "Bluboy";
                Log.i("ZDown info.filePath ",info.filePath);
                File file = new File(info.filePath);
                Log.i("ZDown file",file.getAbsolutePath().toString());
                if (!file.exists()) {
                    file.mkdirs();
                }
            }
        }else{

            File file = new File(info.filePath);
            if (!file.exists()){
                file.mkdirs();
            }
        }

        if (TextUtils.isEmpty(info.fileName)){
            info.fileName = "bluboy"+info.fileNameAdded+".apk";//info.url.substring(info.url.lastIndexOf("/")+1);
        }
        Log.i("ZDown filename",info.fileName);
        Log.i("ZDown filename",info.filePath);

        if (info.reFreshTime < 200){
            info.reFreshTime = 200;
        }
        if (info.listener == null){
            throw new RuntimeException("you need register listener to get network status");
        }


        if (info.threadCount >= 8){
            info.threadCount = 8;
        }

        register(info,lifecyleListener);

        return info;
    }


    public ZTaskBean checkJsonUrl(ZTaskBean info){

        if (TextUtils.isEmpty(info.url)){
            throw new RuntimeException("url can not be null");
        }
        if (info.listener == null){
            throw new RuntimeException("you need register jsonListener to get network status");
        }

        return info;
    }

    private  boolean lacksPermission(Context mContexts, String permission) {
        return ContextCompat.checkSelfPermission(mContexts, permission) !=
                PackageManager.PERMISSION_GRANTED;

    }

    private void register(final ZTaskBean info,InvisiabelFragment.LifecyleListener lifecyleListener){
        if (info.context instanceof FragmentActivity){

            FragmentActivity activity = (FragmentActivity) info.context;
            if (activity.isDestroyed()){
                throw new IllegalArgumentException("You cannot start a load task for a destroyed activity");
            }

            Fragment lifeFramgnet = activity.getSupportFragmentManager().findFragmentByTag(info.url);
            InvisiabelFragment fragment ;
            if (lifeFramgnet != null){
                fragment = (InvisiabelFragment) lifeFramgnet;
            }else{
                fragment = InvisiabelFragment.newInstance();
            }
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            if (!fragment.isAdded()){
                ft.add(fragment,info.url);
                ft.commit();
            }

            fragment.setLifecyleListener(lifecyleListener);

        }
    }
}
