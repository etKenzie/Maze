package edu.wm.cs.cs301.amazebykenzieevan.gui;


import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot.Turn;

/** @author Kenzie Evan
*
* Class: WallFollower
* Responsibilities: Implements RobotDriver interface as a way to solve the maze. This one follows a wall to the exit
* Collaborators: Drives and controls a robot to go through a maze.
* 				 ReliableRobot, Control, Maze, Maze Container, 
* 				 and CardinalDirection
* 
* Set and Get methods are the same as Wizard so those are copied into this implementation. 
* Also, Drive2Exit Function should be the same as Wizard. Drive1Step2Exit is where WallFollower differs
*/
public class WallFollower implements RobotDriver{
	/**
	 * Initializes Robot and Maze to use in this sensor and InitialEnergy
	 */
	Robot robot;
	Maze maze;
	float initialEnergy;
	
	/**
	 * Initialize robot to operate
	 * @param r robot to operate
	 */
	@Override
	public void setRobot(Robot r) {
		// TODO Auto-generated method stub
		this.robot = r;
		this.initialEnergy = this.robot.getBatteryLevel();
	}

	/**
	 * Initialize maze object to go through 
	 * @param maze object to initialize
	 */
	@Override
	public void setMaze(Maze maze) {
		this.maze = maze;
		
	}

	/**
	 * Function that drives the robot to the exit
	 * @return true if robot reaches and directed at the exit, False otherwise
	 * 
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		// While robot still has energy and not yet at exit
		while(this.robot.isAtExit()==false) {
		// return false if robot run out of energy
			if(this.robot.getBatteryLevel()<0 || this.robot.hasStopped() == true) {
				return false;
			}
			this.drive1Step2Exit();
		}
		//return true here as if completes while loop it is at exit
		// If Robot is at exit Position
		if(this.robot.isAtExit()) {
			// While robot is not facing exit position
			while(this.robot.canSeeThroughTheExitIntoEternity(Direction.FORWARD)==false){
				// rotate robot
				this.robot.rotate(Turn.LEFT);
			}
			// Move robot through the exit
			this.robot.move(1);
		}
		return true;
	}

	/**
	 * Implementation of WallFollower. This method is where wallFollower uses its methods to check distance 
	 * and move towards the exit
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		// Initialize boolean moved to determine if robot has moved
		boolean moved = false;
		
		// while has not moved
		while(moved == false && robot.hasStopped()==false) {
			/*
			 *  Initialize integer forwardWallDistance and leftWallDistance based on robot distance sensors
			 *  There is a wall in that direction if these integers is 0. And if it returns -1 the sensor is not working
			 *  
			 *  If the left sensor does not work rotate right until find a working sensor. Do this with a while loop
			 *  while keeping count of the amount of rotations to help rotate back optimally later. Once a working sensor
			 *  is found use the sensor in that direction to set leftWallDistance. Same thing if forward sensor
			 *  is not working. After setting the integer values rotate back to original Direction. Additionally if robot
			 *  has rotated more than 3 times and all sensors is unavailable wait until there the original direction sensor
			 *  is working again. 
			 */
			 
			int forwardWallDistance = this.robot.distanceToObstacle(Direction.FORWARD);
			int leftWallDistance = this.robot.distanceToObstacle(Direction.LEFT);
			
			
			
			// Used to check how many turns the robot has made and Direction Array to adjust which sensor to use
			int leftCounter = 0;
			Direction[] directionArrayLeft = {Direction.FORWARD, Direction.RIGHT, Direction.BACKWARD, Direction.LEFT};
			
			// If initially the left sensor is not working
			if(leftWallDistance==-1) {
				// Meaning that the sensor is not working and robot has not rotated more than 3 times
				while (leftWallDistance == -1 && leftCounter < 4) {
					robot.rotate(Turn.LEFT);
					leftWallDistance = this.robot.distanceToObstacle(directionArrayLeft[leftCounter]);
					leftCounter++;
				}
				
				// This will turn the robot back to its original direction as when leftCounter == 3 the robot has rotated 360 Degrees
				while(leftCounter!= 4) {
					robot.rotate(Turn.LEFT);
					leftCounter++;
				}
				// This is the scenario where all sensors are still not working. Wait until Left sensor is working again
				while(leftWallDistance == -1) {
					leftWallDistance = this.robot.distanceToObstacle(Direction.LEFT);
					Thread.sleep(500);
				}
			}
			
			// if initially the forward sensor is not working
			if(forwardWallDistance==-1) {
				// Used to check how many turns the robot has made and Direction Array to adjust which sensor to use
				int forwardCounter = 0;
				Direction[] directionArrayForward = {Direction.RIGHT, Direction.BACKWARD, Direction.LEFT, Direction.FORWARD};
				
				// Meaning that the sensor is not working and robot has not rotated more than 3 times
				while (forwardWallDistance == -1 && forwardCounter < 4) {
					robot.rotate(Turn.LEFT);
					forwardWallDistance = this.robot.distanceToObstacle(directionArrayForward[forwardCounter]);
					forwardCounter++;
				}
				
				// This will turn the robot back to its original direction as when leftCounter == 3 the robot has rotated 360 Degrees
				while(forwardCounter != 4) {
					robot.rotate(Turn.LEFT);
					forwardCounter++;
				}
				
				// This is the scenario where all sensors are still not working. Wait until Left sensor is working again
				while(forwardWallDistance == -1) {
					forwardWallDistance = this.robot.distanceToObstacle(Direction.LEFT);
					Thread.sleep(500);
				}
			}
			
			// If there is no wall on the left
			if(leftWallDistance != 0) {
				// rotate left, move forward 1 step, and set moved to true
				robot.rotate(Turn.LEFT);
				robot.move(1);
				moved = true;
			}
			// else if there is no wall in front
			else if (forwardWallDistance != 0){
				// Step Forward and set moved to true
				robot.move(1);
				moved = true;
			}
			else {
				robot.rotate(Turn.RIGHT);
			}
		}
		
		return moved;
	}

	/**
	 * Value of total energy spent to go through the maze
	 * @return float value of energy used throughout the maze
	 */
	@Override
	public float getEnergyConsumption() {
		// Subtract Starting energy level by energy level at the end
		return initialEnergy - this.robot.getBatteryLevel();
	}
	
	/**
	 * Function that gets the number of cells traversed when called
	 * @return the total length of journey when called
	 */
	@Override
	public int getPathLength() {
		// Get current odometer reading.
		return this.robot.getOdometerReading();
	}

}
