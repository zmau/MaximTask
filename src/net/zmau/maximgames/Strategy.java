package net.zmau.maximgames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.zmau.maximgames.Field.FieldType;
import net.zmau.maximgames.mapgenerators.BordersGenerator;
import net.zmau.maximgames.mapgenerators.RandomisingBorderGenerator;

public class Strategy { 
	private static int MAX_STEPS = 20;
	private int armySizeLeft, commonGroupSize;
	private Stack<Field> currentTrack;
	private List<Stack<Field>> trackListForCurrentTown;
	private Map<Field, List<Stack<Field>>> allTracks; 
	
	public Strategy(List<Field> towns, int armySize){
		this.armySizeLeft = armySize;
		System.out.println(String.format("%d towns; %d army units", towns.size(), armySize));
		this.commonGroupSize = (int)Math.ceil(armySize / towns.size());
		currentTrack = new Stack<Field>();
		trackListForCurrentTown = new ArrayList<Stack<Field>>();
		allTracks = new HashMap<Field, List<Stack<Field>>>();
	}
	
	public void locateArmyUnitIntoDeepSea(Field town){
		ArmyUnit unit = new ArmyUnit(town.getX(), town.getY());
		int groupSize = Math.min(armySizeLeft, commonGroupSize);
		armySizeLeft -= groupSize;
		System.out.println(String.format("%s; %d units; %d units left", town, groupSize, armySizeLeft));
		locateArmyUnitIntoDeepSea(unit, groupSize);
		System.out.println(String.format("%d tracks : %s", trackListForCurrentTown.size(), trackListForCurrentTown));
		allTracks.put(town, trackListForCurrentTown);
		trackListForCurrentTown = new ArrayList<Stack<Field>>();
	}
	public void locateArmyUnitIntoDeepSea(ArmyUnit unit, int groupSize){
		String padding = String.format("%" + (currentTrack.size() + 1) + "s", "");
		List<Field> possibleFields = unit.getPossibleMoves();
		Collections.sort(possibleFields, new FieldColumnComparator());
		if(currentTrack.size() < MAX_STEPS){
			for(Field possibleNextField : possibleFields){
				if(trackListForCurrentTown.size() < groupSize){
					currentTrack.push(possibleNextField);
					System.out.println(String.format("%s tracking %s", padding, possibleNextField));
					if(possibleNextField.getType() == FieldType.DEEP_SEA){
						trackListForCurrentTown.add((Stack<Field>)currentTrack.clone());
						Board.getInstance().increaseArmy(new ArmyUnit(possibleNextField));
						currentTrack.pop();
						ArmyUnit unitOnPreviousPosition = new ArmyUnit(currentTrack.peek().getX(), currentTrack.peek().getY());
						locateArmyUnitIntoDeepSea(unitOnPreviousPosition, groupSize);
					}
					else {
						ArmyUnit movedUnit = new ArmyUnit(possibleNextField.getX(), possibleNextField.getY());
						locateArmyUnitIntoDeepSea(movedUnit, groupSize);
						currentTrack.pop();
					}

				}
			}
		}
	}
	
	public List<Stack<Field>> getTrackList(){
		return trackListForCurrentTown;
	}
	
	public static void main(String[] args){
		BordersGenerator randomBordersGenerator = new RandomisingBorderGenerator(-1, 1, Board.HEIGHT);
		List<Field> towns = Board.getInstance(randomBordersGenerator).getTowns();
		Board.getInstance().write();
		
		Strategy singleTownStrategy = new Strategy(towns, randomBordersGenerator.armySize());
		for (Field town : towns) {
			singleTownStrategy.locateArmyUnitIntoDeepSea(town);
		}
		System.out.println(singleTownStrategy.getTrackList());
		Board.getInstance().write();
	}
}
