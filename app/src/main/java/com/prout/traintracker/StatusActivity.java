package com.prout.traintracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

public class StatusActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Line Statuses");
        toolbar.setSubtitle("");

        // Loads tube line statuses in the background
        StatusLoader loader = new StatusLoader();
        loader.execute();
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

    /**
     * Opens the official TFL website in a browser of the user's choice
     */
    public void openTflSite(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        String URL = "https://tfl.gov.uk/modes/tube/";
        browserIntent.setData(Uri.parse(URL));
        startActivity(browserIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        StatusLoader loader = new StatusLoader();
        loader.execute();
    }

    /**
     * Updates the RecylerView with the status strings
     */
    protected void updateRecycler(ArrayList<TubeLine> tubeLineArray){
        // Ensure no data views are set to invisible and recycler is set to visible
        // Sets error message as invisible
        TextView textView = findViewById(R.id.textViewNoData);
        textView.setVisibility(View.INVISIBLE);

        // Sets retry button as invisible
        Button button = findViewById(R.id.buttonRetry);
        button.setVisibility(View.INVISIBLE);

        // Makes recycler view visible
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewTubeLines);
        recyclerView.setVisibility(View.VISIBLE);

        // set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StatusAdapter adapter = new StatusAdapter(tubeLineArray);
        recyclerView.setAdapter(adapter);
    }

    protected void showNoDataView(){
        System.out.println("No data");
        // Sets error message as visible
        TextView textView = findViewById(R.id.textViewNoData);
        textView.setVisibility(View.VISIBLE);

        // Sets retry button as visible
        Button button = findViewById(R.id.buttonRetry);
        button.setVisibility(View.VISIBLE);

        // Makes recycler view invisible
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewTubeLines);
        recyclerView.setVisibility(View.INVISIBLE);

    }

    public void startArrivalsActivity(View view){
        Intent intent = new Intent(this, ArrivalsActivity.class);
        startActivity(intent);
    }

    public void startSettingsActivity(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void retryData(View view){
        // Loads tube line statuses in the background
        StatusLoader loader = new StatusLoader();
        loader.execute();
    }

    private void createNotificationChannel() {
        CharSequence name = "default";
        String description = "description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    /**
     * Loads Tube line Statuses.
     *
     * Calls updateRecycler method when the strings have been retrieved.
     */
    private class StatusLoader extends AsyncTask<String, Void, ArrayList<TubeLine>> {


        @Override
        protected ArrayList<TubeLine> doInBackground(String... strings) {
            ArrayList<TubeLine> tubeLineArray = new ArrayList<>();
            try {
                URL url = new URL("https://api.tfl.gov.uk/Line/Mode/tube/Status");

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";
                String data = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                JSONArray array = new JSONArray(data);


                for (int i = 0; i< array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    tubeLineArray.add(new TubeLine(jsonObject.getString("name"),jsonObject.getJSONArray("lineStatuses").getJSONObject(0).getString("statusSeverityDescription")));
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tubeLineArray;
        }

        @Override
        protected void onPostExecute(ArrayList<TubeLine> s) {
            super.onPostExecute(s);
            if (s.size()!=0) {
                updateRecycler(s);
            }
            else {
                showNoDataView();
            }
        }
    }

}

