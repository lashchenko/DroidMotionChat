package com.dm;

import android.graphics.*;

import static com.dm.ThisApp.*;

public class Letters {
	
	public static int[] getTemplate(char c) {
		int[] grid = new int[n * n];
		Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(false);
		paint.setColor(0xff000000);
		paint.setTextSize(n);
		canvas.drawText("" + c, 0, (int) (n * .875), paint);
		for (int y = 0; y < n; y++) {
			for (int x = 0; x < n; x++) {
				if (bitmap.getPixel(x, y) == 0xff000000)
					grid[y * n + x] = 1;
			}
		}
		return grid;
	}
	
}
