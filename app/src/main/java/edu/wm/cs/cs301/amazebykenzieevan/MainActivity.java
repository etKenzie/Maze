package edu.wm.cs.cs301.amazebykenzieevan;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

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

        String mazeGenerator = SpinnerMazeGenerator.getItemAtPosition(SpinnerMazeGenerator.getSelectedItemPosition()).toString();
        String mazeDriver = SpinnerMazeDriver.getItemAtPosition(SpinnerMazeDriver.getSelectedItemPosition()).toString();





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