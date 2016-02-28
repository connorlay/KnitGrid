package com.connorlay.knitgrid.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.connorlay.knitgrid.R;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by connorlay on 2/26/16.
 */
public class PatternGridFragment extends Fragment {

    public static final String TAG = PatternGridFragment.class.getSimpleName();

    public static final int PATTERN_GRID_ROWS = 30;
    public static final int PATTER_GRID_COLUMNS = 10;
    public static final int PATTERN_GRID_PADDING = 16;

    @Bind(R.id.activity_pattern_grid_view_pattern_grid_layout)
    GridLayout mGridLayout;

    @BindColor(android.R.color.white)
    int mCellBackgroundColor;

    @BindColor(R.color.colorPrimary)
    int mCellHighlightColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pattern_grid, container, false);
        ButterKnife.bind(this, rootView);

        populateGridLayout();
        setViewPadding(mGridLayout, PATTERN_GRID_PADDING);

        ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener());

        return rootView;
    }

    private void populateGridLayout() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(mCellHighlightColor);
            }
        };

        int cellSize = calculateCellSize(PATTER_GRID_COLUMNS);
        mGridLayout.setRowCount(PATTERN_GRID_ROWS);
        mGridLayout.setColumnCount(PATTER_GRID_COLUMNS);

        for (int i = 0; i < PATTERN_GRID_ROWS * PATTER_GRID_COLUMNS; i += 1) {
            ImageView cellImageView = new ImageView(getActivity(), null, R.style.PatternGridLayoutCell);
            cellImageView.setImageResource(R.drawable.knit);
            cellImageView.setOnClickListener(listener);
            cellImageView.setBackgroundColor(mCellBackgroundColor);
            mGridLayout.addView(cellImageView, cellSize, cellSize);
        }
    }

    // TODO: sometimes the horizontal scroll view is not full width when it should be. rounding error?
    private int calculateCellSize(int columns) {
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        Point point = new Point();
        display.getSize(point);

        float dp = PATTERN_GRID_PADDING / getResources().getDisplayMetrics().density;
        return (int) ((point.x - 2 * dp) / columns + 0.5f);
    }

    private int convertToPixels(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp / density);
    }

    private void setViewPadding(View view, float dp) {
        int padding = convertToPixels(dp);
        view.setPadding(padding, padding, padding, padding);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float scale = detector.getScaleFactor();
            Log.d(TAG, "Scale factor: " + scale);
            return true;
        }
    }
}
