package com.connorlay.knitgrid.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.connorlay.knitgrid.R;
import com.connorlay.knitgrid.activities.PatternListActivity;
import com.connorlay.knitgrid.models.Pattern;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by connorlay on 3/21/16.
 */
public class PatternRecyclerViewAdapter extends RecyclerView.Adapter<PatternRecyclerViewAdapter
        .PatternViewHolder> {


    private int selectedPosition;

    public interface OnPatternRowClickListener {
        void onPatternRowClick(Pattern pattern);
    }

    private OnPatternRowClickListener mListener;
    private List<Pattern> mPatterns;

    public PatternRecyclerViewAdapter(List<Pattern> patterns, OnPatternRowClickListener listener) {
        mListener = listener;
        mPatterns = patterns;
    }

    @Override
    public PatternViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.row_pattern, parent, false);
        return new PatternViewHolder(row);
    }

    @Override
    public void onBindViewHolder(final PatternViewHolder holder, final int position) {
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

    static class PatternViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.row_pattern_name_textview)
        TextView nameTextView;

        @Bind(R.id.row_pattern_dimension_textview)
        TextView dimensionTextView;

        View fullView;

        public PatternViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            fullView = itemView;
            itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu
                        .ContextMenuInfo menuInfo) {
                    menu.setHeaderTitle("Menu");
                    menu.add(0, PatternListActivity.CONTEXT_ACTION_DELETE, 0, "Delete");
                    menu.add(0, PatternListActivity.CONTEXT_ACTION_EDIT, 0, "Edit");
                    menu.add(0, PatternListActivity.CONTEXT_ACTION_UPLOAD, 0, "Publish");
                }

            });
        }

    }

    public Pattern getPattern(int position) {
        return mPatterns.get(position);
    }

    public void removeItem(int position) {
        mPatterns.remove(position);
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
