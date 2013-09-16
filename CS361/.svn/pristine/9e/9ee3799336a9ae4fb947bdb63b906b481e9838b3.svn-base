package edu.unm.cs.locd011;

public class Woman extends Person {

	int[] preferenceList;
	
	/**
	 * Creates a woman with a name, number, and preference list
	 */
	public Woman(int[] preferenceList, int index, String name) {
		super(index, name);
		this.preferenceList = preferenceList;
	}
	
	/**
	 * Determines which man is preferred for the given woman, and returns him
	 * @param first
	 * @param second
	 * @return
	 */
	public Man prefers(Man first, Man second){
		boolean prefersFirst = preferenceList[first.getIndex()] < preferenceList[second.getIndex()];
		return prefersFirst ? first : second;
	}
}
