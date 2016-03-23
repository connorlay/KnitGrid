package com.connorlay.knitgrid.fragments;

import android.content.Context;
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

    @Bind(R.id.activity_pattern_detail_grid_layout)
    GridLayout mGridLayout;

    @BindColor(android.R.color.white)
    int mCellBackgroundColor;

    @BindColor(R.color.colorPrimary)
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

        return rootView;
    }

    private void populateGridLayout() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundColor(mCellHighlightColor);
            }
        };

        int cellSize = calculateCellSize(mPatternPresenter.getColumns());
        mGridLayout.setRowCount(mPatternPresenter.getRows());
        mGridLayout.setColumnCount(mPatternPresenter.getColumns());

        for (int i = 0; i < mPatternPresenter.getRows(); i += 1) {
            for (int j = 0; j < mPatternPresenter.getColumns(); j += 1) {
                Stitch stitch = mPatternPresenter.getStitch(i, j);
                ImageView cellImageView = new ImageView(getActivity(), null, R.style.PatternGridLayoutCell);
                cellImageView.setImageResource(stitch.getIconID());
                cellImageView.setOnClickListener(listener);
                cellImageView.setBackgroundColor(mCellBackgroundColor);
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
}
