package com.putable.siteriter.locd011;

/**
 * Convenience class for grouping different items together
 * 
 * @author David Lochridge
 * @param <T>
 * @param <U>
 */
public class Tuple<T, U>
{
    private T first;
    private U second;

    /**
     * Construct a Tuple of a pair of Objects
     * @return
     */
    public Tuple(T first, U second)
    {
	this.first = first;
	this.second = second;
    }

    /**
     * Return the first object in this pair
     * @return
     */
    public T getFirst()
    {
	return first;
    }

    /**
     * Return the second object in this pair
     * @return
     */
    public U getSecond()
    {
	return second;
    }

    @Override
    public String toString()
    {
	return first.toString() + " : " + second.toString();
    }
}
