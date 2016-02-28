package com.connorlay.knitgrid.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.fragments.PatternGridFragment;

import butterknife.ButterKnife;

public class PatternGridViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern_grid_view);
        ButterKnife.bind(this);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_pattern_grid_view_content_frame_layout, new PatternGridFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
