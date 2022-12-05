package droneGUI;

import java.util.ArrayList;
import java.util.Random;

public class DroneArena{
	double xDim, yDim;
	private ArrayList<Drone> allDrones;
	ArrayList<String> droneStrings;
	
	private Random ranGen = new Random();
	
	private double speed = 15; //make this customisable
	
	
	
	public DroneArena(double xDim, double yDim, double speed) {
		//parameters
		
		this.xDim = xDim;
		this.yDim = yDim;
		this.speed = speed;
		
		
		
		//add drone types to arraylist
		allDrones = new ArrayList<>();
		
		//add 1 goal drone, 1 blue drone, 1 red drone immediately
		allDrones.add(new AttackDrone(ranGen.nextDouble(getXSize()), ranGen.nextDouble(getYSize()), ranGen.nextDouble(360), speed));
		allDrones.add(new DefenderDrone(ranGen.nextDouble(getXSize()), ranGen.nextDouble(getYSize()), ranGen.nextDouble(360), speed));
		allDrones.add(new TargetDrone(ranGen.nextDouble(getXSize()), ranGen.nextDouble(getYSize())));
		
	}
	
	
	//functions------------------------------------------------------------------
	public void removeLastDrone() {
		allDrones.remove(allDrones.size()-1);
	}
	
	
	public void removeAllDrones() {
		allDrones.clear();
	}
	
	public void updateAllDrones() {
		for(Drone d: allDrones) d.tryToMove(this);
	}
	
	/**
	 * draw all drones in the arena into interface bi
	 * @param bi
	 */
	public void drawArena(MyCanvas mc) {
		
		for(Drone d: allDrones) d.drawDrone(mc);
		
	}
		
		
	//not same id or position + cant find errors with draw world
	public void addAttackDrone(double xPos, double yPos, double angle) {
		allDrones.add(new AttackDrone(xPos, yPos, angle, speed));
	}
	
	public void addDefenderDrone(double xPos, double yPos, double angle) {
		allDrones.add(new DefenderDrone(xPos, yPos, angle, speed));
	}
	
	public void addTargetDrone(double xPos, double yPos) {
		allDrones.add(new TargetDrone(xPos, yPos));
	}
	
	/**
	 * check all drones .. see if need to change angle of moving drones, etc 
	 */
	public void checkDrones() {
		
		for (Drone d : allDrones) d.checkDrone(this);// check all drones
		
		
	}
	/**
	 * adjust all balls .. move any moving ones
	 */
	public void adjustDrones() {
		for (Drone d : allDrones) {
			d.adjustDrone();
		}
	}
	
	
	/** 
	 * Check angle of ball ... if hitting wall, rebound; if hitting drone, change angle
	 * @param x				ball x position
	 * @param y				y
	 * @param rad			radius
	 * @param ang			current angle
	 * @param notID			identify of ball not to be checked
	 * @return				new angle 
	 */
	public double findDroneAngle(double xPos, double yPos, double rad, double angle, int notID) {
		double ans = angle;
		if (xPos <= rad || xPos >= xDim - rad) ans = 180 - ans;
			// if Drone hit (tried to go through) left or right walls, set mirror angle, being 180-angle
		if (yPos <= rad || yPos >= yDim - rad) ans = - ans;
			// if try to go off top or bottom, set mirror angle
		
		for (Drone d : allDrones) 
			if (d.getID() != notID && d.hitting(xPos, yPos, rad)) ans = 180*Math.atan2(yPos-d.getYPos(), xPos-d.getXPos())/Math.PI; //dont we need to set angle mirrored??
				// check all balls except one with given id
				// if hitting, return angle between the other ball and this one.
		
		return ans;		// return the angle
	}
	
	/**
	 * return list of strings defining each ball
	 * @return	drone position descriptions
	 */
	public ArrayList<String> describeAll() {
		droneStrings = new ArrayList<>();
		for (Drone d : allDrones) droneStrings.add(d.toStringforDisplay());			// add string defining each ball
		return droneStrings;												// return string list
	}
	
	
	@Override
	public String toString() {
		String aSize = "Arena size is " + xDim + " by " + yDim + " and has a speed level of " + speed;
		String dPos = "";
		
		if(allDrones.isEmpty()){
			dPos = "No Drones exist \n";
		}
		else {
			for(int i = 0; i < allDrones.size(); i++) {
				dPos += allDrones.get(i).toStringForSave() + "\n";
			}			
		}
		
		return aSize + "\n" + dPos;
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
