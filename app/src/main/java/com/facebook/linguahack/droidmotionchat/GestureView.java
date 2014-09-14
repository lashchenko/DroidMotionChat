package com.facebook.linguahack.droidmotionchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GestureView extends FrameLayout {

    public static String TAG = android.os.Build.MODEL + " GestureView";

    private String userName;
    private String serverUrl;

    public GestureView(String userName, String serverUrl, Context context) {
        super(context);
        this.userName = userName;
        this.serverUrl = serverUrl;

        iv1 = new ImageView(context);
        addView(iv1);

        iv2 = new ImageView(context);
        addView(iv2);

        setAlpha(0.5f);
    }

    private final ImageView iv1;
    private final ImageView iv2;

    private CoreOCREngine coreOCR = new CoreOCREngine();

    final ArrayList<Point> points = new ArrayList<Point>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // clean and prepare for collect data for new pattern
                points.clear();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                // collect data of new pattern
                points.add(new Point((int) x, (int) y));
                break;
            }
            case MotionEvent.ACTION_UP: {
                // recognize new pattern
                Bitmap bitmap = pointsToBitmap(points.toArray(new Point[points.size()]));
                Character symbol = coreOCR.recognize(bitmapToGrid(bitmap));

                if (symbol == CoreNetworkEngine.SYMBOL_NEW)
                    new CoreNetworkEngine.NetworkTask(userName, serverUrl, CoreNetworkEngine.ACTION_NEW_MESSAGE, symbol).execute("");
                else
                    new CoreNetworkEngine.NetworkTask(userName, serverUrl, CoreNetworkEngine.ACTION_UPDATE_MESSAGE, symbol).execute("");
                Log.d(TAG, "RECOGNIZED --> " + symbol);


                iv1.setImageBitmap(gridToBitmap(getRandomTemplate()));
                iv2.setImageBitmap(bitmap);
                break;
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private static final int strokeWidth = 2;

    private static Paint paint;

    //    private static Paint blackPaint;
    static {
//        blackPaint = new Paint();
//        blackPaint.setAntiAlias(false);
//        blackPaint.setColor(Color.BLACK);
//        blackPaint.setTextSize(n*1.375f);
//        blackPaint.setTextAlign(Paint.Align.CENTER);
//        blackPaint.setStrokeWidth(strokeWidth);

        paint = new Paint();
        paint.setAntiAlias(false);
        paint.setDither(false);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth * 2);
        paint.setTextSize(MainActivity.N * 1.375f);
//        paint.setTextSize(MainActivity.M);

//        int size = 0;
//        do {
//            paint.setTextSize(++size);
//        } while(paint.measureText("A") < MainActivity.M);
//        Log.d("", "SISISISISISIS" + size);
//        paint.setTextSize(size);

        paint.setTextAlign(Paint.Align.CENTER);
    }

    public static int[] getTemplate(char c) {
        Bitmap bitmap = Bitmap.createBitmap(MainActivity.N, MainActivity.M, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText("" + c, MainActivity.N / 2, MainActivity.M, paint); // M/2 remove ?
        return bitmapToGrid(bitmap);
    }

    private static Random random = new Random();

    public static int[] getRandomTemplate() {
        return getTemplate((char) ('A' + random.nextInt('Z' - 'A' + 1)));
    }

    public static Bitmap gridToBitmap(int[] grid) {
        Bitmap bitmap = Bitmap.createBitmap(MainActivity.N, MainActivity.M, Bitmap.Config.ARGB_8888);
        for (int y = 0; y < MainActivity.M; y++) {
            for (int x = 0; x < MainActivity.N; x++) {
                bitmap.setPixel(x, y, grid[y * MainActivity.M + x] == 1 ? Color.RED : 0);
            }
        }
        return bitmap;
    }

    public static int[] bitmapToGrid(Bitmap bitmap) {
        int[] grid = new int[MainActivity.N * MainActivity.M];
        Arrays.fill(grid, 0);
        for (int y = 0; y < MainActivity.M; y++) {
            for (int x = 0; x < MainActivity.N; x++) {
                if (bitmap.getPixel(x, y) != 0)
                    grid[y * MainActivity.M + x] = 1;
            }
        }
        return grid;
    }

    public static Bitmap pointsToBitmap(Point[] points) {

        Bitmap bitmap = Bitmap.createBitmap(MainActivity.N, MainActivity.M, Bitmap.Config.ARGB_8888);
        if (points.length == 0) {
            return  bitmap;
        }

        Point p = points[0];
        Point maxPoint = new Point(p.x, p.y);
        Point minPoint = new Point(p.x, p.y);

        for (Point point : points) {
            if (point.x < minPoint.x) minPoint.x = point.x;
            else if (point.x > maxPoint.x) maxPoint.x = point.x;

            if (point.y < minPoint.y) minPoint.y = point.y;
            else if (point.y > maxPoint.y) maxPoint.y = point.y;
        }

        Canvas canvas = new Canvas(bitmap);
        float w = maxPoint.x - minPoint.x;
        float h = maxPoint.y - minPoint.y;
        int nWithoutPadding = MainActivity.N - strokeWidth * 2;
        for (int i = 1; i < points.length; i++) {
            canvas.drawLine(
                    (points[i - 1].x - minPoint.x) / w * nWithoutPadding + strokeWidth,
                    (points[i - 1].y - minPoint.y) / h * nWithoutPadding + strokeWidth,
                    (points[i].x - minPoint.x) / w * nWithoutPadding + strokeWidth,
                    (points[i].y - minPoint.y) / h * nWithoutPadding + strokeWidth,
                    paint
            );
        }
        return bitmap;

    }
}
