package com.putable.labs.lab10;

public class Paragraph
{
    private String value;
    /**
     * Create 
     * @param value
     */
    public Paragraph(String value)
    {
	this.value = '¶' + value;
    }
    
    public String getValue()
    {
	return value;
    }
    
    public String toString()
    {
	return getValue();
    }
}
