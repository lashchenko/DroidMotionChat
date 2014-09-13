package com.dm;

import java.util.*;

import android.util.Log;
import android.widget.*;
import android.view.*;
import android.content.*;

import static com.dm.ThisApp.*;
import static com.dm.Grid.*;
import android.graphics.*;

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

	final ArrayList<Point> points = new ArrayList<Point>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // clean and prepare for collect data for new pattern
                points.clear();
            }
            case MotionEvent.ACTION_MOVE: {
                // collect data of new pattern
                points.add(new Point((int)x, (int)y));
                break;
            }
            case MotionEvent.ACTION_UP: {
                // recognize new pattern
				Bitmap bitmap = pointsToBitmap(points.toArray(new Point[points.size()]));
                Integer value = coreOCR.recognize(bitmapToGrid(bitmap));
                Log.d(TAG, "RECOGNIZED --> " + (char)value.intValue());
				iv.setImageBitmap(gridToBitmap(getRandomTemplate()));
            }
        }
        return true;
    }
	
	private final ImageView iv;

    public GestureView(Context context) {
        super(context);
		iv = new ImageView(context);
		addView(iv);
    }

}
