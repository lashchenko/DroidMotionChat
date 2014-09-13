package com.dm;

import android.graphics.*;

import static com.dm.ThisApp.*;

public class Letters {

    public static String TAG = "Letters";
	
	public static int[] getTemplate(char c) {
		int[] grid = new int[n * n];
		Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(false);
		paint.setColor(Color.BLACK);
		paint.setTextSize(n);
		canvas.drawText("" + c, 0, (int) (n * .875), paint);
		for (int y = 0; y < n; y++) {
			for (int x = 0; x < n; x++) {
				if (bitmap.getPixel(x, y) == Color.BLACK)
					grid[y * n + x] = 1;
			}
		}
		return grid;
	}
	
	public static Bitmap gridToBitmap(int[] grid) {
		Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(false);
		paint.setColor(0xff000000);
		paint.setTextSize(n);
		for (int y = 0; y < n; y++) {
			for (int x = 0; x < n; x++) {
				if (grid[y * n + x] == 1)
					bitmap.setPixel(x, y, 0xff000000);
				else bitmap.setPixel(x, y, 0xffffffff);
			}
		}
		return bitmap;
	}
	
}
