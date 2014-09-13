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

    public static String TAG = android.os.Build.MODEL + "GestureView";

    private CoreOCREngine coreOCR = new CoreOCREngine();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
                Character symbol = (char)value.intValue();
//                CoreNetworkEngine.postMessage(symbol);
                CoreNetworkEngine.BaseTask task = new CoreNetworkEngine.BaseTask();
                task.action = "post";
                task.symbol = symbol;
                task.execute("");
                Log.d(TAG, "RECOGNIZED --> " + (char)value.intValue());
				iv1.setImageBitmap(gridToBitmap(getRandomTemplate()));
				iv2.setImageBitmap(bitmap);
            }
        }
        return true;
    }
	
	private final ImageView iv1, iv2;

    public GestureView(Context context) {
        super(context);
		iv1 = new ImageView(context);
		addView(iv1);
		iv2 = new ImageView(context);
		addView(iv2);
    }

}
