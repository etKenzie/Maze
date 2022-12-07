package edu.wm.cs.cs301.amazebykenzieevan.gui;

import edu.wm.cs.cs301.amazebykenzieevan.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot.Turn;

/** @author Kenzie Evan
 *
 * Class: SmartWizard
 * Responsibilities: Implements RobotDriver interface as a way to solve the maze but added jump functionality
 * Collaborators: Drives and controls a robot to go through a maze.
 * 				  ReliableRobot, Control, Maze, Maze Container, 
 * 				  and CardinalDirection
 * 
 * This Version of Wizard goes looks at all direction and goes to the cell that has the lowest distance to exit even if there is 
 * a wall by jumping over it. 
 */
public class SmartWizard implements RobotDriver {

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
	 * Moves the robot one step closer to the exit.
	 * and at the exit position turns the robot to face the exit
	 * @return true if robot moves from current location to adjacent cell, false otherwise
	 * 
	 */
	@Override
	public boolean drive1Step2Exit() throws Exception {
		//Check Robot Battery
		if (this.robot.getBatteryLevel()<=5) {
			return false;
		}
		// Initialize coordinates and current direction
		int x = this.robot.getCurrentPosition()[0];
		int y = this.robot.getCurrentPosition()[1];
		
		// Get distance to exit of cells around robot current postion
		int up = Integer.MAX_VALUE;
		int left = Integer.MAX_VALUE;
		int right = Integer.MAX_VALUE;
		int down = Integer.MAX_VALUE;
		
		// Make sure that it is a valid position to check get distance to exit
		if(maze.isValidPosition(x, y+1)) {
			up = this.maze.getDistanceToExit(x, y+1); 
		}
		
		if(maze.isValidPosition(x-1, y)) {
			left = this.maze.getDistanceToExit(x-1, y); 
		}
		
		if(maze.isValidPosition(x+1, y)) {
			right = this.maze.getDistanceToExit(x+1, y); 
		}
		if(maze.isValidPosition(x, y-1)) {
			down = this.maze.getDistanceToExit(x, y-1);
		}
		
		// Get the Lowest Value of each direction
		int lowest1 = Math.min(up, left);
		int lowest2 = Math.min(lowest1, right);
		int lowest3 = Math.min(lowest2, down);
		
		CardinalDirection destinationDirection = CardinalDirection.North;
		
		// Set the destination Direction to the lowest getDistanceToExit
		if (lowest3 == up) {
			destinationDirection = CardinalDirection.South;
		}
		else if (lowest3 == left) {
			destinationDirection = CardinalDirection.West;
		}
		else if (lowest3 == right) {
			destinationDirection = CardinalDirection.East;
		}
		else if (lowest3 == down) {
			destinationDirection = CardinalDirection.North;
		}
		
		// Get closer cell with getNeighbotCloserToExit. This one does not include direction with walls
		int[] destination = this.maze.getNeighborCloserToExit(x, y);
		int dx = destination[0] - x;
		int dy = destination[1] - y;
		
		int jumpCost = 40;
		
		/*
		 *  WalkCost is the difference of getDistanceToExit by jumping vs simply walking through the maze
		 *  to that point. Also since walking through the maze would actually cause a number of more rotations
		 *  I decide to multiply that difference by 12 instead of what simply moving forward would cost which is 6.
		 *  This is means on average for every move there will be two rotations as rotating twice would cost 6 energy as well.
		 */
		int walkCost = (this.maze.getDistanceToExit(destination[0], destination[1]) - lowest3)*12;
		
		if (walkCost<jumpCost) {
			destinationDirection = CardinalDirection.getDirection(dx, dy);
		}
		
		// Adjust robot direction to head towards that cell
		while(this.robot.getCurrentDirection() != destinationDirection) {
			if (this.robot.getBatteryLevel()<=2) {
				return false;
			}
			
			this.robot.rotate(Turn.LEFT);
		}
		
		// Make the Robot Jump. This robot just walks instead if there is no wall in the robot given forward direction
		this.robot.jump();
		
		return true;
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
