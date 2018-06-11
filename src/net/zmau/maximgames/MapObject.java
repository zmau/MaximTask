package net.zmau.maximgames;

import java.awt.Point;

public abstract class MapObject {
	protected Point location; // this is redundant inside board, but it could be useful
	
	public MapObject(int x, int y){
		if (x < 0 || x > Board.WIDTH)
			throw new IllegalArgumentException(String.format("X must be between 0 and %d, including borders", Board.WIDTH));
		if (y < 0 || y > Board.HEIGHT)
			throw new IllegalArgumentException(String.format("Y must be between 0 and %d, including borders", Board.HEIGHT));
		this.location = new Point(x, y);
	}
	
	public Point getLocation(){
		return location;
	}
	public int getX(){
		return (int)location.getX();
	}
	public int getY(){
		return (int)location.getY();
	}

	public int getManhattanDistanceFrom(MapObject other){
		return Math.abs(this.getX() - other.getX()) + Math.abs(this.getY() - other.getY());
	}
}
