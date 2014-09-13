package com.dm;

import android.graphics.*;

import java.util.Arrays;

import static com.dm.ThisApp.*;

public class Grid {

    private static Paint blackFatPaint;
    static {
        blackFatPaint = new Paint();
        blackFatPaint.setAntiAlias(false);
        blackFatPaint.setColor(Color.BLACK);
		blackFatPaint.setStrokeWidth(4);
    }

    private static Paint blackTextPaint;
    static {
        blackTextPaint = new Paint();
        blackTextPaint.setAntiAlias(false);
        blackTextPaint.setColor(Color.BLACK);
        blackTextPaint.setTextSize(4);
    }

    public static int[] getTemplate(char c) {
        Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText("" + c, 0, (int) (n * .875), blackTextPaint);
        return bitmapToGrid(bitmap);
    }

    public static Bitmap gridToBitmap(int[] grid) {
        Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                bitmap.setPixel(x,y, grid[y * n + x] == 1 ? Color.BLACK : Color.WHITE);
            }
        }
        return bitmap;
    }

    public static int[] bitmapToGrid(Bitmap bitmap) {
        int[] grid = new int[n * n];
        Arrays.fill(grid, 0);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (bitmap.getPixel(x, y) != Color.WHITE)
                    grid[y * n + x] = 1;
            }
        }
        return grid;
    }

    public static Bitmap pointsToBitmap(Point[] points) {
		
		float xmax = 0;
		float ymax = 0;
		float xmin = 0;
		float ymin = 0;
		for (int i = 0; i < points.length; i++) {
			Point point = points[i];
			if (point.x < xmin || xmin == 0) xmin = point.x;
			else if (point.x > xmax) xmax = point.x;
			if (point.y < ymin || ymin == 0) ymin = point.y;
			else if (point.y > ymax) ymax = point.y;
		}

        Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
		float w = xmax - xmin; float h = ymax - ymin;
        for (int i = 1; i < points.length; i++) {
            /*canvas.drawLine(
            	(points[i - 1].x - xmin) / w * n,
                (points[i - 1].y - ymin) / h * n,
               	(points[i].x - xmin) / w * n,
                (points[i].y - ymin) / w * n,
                blackFatPaint
			);*/
			canvas.drawLine(0, 0, n, n, blackFatPaint);
        }
        return bitmap;
		
    }

}
