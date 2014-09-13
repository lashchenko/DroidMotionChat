package com.dm;

import java.util.*;

import android.util.Log;
import android.widget.*;
import android.view.*;
import android.content.*;

import static com.dm.ThisApp.*;

public class GestureView extends FrameLayout {

    public static String TAG = "GestureView";

    private int w, h;
    private CoreOCREngine coreOCR = new CoreOCREngine();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
    }

    private int[] grid = new int[n * n];

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // clean and prepare for collect data for new pattern
                Arrays.fill(grid, 0);
            }
            case MotionEvent.ACTION_MOVE: {
                // collect data of new pattern
                int index = (int)((x / w * n) + (y / h * n) * n);
                grid[index] = 1;
                break;
            }
            case MotionEvent.ACTION_UP: {
                // recognize new pattern
                Integer value = coreOCR.recognize(grid);
                Log.d(TAG, "RECOGNIZED --> " + (char)value.intValue());
            }
            default: {
                // do nothing
                break;
            }
        }
        return true;
    }

    public GestureView(Context context) {
        super(context);
    }

}
