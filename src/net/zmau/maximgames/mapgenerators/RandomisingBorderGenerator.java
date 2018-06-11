package net.zmau.maximgames.mapgenerators;

import java.util.Random;

public class RandomisingBorderGenerator implements BordersGenerator{
	private static int MIN_ARMY_UNITS_COUNT = 18;
	private static int MAX_ARMY_UNITS_COUNT = 22;

	private Random r;
	int lowBorder, highBorder; 
	int lenth;
	
	public RandomisingBorderGenerator(int lowBorder, int highBorder/*inclusive*/, int lenth){
		r = new Random();
		this.lowBorder = lowBorder;
		this.highBorder = highBorder+1;
		this.lenth = lenth;
	}
	
	@Override
	public int next(){
		return r.nextInt(highBorder-lowBorder) + lowBorder;
	}
	
	@Override
	public int armySize(){
		return (int)((MAX_ARMY_UNITS_COUNT - MIN_ARMY_UNITS_COUNT)*Math.random() + MIN_ARMY_UNITS_COUNT);
	}
}
