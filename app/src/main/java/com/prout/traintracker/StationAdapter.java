package com.prout.traintracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * RecyclerView adapter for displaying available Stations
 */
public class StationAdapter extends RecyclerView.Adapter<StationAdapter.ViewHolder> {

    private JSONArray localDataSet;

    /**
     * The ViewHolder class for displaying an individual Arrival
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button openMapsButton;
        private final Button openArrivalsButton;

        /**
         * Constructor
         *
         * @param view
         */
        public ViewHolder(View view) {
            super(view);
            openMapsButton = (Button) view.findViewById(R.id.buttonOpenMaps);
            openArrivalsButton = (Button) view.findViewById(R.id.buttonOpenArrivals);
        }

        public Button getOpenMapsButton() {
            return openMapsButton;
        }

        public Button getOpenArrivalsButton() {
            return openArrivalsButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public StationAdapter() {
        try {
            String naptans = "[{\"naptanID\":\"940GZZLUBST\",\"commonName\":\"Baker Street Underground Station\"},{\"naptanID\":\"940GZZLUCHX\",\"commonName\":\"Charing Cross Underground Station\"},{\"naptanID\":\"940GZZLUEAC\",\"commonName\":\"Elephant & Castle Underground Station\"},{\"naptanID\":\"940GZZLUEMB\",\"commonName\":\"Embankment Underground Station\"},{\"naptanID\":\"940GZZLUERB\",\"commonName\":\"Edgware Road (Bakerloo) Underground Station\"},{\"naptanID\":\"940GZZLUHAW\",\"commonName\":\"Harrow & Wealdstone Underground Station\"},{\"naptanID\":\"940GZZLUHSN\",\"commonName\":\"Harlesden Underground Station\"},{\"naptanID\":\"940GZZLUKEN\",\"commonName\":\"Kenton Underground Station\"},{\"naptanID\":\"940GZZLUKPK\",\"commonName\":\"Kilburn Park Underground Station\"},{\"naptanID\":\"940GZZLUKSL\",\"commonName\":\"Kensal Green Underground Station\"},{\"naptanID\":\"940GZZLULBN\",\"commonName\":\"Lambeth North Underground Station\"},{\"naptanID\":\"940GZZLUMVL\",\"commonName\":\"Maida Vale Underground Station\"},{\"naptanID\":\"940GZZLUMYB\",\"commonName\":\"Marylebone Underground Station\"},{\"naptanID\":\"940GZZLUNWY\",\"commonName\":\"North Wembley Underground Station\"},{\"naptanID\":\"940GZZLUOXC\",\"commonName\":\"Oxford Circus Underground Station\"},{\"naptanID\":\"940GZZLUPCC\",\"commonName\":\"Piccadilly Circus Underground Station\"},{\"naptanID\":\"940GZZLUQPS\",\"commonName\":\"Queen's Park Underground Station\"},{\"naptanID\":\"940GZZLURGP\",\"commonName\":\"Regent's Park Underground Station\"},{\"naptanID\":\"940GZZLUSGP\",\"commonName\":\"Stonebridge Park Underground Station\"},{\"naptanID\":\"940GZZLUSKT\",\"commonName\":\"South Kenton Underground Station\"},{\"naptanID\":\"940GZZLUWJN\",\"commonName\":\"Willesden Junction Underground Station\"},{\"naptanID\":\"940GZZLUWKA\",\"commonName\":\"Warwick Avenue Underground Station\"},{\"naptanID\":\"940GZZLUWLO\",\"commonName\":\"Waterloo Underground Station\"},{\"naptanID\":\"940GZZLUWYC\",\"commonName\":\"Wembley Central Underground Station\"}]";
            this.localDataSet = new JSONArray(naptans);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates item views as needed by the Recycler
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item_station, viewGroup, false);

        return new ViewHolder(view);
    }

    /**
     * Binds viewholder to data.
     *
     * @param viewHolder to bind to
     * @param position   within the recycler
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        // Sets viewholder elements to the value of the dataset item
        try {
            viewHolder.getOpenMapsButton().setText(localDataSet.getJSONObject(position).getString("commonName"));
            viewHolder.getOpenArrivalsButton().setText(localDataSet.getJSONObject(position).getString("naptanID"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the size of dataset.
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return localDataSet.length();
    }
}

