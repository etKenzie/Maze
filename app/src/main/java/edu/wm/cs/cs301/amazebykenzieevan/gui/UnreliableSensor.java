package edu.wm.cs.cs301.amazebykenzieevan.gui;

//import generation.CardinalDirection;

import edu.wm.cs.cs301.amazebykenzieevan.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot.Direction;

/**
 * @author Kenzie Evan
 *
 * Class: UnreliableSensor
 * Responsibilities: Creates a sensor that is Unreliable for the robot to use. 
 * Collaborators: Mounted to a robot to be used to sense Distance in given direction. 
 * 				  Uses a Maze object and Direction to continuously check Direction.
 * 
 * 
 * Distance Sensing, Set Methods, and Cost of Sensing is same as Reliable Sensor so that is brought over here.
 * While Adding additional constraints. 
 */
public class UnreliableSensor implements DistanceSensor, Runnable {
	/**
	 * Initialize Robot to use
	 * Initialize Maze to use
	 * Initialize Direction of the sensor
	 */
	Maze maze;
	Direction sensorDirection;
	
	/**
	 * Initialize a boolean "runningThread" that tells if the thread failure and repair process should be running or not
	 * Initialize a boolean "working" that tells if sensor is currently running or not
	 * Initialize time variables to use for repair and fail. 
	 * "timeToRepair"
	 * "timeTillFailure"
	 * 
	 */
	boolean runningThread;
	boolean working = true;
	
	int timeToRepair;
	int timeTillFailure;
	
	
	/**
	 * A class that calculates the distance from a robot to an obstacle
	 * @return the distance from a robot to an obstacle in a given direction 
	 */
	@Override
	public int distanceToObstacle(int[] currentPosition, CardinalDirection currentDirection, float[] powersupply)
			throws Exception {
		// check Power supply is greater than 0
		if(powersupply[0] <= 0) {
			throw new IndexOutOfBoundsException();
		}
		
		//decrease power supply by 1
		powersupply[0] -= 1;
		
		/*
		 *  This is going to be how the program determines if the sensor is working. If the robots call to distance to obstacle
		 *  returns -1 that means the sensor is not working.
		 */
	
		if(this.working == false) {
			return -1;
		}
		
		//Initialize Maze object to use
		Maze currentMaze = this.maze; 
		
		//Initialize distance = 0
		int distance = 0; 
		
		// X and Y as current position 
		int x = currentPosition[0];
		int y = currentPosition[1];
		
		// Get Direction of robot
		int[] curDirection = currentDirection.getDxDyDirection();
		int dx = curDirection[0];
		int dy = curDirection[1];
		
		
		
		
		
		//While at position x and y in the given direction does not have a wall
		while(currentMaze.hasWall(x, y, currentDirection) == false) {	
			
			//go to next cell in given direction
			x += dx;
			y += dy;
			
			// Check that New Position is Valid. Need To check here as while loop gives error if not valid position
			// Also if not a valid position it means that we have gone beyond the maze through the exit Position
			if(currentMaze.isValidPosition(x, y) == false) {
				return Integer.MAX_VALUE;
			}
			//Increment distance
			distance++;
		}
			
		//return distance
		return distance;
	}

	/**
	 * Sets the maze to use for this game
	 */
	@Override
	public void setMaze(Maze maze) {
		this.maze = maze;
	}
	
	/**
	 * Sets the angle the sensor is directed towards relative to the robot current direction
	 */
	@Override
	public void setSensorDirection(Direction mountedDirection) {
		// change Direction of sensor object
		this.sensorDirection = mountedDirection;
	}
	
	/**
	 * Gets the amount of energy used for sensing once
	 */
	@Override
	public float getEnergyConsumptionForSensing() {
		// Should just return 1
		return 1;
	}

	/**
	 * Starts a thread while adjusting setting the timeToRepair and timeToFailure from given inputs
	 * @input meanTimeBetweenFailures is the mean 
	 * time before the sensor fails
	 * @input meanTImeToRepair is the mean time it takes to repair the sensor
	 */
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// Multiply Input times by 1000 as threads go in milliseconds and sets timeToRepair and timeTillFailure to those values
		timeToRepair = meanTimeToRepair*1000;
		timeTillFailure = meanTimeBetweenFailures*1000;
		
		this.runningThread = true;
		this.working = true;
		
		// Initialize and start a thread
		Thread thread = new Thread(this);
		
		thread.start();
		
	}
	
	/**
	 * Stops the failure and repair process and sets the boolean working to true and boolean threadRunning to False. 
	 */
	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		this.runningThread = false;
		this.working = true;
	}
	/**
	 * This contains the code to be executed in the thread. This is the part that implements Runnable
	 */
	@Override
	public void run() {
		// While runningThread
		while (runningThread) {
			try {
				// Sets working to false and Makes the thread sleep for timeToRepair
				this.working = false;
				Thread.sleep(timeToRepair);
				
				// Sets working to true and and wait for the amount of time until failure
				this.working = true;
				Thread.sleep(timeTillFailure);
				
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
		
	}
	


}
