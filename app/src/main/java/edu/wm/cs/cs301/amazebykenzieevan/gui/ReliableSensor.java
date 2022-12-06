package edu.wm.cs.cs301.amazebykenzieevan.gui;

//import generation.CardinalDirection;

import edu.wm.cs.cs301.amazebykenzieevan.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot.Direction;

/** @author Kenzie Evan
 * 
 * Class: ReliableSensor
 * Responsibilities: Implements DistanceSensor Interface to create and use sensors
 * Collaborators: Used by a Robot to get distance. Uses Control, Cardinal Direction, 
 * 				  Floorplan and Maze
 */
public class ReliableSensor implements DistanceSensor {
	/**
	 * Initialize Robot to use
	 * Initialize Maze to use
	 * Initialize Direction of the sensor
	 */
	Maze maze;
	Direction sensorDirection;
	
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
		
		
		//decrease power supply by 1
		powersupply[0] -= 1;
		
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
		// TODO Auto-generated method stub
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
	
	@Override
	public void startFailureAndRepairProcess(int meanTimeBetweenFailures, int meanTimeToRepair)
			throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopFailureAndRepairProcess() throws UnsupportedOperationException {
		// TODO Auto-generated method stub
		
	}

}
