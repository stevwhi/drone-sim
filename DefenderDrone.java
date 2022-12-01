package droneGUI;

public class DefenderDrone extends Drone{
	
	double angle, speed;

	protected DefenderDrone(double xPos, double yPos, double angle, double speed) {
		super(xPos, yPos, 5, 'g');
		this.angle = angle;
		this.speed = speed;
	}

	@Override
	protected void checkDrone(DroneArena da) {
		angle = da.findDroneAngle(xPos, yPos, rad, angle, droneID);
		
	}

	/**
	 * adjustDrone
	 * Here, move ball depending on speed and angle
	 */
	@Override
	protected void adjustDrone() {
		double radAngle = angle*Math.PI/180;		// put angle in radians
		xPos += speed * Math.cos(radAngle);		// new X position
		yPos += speed * Math.sin(radAngle);
	}
	
	@Override
	protected void drawDrone(MyCanvas mc) {
		mc.showCircle(xPos, yPos, rad, col);
	}
	
	@Override
	protected String getStrType() {
		return "Defender Drone";
	}
	
	@Override
	protected String toStringForSave() {
		return getStrType() + " is at " + xPos + " , " + yPos 
				+ " and has speed " + speed + "and angle " + angle;

	}
	


}
