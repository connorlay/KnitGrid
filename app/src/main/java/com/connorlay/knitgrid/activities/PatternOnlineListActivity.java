package com.connorlay.knitgrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.adapters.PatternOnlineRecyclerViewAdapter;
import com.connorlay.knitgrid.models.Pattern;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Greg on 4/14/2016.
 */
public class PatternOnlineListActivity extends AppCompatActivity{


    public static final int CONTEXT_ACTION_DOWNLOAD = 102;

    @Bind(R.id.row_pattern_recycylerview)
    RecyclerView mRecyclerView;

    PatternOnlineRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_list);
        ButterKnife.bind(this);

        List<Pattern> patterns = Pattern.listAll(Pattern.class);

        adapter = new PatternOnlineRecyclerViewAdapter(patterns, new PatternOnlineRecyclerViewAdapter
                .OnPatternRowClickListener() {
            @Override
            public void onPatternRowClick(Pattern pattern) {
                Intent intent = new Intent(PatternOnlineListActivity.this, PatternDetailActivity.class);
                intent.putExtra(PatternDetailActivity.ARG_PATTERN, pattern);
                startActivity(intent);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);

        registerForContextMenu(mRecyclerView);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == CONTEXT_ACTION_DOWNLOAD){

        }

        return super.onContextItemSelected(item);
    }
}
