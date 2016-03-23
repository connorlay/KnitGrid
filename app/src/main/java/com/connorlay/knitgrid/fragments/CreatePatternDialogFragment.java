package com.connorlay.knitgrid.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.activities.PatternListActivity;
import com.connorlay.knitgrid.models.Pattern;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by Magisus on 3/22/2016.
 */
public class CreatePatternDialogFragment extends DialogFragment {

    public interface PatternCreationListener {
        void onPatternCreated(Pattern pattern);
    }

    PatternCreationListener listener;

    @Bind(R.id.create_pattern_name_edit_text)
    EditText nameInput;

    @Bind(R.id.rows_edit_text)
    EditText rowsInput;

    @Bind(R.id.columns_edit_text)
    EditText columnsInput;

    @Bind(R.id.checkbox_show_even_rows)
    CheckBox showEvenRowsCheckbox;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        listener = (PatternCreationListener) getActivity();

        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout
                .dialog_fragment_create_pattern, null);

        ButterKnife.bind(this, rootView);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(rootView)
                .setTitle(R.string.create_pattern_dialog_title)
                .setPositiveButton(R.string.create_pattern_dialog_positive_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = nameInput.getText().toString();
                                String rowCountString = rowsInput.getText().toString();
                                String colCountString = columnsInput.getText().toString();

                                // TODO these still allow the dialog to close. Is there a way
                                // to keep it open?
                                if (name.isEmpty()) {
                                    Toast.makeText(getActivity(), "Please enter a name",
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }
                                if (rowCountString.isEmpty() || colCountString.isEmpty()) {
                                    Toast.makeText(getActivity(), "Please enter valid dimensions.",
                                            Toast.LENGTH_LONG).show();
                                    return;

                                }

                                int rows = Integer.parseInt(rowsInput.getText().toString());
                                int columns = Integer.parseInt(columnsInput.getText()
                                        .toString());
                                boolean evenRowsShown = showEvenRowsCheckbox.isChecked();

                                Pattern pattern = new Pattern(name, rows, columns,
                                        evenRowsShown);
                                listener.onPatternCreated(pattern);
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                CreatePatternDialogFragment.this.dismiss();
                                Intent intent = new Intent(getActivity(),
                                        PatternListActivity.class);
                                startActivity(intent);
                            }
                        }).create();

        return dialog;
    }
}
