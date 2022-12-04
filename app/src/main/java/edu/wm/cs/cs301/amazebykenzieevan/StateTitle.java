package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author kenzieevan
 *
 * Class that implements UI interface in state_title Layout file.
 */
public class StateTitle extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView textLevel;
    private SeekBar seekbarLevel;

    private Button buttonExplore;
    private Button buttonRevisit;

    private Boolean roomState;
    private String mazeGenerator;
    private String Level;
    private int skill;

    private static final String TAG = "StateTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_title);

        // Maze Generator Spinner Instance
        Spinner SpinnerMazeGenerator = findViewById(R.id.SpinnerMazeGenerator);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MazeGenerators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerMazeGenerator.setAdapter(adapter);
        SpinnerMazeGenerator.setOnItemSelectedListener(this);

        // Default Instance of Level. Used when slider not moved.
        Level = "Level 3";
        skill = 3;

        // Level Seekbar Implementation
        textLevel = (TextView) findViewById(R.id.textLevel);
        seekbarLevel = (SeekBar) findViewById(R.id.seekbarLevel);

        seekbarLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textLevel.setText("Level " + progress);
                // Convert TextLevel to String
                Level = textLevel.getText().toString();
                skill = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, Level + " set");
                Toast.makeText(StateTitle.this, Level + " set", Toast.LENGTH_SHORT).show();

            }

        });



        // Buttons to Transition to new activity
        buttonExplore = (Button) findViewById(R.id.buttonExplore);
        buttonRevisit = (Button) findViewById(R.id.buttonRevisit);

        buttonExplore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Strings that keep which Maze Generator
                mazeGenerator = SpinnerMazeGenerator.getItemAtPosition(SpinnerMazeGenerator.getSelectedItemPosition()).toString();

                // Create Boolean to check if room should be on or off.
                Switch switchRoom = (Switch) findViewById(R.id.switchRoom);
                roomState = switchRoom.isChecked();

                openStateGenerating();
            }
        });

        // Revisit Button Instance
        buttonRevisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mazeGenerator = SpinnerMazeGenerator.getItemAtPosition(SpinnerMazeGenerator.getSelectedItemPosition()).toString();


                // Create Boolean to check if room should be on or off.
                Switch switchRoom = (Switch) findViewById(R.id.switchRoom);
                roomState = switchRoom.isChecked();

                revisitStateGenerating();
            }
        });

    }

    /**
     * Class to transition to State Generating from New Maze Button
     */
    public void openStateGenerating() {
        // Generate Random Seed
        int randomSeed = 1 + (int) (Math.random()*((20000-1)+1));


        // Intent to switch activity and inputs that need to be brought over
        Intent intent = new Intent(this, StateGenerating.class);
        intent.putExtra("mazeGenerator", mazeGenerator);
        intent.putExtra("roomState", roomState);
        intent.putExtra("skill", skill);
        intent.putExtra("Seed", randomSeed);

        // Saving user inputs for revisiting
        SharedPreferences sharedPreferences = getSharedPreferences("oldMaze", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("oldMazeGenerator", mazeGenerator);
        editor.putBoolean("oldRoomState", roomState);
        editor.putInt("oldSkill", skill);
        editor.putInt("oldSeed", randomSeed);

        editor.apply();

        Toast.makeText(this, "Saved Current maze", Toast.LENGTH_SHORT).show();

        // Logging User Inputs gotten from this activity
        Log.d(TAG, "Maze Generator: " + mazeGenerator);
        Log.d(TAG, "Room State: " + roomState);
        Log.d(TAG, "Level: " + skill);
        Log.d(TAG, "Seed: " + randomSeed);


        // Change current activity
        startActivity(intent);
    }

    /**
     * Class to transition to StateGenerating for Revisit Button
     */
    public void revisitStateGenerating() {
        // RandomSeed to use if no previous maze to pop from
        int randomSeed = 1 + (int) (Math.random()*((20000-1)+1));

        // Recall previous maze setting and calling them. Default values of current choices if does not exist
        SharedPreferences sharedPreferences = getSharedPreferences("oldMaze", MODE_PRIVATE);

        String oldMazeGenerator = sharedPreferences.getString("oldMazeGenerator", mazeGenerator);
        Boolean oldRoomState = sharedPreferences.getBoolean("oldRoomState", roomState);
        int oldSkill = sharedPreferences.getInt("oldSkill", skill);
        int oldSeed = sharedPreferences.getInt("oldSeed", randomSeed);

        // Intent and transition to new activity
        Intent intent = new Intent(this, StateGenerating.class);
        intent.putExtra("mazeGenerator", oldMazeGenerator);
        intent.putExtra("roomState", oldRoomState);
        intent.putExtra("skill", oldSkill);
        intent.putExtra("Seed", oldSeed);

        // Logging User Inputs gotten from this activity
        Log.d(TAG, "Maze Generator: " + oldMazeGenerator);
        Log.d(TAG, "Room State: " + oldRoomState);
        Log.d(TAG, "Level: " + oldSkill);
        Log.d(TAG, "Seed: " + oldSeed);

        startActivity(intent);

    }

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
}