package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author kenzieevan
 *
 * Class that implements UI interface in activiy_losing Layout file.
 */
public class LosingActivity extends AppCompatActivity {
    // Path Length and Text instance
    int pathLength;
    TextView textPathLength;

    int shortestPath;
    TextView textShortestPath;

    // Energy Consumed by Robot and Text instance
    float energyConsumed;
    TextView textEnergyConsumed;

    // Losing Reason and Text instance
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
        shortestPath = intent.getExtras().getInt("shortestPath");
        textShortestPath = findViewById(R.id.textShortestPath2);

        textShortestPath.setText("Shortest Path Length: " + shortestPath);

        // Adjust Energy Consumed
        textEnergyConsumed = findViewById(R.id.textEnergyConsumed2);
        energyConsumed = intent.getExtras().getFloat("energyConsumed");

        textEnergyConsumed.setText("Robot Energy Consumed: " + energyConsumed);

        // Get Reason for loss and adjust String
        textLosingReason = (TextView) findViewById(R.id.textLosingReason);
        losingReason = intent.getStringExtra("losingReason");

        textLosingReason.setText(losingReason);

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