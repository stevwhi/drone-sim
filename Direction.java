package droneGUI;

import java.util.Random;



public enum Direction {
	NORTH, SOUTH, EAST, WEST;
	
	public static Direction getRandomDirection() {
		Random random = new Random();
		return values()[random.nextInt(values().length)];
	}
	
	public static Direction nextDirection(Direction dir) {
		return values()[(dir.ordinal() + 1) % values().length];
	}
	
}