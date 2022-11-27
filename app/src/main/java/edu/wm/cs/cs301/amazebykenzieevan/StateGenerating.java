package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StateGenerating extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    // Values from State Title
    String Level;
    String mazeGenerator;
    Boolean roomState;
    int seed;

    Spinner spinnerMazeDriver;
    Spinner spinnerRobotConfiguration;
    String mazeDriver;
    String robotConfiguration;


    ProgressBar progbarMaze;
    TextView textProgressPercentage;
    private static final String TAG = "StateGenerating";

    Button buttonGoNext;
    Boolean progressFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_generating);

        // Retrieve Previous Values
        Intent intent = getIntent();

        Level = intent.getStringExtra("Level");
        mazeGenerator = intent.getStringExtra("mazeGenerator");
        roomState = intent.getExtras().getBoolean("roomState");
        seed = intent.getExtras().getInt("Seed");


        // Spinner Maze Driver Instance
        spinnerMazeDriver = (Spinner) findViewById(R.id.spinnerMazeDriver);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MazeDrivers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMazeDriver.setAdapter(adapter);
        spinnerMazeDriver.setOnItemSelectedListener(this);

        // Spinner Robot Configuration Instance
        spinnerRobotConfiguration = (Spinner) findViewById(R.id.spinnerRobotConfiguration);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.RobotConfigurations, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRobotConfiguration.setAdapter(adapter2);
        spinnerRobotConfiguration.setOnItemSelectedListener(this);

        // Progress Bar Implementation
        progressFinished = false;
        startCount();

        // Button to Go to Next Activity

        buttonGoNext = findViewById(R.id.buttonGoNext);

        buttonGoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mazeDriver = spinnerMazeDriver.getItemAtPosition(spinnerMazeDriver.getSelectedItemPosition()).toString();
                robotConfiguration = spinnerRobotConfiguration.getItemAtPosition(spinnerRobotConfiguration.getSelectedItemPosition()).toString();

                if(mazeDriver.equals("Select")) {
                    Toast.makeText(StateGenerating.this, "Please Select Driver", Toast.LENGTH_SHORT).show();
                }
                if(robotConfiguration.equals("Select") && mazeDriver.equals("Manual")==false) {
                    Toast.makeText(StateGenerating.this, "Please Select Robot Configuration", Toast.LENGTH_SHORT).show();
                }
                if (progressFinished == false) {
                    Toast.makeText(StateGenerating.this, "Wait Until Progress Finished", Toast.LENGTH_SHORT).show();
                }

                // If none of the three statements above are true able to proceed to next activity
                if(mazeDriver.equals("Select") == false && progressFinished) {
                    // Go to Next Activity Depending on Maze Driver
                    if(mazeDriver.equals("Manual")) {
                        goManualActivity();
                    }
                    else{
                        // if driver is not manual and robot configuration is selected
                        if (robotConfiguration.equals("Select") == false) {
                            goAnimationActivity();
                        }
                    }
                }

            }
        });




    }

    /**
     * Starts the Progressbar Count to 100 and text changing at the same time
     */
    private void startCount() {
        progbarMaze = (ProgressBar) findViewById(R.id.progbarMaze);
        textProgressPercentage = (TextView) findViewById(R.id.textProgressPercentage);

        // BackgroundThread class to use for to wait amount of time seconds

        new Thread(new Runnable() {
            @Override
            public void run() {
                double seconds = 10;
                for (int i = 0; i<= (int)seconds; i++){
                    try {
                        double curProg = (i/seconds)*100;
                        int progress = (int) curProg;
                        Log.d(TAG, "run: " + curProg);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textProgressPercentage.setText(progress + "%");
                                progbarMaze.setProgress(progress);

                            }
                        });
                        if(i == (int)seconds){
                            progressFinished = true;
                            Log.d(TAG, "Maze Generated!!");
                        }
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    /**
     * Controls what happens when spinner bar item is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void goManualActivity() {
        Intent intent = new Intent(this, PlayManuallyActivity.class);
        startActivity(intent);
    }

    public void goAnimationActivity() {
        Intent intent = new Intent(this, PlayAnimationActivity.class);
        intent.putExtra("mazeDriver", mazeDriver);
        intent.putExtra("robotConfiguration", robotConfiguration);

        startActivity(intent);
    }
}