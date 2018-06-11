package net.zmau.maximgames;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.zmau.maximgames.Field.FieldType;

public class ArmyUnit extends MapObject {
	
	public ArmyUnit(int x, int y){
		super(x, y);
	}
	
	public ArmyUnit(Field f){
		this(f.getX(), f.getY());
	}
	
	@Override
	public String toString(){
		return String.format("A  %d:%d", location.x, location.y);
	}
 
	public Field getGroundField(){
		return Board.getInstance().getFieldAt(getX(), getY());
	}
	
	public FieldType getGroundType(){
		return getGroundField().getType();
	}
	
	public List<Field> getNeightboringCoast(){
		return Board.getInstance().allFields().stream().filter(f -> f.getType() == FieldType.COAST && getManhattanDistanceFrom(f) == 1)
				.collect(Collectors.toList());
	}
	
	private int getMoveLength(){
		switch (getGroundType()) {
			case SEA: return 4;	
			case DEEP_SEA: return 4;	
			case LAND: return 7;
			case TOWN: return 7;
			default: throw new IllegalStateException();
		}
	}
	
	/*package level*/ List<Field> getPossibleMoves(){
		final int moveLength = getMoveLength();
		List<Field> availableFields = Board.getInstance().allFields().stream()
				.filter(f -> 
					(getManhattanDistanceFrom(f) == moveLength) 
					&& f.getType().compatibleTo(this.getGroundType())
					&& !Board.getInstance().isArmyOn(f)
				).collect(Collectors.toList());
		List<Field> neightboringCoast = getNeightboringCoast();
		if(neightboringCoast != null && neightboringCoast.size() > 0){
			Map<Point, Field> otherSide = new HashMap<>(); 
			for(Field coastPoint : neightboringCoast){
				List<Field> otherSideFieldsForOneCoastalPoint = Board.getInstance().allFields().stream()
				.filter(f -> 
					(coastPoint.getManhattanDistanceFrom(f) == 1) 
					&& f.getType() != this.getGroundType()
					&& f.getType() != FieldType.COAST
					&& !Board.getInstance().isArmyOn(f)
				).collect(Collectors.toList());
				for(Field otherSideField : otherSideFieldsForOneCoastalPoint)
					otherSide.put(otherSideField.getLocation(), otherSideField); // map is used to avoid doubling
			}
			availableFields.addAll(otherSide.values());
		}
		return availableFields;
	}

}
