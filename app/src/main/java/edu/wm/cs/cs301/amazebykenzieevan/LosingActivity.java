package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LosingActivity extends AppCompatActivity {

    int pathLength;
    TextView textPathLength;

    TextView textShortestPath;

    int energyConsumed;
    TextView textEnergyConsumed;

    TextView textLosingReason;
    String losingReason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_losing);

        // Get int moves from intent
        Intent intent = getIntent();

        pathLength = intent.getExtras().getInt("pathLength");
        textPathLength = findViewById(R.id.textPathLength2);

        textPathLength.setText("Path Length: " + pathLength);

        // Adjust Shortest Path TextView
        textShortestPath = findViewById(R.id.textShortestPath2);

        // Adjust Energy Consumed
        textEnergyConsumed = findViewById(R.id.textEnergyConsumed2);
        energyConsumed = intent.getExtras().getInt("energyConsumed");

        if (energyConsumed == 0){
            textEnergyConsumed.setText("");
        }
        else {
            textEnergyConsumed.setText("Robot Energy Consumed: " + energyConsumed);
        }

        // Get Reason for loss and adjust String
        textLosingReason = (TextView) findViewById(R.id.textLosingReason);
        losingReason = intent.getStringExtra("losingReason");

        textLosingReason.setText(losingReason);

    }
}