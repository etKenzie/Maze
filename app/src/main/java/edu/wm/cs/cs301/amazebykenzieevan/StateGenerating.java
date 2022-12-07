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

import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
import edu.wm.cs.cs301.amazebykenzieevan.generation.MazeFactory;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Order;

/**
 * @author kenzieevan
 *
 * Class that implements UI interface in activity_state_generating Layout file.
 */
public class StateGenerating extends AppCompatActivity implements AdapterView.OnItemSelectedListener, Order {
    // Values from State Title
    int skill;
    String mazeGenerator;
    Boolean roomState;
    int seed;

    // Spinner instances and String outputs from those
    Spinner spinnerMazeDriver;
    Spinner spinnerRobotConfiguration;
    String mazeDriver;
    String robotConfiguration;

    // ProgressBar of Maze Generation
    ProgressBar progbarMaze;
    TextView textProgressPercentage;
    private static final String TAG = "StateGenerating";

    // Transition Helpers
    Button buttonGoNext;
    Boolean progressFinished;

    // Maze to Traverse
    Maze newMaze;
    private static StateGenerating instance;

    int progress;
    private MazeHolder mazeHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_generating);

        // Retrieve Previous Values
        Intent intent = getIntent();

        skill = intent.getExtras().getInt("skill");
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

        // Button to Go to Next Activity
        buttonGoNext = findViewById(R.id.buttonGoNext);

        buttonGoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Current choices for Driver and Robot
                mazeDriver = spinnerMazeDriver.getItemAtPosition(spinnerMazeDriver.getSelectedItemPosition()).toString();
                robotConfiguration = spinnerRobotConfiguration.getItemAtPosition(spinnerRobotConfiguration.getSelectedItemPosition()).toString();

                // If driver has not been chosen
                if(mazeDriver.equals("Select")) {
                    Toast.makeText(StateGenerating.this, "Please Select Driver", Toast.LENGTH_SHORT).show();
                }
                // If driver is not manual robot must be chosen
                if(robotConfiguration.equals("Select") && mazeDriver.equals("Manual")==false) {
                    Toast.makeText(StateGenerating.this, "Please Select Robot Configuration", Toast.LENGTH_SHORT).show();
                }
                // Maze has not been generated
                if (progressFinished == false) {
                    Toast.makeText(StateGenerating.this, "Wait Until Progress Finished", Toast.LENGTH_SHORT).show();
                }

                // If none of the three statements above are true able to proceed to next activity
                if(mazeDriver.equals("Select") == false && progressFinished) {

                    // Store Maze as a Singleton
                    MazeHolder.getInstance().setData(newMaze);
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

    @Override
    protected void onStart()
    {
        super.onStart();
        // Generate Maze to traverse for future activities
        MazeFactory factory = new MazeFactory();
//        DefaultOrder order = new DefaultOrder(getSkillLevel(), getBuilder(), isPerfect(), getSeed());

        //create factory then return a new maze
        factory.order(this);

//        Log.d(TAG, "Initial Distance To Exit: " + newMaze.getDistanceToExit(2,2));



    }

    public void updateProgressBar(int progress) {
        progbarMaze = (ProgressBar) findViewById(R.id.progbarMaze);
        textProgressPercentage = (TextView) findViewById(R.id.textProgressPercentage);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textProgressPercentage.setText(progress + "%");
                progbarMaze.setProgress(progress);

            }
        });
        if (progress >= 100) {
            progressFinished = true;

        }

    }


    /**
     * Controls what happens when spinner bar item is selected
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Log Spinner Selection and Make Toast of it
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
        Log.d(TAG, text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    /**
     * Class to transition to Manual Activity.
     */
    public void goManualActivity() {

        // Intent to change and then changes activity
        Intent intent = new Intent(this, PlayManuallyActivity.class);
        startActivity(intent);


    }

    /**
     * Class to transition to Play Animation Activity.
     */
    public void goAnimationActivity() {
        // Intent to change and then changes activity while storing some values
        Intent intent = new Intent(this, PlayAnimationActivity.class);
        intent.putExtra("mazeDriver", mazeDriver);
        intent.putExtra("robotConfiguration", robotConfiguration);

        startActivity(intent);
    }

    @Override
    public int getSkillLevel() {
        Log.d(TAG, "getSkillLevel: " + skill);
        return skill;
    }

    @Override
    public Builder getBuilder() {
        // Create Builder object to use and adjust based on input
        Order.Builder builder = Order.Builder.DFS;
        if (mazeGenerator == "Prim"){
            builder = Order.Builder.Prim;
        }
        return builder;
    }

    @Override
    public boolean isPerfect() {
        return roomState;
    }

    @Override
    public int getSeed() {
        return seed;
    }

    @Override
    public void deliver(Maze mazeConfig) {
        this.newMaze = mazeConfig;
    }

    @Override
    public void updateProgress(int percentage) {
        Log.d(TAG, "Completion of Maze Percentage: " + percentage);

        if (0 <= percentage && percentage <= 100) {
            progress = percentage;
            updateProgressBar(progress);
        }
        else {
            progress = (percentage < 0) ? 0 : 100;
            Log.d(TAG, "range violation, " + percentage + " outside 0,1,...,100 range. Used closest legit value for mitigation.");
        }
    }

}