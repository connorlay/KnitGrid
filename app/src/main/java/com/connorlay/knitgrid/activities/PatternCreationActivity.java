package com.connorlay.knitgrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.fragments.BasePatternFragment;
import com.connorlay.knitgrid.fragments.CreatePatternDialogFragment;
import com.connorlay.knitgrid.fragments.PatternCreateFragment;
import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.presenters.PatternPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PatternCreationActivity extends AppCompatActivity implements
        CreatePatternDialogFragment.PatternCreationListener, BasePatternFragment.CellSelectedListener {

    public static final String PATTERN_CREATE = "PATTERN_CREATE";
    public static final String KEY_EDIT_ITEM = "EDIT_ITEM";

    @Bind(R.id.stitch_button_bar)
    GridLayout buttonBar;

    private int selectedRow;
    private int selectedColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_creation);
        ButterKnife.bind(this);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(KEY_EDIT_ITEM)) {
            Pattern pattern = (Pattern) getIntent().getParcelableExtra(KEY_EDIT_ITEM);
            onPatternCreated(pattern);
        } else {
            CreatePatternDialogFragment dialog = new CreatePatternDialogFragment();
            dialog.show(getSupportFragmentManager(), "create_dialog");
        }

        buildButtonBar();
    }

    private void buildButtonBar() {
        List<Stitch> stitches = Stitch.listAll(Stitch.class);
        for (final Stitch stitch : stitches) {
            Button button = new Button(this);
            button.setBackground(getDrawable(stitch.getIconID()));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatternCreateFragment frag = (PatternCreateFragment)
                            getSupportFragmentManager().findFragmentById(R.id.pattern_detail_frame);
                    if (frag == null) {
                        return;
                    }
                    frag.setStitch(selectedRow, selectedColumn, stitch);
                }
            });
            // TODO size should be screen width / 4 ish
            buttonBar.addView(button, 70, 70);
        }
        buttonBar.setVisibility(View.GONE);
    }

    @OnClick(R.id.save_pattern_button)
    public void savePattern() {
        PatternCreateFragment frag = (PatternCreateFragment)
                getSupportFragmentManager().findFragmentById(R.id.pattern_detail_frame);
        if (frag == null) {
            return;
        }

        frag.savePattern();
        Intent intent = new Intent(this, PatternListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.cancel_pattern_creation_button)
    public void cancelPattern() {
        Intent intent = new Intent(this, PatternListActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPatternCreated(Pattern pattern) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment fragment = PatternCreateFragment.newInstance(
                new PatternPresenter(pattern));
        transaction.replace(R.id.pattern_detail_frame, fragment);
        transaction.commit();
    }

    private void showButtonBar() {
        buttonBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCellSelected(int row, int col) {
        selectedRow = row;
        selectedColumn = col;
        showButtonBar();
    }
}
