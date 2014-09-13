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
        blackFatPaint.setTextSize(n);
    }

    private static Paint blackPaint;
    static {
        blackPaint = new Paint();
        blackPaint.setAntiAlias(false);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setTextSize(4);
    }

    public static int[] getTemplate(char c) {
        Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText("" + c, 0, (int) (n * .875), blackFatPaint);
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
        Bitmap bitmap = Bitmap.createBitmap(n, n, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 1; i < points.length; i++) {
            canvas.drawLine(
                    points[i - 1].x,
                    points[i - 1].y,
                    points[i].x,
                    points[i].y,
                    blackPaint);
        }
        return bitmap;
    }

}
