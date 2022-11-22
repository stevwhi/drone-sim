package droneGUI;



public class Drone{
	protected double xPos, yPos, rad;
	protected char col;
	static int droneCount = 0;
	protected int droneID;
	
	protected Drone(double xPos, double yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rad = 5; //when we have multiple types of drones, leave these as uninitialized here
		this.col = 'r'; 
		
		droneID = droneCount++;
	}
	
	public void drawDrone(MyCanvas mc) {
		mc.showCircle(xPos, yPos, rad, col);
	}
	
	public void tryToMove(DroneArena da) {
		//if da.canMoveHere -> move in current direction
		//else -> change its direction
	}
	
	
	//getters/setters--------------------------------------------------
	
	/**
	 * return x position
	 * @return
	 */
	public double getXPos() { return xPos; }
	/**
	 * return y position
	 * @return
	 */
	public double getYPos() { return yPos; }
	/**
	 * return radius of ball
	 * @return
	 */
	public double getRad() { return rad; }
	/**
	 * return the identity of ball
	 * @return
	 */
	public int getID() {return droneID; }
	
	/** 
	 * set the ball at position nx,ny
	 * @param nx
	 * @param ny
	 */
	public void setXY(double xPos, double yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

}
