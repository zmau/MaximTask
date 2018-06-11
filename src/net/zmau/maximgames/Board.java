package net.zmau.maximgames;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.zmau.maximgames.Field.FieldType;
import net.zmau.maximgames.mapgenerators.BordersGenerator;
import net.zmau.maximgames.mapgenerators.RandomisingBorderGenerator;

public class Board {

	public static int WIDTH = /*20*/80;
	public static int HEIGHT = /*16*/60;
	private static int MIN_TOWNS_COUNT = 1;
	private static int MAX_TOWNS_COUNT = 4;
	
	public static void main(String[] args) {
		BordersGenerator randomBordersGenerator = new RandomisingBorderGenerator(-1, 1, HEIGHT);
		Board.getInstance(randomBordersGenerator).write();
	}

	private BordersGenerator bordersGenerator;
	private Field[][] field;
	private List<ArmyUnit> army;
	
	private Board(BordersGenerator bordersGenerator){
		this.bordersGenerator = bordersGenerator;
		army = new ArrayList<>();
		initFields();
		locateTowns();
	}
	
	private static Board singleInstance;
	public static Board getInstance(BordersGenerator bordersGenerator){
		if(singleInstance == null)
			singleInstance = new Board(bordersGenerator);
		return singleInstance;
	}
	public static Board getInstance(){
		if(singleInstance == null)
			throw new RuntimeException("Board not initialised");
		return singleInstance;
	}
	
	private void initFields(){
		field = new Field[HEIGHT][WIDTH];
		
		int landUntil = WIDTH / 3;
		int deepSeaFrom = 2 * WIDTH / 3;

		for(int row = 0; row < Board.HEIGHT; row++){
			int landDelta = bordersGenerator.next(); 
			landUntil += landDelta;
			landUntil = Math.max(0, landUntil);
			landUntil = Math.min(landUntil, WIDTH/2);
			for(int column = 0; column < landUntil; column++)
				field[row][column] = new Field(FieldType.LAND, column, row);
			field[row][landUntil] = new Field(FieldType.COAST, landUntil, row);
		
		
			deepSeaFrom += landDelta + bordersGenerator.next(); // landDelta is here to achieve "*otprilike paralelno s obalom* podijeliti more"
			deepSeaFrom = Math.min(deepSeaFrom, WIDTH-1);
			deepSeaFrom = Math.max(deepSeaFrom, landUntil+2);

			for(int column = landUntil+1; column < deepSeaFrom; column++)
				field[row][column] = new Field(FieldType.SEA, column, row);
			for(int column = deepSeaFrom; column < WIDTH; column++)
				field[row][column] = new Field(FieldType.DEEP_SEA, column, row);
		}
	}
	
	private void locateTowns(){
		int townsCount = new RandomisingBorderGenerator(MIN_TOWNS_COUNT, MAX_TOWNS_COUNT, 1).next();
		//System.out.println("towns count : " + townsCount);
		Set<Integer> townRowSet = new HashSet<>();
		do{
			int townRow = (int)(Math.random()*HEIGHT); // TODO move Math.random() to new method inside RandomisingBorderGenerator
			if(field[townRow][0].getType() == FieldType.LAND){
				field[townRow][0].setType(FieldType.TOWN);
				townRowSet.add(townRow);
			}
		} while (townRowSet.size() < townsCount);
	}
	
	Field getFieldAt(int x, int y){
		return field[y][x];
	}
	public boolean isArmyOn(int x, int y){
		return army.stream().anyMatch(unit -> unit.getLocation().equals(new Point(x, y)));
	}
	
	public boolean isArmyOn(Field f){
		return army.stream().anyMatch(unit -> unit.getLocation().equals(f.getLocation()));
	}
	
	public List<Field> withoutArmyFields(List<Field> fieldList){
		List<Field> result = new ArrayList<Field>();
		for(Field field : fieldList){
			if(!isArmyOn(field))
				result.add(field);
		}
		return result;
	}
	public List<Field> allFields(){
		List<Field> result = new ArrayList<>();
		for(int i = 0; i < HEIGHT; i++)
			for(int j = 0; j < WIDTH; j++)
				result.add(field[i][j]);
		return result;	
	}
	
	public List<Field> getTowns(){
		return allFields().stream().filter(f -> f.getType() == FieldType.TOWN).collect(Collectors.toList());
	}
	
	public void increaseArmy(ArmyUnit unitToAdd){
		army.add(unitToAdd);
	}
	
	public void write(){
		for(int i = 0; i < HEIGHT; i++){
			for(int j = 0; j < WIDTH; j++){
				if(isArmyOn(j, i))
					System.out.print("A");
				else System.out.print(field[i][j].asChar());
			}
			System.out.println();
		}
	}
}
