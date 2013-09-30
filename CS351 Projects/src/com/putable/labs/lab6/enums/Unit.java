package com.putable.labs.lab6.enums;

public enum Unit {
	POUND("lb"),
	GRAM("g"),
	KILOGRAM("Kg");
	
	private String descriptor; 
	
	private Unit(String descriptor)
	{
		this.descriptor = descriptor;
	}
	
	public String toString()
	{
		return descriptor;
	}
}
