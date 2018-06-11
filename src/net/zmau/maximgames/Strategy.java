package net.zmau.maximgames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.zmau.maximgames.Field.FieldType;
import net.zmau.maximgames.mapgenerators.BordersGenerator;
import net.zmau.maximgames.mapgenerators.FixedBorderGenerator;
import net.zmau.maximgames.mapgenerators.RandomisingBorderGenerator;

public class Strategy { 
	private static int MAX_STEPS = 20;
	private int armySizeLeft, commonGroupSize;
	private Stack<Field> currentTrack, currentlyShortestTrack;
	private List<Stack<Field>> trackListForCurrentTown;
	private Map<Field, List<Stack<Field>>> allTracks; 
	private List<ResultingPath> result;
	
	public Strategy(List<Field> towns, int armySize){
		this.armySizeLeft = armySize;
		System.out.println(String.format("%d towns; %d army units", towns.size(), armySize));
		this.commonGroupSize = (int)Math.ceil(armySize / towns.size());
		currentTrack = new Stack<Field>();
		trackListForCurrentTown = new ArrayList<Stack<Field>>();
		allTracks = new HashMap<Field, List<Stack<Field>>>();
		result = new ArrayList<>();
	}
	
	public void locateArmyUnitIntoDeepSea(Field town){
		ArmyUnit unit = new ArmyUnit(town.getX(), town.getY());
		int groupSize = Math.min(armySizeLeft, commonGroupSize);
		armySizeLeft -= groupSize;
		System.out.println(String.format("%s; %d units; %d units left", town, groupSize, armySizeLeft));
		while(groupSize > 0){
			findClosestDeepSeaPoint(unit);
			if(currentlyShortestTrack != null){
				System.out.println("adding army unit on " + currentlyShortestTrack.peek());
				Board.getInstance().increaseArmy(new ArmyUnit(currentlyShortestTrack.peek()));
				trackListForCurrentTown.add(currentlyShortestTrack);
				result.add(new ResultingPath(town, currentlyShortestTrack));
				
				currentlyShortestTrack = null;
				currentTrack = new Stack<Field>();
				groupSize--;
			}
		}
		System.out.println(String.format("%d tracks : %s", trackListForCurrentTown.size(), trackListForCurrentTown));
		allTracks.put(town, trackListForCurrentTown);
		trackListForCurrentTown = new ArrayList<Stack<Field>>();
	}
	// recursive
	private void findClosestDeepSeaPoint(ArmyUnit unit){
		System.out.println(currentTrack);
		if(currentTrack.size() > (currentlyShortestTrack == null ? MAX_STEPS : currentlyShortestTrack.size()))
			return;
		
		String padding = String.format("%" + (currentTrack.size() + 1) + "s", "");
		List<Field> possibleFields = unit.getPossibleMoves();
		Collections.sort(possibleFields, new FieldColumnComparator());
		for(Field possibleNextField : possibleFields){
			if(currentlyShortestTrack != null && possibleNextField.getType() != FieldType.DEEP_SEA && currentTrack.size() >= currentlyShortestTrack.size()-1)
				continue;
			if(!currentTrack.isEmpty() && possibleNextField.getX() <= currentTrack.peek().getX())
				return; // always go right
			currentTrack.push(possibleNextField);
			System.out.println(String.format("%s tracking %s", padding, possibleNextField));
			if(possibleNextField.getType() == FieldType.DEEP_SEA){
				if(currentlyShortestTrack == null || currentTrack.size() < currentlyShortestTrack.size()){
					currentlyShortestTrack = (Stack<Field>)currentTrack.clone();
					System.out.println(String.format("%d steps in track %s", currentlyShortestTrack.size(), currentlyShortestTrack));
					currentTrack.pop();
					break;
				}
			}
			else {
				ArmyUnit movedUnit = new ArmyUnit(possibleNextField.getX(), possibleNextField.getY());
				findClosestDeepSeaPoint(movedUnit);
				currentTrack.pop();
			}
		}
	}

	public void writeResult(){
		for (ResultingPath resultingPath : result)
			System.out.println(resultingPath.toString());
	}

	public List<Stack<Field>> getTrackList(){
		return trackListForCurrentTown;
	}
	
	public static void main(String[] args){
		//BordersGenerator bordersGenerator = new RandomisingBorderGenerator(-1, 1, Board.HEIGHT);
		BordersGenerator bordersGenerator = new FixedBorderGenerator(); // plain vertical borders
		List<Field> towns = Board.getInstance(bordersGenerator).getTowns();
		Board.getInstance().write();
		
		Strategy strategy = new Strategy(towns, bordersGenerator.armySize());
		for (Field town : towns)
			strategy.locateArmyUnitIntoDeepSea(town);
		strategy.writeResult();
		Board.getInstance().write(); // board version with armies on
	}
}
