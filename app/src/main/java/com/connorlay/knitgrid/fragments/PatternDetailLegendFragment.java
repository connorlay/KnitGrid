package com.connorlay.knitgrid.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.activities.PatternDetailActivity;
import com.connorlay.knitgrid.activities.StitchDetailActivity;
import com.connorlay.knitgrid.adapters.ListDivider;
import com.connorlay.knitgrid.adapters.PatternLegendRecyclerViewAdapter;
import com.connorlay.knitgrid.models.Pattern;
import com.connorlay.knitgrid.models.Stitch;
import com.connorlay.knitgrid.models.StitchPatternRelation;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Greg on 3/15/2016.
 */
public class PatternDetailLegendFragment extends Fragment implements PatternLegendRecyclerViewAdapter.OnStitchRowClickListener{

    public static final String ARG_PATTERN = "PatternDetailLegendFragment.Pattern";

    private Pattern pattern;

    @Bind(R.id.fragment_legend_recyclerview)
    public RecyclerView recyclerView;

    public static PatternDetailLegendFragment newInstance(Pattern pattern){
        PatternDetailLegendFragment fragment = new PatternDetailLegendFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PatternDetailLegendFragment.ARG_PATTERN, pattern);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_legend, container, false);
        ButterKnife.bind(this, rootView);


        pattern = getArguments().getParcelable(ARG_PATTERN);
        List<StitchPatternRelation> stitchPatternRelations= pattern.getStitchRelations();
        List<Stitch> stitches = new ArrayList<>();
        for (StitchPatternRelation s: stitchPatternRelations){
            Stitch stitch = s.getStitch();
            if (!stitches.contains(stitch)){
                stitches.add(stitch);
            }
        }
        recyclerView.setAdapter(new PatternLegendRecyclerViewAdapter(stitches, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new ListDivider(getActivity()));

        return rootView;
    }

    @Override
    public void onStitchRowClick(Stitch stitch) {
        Intent intent = new Intent(getActivity(), StitchDetailActivity.class);
        intent.putExtra(PatternDetailActivity.STITCH_EXTRA, (Parcelable) stitch);
        startActivity(intent);
    }

}
