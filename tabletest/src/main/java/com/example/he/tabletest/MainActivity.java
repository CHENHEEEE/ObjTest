package com.example.he.tabletest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements ScrollViewListener{

    private HVscroll hVscroll_1 = null;
    private HVscroll hVscroll_2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hVscroll_1 = (HVscroll)findViewById(R.id.HVscroll_1);
        hVscroll_1.setScrollViewListener(this);
        hVscroll_2 = (HVscroll)findViewById(R.id.HVscroll_2);
        hVscroll_2.setScrollViewListener(this);
    }

    @Override
    public void onScrollChanged(HVscroll hVscroll, int x, int y, int oldx, int oldy) {
        if(hVscroll == hVscroll_2){
            hVscroll_1.scrollTo(x,y);
        }
        if(hVscroll == hVscroll_1){
            y = hVscroll_2.getScrollY();
            hVscroll_2.scrollTo(x,y);
        }
    }
}
