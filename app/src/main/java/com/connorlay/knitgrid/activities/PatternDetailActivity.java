package com.connorlay.knitgrid.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.fragments.PatternDetailFragment;
import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.presenters.PatternPresenter;

import butterknife.ButterKnife;

public class PatternDetailActivity extends AppCompatActivity {

    private static final String TAG = "PatternDetailActivity";
    public static final String STITCH_EXTRA = "STITCH_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_detail_view);
        ButterKnife.bind(this);

        if (isFirstLaunch()) {
            generateDefaultData();
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit().putBoolean("initial_launch", false);
        }

        PatternPresenter patternPresenter = new PatternPresenter(Pattern.first(Pattern.class));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_pattern_detail_content_frame_layout, PatternDetailFragment.newInstance(patternPresenter));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean isFirstLaunch() {
        return getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
                .getBoolean("initial_launch", true);
    }

    private void generateDefaultData() {
        Stitch.generateDefaultStitches();

        Pattern pattern = new Pattern("A Pattern", 10, 10, true);
        pattern.save();

        for (int i = 0; i < 10; i += 1) {
            for (int j = 0; j < 10; j += 1) {
                pattern.setStitch(Stitch.first(Stitch.class), i, j);
            }
        }
    }
}
