package droneGUI;

/**
 * @author Steven Whitby
 * class for Target Drone
 */
public class TargetDrone extends Drone {

	/**
	 * construct target drone at position xPos, yPos
	 * @param xPos		x-coordinate of drone
	 * @param yPos		y-coordinate of drone
	 * @param da		DroneArena
	 */
	public TargetDrone(double xPos, double yPos) {
		super(xPos, yPos, 15, 'b');
	}

	/**
	 * checkDrone - change angle of travel if hitting wall or another drone, redundant for TargetBall
	 * @param da   droneArena
	 */
	@Override
	protected void checkDrone(DroneArena da) {}

	/**
	 * adjustDrone
	 * Here, move ball depending on speed and angle, redundant for TagetBall
	 */
	@Override
	protected void adjustDrone(double speed) {}
	
	/**
	 * return string defining drone type
	 */
	@Override
	protected String getStrType() {
		return "Target Drone";
	}
	
	/**
	 * return string defining drone parameters
	 */
	@Override
	protected String toStringForSave() {
		return getStrType() + " is at " + xPos + " , " + yPos; 
	}

}
