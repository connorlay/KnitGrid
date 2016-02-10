package com.connorlay.knitgrid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Magisus on 2/12/2015.
 */
public class PatternView extends View {

    public static final int ORANGE = Color.rgb(255, 140, 0);

    private int rows;
    private int columns;

    private Paint paintBackground;
    private Paint paintGrid;
    private Paint paintHighlight;

    private Point selectedSquare;

    public PatternView(int rows, int columns, Context context, AttributeSet attrs) {
        super(context, attrs);

        this.rows = rows;
        this.columns = columns;

        selectedSquare = null;

        paintBackground = new Paint();
        paintBackground.setColor(Color.DKGRAY);
        paintBackground.setStyle(Paint.Style.FILL);

        paintGrid = new Paint();
        paintGrid.setColor(Color.LTGRAY);
        paintGrid.setStyle(Paint.Style.STROKE);
        paintGrid.setStrokeWidth(3);

        paintHighlight = new Paint();
        paintHighlight.setColor(ORANGE);
        paintHighlight.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        selectedSquare = getSquare(event.getX(), event.getY());
        invalidate();
        return super.onTouchEvent(event);
    }

    // TODO: this doesn't work for all grid sizes due to rounding issues.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBackground);

        int interval = (int) Math.round(getWidth() / ((double) columns));
        Log.d(MainActivity.class.getSimpleName(), "Interval: " + interval);
        drawGridLines(canvas, interval);
        highlightSelectedSquare(canvas, interval);
    }

    private void drawGridLines(Canvas canvas, int interval) {
        for (int i = 0; i <= columns; i++) {
            canvas.drawLine(i * interval, 0, i * interval, getHeight(), paintGrid);
            canvas.drawLine(0, i * interval, getWidth(), i * interval, paintGrid);
        }
    }

    private void highlightSelectedSquare(Canvas canvas, int interval) {
        if (selectedSquare != null) {
            canvas.drawRect(selectedSquare.x * interval + 2, selectedSquare.y * interval + 2,
                    (selectedSquare.x + 1) * interval - 1, (selectedSquare.y + 1) * interval - 1,
                    paintHighlight);
        }
    }

    private Point getSquare(float xIn, float yIn) {
        int interval = getWidth() / columns;
        int x = (int) (xIn / interval);
        int y = (int) (yIn / interval);
        return new Point(x, y);
    }

    public Point getSelectedSquare() {
        return selectedSquare;
    }

    public void reset() {

    }

    public void clearSelectedSquare() {
        selectedSquare = null;
        invalidate();
    }
}
