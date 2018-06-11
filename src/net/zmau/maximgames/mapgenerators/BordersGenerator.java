package net.zmau.maximgames.mapgenerators;

/**
 * Generates a stream of numbers used for defining a border between map segments
 */
public interface BordersGenerator {
	public int next();

	public int armySize();
}
