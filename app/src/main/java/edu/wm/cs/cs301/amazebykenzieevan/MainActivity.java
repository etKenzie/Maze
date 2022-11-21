package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private TextView textLevel;
    private SeekBar seekbarLevel;

    private Button buttonExplore;
    private Button buttonRevisit;

    private Boolean roomState;
    private String mazeGenerator;
    private String mazeDriver;
    private String Level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner SpinnerMazeGenerator = findViewById(R.id.SpinnerMazeGenerator);
        Spinner SpinnerMazeDriver = findViewById(R.id.SpinnerMazeDriver);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.MazeGenerators, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerMazeGenerator.setAdapter(adapter);
        SpinnerMazeGenerator.setOnItemSelectedListener(this);


        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.MazeDrivers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerMazeDriver.setAdapter(adapter2);
        SpinnerMazeDriver.setOnItemSelectedListener(this);



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
                // Strings that keep which Maze Generator and Driver to Use
                mazeGenerator = SpinnerMazeGenerator.getItemAtPosition(SpinnerMazeGenerator.getSelectedItemPosition()).toString();
                mazeDriver = SpinnerMazeDriver.getItemAtPosition(SpinnerMazeDriver.getSelectedItemPosition()).toString();

                // Create Boolean to check if room should be on or off.
                Switch switchRoom = (Switch) findViewById(R.id.switchRoom);
                roomState = switchRoom.isChecked();

                openStateGenerating();
            }
        });

    }

    public void openStateGenerating() {
        Intent intent = new Intent(this, StateGenerating.class);
        intent.putExtra("mazeGenerator", mazeGenerator);
        intent.putExtra("mazeDriver", mazeDriver);
        intent.putExtra("roomState", roomState);
        intent.putExtra("Level", Level);

        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}