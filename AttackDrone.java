package droneGUI;

/**
 * @author Steven Whitby
 * class for Attacker Drone
 */
public class AttackDrone extends Drone{

	double angle, speed;		//movement parameters
	
	/** Create Attack Drone, size rad at xPos,yPos, moving at angle: angle and speed: speed 
	 * @param xPos		x-coordinate of drone
	 * @param yPos		y-coordinate of drone
	 * @param da		DroneArena
	 * @param angle		angle of travel
	 * @param speed		speed of travel
	 */
	protected AttackDrone(double xPos, double yPos, double angle, double speed) {
		super(xPos, yPos, 5, 'r');
		this.angle = angle;
		this.speed = speed;
	}

	/**
	 * checkDrone - change angle of travel if hitting wall or another drone
	 * @param da   DroneArena
	 */
	@Override
	protected void checkDrone(DroneArena da) {
		angle = da.findDroneAngle(xPos, yPos, rad, angle, droneID, this);
	}

	/**
	 * adjustDrone
	 * Here, move drone depending on speed and angle
	 */
	@Override
	protected void adjustDrone(double speed) {
		double radAngle = angle*Math.PI/180;		// put angle in radians
		xPos += speed * Math.cos(radAngle);		// new X position
		yPos += speed * Math.sin(radAngle);
	}

	/**
	 * return string defining drone type
	 */
	@Override
	protected String getStrType() {
		return "Attack Drone";
	}
	
	/**
	 * return string defining drone parameters
	 */
	@Override
	protected String toStringForSave() {
		return getStrType() + " is at " + xPos + " , " + yPos + " and has angle " + angle;
	}
}
