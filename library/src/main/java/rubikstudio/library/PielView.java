package rubikstudio.library;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

import rubikstudio.library.model.LuckyItem;

/**
 * Created by kiennguyen on 11/5/16.
 */

public class PielView extends View {
    private RectF mRange = new RectF();
    private int mRadius;

    private Paint mArcPaint;
    private Paint mBackgroundPaint;
    private Paint mTextPaint;

    private float mStartAngle = 0;
    private int mCenter;
    private int mPadding;
    private int mTargetIndex;
    private int mRoundOfNumber = 4;
    private boolean isRunning = false;

    private int defaultBackgroundColor = -1;
    private Drawable drawableCenterImage;
    private int textColor = 0xffffffff;

    private int defaultBackgroundImage = -1;

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

    public void setData(List<LuckyItem> luckyItemList) {
        this.mLuckyItemList = luckyItemList;
        invalidate();
    }

    public void setPieBackgroundImage(int backgroundImage) {
        defaultBackgroundImage = backgroundImage;
        invalidate();
    }

    private void drawPieBackgroundWithBitmap(Canvas canvas, int defaultBackgroundImage) {
        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                defaultBackgroundImage);

        canvas.drawBitmap(bitmap, null, new Rect(mPadding/2,mPadding/2,
                getMeasuredWidth() - mPadding/2, getMeasuredHeight()-mPadding/2), null);
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mLuckyItemList == null) {
            return;
        }

        drawPieBackgroundWithBitmap(canvas, defaultBackgroundImage);
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

        mCenter = width/2;

        setMeasuredDimension(width, width);
    }

    /**
     * @return
     */
    private float getAngleOfIndexTarget() {
        return (360f / mLuckyItemList.size()) * mTargetIndex;
    }

    /**
     * @param numberOfRound
     */
    public void setRound(int numberOfRound) {
        mRoundOfNumber = numberOfRound;
    }

    /**
     * @param index
     */
    public void rotateTo(int index) {
        if (isRunning) {
            return;
        }
        mTargetIndex = index;
        setRotation(0);

        //  初始值對齊 \項目0的右邊界線/，正上方箭頭對到的是項目0的右邊界線
//        float targetAngle = 360 * mRoundOfNumber - getAngleOfIndexTarget() + (360f / mLuckyItemList.size()) / 2;

        //  初始值對齊 \項目0的中間/，正上方箭頭對到的是項目0
        float targetAngle = 360f * mRoundOfNumber - getAngleOfIndexTarget() ;

        animate()
                .setInterpolator(new DecelerateInterpolator())
                .setDuration(mRoundOfNumber * 1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isRunning = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isRunning = false;
                        if (mPieRotateListener != null) {
                            mPieRotateListener.rotateDone(mTargetIndex);
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                })
                .rotation(targetAngle)
                .start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
       return false;
    }
}
