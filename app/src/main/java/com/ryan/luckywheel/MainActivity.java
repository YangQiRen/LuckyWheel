package com.ryan.luckywheel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rubikstudio.library.LuckyWheelView;
import rubikstudio.library.model.LuckyItem;

public class MainActivity extends AppCompatActivity {

    // 一回合 5 秒
    private static final int SECOND_OF_ROUND = 10;
    // 幸運輪盤有幾格
    private static final int GRID_OF_LUCKY_WHEEL = 8;

    List<LuckyItem> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final LuckyWheelView luckyWheelView = (LuckyWheelView) findViewById(R.id.luckyWheel);

        for ( int i = 0; i < GRID_OF_LUCKY_WHEEL; i++ ) {
            LuckyItem luckyItem = new LuckyItem();
            data.add(luckyItem);
        }

        /////////////////////

        luckyWheelView.setData(data);
        luckyWheelView.setRound(SECOND_OF_ROUND);
        luckyWheelView.setPieBackgroundImage(R.drawable.wheel3);
//        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);

        /*luckyWheelView.setLuckyWheelBackgrouldColor(0xff0000ff);
        luckyWheelView.setLuckyWheelTextColor(0xffcc0000);
        luckyWheelView.setLuckyWheelCenterImage(getResources().getDrawable(R.drawable.icon));
        luckyWheelView.setLuckyWheelCursorImage(R.drawable.ic_cursor);*/


        findViewById(R.id.play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = getRandomIndex();
                luckyWheelView.startLuckyWheelWithTargetIndex(index);
            }
        });

        luckyWheelView.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
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
}
