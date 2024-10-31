package droneGUI;

public class UserDrone extends Drone{

	protected UserDrone(double xPos, double yPos) {
		super(xPos, yPos, 12, 'x');
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void checkDrone(DroneArena da) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void adjustDrone(double speed) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getStrType() {
		return "User Drone";
	}

	/**
	 * return string defining drone parameters
	 */
	@Override
	protected String toStringForSave() {
		return getStrType() + " is at " + xPos + " , " + yPos;
	}
	
	

}
