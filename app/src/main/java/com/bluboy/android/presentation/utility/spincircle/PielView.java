package com.bluboy.android.presentation.utility.spincircle;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.List;
import java.util.Random;

import androidx.annotation.IntDef;
import androidx.core.graphics.ColorUtils;

public class PielView extends View {
    private RectF mRange = new RectF();
    private int mRadius;
    private Paint mArcPaint;
    private Paint mBackgroundPaint;
    private TextPaint mTextPaint;

    public Canvas canvas;
    public Bitmap bitmap;
    public Rect rect;

    private float mStartAngle = 0;
    private int mCenter;
    private int mPadding;
    private int mTopTextPadding;
    private int mSecondaryTextPadding;
    private int mThirdTextPadding;
    private int mTopTextSize;
    private int mSecondaryTextSize;
    private int mRoundOfNumber = 4;
    private int mEdgeWidth = -1;
    private boolean isRunning = false;
    private int borderColor = 0;
    private int defaultBackgroundColor = 0;
    private Drawable drawableCenterImage;
    private int textColor = 0;
    private int predeterminedNumber = -1;

    private boolean flag = true;

    float viewRotation;
    double fingerRotation;
    long downPressTime, upPressTime;
    double newRotationStore[] = new double[3];

    private List<LuckyItem> mLuckyItemList;

    private PieRotateListener mPieRotateListener;

    public interface PieRotateListener {
        void rotateDone(int index);
    }

    public PielView(Context context) {
        super(context);
    }

    public PielView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPieRotateListener(PieRotateListener listener) {
        this.mPieRotateListener = listener;
    }

    private void init() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);


        if (textColor != 0) mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                getResources().getDisplayMetrics()));

        mRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);
    }

    public int getLuckyItemListSize() {
        return mLuckyItemList.size();
    }

    public void setData(List<LuckyItem> luckyItemList) {
        this.mLuckyItemList = luckyItemList;
        invalidate();
    }

    public void setPieBackgroundColor(int color) {
        defaultBackgroundColor = color;
        invalidate();
    }

    public void setBorderColor(int color) {
        borderColor = color;
        invalidate();
    }

    public void setTopTextPadding(int padding) {
        mTopTextPadding = padding;
        invalidate();
    }

    public void setSecondaryTextPadding(int padding) {
        mSecondaryTextPadding = padding;
        invalidate();
    }

    public void setThirdTextPadding(int padding) {
        mThirdTextPadding = padding;
        invalidate();
    }


    public void setPieCenterImage(Drawable drawable) {
        drawableCenterImage = drawable;
        invalidate();
    }

    public void setTopTextSize(int size) {
        mTopTextSize = size;
        invalidate();
    }

    public void setSecondaryTextSizeSize(int size) {
        mSecondaryTextSize = size;
        invalidate();
    }


    public void setBorderWidth(int width) {
        mEdgeWidth = width;
        invalidate();
    }

    public void setPieTextColor(int color) {
        textColor = color;
        invalidate();
    }

    private void drawPieBackgroundWithBitmap(Canvas canvas, Bitmap bitmap) {
        canvas.drawBitmap(bitmap, null, new Rect(mPadding / 2, mPadding / 2,
                getMeasuredWidth() - mPadding / 2,
                getMeasuredHeight() - mPadding / 2), null);
    }


    /**
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLuckyItemList == null) {
            return;
        }

        drawBackgroundColor(canvas, defaultBackgroundColor);
        init();
        float tmpAngle = mStartAngle;
        float sweepAngle = 360f / mLuckyItemList.size();

        for (int i = 0; i < mLuckyItemList.size(); i++) {

            if (mLuckyItemList.get(i).color != 0) {
                mArcPaint.setStyle(Paint.Style.FILL);


                mArcPaint.setColor(mLuckyItemList.get(i).color);


                canvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint);

            }

            if (borderColor != 0 && mEdgeWidth > 0) {
                mArcPaint.setStyle(Paint.Style.STROKE);
                mArcPaint.setColor(borderColor);
                mArcPaint.setStrokeWidth(mEdgeWidth);
                if (mLuckyItemList.get(i).color == 0xffF5D919) {
                    mArcPaint.setShadowLayer(10f, 0, 0, Color.WHITE);
                }
//                mArcPaint.setS
                canvas.drawArc(mRange, tmpAngle, sweepAngle, true, mArcPaint);
            }

            int sliceColor = mLuckyItemList.get(i).color != 0 ? mLuckyItemList.get(i).color : defaultBackgroundColor;

            if (!TextUtils.isEmpty(mLuckyItemList.get(i).topText))
                drawTopText(canvas, tmpAngle, sweepAngle, mLuckyItemList.get(i).topText, sliceColor);
            if (!TextUtils.isEmpty(mLuckyItemList.get(i).secondaryText) && !mLuckyItemList.get(i).secondaryText.equals("???0"))
                drawSecondaryText(canvas, tmpAngle, mLuckyItemList.get(i).secondaryText, sliceColor);
            if (!TextUtils.isEmpty(mLuckyItemList.get(i).thirdText))
                drawThirdText(canvas, tmpAngle, sweepAngle, mLuckyItemList.get(i).thirdText, sliceColor);
            if (!TextUtils.isEmpty(mLuckyItemList.get(i).fourText))
                drawFourText(canvas, tmpAngle, sweepAngle, mLuckyItemList.get(i).fourText, sliceColor);

            if (mLuckyItemList.get(i).icon != null) {
//                drawImage(canvas, tmpAngle, mLuckyItemList.get(i).icon);

            }
            tmpAngle += sweepAngle;
        }
//        drawCenterImage(canvas, drawableCenterImage);
    }


    private void drawBackgroundColor(Canvas canvas, int color) {
        if (color == 0)
            return;
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(color);
        canvas.drawCircle(mCenter, mCenter, mCenter - 5, mBackgroundPaint);
    }

    /**
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());

        mPadding = getPaddingLeft() == 0 ? 10 : getPaddingLeft();
        mRadius = width - mPadding * 2;

        mCenter = width / 2;

        setMeasuredDimension(width, width);
    }

    /**
     * @param canvas
     * @param tmpAngle
     * @param image
     */
    private void drawImage(Canvas canvas, float tmpAngle, Bitmap image) {
        int imgWidth = mRadius / 5;    ///   SIZE OF IMAGE
        float angle = (float) ((tmpAngle + 360f / mLuckyItemList.size() / 2) * 3.14 / 180);    // 3.14  = Math.PI

        int x = (int) (mCenter + mRadius / 2 / 1.5 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 1.5 * Math.sin(angle));
//        Rect rect = new Rect(x - imgWidth / 3, y - imgWidth / 3,
//                x + imgWidth / 3, y + imgWidth / 3);


//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setDither(true);
//        paint.setFilterBitmap(true);
//
//        float degree = 360f /mLuckyItemList.size();
//        Matrix matrix = new Matrix();
//        matrix.setRotate(45,angle,angle);
//        canvas.drawBitmap(image,matrix,paint);

        Rect rect = new Rect(x - imgWidth, y - imgWidth,
                x + imgWidth, y + imgWidth);

        canvas.drawBitmap(image, null, rect, null);   //original

//        float sweepAngle = 360f / mLuckyItemList.size();
//        canvas.drawBitmap(image,rect,mRange,mArcPaint);

    }

    private void drawCenterImage(Canvas canvas, Drawable drawable) {
        Bitmap bitmap = LuckyWheelUtils.drawableToBitmap(drawable);
        bitmap = Bitmap.createScaledBitmap(bitmap, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), false);
        canvas.drawBitmap(bitmap, getMeasuredWidth() / 2 - bitmap.getWidth() / 2,
                getMeasuredHeight() / 2 - bitmap.getHeight() / 2, null);
    }

    private boolean isColorDark(int color) {
        double colorValue = ColorUtils.calculateLuminance(color);
        double compareValue = 0.30d;
        return colorValue <= compareValue;
    }


    /**
     * @param canvas
     * @param tmpAngle
     * @param sweepAngle
     * @param mStr
     */
    // Display only "Better luck"
    private void drawTopText(Canvas canvas, float tmpAngle, float sweepAngle, String mStr, int backgroundColor) {
        Path path = new Path();
        path.addArc(mRange, tmpAngle, sweepAngle);

//        if (textColor == 0)
        mTextPaint.setColor(isColorDark(backgroundColor) ? 0xffE7B492 : 0xff3C3939);

        Typeface typeface = Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL);
        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setLetterSpacing(0.05f);
//        mTextPaint.setTextSize(mTopTextSize);
        if (mLuckyItemList.size() > 6) {
            mTextPaint.setTextSize(LuckyWheelUtils.convertDpToPixel(9f, getContext()));
        } else {
            mTextPaint.setTextSize(LuckyWheelUtils.convertDpToPixel(14f, getContext()));
        }
        float textWidth = mTextPaint.measureText(mStr);
        int hOffset = (int) (mRadius * Math.PI / mLuckyItemList.size() / 2 - textWidth / 2);

        int vOffset = mTopTextPadding;

        canvas.drawTextOnPath(mStr, path, hOffset, vOffset, mTextPaint);
//        canvas.drawTextOnPath(mStr, path, mTopTextPadding / 7f, mTextPaint.getTextSize() / 2.75f, mTextPaint);
    }

    /**
     * @param canvas
     * @param tmpAngle
     * @param mStr
     * @param backgroundColor
     */
    // Display only "Next"
    private void drawThirdText(Canvas canvas, float tmpAngle, float sweepAngle, String mStr, int backgroundColor) {
        Path path = new Path();
        path.addArc(mRange, tmpAngle, sweepAngle);

//        if (textColor == 0)
        mTextPaint.setColor(isColorDark(backgroundColor) ? 0xffE7B492 : 0xff3C3939);

        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
//        mTextPaint.setTextSize(mSecondaryTextSize);

        mTextPaint.setLetterSpacing(0.3f);
        if (mLuckyItemList.size() > 6) {
            mTextPaint.setTextSize(LuckyWheelUtils.convertDpToPixel(9f, getContext()));
        } else {
            mTextPaint.setTextSize(LuckyWheelUtils.convertDpToPixel(14f, getContext()));
        }
        float textWidth = mTextPaint.measureText(mStr);
        int hOffset = (int) (mRadius * Math.PI / mLuckyItemList.size() / 2 - textWidth / 2);

        int vOffset = mSecondaryTextPadding;

        canvas.drawTextOnPath(mStr, path, hOffset, vOffset, mTextPaint);
//        canvas.drawTextOnPath(mStr, path, mTopTextPadding / 7f, mTextPaint.getTextSize() / 2.75f, mTextPaint);
    }

    // Display only "time"
    private void drawFourText(Canvas canvas, float tmpAngle, float sweepAngle, String mStr, int backgroundColor) {
        Path path = new Path();
        path.addArc(mRange, tmpAngle, sweepAngle);

//        if (textColor == 0)
        mTextPaint.setColor(isColorDark(backgroundColor) ? 0xffE7B492 : 0xff3C3939);

        Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
//        mTextPaint.setTextSize(mSecondaryTextSize);

        //mTextPaint.setLetterSpacing(0.8f);
        if (mLuckyItemList.size() > 6) {
            mTextPaint.setTextSize(LuckyWheelUtils.convertDpToPixel(9f, getContext()));
        } else {
            mTextPaint.setTextSize(LuckyWheelUtils.convertDpToPixel(14f, getContext()));
        }
        float textWidth = mTextPaint.measureText(mStr);
        int hOffset = (int) (mRadius * Math.PI / mLuckyItemList.size() / 2 - textWidth / 2);

        int vOffset = mThirdTextPadding;

        canvas.drawTextOnPath(mStr, path, hOffset, vOffset, mTextPaint);
//        canvas.drawTextOnPath(mStr, path, mTopTextPadding / 7f, mTextPaint.getTextSize() / 2.75f, mTextPaint);
    }


    // Display all Amount
    private void drawSecondaryText(Canvas canvas, float tmpAngle, String mStr, int backgroundColor) {
        canvas.save();
        int arraySize = mLuckyItemList.size();


        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);

        if (textColor != 0) mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
                getResources().getDisplayMetrics()));

        mTextPaint.setColor(isColorDark(backgroundColor) ? 0xffE7B492 : 0xff3C3939);

        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);


        /*canvas.save();
        int arraySize = mLuckyItemList.size();
//        if (textColor == 0)
            mTextPaint.setColor(isColorDark(backgroundColor) ? 0xffE7B492 : 0xff3C3939);
        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);*/
        mTextPaint.setTypeface(typeface);
        mTextPaint.setTextSize(mTopTextSize);
        mTextPaint.setTextAlign(Paint.Align.RIGHT);

        float textWidth = mTextPaint.measureText(mStr);

        float initFloat = (tmpAngle + 360f / arraySize / 2);
        float angle = (float) (initFloat * Math.PI / 180);

        int x = (int) (mCenter + mRadius / 2 / 2 * Math.cos(angle));
        int y = (int) (mCenter + mRadius / 2 / 2 * Math.sin(angle));

        RectF rect = new RectF(x + textWidth, y,
                x - textWidth, y);

        Path path = new Path();
        path.addRect(rect, Path.Direction.CW);
        path.close();
        canvas.rotate(initFloat + (arraySize / 18f), x, y);
        canvas.drawTextOnPath(mStr, path, mTopTextPadding / 7f,
                mTextPaint.getTextSize() / 2.75f,
                mTextPaint);
        canvas.restore();
    }

    /**
     * @return
     */
    private float getAngleOfIndexTarget(int index) {
        return (360f / mLuckyItemList.size()) * index;
    }

    /**
     * @param numberOfRound
     */
    public void setRound(int numberOfRound) {
        mRoundOfNumber = numberOfRound;
    }

    public void setStop(boolean flag) {
        this.flag = flag;
    }


    public void setPredeterminedNumber(int predeterminedNumber) {
        this.predeterminedNumber = predeterminedNumber;
    }

    public void rotateTo(final int index) {
        Random rand = new Random();
        rotateTo(index, SpinRotation.CLOCKWISE, false);
//        rotateTo(index, rotation, false);
    }

    public void rotateToInitial() {
        if (isRunning) {
            return;
        }

        int rotationAssess = 1;

        //If the staring position is already off 0 degrees, make an illusion that the rotation
        // has smoothly been triggered.
        // But this inital animation will just reset the position of the circle to 0 degreees.

        if (getRotation() != 0.0f) {
            setRotation(getRotation() % 360f);
            TimeInterpolator animationStart = new LinearInterpolator();
            //The multiplier is to do a big rotation again if the position is already near 360.
            // ANTI-CLOCK WISE - NOT USE
            float multiplier = getRotation() > 200f ? 2 : 1;
            animate()
                    .setInterpolator(animationStart)
                    .setDuration(0)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isRunning = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isRunning = false;
                            setRotation(0);
                            rotateTo(0, SpinRotation.CLOCKWISE, false);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    })
                    .rotation(360f * multiplier * rotationAssess)
                    .start();
            return;
        }

        float targetAngle =
                ((360f * 0 * rotationAssess) + 270f - getAngleOfIndexTarget(0)
                        - (360f / mLuckyItemList.size()) / 2);
        animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(2L)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunning = false;
                        setRotation(getRotation() % 360f);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .rotation(targetAngle)
                .start();

    }

    /**
     * @param index
     * @param rotation,  spin orientation of the wheel if clockwise or counterclockwise
     * @param startSlow, either animates a slow start or an immediate turn based on the trigger
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public void rotateTo(final int index, @SpinRotation final int rotation, boolean startSlow) {
        if (isRunning) {
            return;
        }

        int rotationAssess = rotation <= 0 ? 1 : -1;

        //If the staring position is already off 0 degrees, make an illusion that the rotation has smoothly been triggered.
        // But this inital animation will just reset the position of the circle to 0 degreees.
        if (getRotation() != 0.0f) {
            setRotation(getRotation() % 360f);
            TimeInterpolator animationStart = startSlow ? new AccelerateInterpolator() : new LinearInterpolator();
            //The multiplier is to do a big rotation again if the position is already near 360.
            // ANTI-CLOCK WISE - NOT USE
            float multiplier = getRotation() > 200f ? 2 : 1;
            animate()
                    .setInterpolator(animationStart)
                    .setDuration(500L)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            isRunning = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isRunning = false;
                            setRotation(0);
                            rotateTo(index, rotation, false);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    })
                    .rotation(360f * multiplier * rotationAssess)
                    .start();
            return;
        }

        // CLOCK - WISE USE
        // This addition of another round count for counterclockwise is to simulate the perception of the same number of spin
        // if you still need to reach the same outcome of a positive degrees rotation with the number of rounds reversed.
        if (rotationAssess < 0) mRoundOfNumber++;

        float targetAngle =
                ((360f * mRoundOfNumber * rotationAssess) + 270f - getAngleOfIndexTarget(index)
                        - (360f / mLuckyItemList.size()) / 2);
        animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(mRoundOfNumber * 300 + 100L)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunning = false;
                        setRotation(getRotation() % 360f);
                        if (mPieRotateListener != null && flag) {
                            mPieRotateListener.rotateDone(index);

                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .rotation(targetAngle)
                .start();
    }

    public boolean touchEnabled = true;

    public boolean isTouchEnabled() {
        return touchEnabled;
    }

    public void setTouchEnabled(boolean touchEnabled) {
        this.touchEnabled = touchEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRunning || !touchEnabled) {
            return false;
        }

        final float x = event.getX();
        final float y = event.getY();

        final float xc = getWidth() / 2.0f;
        final float yc = getHeight() / 2.0f;

        double newFingerRotation;


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                viewRotation = (getRotation() + 360f) % 360f;
//                fingerRotation = Math.toDegrees(Math.atan2(x - xc, yc - y));
//                downPressTime = event.getEventTime();
                return true;
            case MotionEvent.ACTION_MOVE:
//                newFingerRotation = Math.toDegrees(Math.atan2(x - xc, yc - y));
//
//                if (isRotationConsistent(newFingerRotation)) {
//                    setRotation(newRotationValue(viewRotation, fingerRotation, newFingerRotation));
//                }
                return true;
            case MotionEvent.ACTION_UP:
//                newFingerRotation = Math.toDegrees(Math.atan2(x - xc, yc - y));
//                float computedRotation = newRotationValue(viewRotation, fingerRotation, newFingerRotation);
//
//                fingerRotation = newFingerRotation;
//
//                // This computes if you're holding the tap for too long
//                upPressTime = event.getEventTime();
//                if (upPressTime - downPressTime > 700L) {
//                    // Disregarding the touch since the tap is too slow
//                    return true;
//                }
//
//                // These operators are added so that fling difference can be evaluated
//                // with usually numbers that are only around more or less 100 / -100.
//                if (computedRotation <= -250f) {
//                    computedRotation += 360f;
//                } else if (computedRotation >= 250f) {
//                    computedRotation -= 360f;
//                }
//
//                double flingDiff = computedRotation - viewRotation;
//                if (flingDiff >= 200 || flingDiff <= -200) {
//                    if (viewRotation <= -50f) {
//                        viewRotation += 360f;
//                    } else if (viewRotation >= 50f) {
//                        viewRotation -= 360f;
//                    }
//                }
//
//                flingDiff = computedRotation - viewRotation;
//
//                if (flingDiff <= -60 ||
//                        //If you have a very fast flick / swipe, you an disregard the touch difference
//                        (flingDiff < 0 && flingDiff >= -59 && upPressTime - downPressTime <= 200L)) {
//                    if (predeterminedNumber > -1) {
//                        rotateTo(predeterminedNumber, SpinRotation.COUNTERCLOCKWISE, false);
//                    } else {
//                        rotateTo(getFallBackRandomIndex(), SpinRotation.COUNTERCLOCKWISE, false);
//                    }
//                }
//
//                if (flingDiff >= 60 ||
//                        //If you have a very fast flick / swipe, you an disregard the touch difference
//                        (flingDiff > 0 && flingDiff <= 59 && upPressTime - downPressTime <= 200L)) {
//                    if (predeterminedNumber > -1) {
//                        rotateTo(predeterminedNumber, SpinRotation.CLOCKWISE, false);
//                    } else {
//                        rotateTo(getFallBackRandomIndex(), SpinRotation.CLOCKWISE, false);
//                    }
//                }

                return true;
        }
        return super.onTouchEvent(event);
    }

    private float newRotationValue(final float originalWheenRotation, final double originalFingerRotation, final double newFingerRotation) {
        double computationalRotation = newFingerRotation - originalFingerRotation;
        return (originalWheenRotation + (float) computationalRotation + 360f) % 360f;
    }

    private int getFallBackRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(mLuckyItemList.size() - 1);
    }

    /**
     * This detects if your finger movement is a result of an actual raw touch event of if it's from a view jitter.
     * This uses 3 events of rotation temporary storage so that differentiation between swapping touch events can be determined.
     *
     * @param newRotValue
     */
    private boolean isRotationConsistent(final double newRotValue) {
        double evalValue = newRotValue;

        if (Double.compare(newRotationStore[2], newRotationStore[1]) != 0) {
            newRotationStore[2] = newRotationStore[1];
        }
        if (Double.compare(newRotationStore[1], newRotationStore[0]) != 0) {
            newRotationStore[1] = newRotationStore[0];
        }

        newRotationStore[0] = evalValue;

        if (Double.compare(newRotationStore[2], newRotationStore[0]) == 0
                || Double.compare(newRotationStore[1], newRotationStore[0]) == 0
                || Double.compare(newRotationStore[2], newRotationStore[1]) == 0

                //Is the middle event the odd one out
                || (newRotationStore[0] > newRotationStore[1] && newRotationStore[1] < newRotationStore[2])
                || (newRotationStore[0] < newRotationStore[1] && newRotationStore[1] > newRotationStore[2])
        ) {
            return false;
        }
        return true;
    }


    @IntDef({
            SpinRotation.CLOCKWISE,
            SpinRotation.COUNTERCLOCKWISE
    })
    @interface SpinRotation {
        int CLOCKWISE = 0;
        int COUNTERCLOCKWISE = 1;
    }
}









