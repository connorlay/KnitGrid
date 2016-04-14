package com.connorlay.knitgrid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.fragments.PatternDownloadDialogFragment;
import com.connorlay.knitgrid.models.Pattern;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Greg on 4/14/2016.
 */
public class PatternOnlineRecyclerViewAdapter extends RecyclerView.Adapter<PatternOnlineRecyclerViewAdapter.PatternOnlineViewHolder> {

    private List<Pattern> mPatterns;

    private int selectedPosition;

    public PatternOnlineRecyclerViewAdapter(List<Pattern> patterns, OnPatternRowClickListener listener) {
        mListener = listener;
        mPatterns = patterns;
    }

    public interface OnPatternRowClickListener {
        void onPatternRowClick(Pattern pattern);
    }

    private OnPatternRowClickListener mListener;

    @Override
    public PatternOnlineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.row_pattern, parent, false);
        return new PatternOnlineViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final PatternOnlineViewHolder holder, final int position) {
        Pattern pattern = mPatterns.get(position);
        holder.nameTextView.setText(pattern.getName());
        holder.dimensionTextView.setText(String.format("%s x %s", pattern.getRows(), pattern
                .getColumns()));

        holder.fullView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onPatternRowClick(mPatterns.get(holder.getAdapterPosition()));
            }
        });
        holder.fullView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectedPosition = position;
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPatterns.size();
    }


    static class PatternOnlineViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_pattern_name_textview)
        TextView nameTextView;

        @Bind(R.id.row_pattern_dimension_textview)
        TextView dimensionTextView;

        View fullView;

        public PatternOnlineViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            fullView = itemView;
            itemView.setOnCreateContextMenuListener(new PatternDownloadDialogFragment(){

            });
        }

    }
}
