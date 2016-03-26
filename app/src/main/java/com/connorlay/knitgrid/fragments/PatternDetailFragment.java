package com.connorlay.knitgrid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.presenters.PatternPresenter;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by connorlay on 2/26/16.
 */
public class PatternDetailFragment extends Fragment {
    public static final int PATTERN_GRID_PADDING = 16;
    public static final String ARG_PATTERN_PRESENTER = "PatternDetailFragment.PatternPresenter";
    public static final String HIGHLIGHT_ROW_KEY = "_highlight_row";
    public static final String HIGHLIGHT_COLUMN_KEY = "_highlight_column";

    @Bind(R.id.activity_pattern_detail_grid_layout)
    GridLayout mGridLayout;

    @BindColor(R.color.cellDefault)
    int mCellDefaultColor;

    @BindColor(R.color.cellHighlight)
    int mCellHighlightColor;

    private PatternPresenter mPatternPresenter;

    public static PatternDetailFragment newInstance(PatternPresenter patternPresenter) {
        PatternDetailFragment fragment = new PatternDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PATTERN_PRESENTER, patternPresenter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pattern_detail, container, false);
        ButterKnife.bind(this, rootView);

        mPatternPresenter = getArguments().getParcelable(ARG_PATTERN_PRESENTER);

        populateGridLayout();
        setViewPadding(mGridLayout, PATTERN_GRID_PADDING);

        int[] highlightCoords = getHighlightPrefs(mPatternPresenter.getPatternId().toString());
        highlightUpToCell(highlightCoords[0], highlightCoords[1]);

        return rootView;
    }

    private void populateGridLayout() {
        int cellSize = calculateCellSize(mPatternPresenter.getColumns());
        mGridLayout.setRowCount(mPatternPresenter.getRows());
        mGridLayout.setColumnCount(mPatternPresenter.getColumns());

        for (int i = 0; i < mPatternPresenter.getRows(); i += 1) {
            for (int j = 0; j < mPatternPresenter.getColumns(); j += 1) {
                final int row = i;
                final int column = j;

                Stitch stitch = mPatternPresenter.getStitch(row, column);
                ImageView cellImageView = new ImageView(getActivity(), null, R.style.PatternGridLayoutCell);

                cellImageView.setImageResource(stitch.getIconID());
                cellImageView.setBackgroundColor(mCellDefaultColor);
                cellImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        highlightUpToCell(row, column);
                        setHighlightPrefs(mPatternPresenter.getPatternId().toString(), row, column);
                    }
                });

                mGridLayout.addView(cellImageView, cellSize, cellSize);
            }
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

    private void highlightUpToCell(int row, int column) {
        int defaultUpToIndex = row * mPatternPresenter.getColumns();
        int pivotIndex = defaultUpToIndex + column;
        int highlightAfterIndex = (row + 1) * mPatternPresenter.getColumns();

        for (int i = 0; i < mPatternPresenter.getRows() * mPatternPresenter.getColumns(); i += 1) {
            ImageView cell = (ImageView) mGridLayout.getChildAt(i);

            if (mPatternPresenter.showsEvenRows() && row % 2 == 0) {
                if (i < defaultUpToIndex || i > pivotIndex && i < highlightAfterIndex) {
                    cell.setBackgroundColor(mCellDefaultColor);
                } else {
                    cell.setBackgroundColor(mCellHighlightColor);
                }
            } else {
                if (i < pivotIndex) {
                    cell.setBackgroundColor(mCellDefaultColor);
                } else {
                    cell.setBackgroundColor(mCellHighlightColor);
                }
            }
        }
    }

    private void setHighlightPrefs(String patternId, int row, int column) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("KnitGrid", Context.MODE_PRIVATE).edit();
        editor.putInt(patternId + HIGHLIGHT_ROW_KEY, row);
        editor.putInt(patternId + HIGHLIGHT_COLUMN_KEY, column);
        editor.commit();
    }

    private int[] getHighlightPrefs(String patternId) {
        SharedPreferences prefs = getActivity().getSharedPreferences("KnitGrid", Context.MODE_PRIVATE);
        int row = prefs.getInt(patternId + HIGHLIGHT_ROW_KEY, 0);
        int column = prefs.getInt(patternId + HIGHLIGHT_COLUMN_KEY, 0);
        return new int[]{row, column};
    }
}
