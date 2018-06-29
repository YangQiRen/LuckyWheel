package com.ryan.luckywheel;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rubikstudio.library.model.LuckyItem;
import rubikstudio.library.model.WheelView;

public class MainActivity extends AppCompatActivity {

    // 上一次的總角度
    float preAngle = 0;

    ImageView cursorView;
    ObjectAnimator animCursor;
    ValueAnimator animCursorSlowDown;

    // 一回合 多少 秒
    private static final int SECOND_OF_ROUND = 10;
    // 幸運輪盤有幾格
    private static final int GRID_OF_LUCKY_WHEEL = 8;

    List<LuckyItem> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final WheelView wheelView = findViewById(R.id.luckyWheel);
        cursorView = findViewById(R.id.imageView);


        for ( int i = 0; i < GRID_OF_LUCKY_WHEEL; i++ ) {
            LuckyItem luckyItem = new LuckyItem();
            data.add(luckyItem);
        }

        /////////////////////

        wheelView.setData(data);
        wheelView.setRound(SECOND_OF_ROUND);
        Glide.with(this).load(R.drawable.wheel3).into(wheelView);
//        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);

        // Play Click listener
        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = getRandomIndex();
                wheelView.startLuckyWheelWithTargetIndex(index);

                // cursor動畫
                startCursorAnimation(index);
            }
        });


        wheelView.setLuckyRoundItemSelectedListener(new WheelView.LuckyRoundItemSelectedListener() {
            @Override
            public void LuckyRoundItemSelected(int index) {
                Toast.makeText(getApplicationContext(), String.valueOf(index), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getRandomIndex() {
        Random rand = new Random();
        return rand.nextInt(data.size() - 1);
    }

    private int getRandomRound() {
        Random rand = new Random();
        return rand.nextInt(10) + 15;
    }

    /**
     * @return
     */
    private float getAngleOfIndexTarget(int index) {
        return (360f / GRID_OF_LUCKY_WHEEL) * index;
    }

    private void startCursorAnimation(int targetIndex) {
        //小動畫
        // 200ms
        animCursor = ObjectAnimator.ofFloat(cursorView, "rotation", 0f, -30f, 0f);
        animCursor.setDuration(200);
        animCursor.setInterpolator(new AccelerateInterpolator());
        animCursor.setRepeatCount(0);
        animCursor.setRepeatMode(ValueAnimator.RESTART);

        // 設定cursor旋轉的中心點
        cursorView.setPivotX(0f);
        cursorView.setPivotY(cursorView.getHeight()/2);

        // 算角度，要轉幾圈（360度 * 圈數 - 目標角度）跟輪盤轉的角度一樣
        float targetAngle = 360f * SECOND_OF_ROUND - getAngleOfIndexTarget(targetIndex) ;

        // slow down
        animCursorSlowDown = ValueAnimator.ofFloat(0, targetAngle);
        // 10秒 1000=1秒
        animCursorSlowDown.setDuration(SECOND_OF_ROUND * 1000);
        animCursorSlowDown.setInterpolator(new DecelerateInterpolator());
        animCursorSlowDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                // 目前的角度 + 一開始的偏移角度22.5
                float currentAngle = (float) animation.getAnimatedValue() + ((360f / GRID_OF_LUCKY_WHEEL) / 2);

                // 這次轉的角度
                float  angleOfThisTurn = currentAngle - preAngle;
                // 是否執行動畫
                int isStartAnimation = (int) (angleOfThisTurn / (360f / GRID_OF_LUCKY_WHEEL));

                if ( isStartAnimation > 0 && !animCursor.isRunning() ) {
                    Log.e("animCursor", "start()");
                    preAngle += isStartAnimation * (360f / GRID_OF_LUCKY_WHEEL);
                    animCursor.start();
                }
            }
        });
        animCursorSlowDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                preAngle = 0;
            }
        });

        if ( !animCursorSlowDown.isStarted() ) {
            animCursorSlowDown.start();
        }

    }
}
