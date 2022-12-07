package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author kenzieevan
 *
 * Class that implements UI interface in activity_winning Layout file.
 */
public class WinningActivity extends AppCompatActivity {
    // Path Length and Text instance
    int pathLength;
    TextView textPathLength;

    int shortestPath;
    TextView textShortestPath;

    // Energy Consumed by Robot and Text instance
    float energyConsumed;
    TextView textEnergyConsumed;

    private static final String TAG = "WinningActivity";

    // Media Player for Music
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);

        // Get int moves from intent
        Intent intent = getIntent();

        pathLength = intent.getExtras().getInt("pathLength");
        textPathLength = findViewById(R.id.textPathLength);

        textPathLength.setText("Path Length: " + pathLength);

        // Adjust Shortest Path TextView
        shortestPath = intent.getExtras().getInt("shortestPath");
        textShortestPath = findViewById(R.id.textShortestPath);

        textShortestPath.setText("Shortest Path Length: " + shortestPath);

        // Adjust Energy Consumed
        textEnergyConsumed = findViewById(R.id.textEnergyConsumed);
        energyConsumed = intent.getExtras().getFloat("energyConsumed");

        if (energyConsumed == 0){
            textEnergyConsumed.setText("");
        }
        else {
            textEnergyConsumed.setText("Robot Energy Consumed: " + energyConsumed);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Starts the mp3
        player = MediaPlayer.create(this, R.raw.winning);

        player.setLooping(true);
        player.start();
        Log.d(TAG, "Media Started");
    }


    /**
     * Function to change what happens when back button is pressed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StateTitle.class);
        startActivity(intent);
    }
    /**
     * Method to Stop the music from playing, resetting it, and releasing it
     */
    private void stopPlayer() {
        if (player!=null) {
            player.release();
            player = null;
            Log.d(TAG, "Media Stopped");
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }
}