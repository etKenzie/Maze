package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WinningActivity extends AppCompatActivity {
    // Amount of Moves from previous activity
    int pathLength;
    TextView textPathLength;

    TextView textShortestPath;

    int energyConsumed;
    TextView textEnergyConsumed;

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
        textShortestPath = findViewById(R.id.textShortestPath);

        // Adjust Energy Consumed
        textEnergyConsumed = findViewById(R.id.textEnergyConsumed);
        energyConsumed = intent.getExtras().getInt("energyConsumed");

        if (energyConsumed == 0){
            textEnergyConsumed.setText("");
        }
        else {
            textEnergyConsumed.setText("Robot Energy Consumed: " + energyConsumed);
        }






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