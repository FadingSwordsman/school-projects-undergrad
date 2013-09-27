package edu.unm.cs.locd011;

import java.util.LinkedList;

public class Man extends Person {
	
	private LinkedList<String> preferenceList;
	
	/**
	 * Creates a Man with a preference list. Also a name and index.
	 * @param preferenceList
	 * @param index
	 * @param name
	 */
	public Man(LinkedList<String> preferenceList, int index, String name) {
		super(index, name);
		this.preferenceList = preferenceList;
	}
	
	/**
	 * Pulls the net match from the preference list
	 * @return
	 */
	public String getNextMatch(){
		return preferenceList.remove();
	}
}
