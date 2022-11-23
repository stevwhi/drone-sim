package droneGUI;



public abstract class Drone{
	protected double xPos, yPos, rad;
	protected char col;
	static int droneCount = 0;
	protected int droneID;
	
	protected Drone(double xPos, double yPos, double rad, char col) {
		droneID = droneCount++;
	}
	
	//functions --------------------------------------------------------
	

	public void tryToMove(DroneArena da) {
		//if da.canMoveHere -> move in current direction
		//else -> change its direction
	}
	
	/**
	 * is ball at ox,oy size or hitting this ball
	 * @param ox
	 * @param oy
	 * @param or
	 * @return true if hitting
	 */
	public boolean hitting(double xPos, double yPos, double rad) {
		return (xPos-this.xPos)*(xPos-this.xPos) + (yPos-this.yPos)*(yPos-this.yPos) 
				< (rad+this.rad)*(rad+this.rad);
	}		// hitting if a^2 = b^2 < rad^2
	
	//abstract functions-----------------------------------------------
	protected abstract void checkDrone(DroneArena da);
	
	protected abstract void adjustDrone();
	
	protected abstract void drawDrone(MyCanvas mc);
	
	
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
	 * return the identity of ball
	 * @return
	 */
	public char getCol() {return col; }
	
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
