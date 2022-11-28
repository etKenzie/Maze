package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.amazebykenzieevan.views.MazePanel2;

public class PlayManuallyActivity extends AppCompatActivity {
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

    // Buttons for User to move around the maze
    ImageButton buttonForwardKey;
    ImageButton buttonLeftKey;
    ImageButton buttonRightKey;
    ImageButton buttonBackKey;

    // Integer to keep track of number of Clicks
    int pathLength;

    MazePanel2 mazePanel;

    private static final String TAG = "PlayManuallyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        // Toggle Button Configuration
        toggleMap = (ToggleButton) findViewById(R.id.toggleMap);
        showMap = false;

        toggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap = toggleMap.isChecked();
                Log.d(TAG, "Map On: " + String.valueOf(showMap));

            }
        });

        // Toggle Walls Configuration
        toggleWalls = (ToggleButton) findViewById(R.id.toggleWalls);
        showWalls = false;

        toggleWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWalls = toggleWalls.isChecked();
                Log.d(TAG, "Walls On: " + String.valueOf(showWalls));

            }
        });

        // Toggle Solution Configuration
        toggleSolution = (ToggleButton) findViewById(R.id.toggleSolution);
        showSolution = false;

        toggleSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSolution = toggleSolution.isChecked();
                Log.d(TAG, "Solution On: " + String.valueOf(showSolution));
            }
        });


        // Initialize moves value to be 0
        pathLength = 0;

        // Forward Button Implementation
        buttonForwardKey = (ImageButton) findViewById(R.id.buttonFowardKey);

        buttonForwardKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add 1 to moves and log direction
                pathLength += 1;
                Log.d(TAG, "Move Forward");
            }
        });

        // Left Button Implementation
        buttonLeftKey = (ImageButton) findViewById(R.id.buttonLeftKey);

        buttonLeftKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Rotate Left");
            }
        });

        // Right Button Implementation
        buttonRightKey = (ImageButton) findViewById(R.id.buttonRightKey);

        buttonRightKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Rotate Right");
            }
        });

        // Back button Implementation
        buttonBackKey = (ImageButton) findViewById(R.id.buttonBackKey);

        buttonBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathLength += 1;
                Log.d(TAG, "Move Backwards");
            }
        });

        // Zoom Buttons Implementation
        buttonZoomIn = (Button) findViewById(R.id.buttonZoomIn);

        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Zoom In");
            }
        });

        buttonZoomOut = (Button) findViewById(R.id.buttonZoomOut);

        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Zoom Out");
            }
        });


        // Shortcut Exclusive p6 button
        Button buttonShortcut = (Button) findViewById(R.id.buttonShortcut2);
        buttonShortcut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goStateWinning();
            }
        });

        // Maze Panel Instance
        mazePanel = (MazePanel2) findViewById(R.id.mazePanelAnimation);

    }

    private void goStateWinning() {
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("pathLength", pathLength);
        intent.putExtra("energyConsumed", 0);
        startActivity(intent);
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