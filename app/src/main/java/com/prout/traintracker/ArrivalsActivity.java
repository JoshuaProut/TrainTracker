package com.prout.traintracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

public class ArrivalsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrivals);

        // Loads the station data
        updateRecyclerStations(null);

        // Sets up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                startSettingsActivity(null);
                return true;
            case R.id.action_tfl_site:
                openTflSite();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void startSettingsActivity(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * Opens the official TFL website in a browser of the user's choice
     */
    public void openTflSite(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        String URL = "https://tfl.gov.uk/modes/tube/";
        browserIntent.setData(Uri.parse(URL));
        startActivity(browserIntent);
    }

    /**
     * Sets the recycler to display arrivals to a station
     *
     * @param arrivals arraylist of arrival objects to be displayed
     */
    protected void updateRecyclerArrivals(ArrayList<Arrival> arrivals) {
        // Makes alert textview and retry button invisible
        TextView textViewNoData = findViewById(R.id.textViewNoDataArrivals);
        Button buttonRetry = findViewById(R.id.buttonRetryArrivals);
        textViewNoData.setVisibility(View.INVISIBLE);
        buttonRetry.setVisibility(View.INVISIBLE);

        // Make back button visible
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setVisibility(View.VISIBLE);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewArrivals);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrivalAdapter adapter = new ArrivalAdapter(arrivals);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Sets the recycler to show available stations
     *
     * @param view
     */
    public void updateRecyclerStations(View view) {
        // Makes alert textview and retry button invisible
        TextView textViewNoData = findViewById(R.id.textViewNoDataArrivals);
        Button buttonRetry = findViewById(R.id.buttonRetryArrivals);
        textViewNoData.setVisibility(View.INVISIBLE);
        buttonRetry.setVisibility(View.INVISIBLE);

        // Make back button invisible
        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setVisibility(View.INVISIBLE);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewArrivals);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StationAdapter adapter = new StationAdapter();
        recyclerView.setAdapter(adapter);
    }

    /**
     * If data cannot be loaded, displays an error message and retry button
     */
    public void noData() {
        // Makes alert textview and retry button visible
        TextView textViewNoData = findViewById(R.id.textViewNoDataArrivals);
        Button buttonRetry = findViewById(R.id.buttonRetryArrivals);
        textViewNoData.setVisibility(View.VISIBLE);
        buttonRetry.setVisibility(View.VISIBLE);

        // Makes recycler invisible
        RecyclerView recyclerView = findViewById(R.id.recyclerViewArrivals);
        recyclerView.setVisibility(View.INVISIBLE);
    }

    /**
     * Opens line status activity
     *
     * @param view View that called the method
     */
    public void startStatusActivity(View view){
        Intent intent = new Intent(this, StatusActivity.class);
        startActivity(intent);
    }

    /**
     * button onClick method for the retry button, tries to load data again after a data loading failure.
     *
     * @param view View that called the method.
     */
    public void retryData(View view) {
        //Loads arrival data
        ArrivalLoader loader = new ArrivalLoader();
        loader.execute();
    }

    /**
     * button onClick method to open a mapping app to display selected station.
     * <p>
     * Sends an implicit ACTION_VIEW intent to mapping apps, with a search parameter of the station
     * name.
     *
     * @param view Button that called the method
     */
    public void openMapsIntent(View view) {
        // Collects station name from pressed button passed to method
        Button caller = (Button) view;
        String searchParameter = (String) caller.getText();

        // Generates implicit intent for opening the map app
        Uri gmmIntentUri = Uri.parse("geo:51.524511,-0.1279624?z=11.68&q=" + searchParameter);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    /**
     * Retrieves the Naptan (unique TFL identifier) from the pressed button and calls the arrivals
     * loader.
     *
     * @param view Button that called the method
     */
    public void openArrivals(View view) {
        // Gets naptan ID from button text
        Button caller = (Button) view;
        String naptan = (String) caller.getText();

        // Calls arrivals loader
        ArrivalLoader loader = new ArrivalLoader();
        loader.execute(naptan);
    }

    /**
     * Loads Arrivals for a given station.
     * <p>
     * Calls updateRecycler method when the strings have been retrieved.
     */
    private class ArrivalLoader extends AsyncTask<String, Void, ArrayList<Arrival>> {

        /**
         * Loads arrivals for station in background.
         *
         * @param string String array, the first element must be a valid Naptan ID string
         * @return ArrayList of Arrival objects for the station
         */
        @Override
        protected ArrayList<Arrival> doInBackground(String... string) {
            ArrayList<Arrival> arrivals = new ArrayList<>();
            try {
                // Opens connection to TFL API
                URL url = new URL("https://api.tfl.gov.uk/Line/bakerloo,central,circle,district,jubilee,metropolitan,northern,piccadilly,victoria/Arrivals/" + string[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                // Reads data from response
                String line = "";
                StringBuilder data;
                data = new StringBuilder();
                while (line != null) {
                    line = bufferedReader.readLine();
                    data.append(line);
                }

                // Converts JSONArray into ArrayList of Arrival objects
                JSONArray array = new JSONArray(data.toString());
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonArrival = (JSONObject) array.get(i);
                    arrivals.add(new Arrival(jsonArrival.getString("destinationName"), jsonArrival.getInt("timeToStation")));
                }

                // Sorts arrivals array so that the next arriving trains are shown first
                arrivals.sort(Comparator.comparing(Arrival::getEta));

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return arrivals;
        }

        /**
         * Executes once data has been fully loaded
         *
         * @param arrivals ArrayList of Arrival objects
         */
        @Override
        protected void onPostExecute(ArrayList<Arrival> arrivals) {
            super.onPostExecute(arrivals);

            // If data has been successfully retrieved, calls method to update RecyclerView with the new data,
            // if data could not be retrieved then the noData method is called.
            if (arrivals.size() != 0) {
                updateRecyclerArrivals(arrivals);
            } else {
                noData();
            }
        }
    }
}