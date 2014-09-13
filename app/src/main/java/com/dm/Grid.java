package com.dm;

import android.graphics.*;

import java.util.Arrays;

import static com.dm.ThisApp.*;
import java.util.*;

public class Grid {
	
	private static final int strokeWidth = 2; 

    private static Paint blackPaint;
    static {
        blackPaint = new Paint();
        blackPaint.setAntiAlias(false);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setTextSize(n*1.375f);
		blackPaint.setTextAlign(Paint.Align.CENTER);
		blackPaint.setStrokeWidth(strokeWidth);
    }

    public static int[] getTemplate(char c) {
        Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText("" + c, n/2, n, blackPaint);
        return bitmapToGrid(bitmap);
    }
	
	private static Random random = new Random();
	public static int[] getRandomTemplate() {
		return getTemplate((char) ('A' + random.nextInt('Z' - 'A' + 1)));
	}

    public static Bitmap gridToBitmap(int[] grid) {
        Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                bitmap.setPixel(x,y, grid[y * n + x] == 1 ? Color.RED : 0);
            }
        }
        return bitmap;
    }

    public static int[] bitmapToGrid(Bitmap bitmap) {
        int[] grid = new int[n * n];
        Arrays.fill(grid, 0);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (bitmap.getPixel(x, y) != 0)
                    grid[y * n + x] = 1;
            }
        }
        return grid;
    }

    public static Bitmap pointsToBitmap(Point[] points) {
        Point p = points[0];
        Point maxPoint = new Point(p.x, p.y);
        Point minPoint = new Point(p.x, p.y);

        for (Point point : points) {
            if (point.x < minPoint.x) minPoint.x = point.x;
            else if (point.x > maxPoint.x) maxPoint.x = point.x;

            if (point.y < minPoint.y) minPoint.y = point.y;
            else if (point.y > maxPoint.y) maxPoint.y = point.y;
        }

        Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
		float w = maxPoint.x - minPoint.x;
        float h = maxPoint.y - minPoint.y;
		int nWithoutPadding = n - strokeWidth * 2;
        for (int i = 1; i < points.length; i++) {
            canvas.drawLine(
            	(points[i - 1].x - minPoint.x) / w * nWithoutPadding + strokeWidth,
                (points[i - 1].y - minPoint.y) / h * nWithoutPadding + strokeWidth,
               	(points[i].x - minPoint.x) / w * nWithoutPadding + strokeWidth,
                (points[i].y - minPoint.y) / h * nWithoutPadding + strokeWidth,
                blackPaint
			);
			/*canvas.drawCircle(
				(points[i].x - minPoint.x) / w * nWithoutPadding + strokeWidth,
				(points[i].y - minPoint.y) / h * nWithoutPadding + strokeWidth,
				strokeWidth,
				blackPaint
			);*/
        }
        return bitmap;
		
    }

}
