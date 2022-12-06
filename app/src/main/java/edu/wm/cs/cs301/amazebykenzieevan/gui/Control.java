 package edu.wm.cs.cs301.amazebykenzieevan.gui;

 import java.util.logging.Logger;

 import edu.wm.cs.cs301.amazebykenzieevan.MazeHolder;
 import edu.wm.cs.cs301.amazebykenzieevan.generation.CardinalDirection;
 import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
 import edu.wm.cs.cs301.amazebykenzieevan.views.MazePanel;

 /**
 * Class handles the user interaction through the different stages of the game.
 * 
 * It implements a state pattern where this class is the Context, 
 * State is the state interface and its implementing state classes
 * all carry a prefix State in its name.
 * The transition from one state to another is distributed across states.
 *  
 * The game currently has 4 states.
 * StateTitle: the starting state where the user can pick the skill-level
 * StateGenerating: the state in which the factory computes the maze to play
 * and the screen shows a progress bar.
 * StatePlaying: the state in which the user plays the game and
 * the screen shows the first person view and the map view.
 * StateWinning: the finish screen that shows the winning message.
 * 
 * This class implements an automaton with states for the different stages of the game.
 * It has state-dependent behavior that controls the display and reacts to key board input from a user.
 * To handle user keyboard input it implements a key listener, digests the input 
 * and delegates the responsibility to respond to its state object.
 *
 * This code is refactored code from Maze.java by Paul Falstad, 
 * www.falstad.com, Copyright (C) 1998, all rights reserved
 * Paul Falstad granted permission to modify and use code for teaching purposes.
 * Refactored by Peter Kemper
 * 
 * @author Peter Kemper
 */
public class Control {

	// not used, just to make the compiler, static code checker happy
	private static final long serialVersionUID = 1L;
	
	// developments vs production version
	// for development it is more convenient if we produce the same maze over an over again
	// by setting the following constant to false, the maze will only vary with skill level and algorithm
	// but not on its own
	// for production version it is desirable that we never play the same maze 
	// so even if the algorithm and skill level are the same, the generated maze should look different
	// which is achieved with some random initialization
	private static final boolean DEVELOPMENT_VERSION_WITH_DETERMINISTIC_MAZE_GENERATION = false;
	// rooms are an additional feature that generalizes the text book maze generation algorithms
	// for development it can be useful to turn of the room generation to focus on the standard algorithm
	private static final boolean DEVELOPMENT_VERSION_MAZE_GENERATION_WITHOUT_ROOMS = false;
	
	/**
	 * The logger is used to track execution and report issues.
	 * Collaborators are the UI and the MazeFactory.
	 * Level INFO: logs mitigated issues such as unexpected user input
	 * Level FINE: logs information flow.
	 */
	private static final Logger LOGGER = Logger.getLogger(Control.class.getName());

	/**
     * Specifies if the maze is perfect, i.e., it has
     * no loops, which is guaranteed by the absence of 
     * rooms and the way the generation algorithms work.
     */
    boolean perfect;
    /**
     * In the deterministic setting (true), 
     * the same random maze will be generated over 
     * and over again for the same settings of skill level, 
     * builder, and perfect.
     * If false, a different maze will be generated each
     * and every time, even if settings of skill level, 
     * builder, and perfect remain the same.
     */
    boolean deterministic;
    
	/**
	 * The current state of the controller and the game.
	 * All state objects share the same interface and can be
	 * operated in the same way, although the behavior is 
	 * vastly different.
	 * currentState is never null and updated from the 
	 * state objects themselves when they hand over 
	 * control to the next state.
	 * The game goes through 4 states: 
	 * 1: show the title screen, wait for user input for skill level
	 * 2: show the generating screen with the progress bar during 
	 * maze generation
	 * 3: show the playing screen, have the user or robot driver
	 * play the game
	 * 4: show the finish screen with the winning/loosing message
	 * 
	 * The initial state is the title state.
	 */
	State currentState;
    /**
     * The panel is used to draw on the screen for the UI.
     * It can be set to null for dry-running the controller
     * for testing purposes but otherwise panel is never null.
     */
    MazePanel panel;

    /**
     * Default constructor
     */
	public Control(MazePanel panel) {
		
		this.panel = panel;

	}


    

 
    /**
     * Turns of graphics to dry-run controller for testing purposes.
     * This is irreversible and subsequent calls to getPanel() will return null. 
     */
    public void turnOffGraphics() {
    	LOGGER.config("Turning graphics permanently off, makes sense for unit testing.");
    	panel = null;
    }
    
    //// Extension in preparation for Project 3: robot and robot driver //////
    // TODO: decide if this should move to StatePlaying 
    /**
     * The robot that interacts with the controller starting from P3
     */
    Robot robot;
    /**
     * The driver that interacts with the robot starting from P3
     */
    RobotDriver driver;
    
    /**
     * Sets the robot and robot driver
     * @param robot the robot that is used for the automated playing mode
     * @param robotdriver the driver that is used for the automated playing mode
     */
    public void setRobotAndDriver(Robot robot, RobotDriver robotdriver) {
        this.robot = robot;
        driver = robotdriver;
        
    }
    /**
     * The robot that is used in the automated playing mode.
     * Null if run in the manual mode.
     * @return the robot, may be null
     */
    public Robot getRobot() {
        return robot;
    }
    /**
     * The robot driver that is used in the automated playing mode.
     * Null if run in the manual mode.
     * @return the driver, may be null
     */
    public RobotDriver getDriver() {
        return driver;
    }
    /**
     * Provides access to the maze. 
     * This is needed for a robot to be able to recognize walls
     * for the distance to walls calculation, to see if it 
     * is in a room or at the exit. 
     * Note that the current position is stored by the 
     * controller. The maze itself is not changed during
     * the game.
     * This method should only be called in the playing state.
     * @return the maze
     */

    public Maze getMaze() {
        return MazeHolder.getInstance().getData();
    }
    
    /**
     * Provides access to the current position.
     * The controller keeps track of the current position
     * while the maze holds information about walls.
     * This method should only be called in the playing state.
     * @return the current position as [x,y] coordinates, 
     * {@code 0 <= x < width, 0 <= y < height}
     */
    
    public int[] getCurrentPosition() {
        return ((StatePlaying)currentState).getCurrentPosition();
    }
    
    /**
     * Provides access to the current direction.
     * The controller keeps track of the current position
     * and direction while the maze holds information about walls.
     * This method should only be called in the playing state.
     * @return the current direction
     */
    
    public CardinalDirection getCurrentDirection() {
        return ((StatePlaying)currentState).getCurrentDirection();
    }
	
	////////////// end of P3 specific additions //////////////////



}
