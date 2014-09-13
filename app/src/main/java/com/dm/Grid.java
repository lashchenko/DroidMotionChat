package com.dm;

import android.graphics.*;

import static com.dm.ThisApp.*;

public class Grid {
	
	public static int[] getTemplate(char c) {
		Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(false);
		paint.setColor(0xff000000);
		paint.setTextSize(n);
		canvas.drawText("" + c, 0, (int) (n * .875), paint);
		return bitmapToGrid(bitmap);
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
	
	public static int[] bitmapToGrid(Bitmap bitmap) {
		int[] grid = new int[n * n];
		for (int y = 0; y < n; y++) {
			for (int x = 0; x < n; x++) {
				if (bitmap.getPixel(x, y) == 0xff000000)
					grid[y * n + x] = 1;
			}
		}
		return grid;
	}
	
	public static Bitmap pointsToBitmap(Point[] points) {
		Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAntiAlias(false);
		paint.setColor(0xff000000);
		paint.setStrokeWidth(4);
		for (int i = 1; i < points.length; i++) {
			canvas.drawLine(
				points[i - 1].x,
				points[i - 1].y,
				points[i].x,
				points[i].y,
				paint
			);
		}
		return bitmap;
	}
	
}
