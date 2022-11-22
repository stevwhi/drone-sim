package droneGUI;

import java.util.ArrayList;

public class DroneArena{
	double xDim, yDim;
	private ArrayList<Drone> allDrones;
	
	
	public DroneArena(double xDim, double yDim) {
		//parameters
		
		this.xDim = xDim;
		this.yDim = yDim;
		
		//add drone types to arraylist
		allDrones = new ArrayList<>();
		
		//add 1 goal drone, 1 blue drone, 1 red drone immediately
	}
	
	public void moveAllDrones() {
		for(Drone d: allDrones) d.tryToMove(this);
	}
	
	/**
	 * draw all balls in the arena into interface bi
	 * @param bi
	 */
	public void drawArena(MyCanvas mc) {
		for(Drone d: allDrones) d.drawDrone(mc);
	}
	
	public void addDrone(String s) {
		//if s is x type of drone -> put it in random position in certain sector
		
		
		allDrones.add(new Drone(10, 60));
	}
	
	
	
	
	public double findPX(Drone d) {
		//work out proposed x
		
		double dub = 0;
		
		return dub;
	}
	
	public double findPY(Drone d) {
		//work out proposed y
		double dub = 0;
		
		
		return dub;
	}
	
	
	public boolean canMoveHere(Drone d) {
		//findPY, find PX
		
		//if pPos touches edge -> return false
		
		//if pPos touches drone -> return false
		
		
		return true;
	}
	
	//getters/setters------------------------------------------------
	/**
	 * return arena size in x direction
	 * @return
	 */
	public double getXSize() {
		return xDim;
	}
	/**
	 * return arena size in y direction
	 * @return
	 */
	public double getYSize() {
		return yDim;
	}
}
