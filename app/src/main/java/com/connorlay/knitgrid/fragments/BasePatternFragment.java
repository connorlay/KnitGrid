package com.connorlay.knitgrid.fragments;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.models.StitchPatternRelation;
import com.connorlay.knitgrid.presenters.PatternPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

/**
 * Created by connorlay on 3/29/16.
 */
public abstract class BasePatternFragment extends Fragment {
    public interface CellSelectedListener {
        void onCellSelected(int row, int col);
    }

    public static final int PATTERN_GRID_PADDING = 16;
    public static final String ARG_PATTERN_PRESENTER = "BasePatternFragment.PatternPresenter";

    @Bind(R.id.activity_pattern_detail_grid_layout)
    GridLayout mGridLayout;

    @BindColor(R.color.cellDefault)
    int mCellDefaultColor;

    @BindColor(R.color.cellHighlight)
    int mCellHighlightColor;

    @BindColor(R.color.Red)
    int red;

    @BindColor(R.color.Yellow)
    int yellow;

    @BindColor(R.color.Blue)
    int blue;

    @BindColor(R.color.Purple)
    int purple;

    @BindColor(R.color.White)
    int white;


    protected PatternPresenter mPatternPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pattern_detail, container, false);
        ButterKnife.bind(this, rootView);

        mPatternPresenter = getArguments().getParcelable(ARG_PATTERN_PRESENTER);
        populateGridLayout();
        setViewPadding(mGridLayout, PATTERN_GRID_PADDING);

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
                ImageView cellImageView = new ImageView(getActivity(), null,
                        R.style.PatternGridLayoutCell);

                if (stitch == null) {
                    cellImageView.setImageResource(R.drawable.blank);
                } else {
                    cellImageView.setImageResource(stitch.getIconID());
                }

                if (stitch != null){
                    cellImageView.setBackgroundColor(stitch.getColorID());
                }

                mGridLayout.addView(cellImageView, cellSize, cellSize);
            }
        }
    }

    protected void bindCellListener(final CellSelectedListener listener, final CellSelectedListener listenerLongClick) {
        for (int i = 0; i < mPatternPresenter.getRows(); i++) {
            for (int j = 0; j < mPatternPresenter.getColumns(); j++) {
                final int row = i;
                final int col = j;

                ImageView cell = (ImageView) mGridLayout.getChildAt(row * mPatternPresenter.getColumns() + col);

                cell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onCellSelected(row, col);
                    }
                });
                cell.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        listenerLongClick.onCellSelected(row, col);
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        MulticolorFragment mcf = MulticolorFragment.newInstance(BasePatternFragment.this, row, col);
                        mcf.show(fm, "BasePatternFragment");

                        return false;
                    }
                });
            }
        }
    }

    // TODO: sometimes the horizontal scroll view is not full width when it should be. rounding
    // error?
    private int calculateCellSize(int columns) {
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context
                .WINDOW_SERVICE);
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

    protected void setGridBackgroundColor(int color) {
        for (int i = 0; i < mGridLayout.getChildCount(); i++) {
            mGridLayout.getChildAt(i).setBackgroundColor(color);
        }
    }

    protected void setGridBackgroundMultiColor(){
        List<StitchPatternRelation> list = mPatternPresenter.getPattern().getStitchRelations();
        for (StitchPatternRelation s: list){
            mGridLayout.getChildAt(s.getRow() * mPatternPresenter.getColumns() + s.getCol()).setBackgroundColor(s.getColorID());
            mPatternPresenter.getStitch(s.getRow(), s.getCol()).setColorID(s.getColorID());
        }
    }

}
