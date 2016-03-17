package com.connorlay.knitgrid.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.data.Stitch;
import com.connorlay.knitgrid.fragments.PatternGridFragment;

import butterknife.ButterKnife;

public class PatternGridViewActivity extends AppCompatActivity {

    private static final String TAG = "PatternGridViewActivity";
    public static final String STITCH_EXTRA = "STITCH_EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_grid_view);
        ButterKnife.bind(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_pattern_grid_view_content_frame_layout, new PatternGridFragment());
        transaction.addToBackStack(null);
        transaction.commit();

        if (isFirstLaunch()) {
            Stitch.generateDefaultStitches();
            Log.d(TAG, String.valueOf(Stitch.listAll(Stitch.class).size()));
            getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE).edit()
                   .putBoolean("initial_launch", false);
        }
    }

    private boolean isFirstLaunch() {
        return getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
                .getBoolean("initial_launch", true);
    }
}
