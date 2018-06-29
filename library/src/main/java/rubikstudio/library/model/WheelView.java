package rubikstudio.library.model;

import android.animation.Animator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import java.util.List;

public class WheelView extends android.support.v7.widget.AppCompatImageView {

    private LuckyRoundItemSelectedListener mLuckyRoundItemSelectedListener;

    private int mTargetIndex;
    private int mRoundOfNumber = 4;
    private boolean isRunning = false;


    private List<LuckyItem> mLuckyItemList;

    public WheelView(Context context) {
        super(context);
    }

    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WheelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setData(List<LuckyItem> luckyItemList) {
        this.mLuckyItemList = luckyItemList;
    }

    public interface LuckyRoundItemSelectedListener {
        void LuckyRoundItemSelected(int index);
    }

    public void setLuckyRoundItemSelectedListener(LuckyRoundItemSelectedListener listener) {
        this.mLuckyRoundItemSelectedListener = listener;
    }

    public void startLuckyWheelWithTargetIndex(int index) {
        rotateTo(index);
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
                        if (mLuckyRoundItemSelectedListener != null) {
                            mLuckyRoundItemSelectedListener.LuckyRoundItemSelected(mTargetIndex);
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
