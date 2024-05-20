package com.prout.traintracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private ArrayList<TubeLine> localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textView2;
        private final FrameLayout frameLayout;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textViewStationName);
            textView2 = (TextView) view.findViewById(R.id.textViewEta);
            frameLayout = (FrameLayout) view.findViewById(R.id.frameLayoutTubeLine);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getTextView2() {
            return textView2;
        }

        public FrameLayout getFrameLayout() {
            return frameLayout;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public StatusAdapter(ArrayList<TubeLine> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);

        return new ViewHolder(view);

    }

    /**
     * Binds data to viewholder
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Sets element to dataset value
        viewHolder.getTextView().setText(localDataSet.get(position).getName());
        viewHolder.getTextView2().setText(localDataSet.get(position).getStatus());

        // Colour codes the background of the frame layout based on the line status
        String status = localDataSet.get(position).getStatus();
        if (status.equals("Good Service")) {
            viewHolder.getFrameLayout().setBackgroundResource(R.color.green);
        } else if (status.equals("Part Closure") || status.equals("Minor Delays")
                || status.equals("Special Service") || status.equals("Part Suspended")) {
            viewHolder.getFrameLayout().setBackgroundResource(R.color.yellow);
        } else if (status.equals("Severe Delays") || status.equals("Planned Closure")) {
            viewHolder.getFrameLayout().setBackgroundResource(R.color.red);
        }
    }

    /**
     * Returns size of dataset.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

