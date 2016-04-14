package com.connorlay.knitgrid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.connorlay.knitgrid.models.Pattern;

/**
 * Created by Greg on 4/14/2016.
 */
public class PatternDownloadDialogFragment extends DialogFragment {

    public interface PatternDownloadListener {
        void onPatternDownload(Pattern pattern);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Download this pattern?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        return dialog;
    }
}
