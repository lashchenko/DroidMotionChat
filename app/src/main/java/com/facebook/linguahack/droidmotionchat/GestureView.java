package com.facebook.linguahack.droidmotionchat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
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

//        sampleView = new ImageView(context);
//        addView(sampleView);

        patternView = new ImageView(context);
        addView(patternView);

        setAlpha(0.2f);
    }

//    private final ImageView sampleView;
    private final ImageView patternView;

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

                if (symbol == CoreOCREngine.SYMBOL_NOT_RECOGNIZED) {
                    return false;
                } else if (symbol == CoreNetworkEngine.SYMBOL_NEW) {
                    new CoreNetworkEngine.NetworkTask(userName, serverUrl, CoreNetworkEngine.ACTION_NEW_MESSAGE, symbol).execute("");
                } else {
                    new CoreNetworkEngine.NetworkTask(userName, serverUrl, CoreNetworkEngine.ACTION_UPDATE_MESSAGE, symbol).execute("");
                }
                Log.d(TAG, "RECOGNIZED --> " + symbol);

//                sampleView.setImageBitmap(gridToBitmap(getRandomTemplate()));
                patternView.setImageBitmap(bitmap);
                break;
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private static final int strokeWidth = 3;

    private static Paint paint;
    static {
        Typeface tf = Typeface.create("Verdana",Typeface.NORMAL);
//        Typeface tf = Typeface.create("Symbol",Typeface.NORMAL);

        paint = new Paint();
        paint.setAntiAlias(false);
        //paint.setDither(false);
//        paint.setColor(Color.BLACK);
        paint.setColor(Color.RED);
        //paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth * 2);
        paint.setTypeface(tf);
        paint.setTextSize(MainActivity.N * 1.4375f);
        paint.setTextAlign(Paint.Align.CENTER);
    }

    private static Paint paint2;
    static {
        Typeface tf = Typeface.create("Symbol", Typeface.NORMAL);

        paint2 = new Paint();
        paint2.setAntiAlias(false);
        //paint.setDither(false);
//        paint.setColor(Color.BLACK);
        paint2.setColor(Color.RED);
        //paint.setStyle(Paint.Style.FILL_AND_STROKE);
        //paint.setStrokeJoin(Paint.Join.ROUND);
        paint2.setStrokeCap(Paint.Cap.ROUND);
        paint2.setStrokeWidth(strokeWidth * 2);
        paint2.setTypeface(tf);
        paint2.setTextSize(MainActivity.N * 1.4375f);
        paint2.setTextAlign(Paint.Align.CENTER);
    }

    public static int[] getTemplate2(char c) {
        return getTemplate(c, paint2);
    }

    public static int[] getTemplate(char c) {
        return getTemplate(c, paint);
    }

    public static int[] getTemplate(char c, Paint paint) {

        Bitmap bitmap = Bitmap.createBitmap(MainActivity.N, MainActivity.M, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawText("" + c, MainActivity.N / 2, MainActivity.M, paint);
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
                if (bitmap.getPixel(x, y) != 0) // CHECK OUR CONTOUR
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
		int offsetX = 0; int offsetY = 0;
		float k = 0;
		if (h > w) {
			offsetX = (int) ((h - w) / 2);
			k = h;
		} else {
			offsetY = (int) ((w - h) / 2);
			k = w;
		}
        int nWithoutPadding = MainActivity.N - strokeWidth * 2;
//        int sw = paint.getStrokeWidth();
        for (int i = 1; i < points.length; i++) {
            canvas.drawLine(
                    (points[i - 1].x - minPoint.x + offsetX) / k * nWithoutPadding + strokeWidth,
                    (points[i - 1].y - minPoint.y + offsetY) / k * nWithoutPadding + strokeWidth,
                    (points[i].x - minPoint.x + offsetX) / k * nWithoutPadding + strokeWidth,
                    (points[i].y - minPoint.y + offsetY) / k * nWithoutPadding + strokeWidth,
                    paint
            );
        }
        return bitmap;

    }
}
