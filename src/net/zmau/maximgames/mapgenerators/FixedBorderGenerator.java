package net.zmau.maximgames.mapgenerators;

/**
 * Used in unit tests only
 */
public class FixedBorderGenerator  implements BordersGenerator{
	
	@Override
	public int next(){
		return 0;
	}

	@Override
	public int armySize(){
		return 20;
	}

}
