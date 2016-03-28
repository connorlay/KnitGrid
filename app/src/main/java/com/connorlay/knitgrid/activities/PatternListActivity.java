package com.connorlay.knitgrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.adapters.PatternRecyclerViewAdapter;
import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.Stitch;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PatternListActivity extends AppCompatActivity {

    public static final int REQUEST_CREATE = 1;

    @Bind(R.id.row_pattern_recycylerview)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_list);
        ButterKnife.bind(this);

        if (isFirstLaunch()) {
            generateDefaultData();
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit().putBoolean("initial_launch", false).commit();
        }

        List<Pattern> patterns = Pattern.listAll(Pattern.class);

        PatternRecyclerViewAdapter adapter = new PatternRecyclerViewAdapter(patterns, new PatternRecyclerViewAdapter.OnPatternRowClickListener() {
            @Override
            public void onPatternRowClick(Pattern pattern) {
                Intent intent = new Intent(PatternListActivity.this, PatternDetailActivity.class);
                intent.putExtra(PatternDetailActivity.ARG_PATTERN, pattern);
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    private boolean isFirstLaunch() {
        return getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).getBoolean("initial_launch", true);
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

    @OnClick(R.id.activity_pattern_list_fab)
    public void goToPatternCreation() {
        Intent intent = new Intent(this, PatternCreationActivity.class);
        startActivityForResult(intent, REQUEST_CREATE);
    }
}
