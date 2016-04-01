package com.connorlay.knitgrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.AdapterView;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.adapters.PatternRecyclerViewAdapter;
import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.models.StitchPatternRelation;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PatternListActivity extends AppCompatActivity {

    public static final int REQUEST_CREATE = 1;
    public static final int CONTEXT_ACTION_DELETE = 100;
    public static final int CONTEXT_ACTION_EDIT = 101;
    public static final int REQUEST_EDIT = 200;

    @Bind(R.id.row_pattern_recycylerview)
    RecyclerView mRecyclerView;

    PatternRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_list);
        ButterKnife.bind(this);

        if (isFirstLaunch()) {
            generateDefaultData();
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit().putBoolean
                    ("initial_launch", false).commit();
        }

        List<Pattern> patterns = Pattern.listAll(Pattern.class);

        adapter = new PatternRecyclerViewAdapter(patterns, new PatternRecyclerViewAdapter
                .OnPatternRowClickListener() {
            @Override
            public void onPatternRowClick(Pattern pattern) {
                Intent intent = new Intent(PatternListActivity.this, PatternDetailActivity.class);
                intent.putExtra(PatternDetailActivity.ARG_PATTERN, pattern);
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        registerForContextMenu(mRecyclerView);
    }

    private boolean isFirstLaunch() {
        return getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).getBoolean
                ("initial_launch", true);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CONTEXT_ACTION_DELETE) {
            Pattern selectedItem = adapter.getPattern(adapter.getSelectedPosition());
            StitchPatternRelation.deleteAll(StitchPatternRelation.class, "pattern = ?",
                    String.valueOf(selectedItem.getId()));
            selectedItem.delete();
            adapter.removeItem(adapter.getSelectedPosition());
        } else if (item.getItemId() == CONTEXT_ACTION_EDIT) {
            Pattern selectedItem = adapter.getPattern(adapter.getSelectedPosition());
            Intent intent = new Intent(this, PatternCreationActivity.class);
            intent.putExtra(PatternCreationActivity.KEY_EDIT_ITEM, selectedItem);
            startActivity(intent);
        } else {
            return false;
        }
        return true;
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
