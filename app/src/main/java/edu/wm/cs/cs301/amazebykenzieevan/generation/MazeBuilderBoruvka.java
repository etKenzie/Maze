package edu.wm.cs.cs301.amazebykenzieevan.generation;

//import java.awt.Point;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.logging.Logger;




public class MazeBuilderBoruvka extends MazeBuilder implements Runnable {
	
	/**
	 * The logger is used to track execution and report issues.
	 */
	private static final Logger LOGGER = Logger.getLogger(MazeBuilderBoruvka.class.getName());

	public MazeBuilderBoruvka() {
		super();
		LOGGER.config("Using Boruvka's algorithm to generate maze.");
	}
	
	
	/**
	 * Initialize a three dimensional array edgeWeights[width][height][4] and set all values infinity
	 * 0 == North, 1 == West, 2 == East, 3 == South
	 */
	
	int [][][] edgeWeights;
	
	
	/** Function to get the weight of an edge when given parameters
	 * @param x and y coordinates, and direction wallboard is facing. 
	 * @return return a unique value based on given parameters. Make sure value would be same for the same inputs. 
	 */
	public int getEdgeWeight(int x, int y, CardinalDirection dir) {
		//set a new number dirNumber. If dir == north, dirNumber = 0, .....
		int dirNumber = 0;
		switch (dir) {
			case North:
				break;
			case West:
				dirNumber = 1;
				break;
			case East:
				dirNumber = 2;
				break;
			case South:
				dirNumber = 3;
				break;
			default:
				break;
		}
		return edgeWeights[x][y][dirNumber];
	}
	
	/**
	 *  Class that Initialize edge weights of the graph. It sets all valid values to a random positive number
	 *  while setting border values as -1
	 */
	public void initializeEdgeWeights () {
		//initialize arraylist<int> usedWeights to check if given random number has not been used.
		//This is to ensure no two weights are the same.
		ArrayList<Integer> usedWeights = new ArrayList<>();
		
		// Sets initially all values to be -2. This acts as a visited function.
		// if the value is -2 then it has not been touched. 
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				for(int dir = 0; dir<4; dir++) {
					edgeWeights[x][y][dir] = -2;
				}
			
			}
		}
		// Set all edge weights in edgeWeights array. 
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				for(int dir = 0; dir<4; dir++) {
					//Makes sure entry is not previous counterpart
					if(edgeWeights[x][y][dir] < 0) {
						int newWeight = random.nextInt(); 
						
						while(usedWeights.contains(newWeight) || newWeight<1) {
							newWeight = random.nextInt(); 
							
						}
						//Add current weight to usedWeights
						usedWeights.add(newWeight);
						
						//set that edge to newWeight
						edgeWeights[x][y][dir] = newWeight;
						
						//also set its counterpart to newWeight
						switch(dir) {
							case 0:
								if(y-1>=0) {
									edgeWeights[x][y-1][3] = newWeight;
								}
								break;
							case 1:
								if(x-1>=0) {
									edgeWeights[x-1][y][2] = newWeight;
								}
								break;
							case 2:
								if(x+1<=width-1) {
									edgeWeights[x+1][y][1] = newWeight;
								}
								break;
							case 3:
								if(y+1<=height-1) {
									edgeWeights[x][y+1][0] = newWeight;
								}
								break;
						}
					}
				}
			}
		}
		//set border numbers to be -1 as these should not be considered
		for(int x = 0; x<width; x++) {
			for(int y = 0; y<height; y++) {
				//set borderwalls to be -1
				if (x == 0) {
					edgeWeights[x][y][1] = -1;
				}
				if (y == 0) {
					edgeWeights[x][y][0] = -1;
				}
				if(y == height-1) {
					edgeWeights[x][y][3] = -1;
				}
				if (x == width-1) {
					edgeWeights[x][y][2] = -1;
				}
			}
		}
	}
	
	
	/** Create Point[] findMyComponent(Point node, Arraylist<arraylistpoint[]> components)
	 * @param a vertex node that is stored as a Point that has x&y values
	 * @return the component this node belongs too
	 */
	private ArrayList<Point> findMyComponent(Point node, ArrayList<ArrayList<Point>> components) {
		for (int index = 0; index<components.size(); index++) {
			ArrayList<Point> component = components.get(index);
			
			for (int i = 0; i<component.size(); i++) {
				if(node.equals(component.get(i))) {
					return component;
				}
			}
		}
		return null;
	}
	
	/** Create Arraylist<arraylistpoint[]> mergeComponents(arraylistpoint component1, arraylistpoint component2, Arraylist<Point[]> old)
	 * @param two Point Arrays and the initial arraylist
	 * @return a new Arraylist that has the two components merged
	 */
	private ArrayList<ArrayList<Point>> mergeComponents(ArrayList<Point> component1, ArrayList<Point> component2, ArrayList<ArrayList<Point>> old){
		//remove old components
		old.remove(component1);
		old.remove(component2);
		
		//combine previous components together
		for (int index = 0; index<component2.size(); index++) {
			component1.add(component2.get(index));
		}
		
		//add in the new mergedComponent to components arraylist
		old.add(component1);
		
		return old;
	}
	
	/** Create a method that gets the component an edge points towards
	 * given edge properties and components list
	 * @return a component an edge points towards
	 */
	private ArrayList<Point> getDestinationComponent(int x, int y, CardinalDirection dir, ArrayList<ArrayList<Point>> components) {
		Point destinationNode = new Point();
		switch (dir) {
			case North:
				destinationNode.x = x;
				destinationNode.y = y-1;
				break;
			case West:
				destinationNode.x = x-1;
				destinationNode.y = y;
				break;
			case East:
				destinationNode.x = x+1;
				destinationNode.y = y;
				break;
			case South:
				destinationNode.x = x;
				destinationNode.y = y+1;
				break;
			default:
				break;
		}
		return findMyComponent(destinationNode, components);
	}
	
	
	/** Generate Pathways for Boruvka's Algorithm generatePathways()
	 * Meant to override same function in MazeBuilder class to fit Boruvka's Algorithm
	 */
	@Override
	protected void generatePathways() {
		//Initialize a new components Arraylist that contains all nodes as single length Point arrays
		//This will be our MST
		ArrayList<ArrayList<Point>> components = new ArrayList<>();
		
		edgeWeights = new int[width][height][4];
		
		//Call initializeEdgeWeights() to set all edge weights in the graph
		initializeEdgeWeights();
		
		//Run through all points in maze and initialize each as a new component.
		for (int x = 0; x<width; x++) {
			for (int y = 0; y<height; y++) {
				Point cell = new Point(x,y);
				ArrayList<Point> component = new ArrayList<>();
				component.add(cell);
				
				components.add(component);
			}
		}
		
		//While components.length>1 (there isnt only one component where all nodes can reach the other
		while(components.size()>1) {
			ArrayList<Wallboard> connectingEdges = new ArrayList<>(); // this represents an empty set of edges S
			// Makes sure if two edges are equal they are not both added to connecting edges
			//this arraylist contains all previous edge weights that have been used
			ArrayList<Integer> previousEdges = new ArrayList<>(); 
			
			// for each component in components
			for (int index = 0; index < components.size(); index ++) {
				ArrayList<Point> component = components.get(index);
				int smallest = 2147483647;
				
				//for each point node in that component
				for (int i = 0; i<component.size(); i++) {
					Point node = component.get(i);
					int curx = 0;
					int cury = 0;
					CardinalDirection curDirection = CardinalDirection.North;
					
					for (int direction = 0; direction < 4; direction++) {
						//find the cheapest edge that connects another component
						CardinalDirection[] listOfDirections = {CardinalDirection.North, CardinalDirection.West, 
								CardinalDirection.East, CardinalDirection.South};
						int weight = getEdgeWeight(node.x, node.y, listOfDirections[direction]);
						
						//if not a border weight
						if(weight > 0) {
							//if weight is smaller than smallest
							if(weight<smallest) {
								//if the component the edge points to is not part of the same component
								if(!findMyComponent(node, components).equals(getDestinationComponent(node.x, node.y, listOfDirections[direction], components))) {
									smallest = weight;
									curx = node.x;
									cury = node.y;
									curDirection = listOfDirections[direction];
								}
							}
						}
					}
					//Add that edge to connectingEdges as a wallboard
					Wallboard newEdge = new Wallboard(curx, cury, curDirection);
					
					//Make sure no duplicate edges are added to connectingEdges
					if(!previousEdges.contains(getEdgeWeight(curx, cury, curDirection))) {
						connectingEdges.add(newEdge);
					}
					previousEdges.add(getEdgeWeight(curx, cury, curDirection));
		
				}
			}
			
			//while there is a wallboard in connectingEdges
			while(connectingEdges.size() > 0) {
				Wallboard wallToBreak = connectingEdges.get(0);
				
				// Breakdown the wall between these edges by floorplan.deletewallboard()
				if (floorplan.canTearDown(wallToBreak)) {
					floorplan.deleteWallboard(wallToBreak);
					
					// merge the components that are connected as a result of deleting the wall
					Point curNode = new Point(wallToBreak.getX(),wallToBreak.getY());
					

					ArrayList<Point> curComponent = findMyComponent(curNode, components);
					ArrayList<Point> destComponent = getDestinationComponent(wallToBreak.getX(),wallToBreak.getY(), wallToBreak.getDirection(), components);
					
					mergeComponents(curComponent, destComponent, components);
				}
				
				// remove the wallboard from connectingEdges
				connectingEdges.remove(0);
			}
		}
	}
}
