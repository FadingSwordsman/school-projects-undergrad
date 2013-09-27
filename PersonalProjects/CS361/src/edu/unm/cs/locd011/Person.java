package edu.unm.cs.locd011;

public abstract class Person{	
	private int index; 
	private String name;
	
	/**
	 * Creates a person with a name and number
	 * @param index
	 * @param name
	 */
	public Person(int index, String name){
		this.index = index;
		this.name = name;
	}
	
	/**
	 * Returns the person's name
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Returns the person's number
	 * @return
	 */
	public int getIndex(){
		return index;
	}
	
	/**
	 * Returns the name of the person
	 */
	@Override
	public String toString(){
		return name;
	}
	
}
