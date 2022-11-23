package droneGUI;

import java.util.ArrayList;
import java.util.Random;

public class DroneArena{
	double xDim, yDim;
	private ArrayList<Drone> allDrones;
	private Random ranGen = new Random();
	
	
	public DroneArena(double xDim, double yDim) {
		//parameters
		
		this.xDim = xDim;
		this.yDim = yDim;
		
		//add drone types to arraylist
		allDrones = new ArrayList<>();
		
		//add 1 goal drone, 1 blue drone, 1 red drone immediately
		allDrones.add(new AttackDrone(ranGen.nextDouble(xDim), ranGen.nextDouble(yDim), ranGen.nextDouble(360), 15));
		allDrones.add(new DefenderDrone(ranGen.nextDouble(xDim), ranGen.nextDouble(yDim), 0, 15));
		allDrones.add(new TargetDrone(ranGen.nextDouble(xDim), ranGen.nextDouble(yDim)));
	}
	
	
	//functions------------------------------------------------------------------
	
	public void updateAllDrones() {
		for(Drone d: allDrones) d.tryToMove(this);
	}
	
	/**
	 * draw all drones in the arena into interface bi
	 * @param bi
	 */
	public void drawArena(MyCanvas mc) {
		for(Drone d: allDrones) {
			System.out.println(d.getID());
			d.drawDrone(mc);
		}
	}
		
		
	//not same id or position + cant find errors with draw world
	public void addAttackDrone(double speed) {
		double x = ranGen.nextDouble(xDim);
		double y = ranGen.nextDouble(yDim);
		
		allDrones.add(new AttackDrone(x, y, ranGen.nextDouble(360), speed));
		System.out.println("attacker");
		System.out.println(x);
		System.out.println(y);
	}
	
	public void addDefenderDrone(double speed) {
		double x = ranGen.nextDouble(xDim);
		double y = ranGen.nextDouble(yDim);
		
		allDrones.add(new DefenderDrone(x, y, 0, speed));
		System.out.println("defender");
		System.out.println(x);
		System.out.println(y);
	}
	
	public void addTargetDrone() {
		allDrones.add(new TargetDrone(ranGen.nextDouble(xDim), ranGen.nextDouble(yDim)));
	}
	
	/**
	 * check all drones .. see if need to change angle of moving drones, etc 
	 */
	public void checkDrones() {
		for (Drone d : allDrones) d.checkDrone(this);	// check all balls
	}
	/**
	 * adjust all balls .. move any moving ones
	 */
	public void adjustDrones() {
		for (Drone d : allDrones) d.adjustDrone();
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
		if (xPos < rad || xPos > xDim - rad) ans = 180 - ans;
			// if Drone hit (tried to go through) left or right walls, set mirror angle, being 180-angle
		if (yPos < rad || yPos > yDim - rad) ans = - ans;
			// if try to go off top or bottom, set mirror angle
		
		for (Drone d : allDrones) 
			if (d.getID() != notID && d.hitting(xPos, yPos, rad)) ans = 180*Math.atan2(yPos-d.getYPos(), xPos-d.getXPos())/Math.PI; //dont we need to set angle mirrored??
				// check all balls except one with given id
				// if hitting, return angle between the other ball and this one.
		
		return ans;		// return the angle
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
