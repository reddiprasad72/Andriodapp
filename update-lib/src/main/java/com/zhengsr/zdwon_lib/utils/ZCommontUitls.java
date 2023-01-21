package com.zhengsr.zdwon_lib.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ZCommontUitls {

    public static long getAvailDiskSize(String path){
        StatFs sf = new StatFs(path);
        long blockSize = sf.getBlockSizeLong();
        long availCount = sf.getAvailableBlocksLong();

        return blockSize * availCount;
    }

    public static <T> ObservableTransformer<T,T> rxScheduers(){
        return new ObservableTransformer<T,T>(){
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytesToHexString(digest.digest());
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static void installApk(Context mContext,String latestVersionCode){
        try {
            File file = new File(mContext.getExternalFilesDir(null).getAbsolutePath() //Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "Bluboy" + File.separator + "bluboy"+ latestVersionCode + ".apk");

            Log.i("ZDown filename",file.getAbsolutePath());
            if (!file.exists()) {
                return;
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);

            if (Build.VERSION.SDK_INT < 24) {
                intent.setAction("android.intent.action.VIEW");
                intent.addFlags(1);
                intent.putExtra("android.intent.extra.NOT_UNKNOWN_SOURCE", true);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                intent.setFlags(268435456);
            } else {
                Uri downloaded_apk = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
                Log.i("ZDown filename",downloaded_apk.toString());
                intent.setDataAndType(downloaded_apk, "application/vnd.android.package-archive");
                List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, 65536);
                Iterator var6 = resInfoList.iterator();

                while(true) {
                    if (!var6.hasNext()) {
                        intent.setFlags(335544323);
                        mContext.startActivity(intent);
                        break;
                    }

                    ResolveInfo resolveInfo = (ResolveInfo)var6.next();
                    mContext.grantUriPermission(mContext.getApplicationContext().getPackageName() + ".provider", downloaded_apk, 3);
                }
            }
            mContext.startActivity(intent);
        }catch (Exception var8){
            Log.e("ErrorInstallAPK : ", "" + var8.getMessage());
            var8.printStackTrace();
        }
    }
}
