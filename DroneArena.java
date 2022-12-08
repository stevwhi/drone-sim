package droneGUI;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Steven Whitby
 * Class for Arena of drones
 */
public class DroneArena{
	//arena parameters
	private double xDim, yDim;
	private double score;
	private double speed;
	
	//drones and their info
	private ArrayList<Drone> allDrones;
	private ArrayList<String> droneStrings;
	
	private Random ranGen = new Random();
	
	/**
	 * construct arena of size xDim by yDim
	 * @param xDim
	 * @param yDim
	 */
	public DroneArena(double xDim, double yDim, double speed, double score) {
		//parameters
		
		this.xDim = xDim;
		this.yDim = yDim;
		this.speed = speed;
		this.score = score;
		
		//all drone types to arraylist
		allDrones = new ArrayList<>();
		
		//add 1 goal drone, 1 blue drone, 1 red drone immediately
		addUserDrone(ranGen.nextDouble(xDim), ranGen.nextDouble(yDim));
		addTargetDrone(ranGen.nextDouble(xDim), ranGen.nextDouble(yDim));
		addAttackDrone(ranGen.nextDouble(xDim), ranGen.nextDouble(yDim), ranGen.nextDouble(360));
		addDefenderDrone(ranGen.nextDouble(xDim), ranGen.nextDouble(yDim), ranGen.nextDouble(360));
	}
	
	
	//functions-----------------------------------------------------------------

	/**
	 * check if max no. of drones reached
	 * @return boolean whether max no. of drones reached
	 */
	public boolean checkDroneMax() {
		if(allDrones.size() == 40) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * check if min no. of drones reached
	 * @return boolean whether min no. of drones reached
	 */
	public boolean checkDroneMin() {
		if(allDrones.size() == 0) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * remove last added drone from arena
	 */
	public void removeLastDrone() {
		allDrones.remove(allDrones.size()-1);
	}
	
	/**
	 * remove all drones from arena
	 */
	public void removeAllDrones() {
		allDrones.clear();
	}
	
	/**
	 * draw all drones in the arena to canvas 
	 * @param mc	canvas to be drawn on
	 */
	public void drawArena(MyCanvas mc) {
		for(Drone d: allDrones) d.drawDrone(mc);
	}
		
		
	/**
	 * add Attack Drone to arena
	 * @param xPos		x-coordinate of drone
	 * @param yPos		y-coordinate of drone
	 * @param angle		angle of travel of drone
	 */
	public void addAttackDrone(double xPos, double yPos, double angle) {
		AttackDrone ad = new AttackDrone(xPos, yPos, angle, speed);
		double x = xPos;
		double y = yPos;
		
		while(!canGoHere(x, y, ad.getRad(), ad.getID())) {
			x = ranGen.nextDouble(xDim);
			y = ranGen.nextDouble(yDim);
		}
		
		ad.setXY(x, y);
		
		allDrones.add(ad);
	}
	
	/**
	 * add Defender Drone to arena
	 * @param xPos		x-coordinate of drone
	 * @param yPos		y-coordinate of drone
	 * @param angle		angle of travel of drone
	 */
	public void addDefenderDrone(double xPos, double yPos, double angle) {
		DefenderDrone dd = new DefenderDrone(xPos, yPos, angle, speed);
		double x = xPos;
		double y = yPos;
		
		while(!canGoHere(x, y, dd.getRad(), dd.getID())) {
			x = ranGen.nextDouble(xDim);
			y = ranGen.nextDouble(yDim);
		}
		
		dd.setXY(x, y);
		
		allDrones.add(dd);
	}
	
	/**
	 * add Target Drone to arena
	 * @param xPos		x-coordinate of drone
	 * @param yPos		y-coordinate of drone
	 */
	public void addTargetDrone(double xPos, double yPos) {
		TargetDrone td = new TargetDrone(xPos, yPos);
		double x = xPos;
		double y = yPos;
		
		while(!canGoHere(x, y, td.getRad(), td.getID())) {
			x = ranGen.nextDouble(xDim);
			y = ranGen.nextDouble(yDim);
		}
		
		td.setXY(x, y);
		
		allDrones.add(td);
	}
	
	/**
	 * add User Drone to arena
	 * @param xPos		x-coordinate of drone
	 * @param yPos		y-coordinate of drone
	 */
	public void addUserDrone(double xPos, double yPos) {
		UserDrone ud = new UserDrone(xPos, yPos);
		
		if(canGoHere(xPos, yPos, ud.getRad(), ud.getID())) {
			allDrones.add(ud);
		} 
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
			d.adjustDrone(speed);
		}
	}
	
	/**
	 * adjust out of bounds coordinates to in bounds
	 * @param ogPos		original coordinate
	 * @param rad		drone radius
	 */
	public double fixPos(double ogPos, double rad) {
		if(ogPos <= rad) {
			ogPos += rad;
		} else if(ogPos >= xDim - rad){
			ogPos -= rad;
		}
		
		return ogPos;
	}
	
	
	/** 
	 * Check angle of drone ... if hitting wall, rebound; if hitting drone, change angle
	 * @param x				drone x position
	 * @param y				drone y position
	 * @param rad			radius of drone
	 * @param ang			current angle of drone
	 * @param notID			identify of drone not to be checked
	 * @return				new angle 
	 */
	public double findDroneAngle(double xPos, double yPos, double rad, double angle, int notID, Drone currentDrone) {
		double ans = angle;
		if (xPos <= rad || xPos >= xDim - rad) ans = 180 - ans;
			// if Drone hit (tried to go through) left or right walls, set mirror angle, being 180-angle
		if (yPos <= rad || yPos >= yDim - rad) ans = - ans;
			// if try to go off top or bottom, set mirror angle
		
		for (Drone hitDrone : allDrones) 
			if (hitDrone.getID() != notID && hitDrone.hitting(xPos, yPos, rad)) {
				ans = 180*Math.atan2(yPos-hitDrone.getYPos(), xPos-hitDrone.getXPos())/Math.PI; 
					if(hitDrone instanceof TargetDrone && currentDrone instanceof AttackDrone) { score++;}
			}
				// check all drones except one with given id
				// if hitting, return angle between the other drones and this one.
		
		return ans;		// return the angle
	}
	
	/** 
	 * Check if drone is placed in plausible spot
	 * @param x				drone x position
	 * @param y				drone y position
	 * @param rad			radius of drone
	 * @param notID			identify of drone not to be checked
	 * @return				true if plausible
	 */
	private boolean canGoHere(double xPos, double yPos, double rad, int notID) {
		if (xPos <= rad || xPos >= xDim - rad) return false;		//left and right collision
		if (yPos <= rad || yPos >= yDim - rad) return false;		//top and bottom collsion

		for (Drone hitDrone : allDrones) 							//hitting another drone
			if (hitDrone.getID() != notID && hitDrone.hitting(xPos, yPos, rad)) {
				return false;		
			}
				
		return true;		
	}
	
	/**
	 * return list of strings defining each drone
	 * @return	drone position descriptions
	 */
	public ArrayList<String> describeAll() {
		droneStrings = new ArrayList<>();
		for (Drone d : allDrones) droneStrings.add(d.toStringforDisplay());			// add string defining each ball
		return droneStrings;												// return string list
	}
	
	/**
	 * return string describing arena
	 * @return	drone position descriptions
	 */
	@Override
	public String toString() {
		String aSize = "Arena size is " + xDim + " by " + yDim + " , has a speed level of " + speed + " and score " + score;
		String dPos = "";
		
		if(allDrones.isEmpty()){
			dPos = "No Drones exist \n";
		}
		else {
			for(int i = 0; i < allDrones.size(); i++) {
				dPos += allDrones.get(i).toStringForSave() + "\n";	//one description per line
			}			
		}
		
		return aSize + "\n" + dPos;
	}
	
	//getters/setters------------------------------------------------
	/**
	 * return arena size in x direction
	 * @return		x-dimension of arena
	 */
	public double getXSize() {
		return xDim;
	}
	/**
	 * return arena size in y direction
	 * @return		y-dimension of arena
	 */
	public double getYSize() {
		return yDim;
	}
	
	/**
	 * return current score
	 * @return		number of times target ball has been hit by attack drone
	 */
	public double getScore() {
		return score;
	}
	
	/**
	 * set speed of drones in arena
	 * @param speed 	new speed of arena drones		
	 */
	public void setSpeed(Number speed) {
		this.speed = (double) speed;
	}
}
