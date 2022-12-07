package edu.wm.cs.cs301.amazebykenzieevan;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import edu.wm.cs.cs301.amazebykenzieevan.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Floorplan;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
import edu.wm.cs.cs301.amazebykenzieevan.gui.CompassRose;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Constants;
import edu.wm.cs.cs301.amazebykenzieevan.gui.FirstPersonView;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Map;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot;
import edu.wm.cs.cs301.amazebykenzieevan.views.MazePanel;

/**
 * @author kenzieevan
 *
 * Class that implements UI interface in activity_play_manually Layout file.
 */
public class PlayManuallyActivity extends AppCompatActivity {
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

    // Buttons for User to move around the maze
    ImageButton buttonForwardKey;
    ImageButton buttonLeftKey;
    ImageButton buttonRightKey;
    ImageButton buttonBackKey;

    // Integer to keep track of number of Clicks and Shortest Possible Path
    int pathLength;
    int shortestPath;

    // Maze Panel and Maze Instance to go through
    MazePanel mazePanel;
    Maze newMaze;

    // State Playing Drawing Components
    CompassRose cr;
    Map mapView;
    FirstPersonView firstPersonView;

    // Robot Components
    Robot robot;
//    String[] whichSensors; These Not used for manual implementation
//    float energyConsumed;
//    RobotDriver curRobotDriver;
    Floorplan seenCells;


    // Position Components;
    int px,py;
    CardinalDirection cd;

    // Additional Components
    boolean started;

    private static final String TAG = "PlayManuallyActivity";

    // Media Player for music
    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_manually);

        // Toggle Button Configuration
        toggleMap = (ToggleButton) findViewById(R.id.toggleMap);
        showMap = false;

        toggleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap = toggleMap.isChecked();
                Log.d(TAG, "Map On: " + String.valueOf(showMap));
                Toast.makeText(PlayManuallyActivity.this, "Map On: " + String.valueOf(showMap), Toast.LENGTH_SHORT).show();

                draw(cd.angle(),0);

            }
        });

        // Toggle Walls Configuration
        toggleWalls = (ToggleButton) findViewById(R.id.toggleWalls);
        showWalls = false;

        toggleWalls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showWalls = toggleWalls.isChecked();
                Log.d(TAG, "Walls On: " + String.valueOf(showWalls));
                Toast.makeText(PlayManuallyActivity.this, "Walls On: " + String.valueOf(showWalls), Toast.LENGTH_SHORT).show();

                draw(cd.angle(),0);
            }
        });

        // Toggle Solution Configuration
        toggleSolution = (ToggleButton) findViewById(R.id.toggleSolution);
        showSolution = false;

        toggleSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSolution = toggleSolution.isChecked();
                Log.d(TAG, "Solution On: " + String.valueOf(showSolution));
                Toast.makeText(PlayManuallyActivity.this, "Solution On: " + String.valueOf(showSolution), Toast.LENGTH_SHORT).show();
                draw(cd.angle(),0);
            }
        });


        // Initialize moves value to be 0
        pathLength = 0;

        // Forward Button Implementation
        buttonForwardKey = (ImageButton) findViewById(R.id.buttonFowardKey);

        buttonForwardKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Move Forward");
                walk(1);
                Toast.makeText(PlayManuallyActivity.this, "Move Forward", Toast.LENGTH_SHORT).show();

                if(isOutside(px,py)) {
                    goStateWinning();
                }
            }
        });

        // Left Button Implementation
        buttonLeftKey = (ImageButton) findViewById(R.id.buttonLeftKey);

        buttonLeftKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Rotate Left");
                rotate(1);
                Toast.makeText(PlayManuallyActivity.this, "Rotate Left", Toast.LENGTH_SHORT).show();
            }
        });

        // Right Button Implementation
        buttonRightKey = (ImageButton) findViewById(R.id.buttonRightKey);

        buttonRightKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Rotate Right");
                rotate(-1);
                Toast.makeText(PlayManuallyActivity.this, "Rotate Right", Toast.LENGTH_SHORT).show();

                if(isOutside(px,py)) {
                    goStateWinning();
                }
            }
        });

        // Back button Implementation
        buttonBackKey = (ImageButton) findViewById(R.id.buttonBackKey);

        buttonBackKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walk(-1);
                Toast.makeText(PlayManuallyActivity.this, "Move Backwards", Toast.LENGTH_SHORT).show();
            }
        });

        // Zoom Buttons Implementation
        buttonZoomIn = (Button) findViewById(R.id.buttonZoomIn);

        buttonZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Zoom In");
                Toast.makeText(PlayManuallyActivity.this, "Zoom In", Toast.LENGTH_SHORT).show();

                mapView.incrementMapScale();
                draw(cd.angle(),0);
            }
        });

        buttonZoomOut = (Button) findViewById(R.id.buttonZoomOut);

        buttonZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Zoom Out");
                Toast.makeText(PlayManuallyActivity.this, "Zoom Out", Toast.LENGTH_SHORT).show();

                mapView.decrementMapScale();
                draw(cd.angle(),0);
            }
        });


        // Maze Panel Instance to use
        mazePanel = (MazePanel) findViewById(R.id.mazePanelAnimation);

        // Getting maze to play through
        newMaze = MazeHolder.getInstance().getData();

        // Initialize State Playing Components
        firstPersonView =null;
        mapView = null;
        started = false;

        px = 0;
        py = 0;
        cd = CardinalDirection.East;

        seenCells = null;
        cr = null;

//        // Robot Settings For manual no need to set
//        robot = new ReliableRobot();


    }
    @Override
    protected void onStart() {
        super.onStart();

        // Starts the mp3
        player = MediaPlayer.create(this, R.raw.playmanual);
        player.setLooping(true);

        player.start();
        Log.d(TAG, "Media Started");

        Log.d(TAG, "onCreate: " + newMaze.getDistanceToExit(px,py));
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
    protected int[] getCurrentPosition() {
        int[] result = new int[2];
        result[0] = px;
        result[1] = py;
        return result;
    }
    protected CardinalDirection getCurrentDirection() {
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
    private synchronized void rotate(int dir) {
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
    private synchronized void walk(int dir) {
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
     * Class To transition to State Winning
     */
    private void goStateWinning() {
        Intent intent = new Intent(this, WinningActivity.class);
        intent.putExtra("pathLength", pathLength);
        intent.putExtra("shortestPath", shortestPath);
        intent.putExtra("energyConsumed", 0);
        startActivity(intent);
    }

    /**
     * Function to change what happens when back button is pressed
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, StateTitle.class);
        startActivity(intent);
    }

    /**
     * Method to Stop the music from playing, resetting it, and releasing it
     */
    private void stopPlayer() {
        if (player!=null) {
            player.release();
            player = null;
            Log.d(TAG, "Media Stopped");
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        stopPlayer();
    }

}