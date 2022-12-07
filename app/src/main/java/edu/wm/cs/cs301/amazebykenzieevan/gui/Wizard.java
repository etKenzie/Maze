package edu.wm.cs.cs301.amazebykenzieevan.gui;

import edu.wm.cs.cs301.amazebykenzieevan.generation.CardinalDirection;
import edu.wm.cs.cs301.amazebykenzieevan.generation.Maze;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot.Direction;
import edu.wm.cs.cs301.amazebykenzieevan.gui.Robot.Turn;

/** @author Kenzie Evan
 *
 * Class: Wizard
 * Responsibilities: Implements RobotDriver interface as a way to solve the maze
 * Collaborators: Drives and controls a robot to go through a maze.
 * 				  ReliableRobot, Control, Maze, Maze Container, 
 * 				  and CardinalDirection
 */
public class Wizard implements RobotDriver {

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
	 * Moves the robot one step closer to the exit
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
		
		// Get closer cell with getNeighbotCloserToExit

		int[] destination = this.maze.getNeighborCloserToExit(x, y);
		int dx = destination[0] - x;
		int dy = destination[1] - y;
		
		CardinalDirection destinationDirection = CardinalDirection.getDirection(dx, dy);
		
		// Adjust robot direction to head towards that cell
		while(this.robot.getCurrentDirection() != destinationDirection) {
			if (this.robot.getBatteryLevel()<=2) {
				return false;
			}
			this.robot.rotate(Turn.LEFT);
		}
		// Move the Robot 1 Step Forward
		this.robot.move(1);
		
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
