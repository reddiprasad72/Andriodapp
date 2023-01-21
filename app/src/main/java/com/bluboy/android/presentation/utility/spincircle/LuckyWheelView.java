package com.bluboy.android.presentation.utility.spincircle;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.bluboy.android.R;

import java.util.List;
import java.util.Random;

public class LuckyWheelView extends RelativeLayout implements PielView.PieRotateListener {
    private int mBackgroundColor;
    private int mTextColor;
    private int mTopTextSize;
    private int mSecondaryTextSize;
    private int mBorderColor;
    private int mTopTextPadding;
    private int mSecondaryTextPadding;
    private int mThirdTextPadding;
    private int mEdgeWidth;
    private Drawable mCenterImage;
    private Drawable mCursorImage;

    private PielView pielView;
    private ImageView ivCursorView;

    private LuckyRoundItemSelectedListener mLuckyRoundItemSelectedListener;

    @Override
    public void rotateDone(int index) {
        if (mLuckyRoundItemSelectedListener != null) {
            mLuckyRoundItemSelectedListener.LuckyRoundItemSelected(index);
        }
    }

    public interface LuckyRoundItemSelectedListener {
        void LuckyRoundItemSelected(int index);
    }

    public void setLuckyRoundItemSelectedListener(LuckyRoundItemSelectedListener listener) {
        this.mLuckyRoundItemSelectedListener = listener;
    }

    public LuckyWheelView(Context context) {
        super(context);
        init(context, null);
    }

    public LuckyWheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param ctx
     * @param attrs
     */
    private void init(Context ctx, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = ctx.obtainStyledAttributes(attrs, R.styleable.LuckyWheelView);
//            mBackgroundColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwBackgroundColor, 0xffcc0000);
            mTopTextSize = typedArray.getDimensionPixelSize(R.styleable.LuckyWheelView_lkwTopTextSize, (int) LuckyWheelUtils.convertDpToPixel(20f, getContext()));
            mSecondaryTextSize = typedArray.getDimensionPixelSize(R.styleable.LuckyWheelView_lkwSecondaryTextSize, (int) LuckyWheelUtils.convertDpToPixel(10f, getContext()));
            mTextColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwTopTextColor, 0);
            mTopTextPadding = typedArray.getDimensionPixelSize(R.styleable.LuckyWheelView_lkwTopTextPadding,
                    (int) LuckyWheelUtils.convertDpToPixel(14f, getContext())) +
                    (int) LuckyWheelUtils.convertDpToPixel(14f, getContext());
            mSecondaryTextPadding = typedArray.getDimensionPixelSize(R.styleable.LuckyWheelView_lkwSecondaryTextPadding, (int) LuckyWheelUtils.convertDpToPixel(25f, getContext())) + (int) LuckyWheelUtils.convertDpToPixel(25f, getContext());
            mThirdTextPadding = typedArray.getDimensionPixelSize(R.styleable.LuckyWheelView_lkwThirdTextPadding,
                    (int) LuckyWheelUtils.convertDpToPixel(35f, getContext())) +
                    (int) LuckyWheelUtils.convertDpToPixel(35f, getContext());

            mCursorImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkwCursor);
            mCenterImage = typedArray.getDrawable(R.styleable.LuckyWheelView_lkwCenterImage);
            mEdgeWidth = typedArray.getInt(R.styleable.LuckyWheelView_lkwEdgeWidth, 10);
            mBorderColor = typedArray.getColor(R.styleable.LuckyWheelView_lkwEdgeColor, 0);
            typedArray.recycle();
        }

        LayoutInflater inflater = LayoutInflater.from(getContext());
        FrameLayout frameLayout = (FrameLayout) inflater.inflate(R.layout.view_lucky_wheel_layout, this, false);

        pielView = frameLayout.findViewById(R.id.pieView);
        ivCursorView = frameLayout.findViewById(R.id.cursorView);

        pielView.setPieRotateListener(this);
//        pielView.setPieBackgroundColor(mBackgroundColor);
        pielView.setTopTextPadding(mTopTextPadding);
        pielView.setSecondaryTextPadding(mSecondaryTextPadding);
        pielView.setThirdTextPadding(mThirdTextPadding);
        pielView.setTopTextSize(mTopTextSize);
        pielView.setSecondaryTextSizeSize(mSecondaryTextSize);
        pielView.setPieCenterImage(mCenterImage);
        pielView.setBorderColor(mBorderColor);
        pielView.setBorderWidth(mEdgeWidth);


        if (mTextColor != 0)
            pielView.setPieTextColor(mTextColor);

        ivCursorView.setImageDrawable(mCursorImage);

        addView(frameLayout);
    }


    public boolean isTouchEnabled() {
        return pielView.isTouchEnabled();
    }

    public void setTouchEnabled(boolean touchEnabled) {
        pielView.setTouchEnabled(touchEnabled);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //This is to control that the touch events triggered are only going to the PieView
        for (int i = 0; i < getChildCount(); i++) {
            if (isPielView(getChildAt(i))) {
                return super.dispatchTouchEvent(ev);
            }
        }
        return false;
    }

    private boolean isPielView(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < getChildCount(); i++) {
                if (isPielView(((ViewGroup) view).getChildAt(i))) {
                    return true;
                }
            }
        }
        return view instanceof PielView;
    }

    public void setLuckyWheelBackgrouldColor(int color) {
        pielView.setPieBackgroundColor(color);
    }

    public void setLuckyWheelCursorImage(int drawable) {
        ivCursorView.setBackgroundResource(drawable);
    }

    public void setLuckyWheelCenterImage(Drawable drawable) {
        pielView.setPieCenterImage(drawable);
    }

    public void setBorderColor(int color) {
        pielView.setBorderColor(color);
    }

    public void setLuckyWheelTextColor(int color) {
        pielView.setPieTextColor(color);
    }

    /**
     * @param data
     */
    public void setData(List<LuckyItem> data) {
        pielView.setData(data);
    }


    /**
     * @param numberOfRound
     */
    public void setRound(int numberOfRound) {
        pielView.setRound(numberOfRound);
    }


    public void setStop(boolean flag) {
        pielView.setStop(flag);
    }


    /**
     * @param fixedNumber
     */
    public void setPredeterminedNumber(int fixedNumber) {
        pielView.setPredeterminedNumber(fixedNumber);
    }

    public void startLuckyWheelWithTargetIndex(int index) {
        pielView.rotateTo(index);
    }

    public void startInitial() {
        pielView.rotateToInitial();
    }

    public void startLuckyWheelWithRandomTarget() {
        Random r = new Random();
        pielView.rotateTo(r.nextInt(pielView.getLuckyItemListSize() - 1));
    }
}
