package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.amazebykenzieevan.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Floorplan;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
import edu.wm.cs.cs301.amazebykenzieevan.gui.CompassRose;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Constants;
import edu.wm.cs.cs301.amazebykenzieevan.gui.DistanceSensor;
import edu.wm.cs.cs301.amazebykenzieevan.gui.FirstPersonView;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Map;
import edu.wm.cs.cs301.amazebykenzieevan.gui.ReliableRobot;
import edu.wm.cs.cs301.amazebykenzieevan.gui.ReliableSensor;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot;
import edu.wm.cs.cs301.amazebykenzieevan.gui.RobotDriver;
import edu.wm.cs.cs301.amazebykenzieevan.gui.UnreliableRobot;
import edu.wm.cs.cs301.amazebykenzieevan.gui.UnreliableSensor;
import edu.wm.cs.cs301.amazebykenzieevan.gui.WallFollower;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Wizard;
import edu.wm.cs.cs301.amazebykenzieevan.views.MazePanel;

/**
 * @author kenzieevan
 *
 * Class that implements UI interface in activity_play_animation Layout file.
 */
public class PlayAnimationActivity extends AppCompatActivity {
    // Driver and Robot to use from State Generating
    String mazeDriver;
    String robotConfiguration;

    // Play Button for maze
    ToggleButton togglePlay;
    Boolean play;

    // Toggle Button instance and boolean that gets the current state
    ToggleButton toggleMap;
    boolean showMap;

    // Toggle Button for showing Walls
    ToggleButton toggleWalls;
    boolean showWalls;

    // Toggle Button for showing Solution
    ToggleButton toggleSolution;
    boolean showSolution;

    // Zoom Buttons
    Button buttonZoomIn;
    Button buttonZoomOut;

    // Sensor Status. To Change status change background tint of textview
    TextView textF;
    TextView textL;
    TextView textR;
    TextView textB;

    // Seekbar, speed, and Text
    SeekBar seekbarSpeed;
    TextView textSpeed;
    int speed;

    // int of amount of moves robot has taken and enery consumed
    int pathLength;
    float initialEnergy;
    float energyConsumed;
    int shortestPath;

    // Buttons to go to new activity
    String losingReason;

    // Maze Panel Instance and Maze Instance to Go through
    MazePanel mazePanel;
    Maze newMaze;

    // State Playing Drawing Components
    CompassRose cr;
    Map mapView;
    FirstPersonView firstPersonView;

    // Robot Components
    Robot robot;
    String[] whichSensors;
    RobotDriver curRobotDriver;
    Floorplan seenCells;

    // Position Components;
    int px,py;
    CardinalDirection cd;

    // Additional Components
    boolean started;

    // Handler to handle UI
    private Handler mHandler = new Handler();
    boolean stopRunnable;
    int runnableSpeed;

    // Adjust Energy Level
    ProgressBar progbarEnergyLevel;
    TextView textEnergyLevel;

    private static final String TAG = "PlayAnimationActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_animation);

        // Get Maze Driver and Robot Type
        Intent intent = getIntent();

        mazeDriver = intent.getStringExtra("mazeDriver");
        robotConfiguration = intent.getStringExtra("robotConfiguration");

        // Play Button Implementation
        togglePlay = (ToggleButton) findViewById(R.id.togglePlay);
        play = true;

        togglePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play = togglePlay.isChecked();
                Log.d(TAG, "Play: " + String.valueOf(play));
                Toast.makeText(PlayAnimationActivity.this, "Play: " + String.valueOf(play), Toast.LENGTH_SHORT).show();

                if (play==true) {
                    stopRunnable = true;
                    mazeRunnable.run();
                }
                else {
                    stopRunnable = false;
                    mHandler.removeCallbacks(mazeRunnable);
                }
            }
        });



        // Toggle Button Configuration
        toggleMap = (ToggleButton) findViewById(R.id.toggleMap2);
        showMap = false;

        toggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap = toggleMap.isChecked();
                Log.d(TAG, "Map On: " + String.valueOf(showMap));
                Toast.makeText(PlayAnimationActivity.this, "Map On: " + String.valueOf(showMap), Toast.LENGTH_SHORT).show();

                draw(cd.angle(),0);
            }
        });

        // Toggle Walls Configuration
        toggleWalls = (ToggleButton) findViewById(R.id.toggleWalls2);
        showWalls = false;

        toggleWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWalls = toggleWalls.isChecked();
                Log.d(TAG, "Walls On: " + String.valueOf(showWalls));
                Toast.makeText(PlayAnimationActivity.this, "Walls On: " + String.valueOf(showWalls), Toast.LENGTH_SHORT).show();

                draw(cd.angle(),0);
            }
        });

        // Toggle Solution Configuration
        toggleSolution = (ToggleButton) findViewById(R.id.toggleSolution2);
        showSolution = false;

        toggleSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSolution = toggleSolution.isChecked();
                Log.d(TAG, "Solution On: " + String.valueOf(showSolution));
                Toast.makeText(PlayAnimationActivity.this, "Solution On: " + String.valueOf(showSolution), Toast.LENGTH_SHORT).show();

                draw(cd.angle(),0);
            }
        });

        // Zoom Buttons Implementation
        buttonZoomIn = (Button) findViewById(R.id.buttonZoomIn2);

        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Zoom In");
                Toast.makeText(PlayAnimationActivity.this, "Zoom In", Toast.LENGTH_SHORT).show();

                mapView.incrementMapScale();
                draw(cd.angle(),0);
            }
        });

        buttonZoomOut = (Button) findViewById(R.id.buttonZoomOut2);

        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Zoom Out");
                Toast.makeText(PlayAnimationActivity.this, "Zoom Out", Toast.LENGTH_SHORT).show();

                mapView.decrementMapScale();
                draw(cd.angle(),0);
            }
        });

        // Sensor Status Texts
        textF = (TextView) findViewById(R.id.textF);
        textL = (TextView) findViewById(R.id.textL);
        textR = (TextView) findViewById(R.id.textR);
        textB = (TextView) findViewById(R.id.textB);

        // Example how to change Color. Hex Value #F52424 for red and #69F421 for green
//        textF.setBackgroundColor(Color.parseColor("#F52424"));

        // Speed Seekbar Configuration
        seekbarSpeed = (SeekBar) findViewById(R.id.seekbarSpeed);
        textSpeed = (TextView) findViewById(R.id.textSpeed);
        speed = 50;
        runnableSpeed = 300;


        seekbarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                speed = progress;
                textSpeed.setText("Speed: " + speed + "%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "Speed: " + speed + "%");
                Toast.makeText(PlayAnimationActivity.this, "Speed: " + speed + "%", Toast.LENGTH_SHORT).show();

                runnableSpeed = (100-speed) * 6;

                Log.d(TAG, "runnableSpeed: " + runnableSpeed);
                // Only if currently the maze is still running
                if (play == true) {
                    mHandler.removeCallbacks(mazeRunnable);
                    mazeRunnable.run();
                }

            }
        });

        // MazePanel Call and Instance
        mazePanel = findViewById(R.id.mazePanelAnimation);
        mazePanel.testImage();

        // New Maze Instance call
        newMaze = MazeHolder.getInstance().getData();

        // Energy Progress Bar Initialization
        progbarEnergyLevel = (ProgressBar) findViewById(R.id.progbarEnergyLevel);
        textEnergyLevel = (TextView) findViewById(R.id.textEnergyLevel);


    }


    private Runnable mazeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                stopRunnable = false;
                if (robot.isAtExit()) {
                    stopRunnable = true;
                    // While robot is not facing exit position
                    while(robot.canSeeThroughTheExitIntoEternity(Robot.Direction.FORWARD)==false){
                        // rotate robot
                        robot.rotate(Robot.Turn.LEFT);
                    }
                    // Move robot through the exit. But here it would cause Invalid Position Error so just add energy consumed
                    // as this simulates moving forward once
                    energyConsumed += 6;
                    pathLength += 1;

                    // For Loop to Stop the Threads from running.
                    Robot.Direction[] directionArray = {Robot.Direction.FORWARD, Robot.Direction.LEFT, Robot.Direction.RIGHT, Robot.Direction.BACKWARD};
                    for(int index = 0; index<4; index++) {
                        if(whichSensors[index].equals("0")) {
                            robot.stopFailureAndRepairProcess(directionArray[index]);
                        }
                    }

                    // Go to Winning Activity and Set energyConsumed
                    energyConsumed = initialEnergy - robot.getBatteryLevel();

                    goWinningActivity();
                }

                // If Robot Runs out of Battery
                if (robot.getBatteryLevel() <= 7) {
                    stopRunnable = true;

                    losingReason = "Robot unable to Move Anymore.";

                    // For Loop to Stop the Threads from running.
                    Robot.Direction[] directionArray = {Robot.Direction.FORWARD, Robot.Direction.LEFT, Robot.Direction.RIGHT, Robot.Direction.BACKWARD};
                    for(int index = 0; index<4; index++) {
                        if(whichSensors[index].equals("0")) {
                            robot.stopFailureAndRepairProcess(directionArray[index]);
                        }
                    }

                    // Go to Winning Activity and Set energyConsumed
                    energyConsumed = initialEnergy - robot.getBatteryLevel();

                    goLosingActivity();
                }

                // If nothing has trigered runnable to stop
                if (stopRunnable == false){
                    // Adjust Energy Level
                    double energyPercentage =  (robot.getBatteryLevel()/initialEnergy) * 100;
                    int percent = (int) energyPercentage;

                    progbarEnergyLevel.setProgress(percent);
                    textEnergyLevel.setText("Energy: " + percent + "%");



                    curRobotDriver.drive1Step2Exit();
                    mHandler.postDelayed(this, runnableSpeed);

                }



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        start();

    }


    public void start() {
        assert null != newMaze : "StatePlaying.start: maze must exist!";

        started = true;

        // adjust internal state of maze model
        // init data structure for visible walls
        seenCells = new Floorplan(newMaze.getWidth()+1, newMaze.getHeight()+1);
        // set the current position and direction consistently with the viewing direction
        setPositionDirectionViewingDirection();

        if (mazePanel != null) {
            startDrawer();
        }
        else {
            // else: dry-run without graphics, most likely for testing purposes
            printWarning();
        }

        // By Default all sensors is Reliable
        DistanceSensor forwardSensor = new ReliableSensor();
        DistanceSensor leftSensor = new ReliableSensor();
        DistanceSensor rightSensor = new ReliableSensor();
        DistanceSensor backwardSensor = new ReliableSensor();

        // According to Robot Configuration Edit whichSensors
        String[] sensor = {"1","1","1","1"};
        if(robotConfiguration.equals("Premium")){
            // by Default all sensors work already
        }
        else if (robotConfiguration.equals("Mediocre")){
            sensor = new String[]{"1","0","0","1"};
        }
        else if (robotConfiguration.equals("Soso")){
            sensor = new String[]{"0","1","1","0"};
        }
        else if (robotConfiguration.equals("Shaky")){
            sensor = new String[]{"0","0","0","0"};
        }

        // Integer to help set robot type. If 0 robot is reliable if 1 robot is Unreliable
        int robotType = 0;

        /*
         *  Set Sensors according to String Input. By Default all Sensors is Reliable.
         *  Also, if any of the sensors are unreliable robotType = 1 as now the robot should
         *  also be unreliable.
         */

        // This is for testing. Assume if not given whichSensors, by default it is all reliable.
        if (this.whichSensors == null) {

            this.whichSensors = sensor;
        }

        for (int index = 0; index < whichSensors.length; index++) {
            if (index == 0 && whichSensors[index].equals("0")) {
                robotType = 1;
                forwardSensor = new UnreliableSensor();
            }
            if (index == 1 && whichSensors[index].equals("0")) {
                robotType = 1;
                leftSensor = new UnreliableSensor();
            }
            if (index == 2 && whichSensors[index].equals("0")) {
                robotType = 1;
                rightSensor = new UnreliableSensor();
            }
            if (index == 3 && whichSensors[index].equals("0")) {
                robotType = 1;
                backwardSensor = new UnreliableSensor();
            }
        }

        // By Default robot is reliable.
        robot = new ReliableRobot();
        if (robotType == 1) {
            // That means the robot will be unreliable
            robot = new UnreliableRobot();

        }
        robot.setActivity(this);

        // Hardcoded Values for p6 for pathLength and energyConsumed
        initialEnergy = robot.getBatteryLevel();
        losingReason = "Robot ran out of Energy.";

        robot.addDistanceSensor(forwardSensor, Robot.Direction.FORWARD);
        robot.addDistanceSensor(leftSensor, Robot.Direction.LEFT);
        robot.addDistanceSensor(rightSensor, Robot.Direction.RIGHT);
        robot.addDistanceSensor(backwardSensor, Robot.Direction.BACKWARD);

        /*
         *  Array of Directions that coincides with whichSensors. This is used to go with a for loop
         *  to start the failure and repair process of a unreliable sensor in that robots direction.
         *  Also, Thread is used here to adjust the amount of time in between starting these processes
         *  so that not all unreliable sensors are working and repairing at the exact same time.
         */
        Robot.Direction[] directionArray = {Robot.Direction.FORWARD, Robot.Direction.LEFT, Robot.Direction.RIGHT, Robot.Direction.BACKWARD};

        for(int index = 0; index<4; index++) {
            if(whichSensors[index].equals("0")) {
                robot.startFailureAndRepairProcess(directionArray[index], 4, 2);
                try {
                    // waits for 1.3 seconds before next start of failure and repair process
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }



        // Wizard Robot Driver Runner
        if(mazeDriver.equals("Wizard")) {
            curRobotDriver = new Wizard();



            curRobotDriver.setMaze(newMaze);
            curRobotDriver.setRobot(robot);

//            if (curRobotDriver != null) {
//                try {
//                    // if wizard returns false then something has failed
//                    if (curRobotDriver.drive2Exit() == false) {
////                        success = false;
////                        switchFromPlayingToWinning(curRobotDriver.getPathLength());
//
//                    }
//
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
        }
        // Using Wall Follower Driver
        else if(mazeDriver.equals("WallFollower")) {
            System.out.println("WALLFOLLOWER!!");
            curRobotDriver = new WallFollower();

            curRobotDriver.setMaze(newMaze);
            curRobotDriver.setRobot(robot);

//            if (curRobotDriver != null) {
//                try {
//                    // if wizard returns false then something has failed
//                    if (curRobotDriver.drive2Exit() == false) {
////                        success = false;
////                        switchFromPlayingToWinning(curRobotDriver.getPathLength());
//
//                    }
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }

        }
//        else if(driver == 3) {
//            curRobotDriver = new SmartWizard();
//
//            curRobotDriver.setMaze(maze);
//            curRobotDriver.setRobot(robot);
//
//            control.setRobotAndDriver(robot, curRobotDriver);
//            if (curRobotDriver != null) {
//                try {
//                    // if wizard returns false then something has failed
//                    if (curRobotDriver.drive2Exit() == false) {
//                        success = false;
////                        switchFromPlayingToWinning(curRobotDriver.getPathLength());
//                    }
//
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }


        mazeRunnable.run();

    }

    protected void startDrawer() {
        cr = new CompassRose();
        cr.setPositionAndSize(Constants.VIEW_WIDTH/2,
                (int)(0.1*Constants.VIEW_HEIGHT),35);

        firstPersonView = new FirstPersonView(Constants.VIEW_WIDTH,
                Constants.VIEW_HEIGHT, Constants.MAP_UNIT,
                Constants.STEP_SIZE, seenCells, newMaze.getRootnode()) ;

        mapView = new Map(seenCells, 15, newMaze) ;
        // draw the initial screen for this state
        draw(cd.angle(), 0);
    }

    /**
     * Internal method to set the current position, the direction
     * and the viewing direction to values consistent with the
     * given maze.
     */
    private void setPositionDirectionViewingDirection() {
        int[] start = newMaze.getStartingPosition() ;
        setCurrentPosition(start[0],start[1]) ;

        shortestPath = newMaze.getDistanceToExit(start[0],start[1]);

        cd = CardinalDirection.East;
    }

    /**
     * Draws the current content on panel to show it on screen.
     * @param angle the current viewing angle, east == 0 degrees, south == 90, west == 180, north == 270
     * @param walkStep a counter for intermediate steps within a single step forward or backward
     */
    protected void draw(int angle, int walkStep) {

        if (mazePanel == null) {
            printWarning();
            return;
        }
        // draw the first person view and the map view if wanted
        firstPersonView.draw(mazePanel, px, py, walkStep, angle,
                newMaze.getPercentageForDistanceToExit(px, py)) ;
        if (isInMapMode()) {
            mapView.draw(mazePanel, px, py, angle, walkStep,
                    isInShowMazeMode(),isInShowSolutionMode()) ;
        }
        // update the screen with the buffer graphics
        mazePanel.commit(); ;
    }

    /**
     * Prints the warning about a missing panel only once
     */
    boolean printedWarning = false;
    protected void printWarning() {
        if (printedWarning)
            return;
        Log.d(TAG, "No panel for drawing during executing, dry-run game without graphics!");
        printedWarning = true;
    }

    ////////////////////////////// set methods ///////////////////////////////////////////////////////////////
    ////////////////////////////// Actions that can be performed on the maze model ///////////////////////////
    protected void setCurrentPosition(int x, int y) {
        px = x ;
        py = y ;
    }

    ////////////////////////////// get methods ///////////////////////////////////////////////////////////////
     public int[] getCurrentPosition() {
        int[] result = new int[2];
        result[0] = px;
        result[1] = py;
        return result;
    }
    public CardinalDirection getCurrentDirection() {
        return cd;
    }
    boolean isInMapMode() {
        return showMap ;
    }
    boolean isInShowMazeMode() {
        return showWalls ;
    }
    boolean isInShowSolutionMode() {
        return showSolution ;
    }
    public Maze getMaze() {
        return newMaze ;
    }
    //////////////////////// Methods for move and rotate operations ///////////////

    /**
     * Determines if one can walk in the given direction
     * @param dir is the direction of interest, either 1 or -1
     * @return true if there is no wall in this direction, false otherwise
     */
    protected boolean wayIsClear(int dir) {
        switch (dir) {
            case 1: // forward
                return !newMaze.hasWall(px, py, cd);
            case -1: // backward
                return !newMaze.hasWall(px, py, cd.oppositeDirection());
            default:
                throw new RuntimeException("Unexpected direction value: " + dir);
        }
    }

    /**
     * Draws and waits. Used to obtain a smooth appearance for rotate and move operations
     */
    private void slowedDownRedraw(int angle, int walkStep) {
        Log.d(TAG, "Drawing intermediate figures: angle " + angle + ", walkStep " + walkStep);
        draw(angle, walkStep) ;
        try {
            Thread.sleep(25);
        } catch (Exception e) {
            // may happen if thread is interrupted
            // no reason to do anything about it, ignore exception
        }
    }

    /**
     * Performs a rotation with 4 intermediate views,
     * updates the screen and the internal direction
     * @param dir for current direction, values are either 1 or -1
     */
    public synchronized void rotate(int dir) {
        final int originalAngle = cd.angle();//angle;
        final int steps = 4;
        int angle = originalAngle; // just in case for loop is skipped
        for (int i = 0; i != steps; i++) {
            // add 1/4 of 90 degrees per step
            // if dir is -1 then subtract instead of addition
            angle = originalAngle + dir*(90*(i+1))/steps;
            angle = (angle+1800) % 360;
            // draw method is called and uses angle field for direction
            // information.
            slowedDownRedraw(angle, 0);
        }
        // update maze direction only after intermediate steps are done
        // because choice of direction values are more limited.
        cd = CardinalDirection.getDirection(angle);
        logPosition(); // debugging
        drawHintIfNecessary();

        mazePanel.commit();
    }

    /**
     * Moves in the given direction with 4 intermediate steps,
     * updates the screen and the internal position
     * @param dir, only possible values are 1 (forward) and -1 (backward)
     */
    public synchronized void walk(int dir) {
        // check if there is a wall in the way
        if (!wayIsClear(dir))
            return;
        int walkStep = 0;
        // walkStep is a parameter of FirstPersonView.draw()
        // it is used there for scaling steps
        // so walkStep is implicitly used in slowedDownRedraw
        // which triggers the draw operation in
        // FirstPersonView and Map
        for (int step = 0; step != 4; step++) {
            walkStep += dir;
            slowedDownRedraw(cd.angle(), walkStep);
        }
        // update position to neighbor
        int[] tmpDxDy = cd.getDxDyDirection();
        setCurrentPosition(px + dir*tmpDxDy[0], py + dir*tmpDxDy[1]) ;
        logPosition(); // debugging
        drawHintIfNecessary();

        pathLength += 1;

        mazePanel.commit();
    }

    /**
     * Checks if the given position is outside the maze
     * @param x coordinate of position
     * @param y coordinate of position
     * @return true if position is outside, false otherwise
     */
    private boolean isOutside(int x, int y) {
        return !newMaze.isValidPosition(x, y) ;
    }
    /**
     * Draw a visual cue to help the user unless the
     * map is on display anyway.
     * This is the map if current position faces a dead end
     * otherwise it is a compass rose.
     */
    private void drawHintIfNecessary() {
        if (isInMapMode())
            return; // no need for help
        // in testing environments, there is sometimes no panel to draw on
        // or the panel is unable to deliver a graphics object
        // check this and quietly move on if drawing is impossible
        if ((mazePanel == null || mazePanel.isOperational())) {
            printWarning();
            return;
        }
        // if current position faces a dead end, show map with solution
        // for guidance
        if (newMaze.isFacingDeadEnd(px, py, cd)) {
            //System.out.println("Facing deadend, help by showing solution");
            mapView.draw(mazePanel, px, py, cd.angle(), 0, true, true) ;
        }
        else {
            // draw compass rose
            cr.setCurrentDirection(cd);
            cr.paintComponent(mazePanel);
        }
        mazePanel.commit();
    }

    /////////////////////// Methods for debugging ////////////////////////////////

    private void dbg(String str) {
        Log.d(TAG, str);
    }

    private void logPosition() {
        int[] tmpDxDy = cd.getDxDyDirection();
        Log.d(TAG, "x="+px+",y="+py+",dx="+tmpDxDy[0]+",dy="+tmpDxDy[1]+",angle="+cd.angle());
        //LOGGER.fine("x="+px+",y="+py+",dx="+dx+",dy="+dy+",angle="+angle);
    	/*
        if (!deepdebug)
            return;
        dbg("x="+viewx/Constants.MAP_UNIT+" ("+
                viewx+") y="+viewy/Constants.MAP_UNIT+" ("+viewy+") ang="+
                angle+" dx="+dx+" dy="+dy+" "+viewdx+" "+viewdy);
                */
    }

    /**
     * Class to transition to StateLosing
     */
    private void goLosingActivity() {
        Intent intent = new Intent(this, LosingActivity.class);
        intent.putExtra("pathLength", pathLength);
        intent.putExtra("shortestPath", shortestPath);
        intent.putExtra("energyConsumed", energyConsumed);
        intent.putExtra("losingReason", losingReason);

        startActivity(intent);
    }

    /**
     * Class to transition to StateWinning
     */
    private void goWinningActivity() {
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("pathLength", pathLength);
        intent.putExtra("shortestPath", shortestPath);
        intent.putExtra("energyConsumed", energyConsumed);
        startActivity(intent);
    }

    /**
     * Function to change what happens when back button is pressed
     */
    @Override
    public void onBackPressed() {

        // For Loop to Stop the Threads from running.
        Robot.Direction[] directionArray = {Robot.Direction.FORWARD, Robot.Direction.LEFT, Robot.Direction.RIGHT, Robot.Direction.BACKWARD};
        for(int index = 0; index<4; index++) {
            if(whichSensors[index].equals("0")) {
                robot.stopFailureAndRepairProcess(directionArray[index]);
            }
        }
        Intent intent = new Intent(this, StateTitle.class);
        startActivity(intent);
    }

}