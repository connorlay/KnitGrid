package com.connorlay.knitgrid.fragments;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.presenters.PatternPresenter;

/**
 * Created by connorlay on 3/30/16.
 */
public class PatternCreateFragment extends BasePatternFragment {
    public static PatternCreateFragment newInstance(PatternPresenter patternPresenter) {
        PatternCreateFragment fragment = new PatternCreateFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PATTERN_PRESENTER, patternPresenter);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        final CellSelectedListener listener = (CellSelectedListener) getActivity();

        bindCellListener(new CellSelectedListener() {
            @Override
            public void onCellSelected(int row, int col) {
                setGridBackgroundColor(mCellDefaultColor);
                setGridBackgroundMultiColor();
                listener.onCellSelected(row, col);
                mGridLayout.getChildAt(row * mPatternPresenter.getColumns() + col).setBackgroundColor(mCellHighlightColor);
            }
        }, new CellSelectedListener() {
            @Override
            public void onCellSelected(int row, int col) {
                if (mPatternPresenter.getStitch(row, col) == null) {
                    return;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                MulticolorDialogFragment mcf = MulticolorDialogFragment.newInstance(PatternCreateFragment.this, row, col);
                mcf.show(fm, "BasePatternFragment");
                setGridBackgroundColor(mCellDefaultColor);
                setGridBackgroundMultiColor();
                listener.onCellSelected(row, col);
                mGridLayout.getChildAt(row * mPatternPresenter.getColumns() + col)
                        .setBackgroundColor(mCellHighlightColor);
            }
        });

        return rootView;
    }

    public void setStitch(int row, int column, Stitch stitch) {
        Stitch oldStitch = mPatternPresenter.getStitch(row, column);
        if (oldStitch != null) {
            stitch.setColorID(oldStitch.getColorID());
        } else {
            stitch.setColorID(mCellDefaultColor);
        }
        mPatternPresenter.setStitch(row, column, stitch);
        ImageView cell = (ImageView) mGridLayout.getChildAt(row * mGridLayout.getColumnCount() +
                column);
        cell.setImageResource(stitch.getIconID());
        cell.setBackgroundColor(stitch.getColorID());
    }


    public void savePattern() {
        mPatternPresenter.savePattern();
    }
}
