package com.connorlay.knitgrid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.models.Stitch;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Greg on 3/17/2016.
 */
public class PatternLegendRecyclerViewAdapter extends RecyclerView.Adapter<PatternLegendRecyclerViewAdapter.StitchRecycleViewHolder> {


    private final OnStitchRowClickListener mListener;

    private List<Stitch> mStitches;

    public interface OnStitchRowClickListener {
        void onStitchRowClick(Stitch stitch);
    }

    public PatternLegendRecyclerViewAdapter(List<Stitch> stitches, OnStitchRowClickListener listener) {
        mListener = listener;
        mStitches = stitches;
    }

    @Override
    public StitchRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.legend_row, parent, false);
        return new StitchRecycleViewHolder(row);
    }

    @Override
    public void onBindViewHolder(StitchRecycleViewHolder holder, int position) {

        final Stitch stitch = mStitches.get(position);
        holder.textView.setText(stitch.getAbbreviation());

        ImageView view = holder.imageView;
        view.setImageResource(stitch.getIconID());

        holder.fullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener == null){
                    return;
                }
                mListener.onStitchRowClick(stitch);
            }
        });
        
    }


    @Override
    public int getItemCount() {
        return mStitches.size();
    }

    class StitchRecycleViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.fragment_legend_image)
        public ImageView imageView;

        @Bind(R.id.fragment_legend_name)
        public TextView textView;

        View fullView;

        public StitchRecycleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            fullView = itemView;
        }

    }
}
