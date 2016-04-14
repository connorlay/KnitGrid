package com.connorlay.knitgrid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.adapters.PatternRecyclerViewAdapter;
import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.models.StitchPatternRelation;
import com.connorlay.knitgrid.networking.KnitGridAPI;
import com.connorlay.knitgrid.serializers.PatternSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PatternListActivity extends AppCompatActivity {

    public static final int REQUEST_CREATE = 1;
    public static final int CONTEXT_ACTION_DELETE = 100;
    public static final int CONTEXT_ACTION_EDIT = 101;
    public static final int CONTEXT_ACTION_UPLOAD = 102;
    public static final int REQUEST_EDIT = 200;
    private static final String CREATE_PATTERN_RESPONSE = "CREATE_PATTERN_RESPONSE";

    @Bind(R.id.row_pattern_recycylerview)
    RecyclerView mRecyclerView;

    PatternRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_list);
        ButterKnife.bind(this);

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
        } else if (item.getItemId() == CONTEXT_ACTION_UPLOAD) {
            Pattern selectedItem = adapter.getPattern(adapter.getSelectedPosition());
            if (selectedItem.getUuid() != null) {
                displaySnackbar(getString(R.string.activity_pattern_list_publish_failure));
            } else {
                publishPattern(selectedItem);
            }
        } else {
            return false;
        }
        return true;
    }

    private void displaySnackbar(String message) {
        Snackbar.make(this.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void publishPattern(final Pattern pattern) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Pattern.class, new PatternSerializer());
        Gson gson = gsonBuilder.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://knitgrid-api.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        KnitGridAPI api = retrofit.create(KnitGridAPI.class);

        api.createPattern(pattern).enqueue(new Callback<Pattern>() {
            @Override
            public void onResponse(Call<Pattern> call, Response<Pattern> response) {
                // TODO: write custom stitch pattern deserializer
                pattern.setUuid(response.body().getUuid());
                displaySnackbar(getString(R.string.activity_pattern_list_published_success));
                Log.d(CREATE_PATTERN_RESPONSE, response.toString());
            }

            @Override
            public void onFailure(Call<Pattern> call, Throwable t) {
                Log.d(CREATE_PATTERN_RESPONSE, t.getLocalizedMessage());
            }
        });
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
