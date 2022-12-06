package droneGUI;

import java.util.ArrayList;
import java.util.Random;

public class DroneArena{
	private double xDim, yDim;
	private double score;
	private ArrayList<Drone> allDrones;
	ArrayList<String> droneStrings;
	
	private Random ranGen = new Random();
	
	private double speed = 15; //make this customisable
	
	
	
	public DroneArena(double xDim, double yDim, double speed, double score) {
		//parameters
		
		this.xDim = xDim;
		this.yDim = yDim;
		this.speed = speed;
		this.score = score;
		
		
		
		//add drone types to arraylist
		allDrones = new ArrayList<>();
		
		//add 1 goal drone, 1 blue drone, 1 red drone immediately
		allDrones.add(new AttackDrone(ranGen.nextDouble(getXSize()), ranGen.nextDouble(getYSize()), ranGen.nextDouble(360), speed));
		allDrones.add(new DefenderDrone(ranGen.nextDouble(getXSize()), ranGen.nextDouble(getYSize()), ranGen.nextDouble(360), speed));
		allDrones.add(new TargetDrone(ranGen.nextDouble(getXSize()), ranGen.nextDouble(getYSize())));
		
	}
	
	
	//functions------------------------------------------------------------------
	public boolean checkDroneMax() {
		
		if(allDrones.size() == 30) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public boolean checkDroneMin() {
		if(allDrones.size() == 0) {
			return true;
		}else {
			return false;
		}
	}
	
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
			d.adjustDrone(speed);
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
	public double findDroneAngle(double xPos, double yPos, double rad, double angle, int notID, Drone currentDrone) {
		double ans = angle;
		if (xPos <= rad || xPos >= xDim - rad) ans = 180 - ans;
			// if Drone hit (tried to go through) left or right walls, set mirror angle, being 180-angle
		if (yPos <= rad || yPos >= yDim - rad) ans = - ans;
			// if try to go off top or bottom, set mirror angle
		
		for (Drone hitDrone : allDrones) 
			if (hitDrone.getID() != notID && hitDrone.hitting(xPos, yPos, rad)) {
				ans = 180*Math.atan2(yPos-hitDrone.getYPos(), xPos-hitDrone.getXPos())/Math.PI; //dont we need to set angle mirrored??
					if(hitDrone instanceof TargetDrone && currentDrone instanceof AttackDrone) { score++;}
			}
				
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
		String aSize = "Arena size is " + xDim + " by " + yDim + " , has a speed level of " + speed + " and score " + score;
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
	
	public double getScore() {
		return score;
	}
	
	public void setSpeed(Number speed) {
		this.speed = (double) speed;
	}
}
