package droneGUI;

public class TargetDrone extends Drone {

	
	
	
	public TargetDrone(double xPos, double yPos) {
		super(xPos, yPos, 30, 'r');
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	
	
	@Override
	protected void checkDrone(DroneArena da) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	@Override
	protected void adjustDrone() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void drawDrone(MyCanvas mc) {
		mc.showCircle(xPos, yPos, rad, col);
	}


}
