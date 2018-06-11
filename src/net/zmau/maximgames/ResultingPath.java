package net.zmau.maximgames;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ResultingPath {
	private ArmyUnit armyUnit;
	private Field destinationTown;
	private List<Field> path;
	
	public ResultingPath(Field town, Stack<Field> pathFromTown) {
		destinationTown = town;
		armyUnit = new ArmyUnit(pathFromTown.pop());
		path = new ArrayList<>();
		while(!pathFromTown.isEmpty()){
			path.add(pathFromTown.pop());
		}
	}
	
	@Override
	public String toString(){
		return String.format("%s - %s - %s", armyUnit, path, destinationTown);
	}
}
