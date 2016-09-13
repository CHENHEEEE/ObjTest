package com.example.he.Scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 不滚动的ListView
 * Created by HE on 2015/12/30.
 */
public class NoScrollListview extends ListView {
    public NoScrollListview(Context context ,AttributeSet attrs) {
        super(context,attrs);
    }
    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
