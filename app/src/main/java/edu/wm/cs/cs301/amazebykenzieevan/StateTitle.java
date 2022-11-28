package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class StateTitle extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView textLevel;
    private SeekBar seekbarLevel;

    private Button buttonExplore;
    private Button buttonRevisit;

    private Boolean roomState;
    private String mazeGenerator;
    private String Level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_title);

        Spinner SpinnerMazeGenerator = findViewById(R.id.SpinnerMazeGenerator);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MazeGenerators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerMazeGenerator.setAdapter(adapter);
        SpinnerMazeGenerator.setOnItemSelectedListener(this);




        textLevel = (TextView) findViewById(R.id.textLevel);
        seekbarLevel = (SeekBar) findViewById(R.id.seekbarLevel);

        seekbarLevel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textLevel.setText("Level " + progress);
                // Convert TextLevel to String
                Level = textLevel.getText().toString();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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

    public void openStateGenerating() {
        int randomSeed = 1 + (int) (Math.random()*((20000-1)+1));

        Intent intent = new Intent(this, StateGenerating.class);
        intent.putExtra("mazeGenerator", mazeGenerator);
        intent.putExtra("roomState", roomState);
        intent.putExtra("Level", Level);
        intent.putExtra("Seed", randomSeed);

        SharedPreferences sharedPreferences = getSharedPreferences("oldMaze", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("oldMazeGenerator", mazeGenerator);
        editor.putBoolean("oldRoomState", roomState);
        editor.putString("oldLevel", Level);
        editor.putInt("oldSeed", randomSeed);

        editor.apply();

        Toast.makeText(this, "Saved Current maze", Toast.LENGTH_SHORT).show();

        startActivity(intent);
    }

    public void revisitStateGenerating() {
        int randomSeed = 1 + (int) (Math.random()*((20000-1)+1));

        SharedPreferences sharedPreferences = getSharedPreferences("oldMaze", MODE_PRIVATE);

        String oldMazeGenerator = sharedPreferences.getString("oldMazeGenerator", mazeGenerator);
        Boolean oldRoomState = sharedPreferences.getBoolean("oldRoomState", roomState);
        String oldLevel = sharedPreferences.getString("oldLevel", Level);
        int oldSeed = sharedPreferences.getInt("oldSeed", randomSeed);


        Intent intent = new Intent(this, StateGenerating.class);
        intent.putExtra("mazeGenerator", oldMazeGenerator);
        intent.putExtra("roomState", roomState);
        intent.putExtra("Level", oldLevel);
        intent.putExtra("Seed", oldSeed);

        startActivity(intent);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}