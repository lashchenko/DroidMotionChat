package com.dm;

import java.util.*;

import android.widget.*;
import android.view.*;
import android.content.*;

import static com.dm.ThisApp.*;

public class GestureView extends FrameLayout {

	private int w, h;
	@Override protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.w = w; this.h = h;
	}

	private int[] grid = new int[n * n];

	@Override public boolean onTouchEvent(MotionEvent event) {
		final float x = event.getX();
		final float y = event.getY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				Arrays.fill(grid, 0);
			} case MotionEvent.ACTION_MOVE: {
				grid[
					(int) (x / w * n) +
					(int) (y / h * n) * n
				] = 1;
				break;
			} case MotionEvent.ACTION_UP: {
				
			}
		} return true;
	}
	
	public GestureView(Context context) {
		super(context);
	}
	
}
