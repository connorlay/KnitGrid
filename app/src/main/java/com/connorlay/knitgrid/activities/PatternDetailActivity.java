package com.connorlay.knitgrid.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.fragments.PatternDetailFragment;
import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.presenters.PatternPresenter;

import butterknife.ButterKnife;

public class PatternDetailActivity extends AppCompatActivity {
    public static final String STITCH_EXTRA = "STITCH_EXTRA";
    public static final String ARG_PATTERN = "PatternDetailActivity.ArgPattern";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_detail_view);
        ButterKnife.bind(this);

        Pattern pattern = getIntent().getParcelableExtra(ARG_PATTERN);
        PatternPresenter patternPresenter = new PatternPresenter(pattern);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.activity_pattern_detail_content_frame_layout, PatternDetailFragment.newInstance(patternPresenter));
        transaction.commit();
    }
}
