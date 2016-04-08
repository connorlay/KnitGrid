package com.connorlay.knitgrid.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.connorlay.knitgrid.presenters.PatternPresenter;

/**
 * Created by connorlay on 3/29/16.
 */
public class PatternHighlightFragment extends BasePatternFragment {
    public static final String HIGHLIGHT_ROW_KEY = "_highlight_row";
    public static final String HIGHLIGHT_COLUMN_KEY = "_highlight_column";

    public static PatternHighlightFragment newInstance(PatternPresenter patternPresenter) {
        PatternHighlightFragment fragment = new PatternHighlightFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PATTERN_PRESENTER, patternPresenter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        bindCellListener(new CellSelectedListener() {
            @Override
            public void onCellSelected(int row, int col) {
                highlightUpToCell(row, col);
                setHighlightPrefs(mPatternPresenter.getPatternId().toString(), row, col);
            }
        }, new CellSelectedListener() {
            @Override
            public void onCellSelected(int row, int col) {
                highlightUpToCell(row, col);
                setHighlightPrefs(mPatternPresenter.getPatternId().toString(), row, col);
            }
        });

        Long patternId = mPatternPresenter.getPatternId();
        if (patternId != null) {
            int[] highlightCoords = getHighlightPrefs(mPatternPresenter.getPatternId().toString());
            highlightUpToCell(highlightCoords[0], highlightCoords[1]);
        }
        return rootView;
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
        SharedPreferences prefs = getActivity().getSharedPreferences("KnitGrid", Context
                .MODE_PRIVATE);
        int row = prefs.getInt(patternId + HIGHLIGHT_ROW_KEY, mGridLayout.getRowCount());
        int column = prefs.getInt(patternId + HIGHLIGHT_COLUMN_KEY, mGridLayout.getColumnCount());
        return new int[]{row, column};
    }
}

