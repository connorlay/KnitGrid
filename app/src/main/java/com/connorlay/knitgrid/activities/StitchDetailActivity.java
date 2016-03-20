package com.connorlay.knitgrid.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.models.Stitch;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StitchDetailActivity extends AppCompatActivity {

    @Bind(R.id.stitch_title)
    TextView stitchTitle;

    @Bind(R.id.stitch_icon)
    ImageView icon;

    @Bind(R.id.detail_text)
    TextView detailsText;

    @Bind(R.id.instructions_text)
    TextView instructionsText;

    @Bind(R.id.expand_icon)
    ImageView expandIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stitch_detail);
        ButterKnife.bind(this);

        instructionsText.setVisibility(View.GONE);

        Stitch stitch = (Stitch) getIntent().getSerializableExtra(PatternDetailActivity.STITCH_EXTRA);
        instructionsText.setText(stitch.getInstructions());
        stitchTitle.setText(stitch.getAbbreviation());
        detailsText.setText(stitch.getDetails());
        icon.setImageResource(stitch.getIconID());

    }

    @OnClick({R.id.instructions_expandable_title, R.id.expand_icon})
    public void toggleInstructions() {
        if (instructionsText.isShown()) {
            instructionsText.setVisibility(View.GONE);
            expandIcon.setImageResource(R.drawable.expand_arrow);
        } else {
            instructionsText.setVisibility(View.VISIBLE);
            expandIcon.setImageResource(R.drawable.collapse_arrow);
        }
    }
}
