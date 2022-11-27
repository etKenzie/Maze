package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class PlayAnimationActivity extends AppCompatActivity {
    // Play Button for maze
    ToggleButton togglePlay;
    Boolean play;

    // Toggle Button instance and boolean that gets the current state
    ToggleButton toggleMap;
    boolean showMap;

    // Toggle Button for showing Walls
    ToggleButton toggleWalls;
    boolean showWalls;

    // Toggle Button for showing Solution
    ToggleButton toggleSolution;
    boolean showSolution;

    // Zoom Buttons
    Button buttonZoomIn;
    Button buttonZoomOut;

    // Sensor Status. To Change status change background tint of textview
    TextView textF;
    TextView textL;
    TextView textR;
    TextView textB;

    // Seekbar, speed, and Text
    SeekBar seekbarSpeed;
    TextView textSpeed;
    int speed;

    private static final String TAG = "PlayAnimationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        // Play Button Implementation
        togglePlay = (ToggleButton) findViewById(R.id.togglePlay);
        play = true;

        togglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play = togglePlay.isChecked();
                Log.d(TAG, "Play: " + String.valueOf(play));
            }
        });

        // Toggle Button Configuration
        toggleMap = (ToggleButton) findViewById(R.id.toggleMap2);
        showMap = false;

        toggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap = toggleMap.isChecked();
                Log.d(TAG, "Map On: " + String.valueOf(showMap));

            }
        });

        // Toggle Walls Configuration
        toggleWalls = (ToggleButton) findViewById(R.id.toggleWalls2);
        showWalls = false;

        toggleWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWalls = toggleWalls.isChecked();
                Log.d(TAG, "Walls On: " + String.valueOf(showWalls));

            }
        });

        // Toggle Solution Configuration
        toggleSolution = (ToggleButton) findViewById(R.id.toggleSolution2);
        showSolution = false;

        toggleSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSolution = toggleSolution.isChecked();
                Log.d(TAG, "Solution On: " + String.valueOf(showSolution));
            }
        });

        // Zoom Buttons Implementation
        buttonZoomIn = (Button) findViewById(R.id.buttonZoomIn2);

        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Zoom In");
            }
        });

        buttonZoomOut = (Button) findViewById(R.id.buttonZoomOut2);

        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Zoom Out");
            }
        });

        // Sensor Status Texts
        textF = (TextView) findViewById(R.id.textF);
        textL = (TextView) findViewById(R.id.textL);
        textR = (TextView) findViewById(R.id.textR);
        textB = (TextView) findViewById(R.id.textB);

        // Example how to change Color. Hex Value #F52424 for red and #69F421 for green
//        textF.setBackgroundColor(Color.parseColor("#F52424"));

        // Speed Seekbar Configuration
        seekbarSpeed = (SeekBar) findViewById(R.id.seekbarSpeed);
        textSpeed = (TextView) findViewById(R.id.textSpeed);
        speed = 50;

        seekbarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                speed = progress;
                textSpeed.setText("Speed: " + speed + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * Function to change what happens when back button is pressed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StateTitle.class);
        startActivity(intent);
    }

}