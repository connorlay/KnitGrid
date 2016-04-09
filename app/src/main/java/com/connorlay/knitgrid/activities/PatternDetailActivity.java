package com.connorlay.knitgrid.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.fragments.PatternDetailLegendFragment;
import com.connorlay.knitgrid.fragments.PatternHighlightFragment;
import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.presenters.PatternPresenter;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class PatternDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String STITCH_EXTRA = "ARG_STITCH_EXTRA";
    public static final String ARG_PATTERN = "PatternDetailActivity.ArgPattern";
    private PatternPresenter patternPresenter;
    private Pattern pattern;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_detail_view);
        ButterKnife.bind(this);

        pattern = getIntent().getParcelableExtra(ARG_PATTERN);
        patternPresenter = new PatternPresenter(pattern);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_pattern_detail_content_frame_layout, PatternHighlightFragment.newInstance(patternPresenter));
        transaction.commit();
    }


    @OnClick({R.id.activity_pattern_detail_legend_button, R.id.activity_pattern_detail_pattern_button})
    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()){
            case R.id.activity_pattern_detail_legend_button:
                transaction.replace(R.id.activity_pattern_detail_content_frame_layout, PatternDetailLegendFragment.newInstance(pattern));
                break;
            case R.id.activity_pattern_detail_pattern_button:
                transaction.replace(R.id.activity_pattern_detail_content_frame_layout, PatternHighlightFragment.newInstance(patternPresenter));
                break;
        }
        transaction.commit();
    }
}
