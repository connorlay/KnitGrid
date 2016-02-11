package com.connorlay.knitgrid;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final int PATTERN_GRID_ROWS = 30;
    public static final int PATTER_GRID_COLUMNS = 10;

    public static final int PATTERN_GRID_PADDING = 16;

    @Bind(R.id.activity_main_pattern_grid_layout)
    GridLayout gridLayout;

    @BindColor(R.color.colorAccent)
    int cellHighLight;

    @BindColor(android.R.color.white)
    int cellBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setViewPadding(gridLayout, PATTERN_GRID_PADDING);
        populateGridLayout();
    }

    private void populateGridLayout() {
        gridLayout.setRowCount(PATTERN_GRID_ROWS);
        gridLayout.setColumnCount(PATTER_GRID_COLUMNS);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(cellHighLight);
            }
        };

        int cellSize = calculateCellSize(PATTERN_GRID_ROWS, PATTER_GRID_COLUMNS);

        for (int i = 0; i < PATTERN_GRID_ROWS * PATTER_GRID_COLUMNS; i++) {
            ImageView cellImageView = new ImageView(this, null, R.style.PatternGridLayoutCell);
            cellImageView.setImageResource(R.drawable.knit);
            cellImageView.setOnClickListener(listener);
            cellImageView.setBackgroundColor(cellBackground);
            gridLayout.addView(cellImageView, cellSize, cellSize);
        }
    }

    // TODO: sometimes the horizontal scroll view is not full width when it should be. rounding error?
    private int calculateCellSize(int rows, int cols) {
        Display display = getWindowManager().getDefaultDisplay();
        float density = getResources().getDisplayMetrics().density;
        float dp = PATTERN_GRID_PADDING / density;
        Point point = new Point();
        display.getSize(point);
        return (int) ((point.x - 2 * dp) / cols + 0.5f);
    }

    private int convertToPixels(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp / density);
    }

    private void setViewPadding(View view, float dp) {
        int padding = convertToPixels(dp);
        view.setPadding(padding, padding, padding, padding);
    }
}
