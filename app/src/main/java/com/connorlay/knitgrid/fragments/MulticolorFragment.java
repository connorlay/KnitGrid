package com.connorlay.knitgrid.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.models.StitchPatternRelation;

import java.util.List;

/**
 * Created by Greg on 4/5/2016.
 */
public class MulticolorFragment extends DialogFragment {

    public static MulticolorFragment newInstance(BasePatternFragment bpf,int row, int col){
        MulticolorFragment fragment = new MulticolorFragment();
        fragment.setBasePatternFragment(bpf);
        fragment.setRow(row);
        fragment.setCol(col);
        return fragment;
    }

    public void setBasePatternFragment(BasePatternFragment bpf) {
        basePatternFragment = bpf;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    private BasePatternFragment basePatternFragment;
    private int row;
    private int col;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List<StitchPatternRelation> list = basePatternFragment.mPatternPresenter.getPattern().getStitchRelations();
        StitchPatternRelation stitchPatternRelation = null;
        for (StitchPatternRelation s: list){
            if (s.getCol() == col && s.getRow() == row){
                stitchPatternRelation = s;
            }
        }
        final int index = list.indexOf(stitchPatternRelation);
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_multicolor, null);
        AlertDialog builder = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setSingleChoiceItems(R.array.colors, -1, new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            list.get(index).setColorID(R.color.Red);
                        } else if (which == 1) {
                            list.get(index).setColorID(R.color.Yellow);
                        } else if (which == 2) {
                            list.get(index).setColorID(R.color.Blue);
                        } else if (which == 3) {
                            list.get(index).setColorID(R.color.Purple);
                        } else if (which == 4) {
                            list.get(index).setColorID(R.color.White);
                        }
                    }
                })
                .create();
        return builder;
    }

}
