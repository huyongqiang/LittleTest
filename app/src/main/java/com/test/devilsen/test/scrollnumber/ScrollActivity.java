package com.test.devilsen.test.scrollnumber;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.test.devilsen.test.R;

/**
 * author : dongSen
 * date : 2016-07-22 17:23
 * desc :
 */
public class ScrollActivity extends AppCompatActivity {

    private ScrollNumber scrollNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_number);

        scrollNumber = (ScrollNumber) findViewById(R.id.scroll_number);

//        scrollNumber.setNumber(0, 8, 0);
//        scrollNumber.setTargetNumber(4);
        scrollNumber.setTarget();

        for (int i = 0; i < 100; i++) {
            System.out.println(i);
        }
    }
}