package net.zmau.maximgames;

public class Field extends MapObject{

	enum FieldType {
		DEEP_SEA, // where army can be at moment zero
		SEA,
		COAST,
		LAND,
		TOWN;
		
		public boolean compatibleTo(FieldType other){
			if(this == other)
				return true;
			if(this == SEA && other == DEEP_SEA || this == DEEP_SEA && other == SEA)
				return true;
			if(this == TOWN && other == LAND || this == LAND && other == TOWN)
				return true;
			
			return false;
		}
	}
	
	private FieldType type;
	
	public Field(FieldType type, int x, int y){
		super(x, y);
		this.type = type;
	}
	
	public FieldType getType(){
		return type;
	}
	public void setType(FieldType value){
		this.type = value;
	}

	@Override 
	public String toString(){
		return String.format("%s %d:%d", type, location.x, location.y);
	}
	
	public char asChar(){
		return type.name().charAt(0);
	}
	
	@Override 
	public boolean equals(Object other){
		if (other instanceof Field){
			return (this.location.equals(((Field) other).getLocation()) && (this.type == ((Field)other).type));
		}
		else return false;
	}
}
