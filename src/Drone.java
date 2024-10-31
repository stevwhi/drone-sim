package droneGUI;

/**
 * @author Steven Whitby
 * Abstract class for drone type
 */
public abstract class Drone{
	
	//drone parameters
	protected double xPos, yPos, rad;
	protected char col;
	
	//unique drone identifier
	static int droneCount = 0;
	protected int droneID;
	
	/**
	 * construct a drone at xPos,yPos of size pi*rad^2 and colour col
	 * @param xPos		x-cooridinate of drone
	 * @param yPos		y-cooridinate of drone
	 * @param rad		radius of drone
	 * @param col		colour of drone
	 * @param da 		DroneArena
	 */
	protected Drone(double xPos, double yPos, double rad, char col) {
		droneID = droneCount++;		//increment ID each instantiation to create a unique identifier
		this.xPos = xPos;
		this.yPos = yPos;
		this.rad = rad;
		this.col = col;
		
		
	}
	
	//functions --------------------------------------------------------
	/**
	 * is drone at xPos,yPos size or hitting this drone
	 * @param ox
	 * @param oy
	 * @param or
	 * @return true if hitting
	 */
	public boolean hitting(double xPos, double yPos, double rad) {
		return (xPos-this.xPos)*(xPos-this.xPos) + (yPos-this.yPos)*(yPos-this.yPos) < (rad+this.rad)*(rad+this.rad);
		// hitting if a^2 = b^2 < rad^2
	}		
	
	/** 
	 * return string describing ball
	 */
	public String toStringforDisplay() {
		return getStrType()+" at "+Math.round(xPos)+", "+Math.round(yPos);
	}
	
	/**
	 * draw a ball into the interface bi
	 * @param bi
	 */
	public void drawDrone(MyCanvas mc) {
		mc.showCircle(xPos, yPos, rad, col);
	}
	
	//abstract functions-----------------------------------------------
	
	/**
	 * abstract method for checking a drone in arena da
	 * @param da 	arena the drone is in
	 */
	protected abstract void checkDrone(DroneArena da);
	
	/**
	 * abstract method for adjusting a ball
	 * @param speed			speed of adjustment
	 */
	protected abstract void adjustDrone(double speed);
	
	/**
	 * abstract method for returning drone type
	 * @return 		type of drone
	 */
	protected abstract String getStrType();
	
	/**
	 * abstract method for returning drone parameter string
	 * @return 		string containing drone parameters
	 */
	protected abstract String toStringForSave();
	
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
	 * return radius of drone
	 * @return
	 */
	public double getRad() { return rad; }
	/**
	 * return the identity of drone
	 * @return
	 */
	public int getID() {return droneID; }
	
	public void setXY(double x, double y) {
		this.xPos = x;
		this.yPos = y;
	}

	
}
