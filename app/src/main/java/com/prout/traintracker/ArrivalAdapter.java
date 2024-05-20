package com.prout.traintracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * RecyclerView adapter for displaying Arrivals to a station.
 */
public class ArrivalAdapter extends RecyclerView.Adapter<ArrivalAdapter.ViewHolder> {

    private ArrayList<Arrival> localDataSet;

    /**
     * The ViewHolder class for displaying an individual Arrival
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textView2;
        private final FrameLayout frameLayout;

        /**
         * Constructor
         * @param view
         */
        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textViewStationName);
            textView2 = (TextView) view.findViewById(R.id.textViewEta);
            frameLayout = (FrameLayout) view.findViewById(R.id.frameLayoutTubeLine);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getTextView2(){
            return textView2;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet ArrayList containing the data to be displayed in the recycler
     */
    public ArrivalAdapter(ArrayList<Arrival> dataSet) {
        localDataSet = dataSet;
    }

    /**
     * Creates item views as needed by the Recycler
      */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_arrival, viewGroup, false);

        return new ViewHolder(view);
    }


    /**
     * Binds data to the viewholder
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Sets viewholder elements to the value of the dataset item
        viewHolder.getTextView().setText(localDataSet.get(position).getDestination());
        String eta = String.valueOf(localDataSet.get(position).getEta() / 60) + " Mins";
        viewHolder.getTextView2().setText(eta);
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     * @return number of items in recycler
     */
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}

