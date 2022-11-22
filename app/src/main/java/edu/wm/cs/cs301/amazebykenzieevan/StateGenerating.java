package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StateGenerating extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_generating);

        // Retrieve Previous Values
        Intent intent = getIntent();

        String Level = intent.getStringExtra("Level");
        String mazeGenerator = intent.getStringExtra("mazeGenerator");
        String mazeDriver = intent.getStringExtra("mazeDriver");
        Boolean roomState = intent.getExtras().getBoolean("roomState");
        int seed = intent.getExtras().getInt("Seed");

        TextView main = (TextView) findViewById(R.id.titleStateGenerating);

        main.setText(Level);




    }

}