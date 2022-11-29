package droneGUI;

public class AttackDrone extends Drone{

	double angle, speed;
	//allow user to enter speed at interface
	
	/** Create game ball, size rad at xPos,yPos, moving at angle: angle and speed: speed 
	 * @param xPos
	 * @param yPos
	 * @param rad
	 * @param ia
	 * @param is
	 */
	protected AttackDrone(double xPos, double yPos, double angle, double speed) {
		super(xPos, yPos, 5, 'r');
		this.angle = angle;
		this.speed = speed;
		
		
	}

	/**
	 * checkball - change angle of travel if hitting wall or another ball
	 * @param b   ballArena
	 */
	@Override
	protected void checkDrone(DroneArena da) {
		angle = da.findDroneAngle(xPos, yPos, rad, angle, droneID);
		
	}

	/**
	 * adjustBall
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
	

}
