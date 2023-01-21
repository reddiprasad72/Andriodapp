package com.zhengsr.zdwon_lib.widget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

public class InvisiabelFragment extends Fragment {
    private static final String TAG = "InvisiabelFragment";
    private LifecyleListener mListener;

    public static InvisiabelFragment newInstance() {
        
        Bundle args = new Bundle();
        
        InvisiabelFragment fragment = new InvisiabelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void setLifecyleListener(LifecyleListener listener){
        mListener = listener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mListener != null){
            mListener.onResume();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null){
            mListener.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mListener != null){
            mListener.onDestroy();
        }
    }

    public interface LifecyleListener {
        void onResume();
        void onStop();
        void onDestroy();
    }
}
