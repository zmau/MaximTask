package net.zmau.maximgames;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import net.zmau.maximgames.Field.FieldType;
import net.zmau.maximgames.mapgenerators.FixedBorderGenerator;

public class ArmyUnitTest {

	@Before
	public void init(){
		FixedBorderGenerator borderGenerator =new FixedBorderGenerator(); 
		Board.getInstance(borderGenerator).write();
	}
	
	@Test
	public void testGetPossibleMoves(){
		ArmyUnit unit = new ArmyUnit(Board.WIDTH-2, 2);
		System.out.println("start field " + unit);
		Board.getInstance().increaseArmy(unit);
		List<Field> nextFieldList = unit.getPossibleMoves();
		for(Field possibleNext : nextFieldList){
			System.out.println(possibleNext);
		}
		Assert.assertTrue(nextFieldList.contains(new Field(FieldType.DEEP_SEA, 75, 1)));
		Assert.assertTrue(nextFieldList.contains(new Field(FieldType.DEEP_SEA, 76, 0)));
		Assert.assertTrue(nextFieldList.contains(new Field(FieldType.DEEP_SEA, 74, 2)));
		Assert.assertTrue(nextFieldList.contains(new Field(FieldType.DEEP_SEA, 75, 3)));
		Assert.assertTrue(nextFieldList.contains(new Field(FieldType.DEEP_SEA, 79, 5)));
		
		unit = new ArmyUnit(27, 2);
		System.out.println("start field " + unit);
		Board.getInstance().increaseArmy(unit);
		nextFieldList = unit.getPossibleMoves();
		for(Field possibleNext : nextFieldList){
			System.out.println(possibleNext);
		}
		Assert.assertTrue(nextFieldList.contains(new Field(FieldType.LAND, 25, 2)));
		Assert.assertTrue(nextFieldList.contains(new Field(FieldType.SEA, 29, 0)));
		Assert.assertTrue(nextFieldList.contains(new Field(FieldType.SEA, 30,1)));
		Board.getInstance().write();
	}
	
	@Test
	public void testIsArmyOn(){
		Field f = Board.getInstance().getFieldAt(5, 5);
		ArmyUnit unit = new ArmyUnit(5, 5);
		Board.getInstance().increaseArmy(unit);
		boolean isOn = Board.getInstance().isArmyOn(f);
		Assert.assertTrue(isOn);
	}
}

