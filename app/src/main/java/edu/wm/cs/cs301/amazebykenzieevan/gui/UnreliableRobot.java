package edu.wm.cs.cs301.amazebykenzieevan.gui;

//import java.awt.print.Printable;

import edu.wm.cs.cs301.amazebykenzieevan.PlayAnimationActivity;
import edu.wm.cs.cs301.amazebykenzieevan.generation.CardinalDirection;

/**
 * @author Kenzie Evan
 *
 * Class: UnreliableRobot
 * Responsibilities: A Robot that is Unreliable. Does not work continuously and needs to repair itself.
 * Collaborators: Controller class (control) to holds a maze to explore, 
 * 				  a Robot Driver class (Wizard or Wall-Follower) that operates the Robot.
 * 				  Uses a sensor (UnreliableSensor) to calculate distance
 * 				  to a wall in a certain direction. This Sensor can fail and needs to repair.
 * 	
 * Move, Jump, Set, and Sense methods are same as Reliable Robot so those are brought over. 
 * Add additional Methods for failure and repair.
 */
public class UnreliableRobot implements Robot {
	/**
	 * For all function we subtract the cost to Battery as just calling the function has cost
	 *
	 * Initialize a Controller Object
	 * Initialize Battery for Robot
	 * Initialize Odometer (distance traveled)Object
	 * Initialize stop boolean object for hasStopped operation
	 */

	PlayAnimationActivity Activity;
	float Battery;
	int Odometer;
	boolean stopped;

	// Initialize Sensors to Mount Later on
	DistanceSensor forwardSensor;
	DistanceSensor leftSensor;
	DistanceSensor rightSensor;
	DistanceSensor backwardSensor;


	/**
	 * Constructor for ReliableRobot. Initializes a Direction, total Battery,
	 * and resets DistanceTraveled to 0
	 */
	public UnreliableRobot() {
		this.Battery = 3500;
		this.Odometer = 0;
		this.stopped = false;

	}

	/**
	 * Sets the robot to access controller and makes sure controller is working
	 */
	@Override
	public void setActivity(PlayAnimationActivity activity) {
		this.Activity = activity;

	}


	/**
	 * Initializes Distance Sensors for Robot to use in the direction given as a parameter
	 * @param sensor is the sensor to add to the robot
	 * @param mountedDirection is the Direction to mount the sensor relative to the robot
	 */
	@Override
	public void addDistanceSensor(DistanceSensor sensor, Direction mountedDirection) {
		if (mountedDirection == Direction.FORWARD) {
			this.forwardSensor = sensor;

			// Set forwardSensor Maze and Direction
			forwardSensor.setSensorDirection(mountedDirection);
			forwardSensor.setMaze(Activity.getMaze());


		}
		else if (mountedDirection == Direction.LEFT) {
			this.leftSensor = sensor;

			// Set leftSensor Maze and Direction
			leftSensor.setSensorDirection(mountedDirection);
			leftSensor.setMaze(Activity.getMaze());

		}
		else if (mountedDirection == Direction.RIGHT) {
			this.rightSensor = sensor;
			rightSensor.setSensorDirection(mountedDirection);
			rightSensor.setMaze(Activity.getMaze());
		}
		else if (mountedDirection == Direction.BACKWARD) {
			this.backwardSensor = sensor;
			backwardSensor.setSensorDirection(mountedDirection);
			backwardSensor.setMaze(Activity.getMaze());
		}
	}

	/**
	 * Gets Current Position of Robot from Control object
	 * @return x and y coordinates of Robot in the maze
	 */
	@Override
	public int[] getCurrentPosition() throws Exception {
		int[] curPosition = Activity.getCurrentPosition();
		return curPosition;
	}

	/**
	 * Gets Current Direction of Robot from Control object
	 * @return Direction of robot
	 */
	@Override
	public CardinalDirection getCurrentDirection() {
		CardinalDirection curDirection = Activity.getCurrentDirection();
		return curDirection;
	}
	/**
	 * Gets current Battery Level of the Robot
	 * @return Battery Level
	 */
	@Override
	public float getBatteryLevel() {
		return this.Battery;
	}

	/**
	 * Sets the Battery level of the robot to a certain float.
	 * Make sure level is not a negative number
	 * @param level is the value to set battery level
	 */
	@Override
	public void setBatteryLevel(float level) {
		this.Battery = level;

	}

	/**
	 * Return amount of energy used for a full rotation. 360 Degrees
	 * 3*4 in this case so 12
	 */
	@Override
	public float getEnergyForFullRotation() {
		return 12;
	}

	/**
	 *  Return amount of energy it would take to move one step forward.
	 *  Is 6 in this case
	 */
	@Override
	public float getEnergyForStepForward() {
		return 6;
	}

	/**
	 * @return the current value of the odometer.
	 */
	@Override
	public int getOdometerReading() {
		return this.Odometer;
	}

	/**
	 * sets the odometer value to 0
	 */
	@Override
	public void resetOdometer() {
		// TODO Auto-generated method stub
		this.Odometer = 0;
	}
	/**
	 * Rotates the robot to the given direction
	 * @param turn is for the direction the robot should face after this function call
	 */
	@Override
	public void rotate(Turn turn) {
		//subtract cost of turn from battery
		this.Battery -= 3;

		// if it is around subtract another 3
		if(turn == Turn.AROUND) {
			this.Battery -= 3;
		}

		// Check Battery level & that has stopped is false
		if(Battery < 3 || hasStopped()) {
			stopped = true;
			return;
		}

		//if turn is left
		if (turn == Turn.LEFT) {
			// Do a left press key on the controller
			Activity.rotate(1);
		}


		//else if turn is right
		else if (turn == Turn.RIGHT) {
			// Do a right press key on controller
			Activity.rotate(-1);

		}
		//else if turn is around
		else if (turn == Turn.AROUND) {
			if(Battery < 6) {
				stopped = true;
				return;
			}
			// Go right two times
			Activity.rotate(1);
			Activity.rotate(1);
		}
	}

	/**
	 * Moves the robot forward a distance specified in the parameter
	 * @param distance is the amount of steps to move forward in a straight line
	 */
	@Override
	public void move(int distance) {
		// Subtract cost of moving forward
		Battery -= 6;

		// Check Battery level that it is able to move forward
		if (this.Battery < 6) {
			this.stopped = true;
			return;
		}

		//while distance is greater than 0 && has stopped is false
		while(distance >0 && hasStopped() == false) {
			//check battery level greater than 5
			if(this.Battery < 6) {
				return;
			}

			//get current position and direction to check if there is a wall
			int x = Activity.getCurrentPosition()[0];
			int y = Activity.getCurrentPosition()[1];;

			//check there is no wall in front
			if(Activity.getMaze().hasWall(x, y, Activity.getCurrentDirection()) == false) {
				// Move forward on controller
				Activity.walk(1);
				// Increment Odometer
				Odometer++;
			}
			// This Should Never Occur if RobotDriver is using sensors Properly
			// If it occurs stop the Robot!!!
			else {
				System.out.print("The Robot has CRASHED!!!");
				stopped = true;
			}

			//decrease 1 from distance
			distance--;
		}
	}

	/**
	 * Moves robot in straight line, but if there is a wall jump over it
	 */
	@Override
	public void jump() {

//		// Get current position and direction
//		int x = Activity.getCurrentPosition()[0];
//		int y = Activity.getCurrentPosition()[1];
//
//		int[] cardDirection = getCurrentDirection().getDxDyDirection();
//		int dx = cardDirection[0];
//		int dy = cardDirection[1];
//
//		//Check if there is a wall in front of robot
//		if(Activity.getMaze().hasWall(x, y, Activity.getCurrentDirection())==true) {
//			// Subtract energy for jump. 40 in this case
//			Battery -= 40;
//
//			// Check battery Level if enough for a jump & has stopped is false
//			if(Battery < 40 || hasStopped() == true) {
//				return;
//			}
//
//			// if not a valid position to jump stop the robot!!!
//			if(x+dx < 0 || x+dx > Activity.getMaze().getWidth()-1) {
//				stopped = true;
//				System.out.print("Robot Tries to jump over border!!!");
//				return;
//			}
//			else if(y+dy < 0 || y+dy > Activity.getMaze().getHeight()-1) {
//				stopped = true;
//				System.out.print("Robot Tries to jump over border!!!");
//				return;
//			}
//			// Jump over wall
//			Activity.handleUserInput(UserInput.JUMP, 0);
//
//			// Increment Odometer
//			Odometer++;
//
//		}
//		else {
//			// Move 1 distance instead
//			this.move(1);
//		}

	}

	/**
	 * Check if the current position is at the exit but still in the maze
	 * @return true if at the exit, false otherwise
	 */
	@Override
	public boolean isAtExit() {
		// Get Coordinates of Robot
		int x = Activity.getCurrentPosition()[0];
		int y = Activity.getCurrentPosition()[1];

		// Check if current coordinate has a distance of 1 from exit
		if (Activity.getMaze().getDistanceToExit(x, y) == 1) {
			//return true
			return true;
		}

		//else return false
		return false;
	}

	/**
	 * Check if robot is currently inside a room
	 *@return true if robot is in a room, false otherwise
	 */
	@Override
	public boolean isInsideRoom() {
		// Get current coordinates
		int x = Activity.getCurrentPosition()[0];
		int y = Activity.getCurrentPosition()[1];

		//return Maze.isInRoom(coordinate)
		return Activity.getMaze().isInRoom(x, y);
	}

	/**
	 * Check if robot has stopped for any reason
	 * @return true if robot has stopped, false otherwise
	 */
	@Override
	public boolean hasStopped() {
		// check if stop boolean object is true
		if(stopped == true) {
			//if true return true
			System.out.print("Robot has STOPPED!!");
			return true;
		}

		//return true if battery <= 0
		if(Battery < 1) {
			stopped = true;
			System.out.print("Robot has Run out of Battery");
			return true;
		}

		//else return false
		return false;
	}

	/**
	 * Find the distance to an obstacle (a wall) in the
	 * given direction relative to the robot current forward direction
	 * @param direction is the direction to check from the robot
	 * @return the number of steps to the closest obstacle, if not obstacle return Integer.MAX_VALUE
	 *
	 */
	@Override
	public int distanceToObstacle(Direction direction) throws UnsupportedOperationException {
		// Subtract Battery Power
		Battery -= 1;

		// if(hasStopped) return 0
		if(hasStopped()) {
			System.out.print("no Energy to sense!!");
			return 0;
		}

		// Any Random Positive Float Value would work as powersupply is not that used yet for ReliableRobot
		float[] ps = {131231232};

		int distance = 0;


		// Current Forward Direction
		CardinalDirection forward = Activity.getCurrentDirection();

		// If Direction is Left
		if(direction == Direction.LEFT) {
			// Get left Cardinal Direction
			CardinalDirection left = forward.rotateClockwise().rotateClockwise().rotateClockwise().oppositeDirection();



			try {
				// Use the Distance Sensor and assign that value to distance
				distance = leftSensor.distanceToObstacle(Activity.getCurrentPosition(), left, ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (direction == Direction.RIGHT) {
			CardinalDirection right = forward.rotateClockwise().oppositeDirection();
			try {
				distance = rightSensor.distanceToObstacle(Activity.getCurrentPosition(), right, ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (direction == Direction.BACKWARD) {
			CardinalDirection backward = forward.rotateClockwise().rotateClockwise();
			try {
				distance = backwardSensor.distanceToObstacle(Activity.getCurrentPosition(), backward, ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(direction == Direction.FORWARD) {
			try {
				return forwardSensor.distanceToObstacle(Activity.getCurrentPosition(), forward, ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return distance;
	}

	/**
	 * Checks if it is possible to see the exit from current position and direction
	 * @param direction is the direction to check relative to robot position
	 * @return if an exit is visible in current direction for the robot
	 */
	@Override
	public boolean canSeeThroughTheExitIntoEternity(Direction direction) throws UnsupportedOperationException {
		// Subtract Battery for Sensing
		Battery -= 1;

		if(hasStopped()) {
			System.out.print("no Energy to sense!!");
			return false;
		}

		// Any Random Positive Float Value would work as powersupply is not that used yet for ReliableRobot
		float[] ps = {132312332};

		// Subtract Battery Power
		int distance = 0;

		// Current Forward Direction
		CardinalDirection forward = Activity.getCurrentDirection();

		// If Direction is Left
		if(direction == Direction.LEFT) {
			// Get left Cardinal Direction
			CardinalDirection left = forward.rotateClockwise().rotateClockwise().rotateClockwise();


			// Set the maze of leftSensor
			try {
				// Use the Distance Sensor and assign that value to distance
				distance = leftSensor.distanceToObstacle(Activity.getCurrentPosition(), left, ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (direction == Direction.RIGHT) {
			CardinalDirection right = forward.rotateClockwise();
			try {
				distance = rightSensor.distanceToObstacle(Activity.getCurrentPosition(), right, ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (direction == Direction.BACKWARD) {
			CardinalDirection backward = forward.rotateClockwise().rotateClockwise();
			try {
				distance = backwardSensor.distanceToObstacle(Activity.getCurrentPosition(), backward, ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(direction == Direction.FORWARD) {
			try {

				distance = forwardSensor.distanceToObstacle(Activity.getCurrentPosition(), forward, ps);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// If the sensor invoked returns Integer.Max_value then we are looking through exit
		if (distance == Integer.MAX_VALUE) {
			return true;
		}

		return false;
	}
	
	/**
	 * Makes the sensor in the given direction start the failure and repair process
	 * @input direction is the direction of the sensor to start failing
	 * @input meanTimeBetweenFailures is the mean time before the sensor fails
	 * @input meanTImeToRepair is the mean time it takes to repair the sensor
	 */
	@Override
	public void startFailureAndRepairProcess(Direction direction, int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// If Statements to get the sensor in the given directions
		if(direction == Direction.FORWARD) {
			// Start the failure and repair process for that given sensor by calling that public method
			forwardSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		if(direction == Direction.LEFT) {
			leftSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		if(direction == Direction.RIGHT) {
			rightSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
		if(direction == Direction.BACKWARD) {
			backwardSensor.startFailureAndRepairProcess(meanTimeBetweenFailures, meanTimeToRepair);
		}
			
		
	}
	
	/**
	 * Makes the Sensor in the given direction stop the failure and repair process
	 */
	@Override
	public void stopFailureAndRepairProcess(Direction direction) throws UnsupportedOperationException {
		// If Statements to get the sensor in the given direction
		if(direction == Direction.FORWARD) {
			// Stop the failure and repair process for that given sensor by calling that public method
			forwardSensor.stopFailureAndRepairProcess();;
		}
		if(direction == Direction.LEFT) {
			leftSensor.stopFailureAndRepairProcess();;
		}
		if(direction == Direction.RIGHT) {
			rightSensor.stopFailureAndRepairProcess();;
		}
		if(direction == Direction.BACKWARD) {
			backwardSensor.stopFailureAndRepairProcess();;
		}

	}

}
