package net.zmau.maximgames;

import java.util.Comparator;

public class FieldColumnComparator implements Comparator<Field>{

	@Override
	public int compare(Field f1, Field f2) {
		return (int) Math.signum(f2.getX() - f1.getX());
	}

}
